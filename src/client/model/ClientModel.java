package client.model;

import java.awt.EventQueue;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import business.model.BusinessLocation;
import business.model.BusinessMessageHandler;
import business.model.Customer;
import business.model.CustomerLogin;
import business.model.Dish;
import business.model.Order;
import business.model.Postcode;
import client.gui.ClientApplication;
import client.gui.ClientLoginFrame;
import client.gui.ClientOrderFrame;
import general.model.Comms;
import general.model.Message;
import general.model.MessageHandler;
import general.model.ObjectMessage;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.UpdateAlert;
import general.utility.UpdateAlertObject;
import implementation.BusinessLocations;

public class ClientModel {
	private static final String HOSTNAME = getHostname();
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
	
	// 
	public final Comms comms;

	// This still doesn't work!
	public static String getHostname() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			return "";
		}
	}

	public ClientModel() {
		// Initialise communications
		BusinessLocation location = BusinessLocations.LOCATIONS.get("Southampton_1");
		System.out.println("[MODEL] : Comms starting...");
		comms = new Comms(ClientModel.ADDRESS, location.getAddress());
		MessageHandler messageHandler = new ClientMessageHandler(this);
		new Thread(messageHandler).start();
		System.out.println("[MODEL] : Comms started");
	}

	public boolean login(CustomerLogin login) {
		ObjectMessage tx = new ObjectMessage(Message.Command.SUBMIT_LOGIN);
		tx.addObject("CUSTOMER_LOGIN", login);
		return comms.sendMessage(tx);
	}

	public boolean register(Customer customer) {
		ObjectMessage tx = new ObjectMessage(Message.Command.REGISTER_NEW_CUSTOMER);
		tx.addObject("CUSTOMER", customer);
		return comms.sendMessage(tx);
	}
	
	public ErrorBuilder logout(ErrorBuilder reason) {
		// Reset statuses
		orderReponse.write(reason);
		registerResponse.write(reason);
		// Clear storage
		loggedInCustomer.write(null, reason);
		postcodes.write(null, reason);
		dishes.write(null, reason);
		orders.write(null, reason);
		return null;	
	}
	
	public boolean refreshPostcodes() {
		// Request postcode update
		Message message = new Message(Message.Command.GET_POSTCODES);
		return comms.sendMessage(message);
	}
	
	public boolean refreshOrders() {
		return false;	
	}
	
	public boolean refreshDishes() {
		return false;	
	}

	public boolean sendOrder(Order order) {
		return false;	
	}
	
}
