package client.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import business.model.BusinessLocation;
import business.model.CustomerLogin;
import client.model.ClientModel;
import general.gui.Header;
import general.gui.NavigationBar;
import general.gui.UserAccountHeader;
import general.gui.View;
import general.gui.ViewHandler;
import general.utility.ErrorBuilder;
import general.utility.Utilities;

public class ClientApplication extends JFrame implements ViewHandler {
	private static final long serialVersionUID = 554152094859674365L;
	// Refresh rate of displayed data
	private static final int REFRESH_RATE = 1000; // ms
	public static final int REQUEST_TIMEOUT = 1000; // ms

	// Data model being served
	public final ClientModel model;

	// Header
	private Header pnlHeader;
	private UserAccountHeader userAccountHeader;
	// Content (View)
	private JPanel pnlView;
	private CardLayout cl_pnlView;
	private HashMap<String, View> views;
	private View currentView;
	private Object viewLock;

	/**
	 * Instantiate a new client application targetting a specific business application.
	 * @param location Location for client application to target
	 */
	public ClientApplication(BusinessLocation location) {
		// Pre-initialisation
		Utilities.setNaturalGUI();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.model = new ClientModel(location);

		// Initialise GUI
		init();
		createViewRefresh();

		// Set GUI Properties
		setMinimumSize(new Dimension(770, 600));
		setLocationRelativeTo(null);
	}

	/**
	 * Instantiate a new client application gui.
	 */
	private void init() {
		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// Create header
		pnlHeader = new Header("Client Application");
		contentPane.add(pnlHeader, BorderLayout.NORTH);
		// Create navigation bars
		NavigationBar nbLoggedIn = pnlHeader.addNavigationBar("LOGGED_IN", this);
		NavigationBar nbLoggedOut = pnlHeader.addNavigationBar("LOGGED_OUT", this);
		// Create user account header
		userAccountHeader = new UserAccountHeader();
		pnlHeader.setAccountHeader(userAccountHeader);
		
		// Create content viewing panel
		pnlView = new JPanel();
		cl_pnlView = new CardLayout();
		pnlView.setLayout(cl_pnlView);
		contentPane.add(pnlView, BorderLayout.CENTER);
		// Keep track of views added
		views = new HashMap<>();
		viewLock = new Object();

		// Create views and add them to viewing panel
		final JPanel pnlLogin = new LoginPanel(this);
		addView(pnlLogin, "Login", nbLoggedOut);
		final JPanel pnlRegister = new RegisterPanel(this);
		addView(pnlRegister, "Register", nbLoggedOut);
		final JPanel pnlNewOrder = new NewOrderPanel(this);
		addView(pnlNewOrder, "New Order", nbLoggedIn);
		final JPanel pnlViewOrder = new ViewOrdersPanel(model);
		addView(pnlViewOrder, "View Orders", nbLoggedIn);	
		final JPanel pnlUserAccount = new UserAccountPanel(model);
		addView(pnlUserAccount, "User Account", nbLoggedIn);

		// Start application in logged out state
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				logout();
			}
		});
		
		// [Logout Button] - Handle logout request
		userAccountHeader.getLogoutButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to logout?", "Logout", 
						JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (res == JOptionPane.YES_OPTION) {
					logout();
				}
				
			}	
		});
	}

	/**
	 * Attempt to login to the system, call model behaviour and update application.
	 * 
	 * @param login CustomerLogin to use for attempt 
	 */
	public void login(CustomerLogin login) {
		ErrorBuilder eb = new ErrorBuilder();
		// Handle model behaviour (failure stops login)
		if (model.login(login)) {
			// Wait for message handler to handle response
			if (model.loggedInCustomer.waitForNew(REQUEST_TIMEOUT)) {
				eb.append(model.loggedInCustomer.readComments());
				// Verify and handle response
				if (!eb.isError()) {
					setView("New Order");
					pnlHeader.showNavigationBar("LOGGED_IN");
					userAccountHeader.setCustomerName(model.loggedInCustomer.readObject().getName());
					userAccountHeader.setVisible(true);
				}
			} else {
				eb.addError("Connection successful but server did not reply");
			}
		} else {
			eb.addError("No response from server");
		}
		// Alert user of any errors
		if (eb.isError()) {
			JOptionPane.showMessageDialog(null, eb.listComments("Login Failed"), "Login Failed",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Logout the current user of the model and application. Update the view
	 * respectively.
	 */
	public void logout() {
		model.logout();
		setView("Login");
		pnlHeader.showNavigationBar("LOGGED_OUT");
		userAccountHeader.setVisible(false);
	}

	/**
	 * Create a thread that calls refresh on the current view every time a set refresh rate expires.
	 */
	private void createViewRefresh() {
		// Create view refresh
		new Thread() {
			@Override
			public void run() {
				while (true) {
					// Wait for given refresh rate
					try {
						Thread.sleep(REFRESH_RATE);
					} catch (final InterruptedException e) {
						break;
					}
					// Refresh current view, note that this call is not on the EDT
					refreshView(getView());
				}
			}
		}.start();
	}

	@Override
	public boolean addView(Component component, String name, NavigationBar[] navigationBars) {
		// Do not add if not a view or view of same name exists
		if (!(component instanceof View) || views.containsKey(name)) {
			System.err.println("[APPLICATION] : Unable to add view");
			return false;
		}
		// Add component to card layout
		pnlView.add(component);
		cl_pnlView.addLayoutComponent(component, name);
		// Add to header's navigation bar if it should be accessible directly
		for (NavigationBar navigationBar : navigationBars) {
			navigationBar.addNavigationButton(name);
		}
		// Keep track of relevant view interfaces
		views.put(name, (View) component);
		return true;
	}
	
	@Override
	public void setView(String view) {
		cl_pnlView.show(pnlView, view);
		pnlHeader.setPage(view, this);
		synchronized (viewLock) {
			currentView = views.get(view);
		}
		initialiseView(currentView);
		getRootPane().setDefaultButton(currentView.getAcceptButton());
	}

	@Override
	public View getView() {
		synchronized (viewLock) {
			return currentView;
		}
	}

}
