package client.model;

import business.model.Customer;
import business.model.Dish;
import business.model.Order;
import business.model.Postcode;
import general.model.Message;
import general.model.MessageHandler;
import general.model.ObjectMessage;
import general.model.QuantityMap;

/**
 * Extension of MessageHandler that handles the influence of a received message on the client side.
 *
 * @author David Jones [dsj1n15]
 */
public class ClientMessageHandler extends MessageHandler {
	ClientModel model;

	/**
	 * Instantiate a new message handler that has influence over the client model.
	 *
	 * @param model Client model to interact with
	 */
	public ClientMessageHandler(ClientModel model) {
		super(model.comms);
		this.model = model;
	}

	@Override
	public void handleMessage(Message rx) {
		switch (rx.getCommand()) {
			case REGISTER_RESPONSE:
				handleRegistration(rx);
				break;
			case LOGIN_RESPONSE:
				handleLogin(rx);
				break;
			case UPDATE_POSTCODES:
				handlePostcodeUpdate(rx);
				break;
			case UPDATE_DISH_STOCK:
				handleDishesUpdate(rx);
				break;
			case UPDATE_EXISTING_ORDERS:
				handleExistingOrdersUpdate(rx);
				break;
			case ORDER_RESPONSE:
				handleNewOrder(rx);
				break;
			default:
				throw new IllegalArgumentException(
						String.format("Unsupported message type '%s'", rx.getCommand()));
		}
	}

	/**
	 * Handle the response of a registration attempt. Store response in alert object.
	 *
	 * @param rx Normal message with error builder set
	 */
	private void handleRegistration(Message rx) {
		model.registerResponse.write(rx.getErrorBuilder());
	}

	/**
	 * Handle the response from a login attempt. Expect a message with a customer attached. Null
	 * object implies rejected login. Store response in alert object.
	 *
	 * @param rx Object message with customer attached
	 */
	public void handleLogin(Message rx) {
		// Get expected objects from message
		final ObjectMessage rxObj = expectObjectMessage(rx);
		final Customer customer = rxObj.getObject("CUSTOMER", Customer.class);
		// Store message objects locally
		model.loggedInCustomer.write(customer, rx.getErrorBuilder());
	}

	/**
	 * Handle the response from a postcode request. Expect a message with an array of postcodes
	 * attached. Store response in alert object.
	 *
	 * @param rx Object message with postcodes attached
	 */
	public void handlePostcodeUpdate(Message rx) {
		// Get expected objects from message
		final ObjectMessage rxObj = expectObjectMessage(rx);
		final Postcode[] postcodes = rxObj.getObject("POSTCODES", Postcode[].class);
		// Store message objects locally
		model.postcodes.write(postcodes, rx.getErrorBuilder());
	}

	/**
	 * Handle the response from a dish request. Expect a message with an array of dishes attached.
	 * Store response in alert object.
	 *
	 * @param rx Object message with dishes attached
	 */
	private void handleDishesUpdate(Message rx) {
		// Get expected objects from message
		final ObjectMessage rxObj = expectObjectMessage(rx);
		// Get dishes, use of generics means map must be reconstructed
		final QuantityMap<Dish> dishes =
				QuantityMap.fromObject(rxObj.getObject("DISHES"), Dish.class);
		// Store message objects locally
		model.dishes.write(dishes, rx.getErrorBuilder());
	}

	/**
	 * Handle the response from an existing order request. Expect a message with an array of orders
	 * attached. Store response in alert object.
	 *
	 * @param rx Object message with orders attached
	 */
	private void handleExistingOrdersUpdate(Message rx) {
		// Get expected objects from message
		final ObjectMessage rxObj = expectObjectMessage(rx);
		final Order[] orders = rxObj.getObject("ORDERS", Order[].class);
		// Store message objects locally
		model.orders.write(orders, rx.getErrorBuilder());
	}

	/**
	 * Handle the response of an order attempt. Store response in alert object.
	 *
	 * @param rx Normal message with error builder set
	 */
	private void handleNewOrder(Message rx) {
		model.orderResponse.write(rx.getErrorBuilder());
	}

}
