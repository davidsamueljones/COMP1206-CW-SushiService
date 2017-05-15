package client.model;

import java.net.InetSocketAddress;

import business.model.BusinessLocation;
import business.model.Customer;
import business.model.CustomerLogin;
import business.model.Dish;
import business.model.Order;
import business.model.Postcode;
import general.model.Comms;
import general.model.Message;
import general.model.MessageHandler;
import general.model.ObjectMessage;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.UpdateAlert;
import general.utility.UpdateAlertObject;
import general.utility.Utilities;
import implementation.BusinessLocations;

public class ClientModel {
	private static final String HOSTNAME = Utilities.getHostname();
	private static final int PORT = 0; // auto-set
	public static final InetSocketAddress ADDRESS = new InetSocketAddress(HOSTNAME, PORT);

	// Server Status Flags
	public final UpdateAlert registerResponse = new UpdateAlert();
	public final UpdateAlert orderResponse = new UpdateAlert();
	// Local Server Storage
	public final UpdateAlertObject<Customer> loggedInCustomer = new UpdateAlertObject<>();
	public final UpdateAlertObject<Postcode[]> postcodes = new UpdateAlertObject<>();
	public final UpdateAlertObject<QuantityMap<Dish>> dishes = new UpdateAlertObject<>();
	public final UpdateAlertObject<Order[]> orders = new UpdateAlertObject<>();

	// Message handling
	public final Comms comms;

	public ClientModel(BusinessLocation location) {
		// Initialise communications
		System.out.println("[MODEL] : Comms starting...");
		comms = new Comms(ClientModel.ADDRESS, location.getAddress());
		MessageHandler messageHandler = new ClientMessageHandler(this);
		new Thread(messageHandler).start();
		System.out.println("[MODEL] : Comms started");
	}

	/**
	 * Send a login request to the business.
	 * 
	 * @param login Login object to use for request
	 * @return Whether message successfully sent
	 */
	public boolean login(CustomerLogin login) {
		ObjectMessage tx = new ObjectMessage(Message.Command.SUBMIT_LOGIN);
		tx.addObject("CUSTOMER_LOGIN", login);
		return comms.sendMessage(tx);
	}

	/**
	 * Send a registration request to the business.
	 * 
	 * @param customer Customer object to use for registration request
	 * @return Whether message successfully sent
	 */
	public boolean register(Customer customer) {
		ObjectMessage tx = new ObjectMessage(Message.Command.REGISTER_NEW_CUSTOMER);
		tx.addObject("CUSTOMER", customer);
		return comms.sendMessage(tx);
	}
	
	/**
	 * Send a customer modification request to the business, attempt to modify
	 * the currently logged in customer.
	 * 
	 * @param modified Customer object with modification requests
	 * @return Whether message successfully sent
	 */
	public boolean modifyCustomer(Customer modified) {
		ObjectMessage tx = new ObjectMessage(Message.Command.MODIFY_CUSTOMER);
		tx.addObject("EXISTING_CUSTOMER", loggedInCustomer.readObject());
		tx.addObject("MODIFIED_CUSTOMER", modified);
		return comms.sendMessage(tx);
	}

	/**
	 * Logout the current customer; this results in all server objects being cleared
	 * so that any existing objects do not persist between different users.
	 */
	public void logout() {
		// Reset statuses
		orderResponse.clear();
		registerResponse.clear();
		// Clear storage
		loggedInCustomer.clear();
		postcodes.clear();
		dishes.clear();
		orders.clear();
	}

	/**
	 * Send a postcode served update request to the business.
	 * 
	 * @return Whether message successfully sent
	 */
	public boolean refreshPostcodes() {
		// Request postcode update
		Message tx = new Message(Message.Command.GET_POSTCODES);
		return comms.sendMessage(tx);
	}


	public boolean refreshDishes() {
		// Request dishes update
		Message message = new Message(Message.Command.GET_DISH_STOCK);
		return comms.sendMessage(message);
	}
	
	/**
	 * Send an existing order update request to the business. Use the currently logged
	 * in customer for filtering.
	 * 
	 * @return Whether message successfully sent
	 */
	public boolean refreshOrders() {
		// Request orders update
		ObjectMessage tx = new ObjectMessage(Message.Command.GET_EXISTING_ORDERS);
		tx.addObject("CUSTOMER_LOGIN", loggedInCustomer.readObject().getLogin());
		return comms.sendMessage(tx);
	}

	/**
	 * Send a new order to the business using a list of dishes. Order comes from
	 * the currently logged in customer.
	 * 
	 * @param dishes Dishes and respective quantities to order
	 * @return Whether message successfully sent
	 */
	public boolean sendOrder(QuantityMap<Dish> dishes) {
		Order order = new Order(loggedInCustomer.readObject(), dishes);
		ObjectMessage tx = new ObjectMessage(Message.Command.SUBMIT_ORDER);
		tx.addObject("ORDER", order);
		return comms.sendMessage(tx);
	}

}
