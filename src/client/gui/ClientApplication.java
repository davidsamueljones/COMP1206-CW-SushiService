package client.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import business.gui.CustomersPanel;
import business.gui.DishesPanel;
import business.gui.IngredientsPanel;
import business.gui.OrdersPanel;
import business.gui.SuppliersPanel;
import business.model.BusinessLocation;
import business.model.BusinessModel;
import business.model.CustomerLogin;
import client.model.ClientModel;
import general.gui.Header;
import general.gui.UserAccountPanel;
import general.gui.View;
import general.gui.ViewHandler;
import general.utility.ErrorBuilder;
import general.utility.Utilities;

public class ClientApplication extends JFrame implements ViewHandler {
	private static final long serialVersionUID = 554152094859674365L;
	// Refresh rate of displayed data
	private static final int REFRESH_RATE = 1000; // ms
	public static final int REQUEST_TIMEOUT = 1000;
	
	// Location being served
	private final BusinessLocation location;
	// Data model being served
	public final ClientModel model;
	
	// Header
	private Header pnlHeader;
	// Content (View)
	private JPanel pnlView;
	private CardLayout cl_pnlView;
	private HashMap<String, View> views;
	private View currentView;
	private Object viewLock;

	private ClientLoginFrame loginForm;
	private ClientOrderFrame orderForm;
	
	public ClientApplication(BusinessLocation location) {
		// Pre-initialisation
		Utilities.setNaturalGUI();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.location = location;
		this.model = new ClientModel();
		
		// Initialise GUI
		initGUI();
		createViewRefresh();

		// Set GUI Properties
		//setMinimumSize(new Dimension(810, 300));
		pack();
		setLocationRelativeTo(null);
	}

	private void initGUI() {
		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);
		
		// Create header
		pnlHeader = new Header(this, "Client Application");
		contentPane.add(pnlHeader, BorderLayout.NORTH);

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
		addView(pnlLogin, "Login", true);
		final JPanel pnlRegister = new RegisterPanel(this);
		addView(pnlRegister, "Register", true);		
		final JPanel pnlNewOrder = new NewOrderPanel();
		addView(pnlNewOrder, "New Order", true);
		final JPanel pnlViewOrder = new ViewOrdersPanel();
		addView(pnlViewOrder, "View Orders", true);
		final JPanel pnlUserAccount = new ViewOrdersPanel();
		addView(pnlUserAccount, "User Account", true);
		


		// Set initial selection to ingredients page
		setView("Login");
	}

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
					pnlHeader.setAccountPanel(new UserAccountPanel());
				}	
			}
		} else {
			eb.addError("No response from server");
		}
		// Alert user of any errors
		if (eb.isError()) {
			JOptionPane.showMessageDialog(null, eb.listComments("Login Failed"),
					"Login Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void logout(ErrorBuilder reason) {
		model.logout(reason);
		setView("Login");
	}
	
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
	public boolean addView(Component component, String name, boolean navigable) {
		// Do not add if not a view or view of same name exists
		if (!(component instanceof View) || views.containsKey(name)) {
			System.err.println("[APPLICATION] : Unable to add view");
			return false;
		}
		// Add component to card layout
		pnlView.add(component);
		cl_pnlView.addLayoutComponent(component, name);
		// Add to header's navigation bar if it should be accessible directly
		if (navigable) {
			pnlHeader.getNavigationBar().addNavigationButton(name);
		}
		// Keep track of relevant view interfaces
		views.put(name, (View) component);
		return true;
	}

	@Override
	public void setView(String view) {
		cl_pnlView.show(pnlView, view);
		pnlHeader.setPage(view);
		pnlHeader.getNavigationBar().setSelected(view);
		synchronized (viewLock) {
			currentView = views.get(view);
		}
		initialiseView(currentView);
	}

	@Override
	public View getView() {
		synchronized (viewLock) {
			return currentView;
		}
	}
	
}
