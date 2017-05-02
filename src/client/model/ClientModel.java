package client.model;

import java.awt.EventQueue;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import business.model.BusinessLocation;
import business.model.Customer;
import business.model.Dish;
import business.model.Order;
import business.model.Postcode;
import client.gui.ClientLoginFrame;
import client.gui.ClientOrderFrame;
import general.model.Comms;
import general.model.MessageHandler;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.MonitoredObject;
import implementation.BusinessLocations;

public class ClientModel {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 0; // auto-set
	public static final InetSocketAddress ADDRESS = new InetSocketAddress(HOSTNAME, PORT);

	public static final int GET_LOGIN_TIMEOUT = 1000;
	public static final int GET_POSTCODES_TIMEOUT = 1000;
	public static final int GET_DISHES_TIMEOUT = 1000;
	public static final int GET_ORDERS_TIMEOUT = 1000;

	private ClientLoginFrame loginForm;
	private ClientOrderFrame orderForm;

	// Server updatable information
	private MonitoredObject<Customer> customer;
	private MonitoredObject<Postcode[]> postcodes;
	private MonitoredObject<QuantityMap<Dish>> dishes;
	private MonitoredObject<Order[]> orders;
	private MonitoredObject<Order> order;

	public final Comms comms;

	/**
	 * Launch the client application.
	 */
	public static void main(String[] args) {
		new ClientModel();
	}

	// This still doesn't work!
	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "";
		}
	}

	public ClientModel() {
		// Initialise communication structures
		clearServerData();

		// Initialise communications
		BusinessLocation location = BusinessLocations.LOCATIONS.get("Southampton_1");
		comms = new Comms(ClientModel.ADDRESS, location.getAddress());
		MessageHandler messageHandler = new ClientMessageHandler(this);
		new Thread(messageHandler).start();

		// Show the login screen
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					loginForm = new ClientLoginFrame(ClientModel.this);
					loginForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	public void clearServerData() {
		customer = new MonitoredObject<>();
		postcodes = new MonitoredObject<>();
		dishes = new MonitoredObject<>();
		orders = new MonitoredObject<>();
		order = new MonitoredObject<>();
	}

	public Comms getComms() {
		return comms;
	}

	public MonitoredObject<Customer> getCustomer() {
		return customer;
	}

	public MonitoredObject<Postcode[]> getPostcodes() {
		return postcodes;
	}

	public MonitoredObject<QuantityMap<Dish>> getDishes() {
		return dishes;
	}

	public MonitoredObject<Order[]> getOrders() {
		return orders;
	}

	public MonitoredObject<Order> getOrder() {
		return order;
	}

	public ErrorBuilder login() {
		ErrorBuilder eb = new ErrorBuilder();
		if (customer.waitForNew(GET_LOGIN_TIMEOUT)) {
			if (customer.read() != null) {
				// !!! Put on EDT
				// Open new order frame
				orderForm = new ClientOrderFrame(this);
				orderForm.setVisible(true);
				// Close current login frame
				loginForm.dispose();
			} else {
				eb.append(customer.getErrorBuilder());
			}
		} else {
			eb.addError("No response from server");
		}
		return eb;
	}

	public void logout() {
		clearServerData();
		// !!! Put on EDT
		// Open new login frame
		loginForm = new ClientLoginFrame(this);
		loginForm.setVisible(true);
		// Close current order frame
		orderForm.dispose();
	}

}
