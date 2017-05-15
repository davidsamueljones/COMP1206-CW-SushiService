package business.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import business.model.BusinessLocation;
import business.model.BusinessModel;
import business.model.DataPersistence;
import general.gui.Header;
import general.gui.NavigationBar;
import general.gui.View;
import general.gui.ViewHandler;
import general.utility.Utilities;

/**
 * Main application class for handling a business model. Holds record editing panels, managing
 * navigation and refresh behaviour using view handling interfaces. In addition handles persistence
 * layer shutdown behaviour. Only a single instance of this class can be instantiated on the same
 * system; this is to protect data persistence files.
 *
 * @author David Jones [dsj1n15]
 */
public class BusinessApplication extends JFrame implements ViewHandler {
	private static final long serialVersionUID = 329700147679546872L;
	// Refresh rate of displayed data
	private static final int REFRESH_RATE = 1000; // ms

	// Location being served
	private final BusinessLocation location;
	// Data model being served
	private BusinessModel model;

	// Header
	private Header pnlHeader;
	// Content (View)
	private JPanel pnlView;
	private CardLayout cl_pnlView;
	private HashMap<String, View> views;
	private View currentView;
	private Object viewLock;

	/**
	 * Instantiates the business and GUI.
	 */
	public BusinessApplication(BusinessLocation location) {
		// Pre-initialisation
		Utilities.setNaturalGUI();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.location = location;

		// Set model that GUI should serve
		setModel();
		// Start server in model
		final boolean serverStarted = model.startComms(location.getAddress());
		// Stop any influence of model and GUI construction if communications
		// not instantiated as this implies existing model open
		if (!serverStarted) {
			JOptionPane.showMessageDialog(null,
					"Error Hosting Server: " + "The port number may be in use.\r\n"
							+ "The program will now exit.\r\n"
							+ "Close any other instances of the business application before reopening.",
					"Server Error", JOptionPane.ERROR_MESSAGE);
			// Throw exception to halt construction
			throw new RuntimeException("Unable to host server");
		}

		// Initialise GUI
		initGUI();
		createViewRefresh();
		// Handle model shutdown
		addModelShutdownHandler();
		// Resume model behaviour
		model.resume();

		// Set GUI Properties
		setMinimumSize(new Dimension(810, 300));
		pack();
		setLocationRelativeTo(null);
	}

	/**
	 * Check if a model exists using the persistence layer. If it does, attempt to load it. If there
	 * is a problem throw an unchecked exception to indicate that there is a problem with the
	 * existing model. If no model exists a new model can be created safely.
	 */
	private void setModel() {
		// Check for model from persistence
		Object stored;
		try {
			stored = DataPersistence.get(location.getIdentifier() + "-DM");
			if (stored instanceof BusinessModel) {
				System.out.println("[APPLICATION] : Using existing model...");
				model = (BusinessModel) stored;
			} else {
				// Throw exception to halt construction
				JOptionPane.showMessageDialog(null,
						"Error Loading Model: " + "The stored model may be corrupted.\r\n"
								+ "The program will now exit without changing the stored file.\r\n"
								+ "Please manually delete the stored model to construct a new model.",
						"Persistence Error", JOptionPane.ERROR_MESSAGE);
				throw new RuntimeException("Unable to load model");
			}
		} catch (FileNotFoundException e) {
			// No model exists so create a new model
			System.out.println("[APPLICATION] : Creating new model...");
			model = new BusinessModel();
		}
	}

	/**
	 * Ensure behaviour of model on shutdown is suspended safely and data changes persist.
	 */
	private void addModelShutdownHandler() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Let model safely suspend its state. It is likely threads already interrupted
				// by shutdown but explicit call forces wait so model is in final state before save.
				model.suspend();
				// Save model using data persistence class
				try {
					System.out.println("[APPLICATION] : Storing model...");
					DataPersistence.store(location.getIdentifier() + "-DM", model);
					System.out.println("[APPLICATION] : Model stored");
				} catch (FileNotFoundException e) {
					System.err.println("[APPLICATION] : Unable to store model");
				}
			}
		});
	}

	/**
	 * Instantiate the frames GUI.
	 */
	private void initGUI() {
		final JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		setContentPane(contentPane);

		// Create header
		pnlHeader = new Header("Business Application");
		contentPane.add(pnlHeader, BorderLayout.NORTH);
		final NavigationBar navigationBar = pnlHeader.addNavigationBar("MAIN", this);

		// Create content viewing panel
		pnlView = new JPanel();
		cl_pnlView = new CardLayout();
		pnlView.setLayout(cl_pnlView);
		contentPane.add(pnlView, BorderLayout.CENTER);
		// Keep track of views added
		views = new HashMap<>();
		viewLock = new Object();

		// Create views and add them to viewing panel
		final JPanel pnlIngredients = new IngredientsPanel(model);
		addView(pnlIngredients, "Ingredients", navigationBar);
		final JPanel pnlDishes = new DishesPanel(model);
		addView(pnlDishes, "Dishes", navigationBar);
		final JPanel pnlSuppliers = new SuppliersPanel(model);
		addView(pnlSuppliers, "Suppliers", navigationBar);
		final JPanel pnlCustomers = new CustomersPanel(model);
		addView(pnlCustomers, "Customers", navigationBar);
		final JPanel pnlOrders = new OrdersPanel(model);
		addView(pnlOrders, "Orders", navigationBar);
		final JPanel pnlKitchenStaff = new KitchenStaffPanel(model);
		addView(pnlKitchenStaff, "Kitchen Staff", navigationBar);
		final JPanel pnlDrones = new DronesPanel(model);
		addView(pnlDrones, "Drones", navigationBar);
		final JPanel pnlPostcodes = new PostcodesPanel(model);
		addView(pnlPostcodes, "Postcodes", navigationBar);

		// Set initial selection to ingredients page
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setView("Ingredients");
			}
		});
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
					} catch(InterruptedException e) {
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
		for (final NavigationBar navigationBar : navigationBars) {
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
	}

	@Override
	public View getView() {
		synchronized (viewLock) {
			return currentView;
		}
	}

}
