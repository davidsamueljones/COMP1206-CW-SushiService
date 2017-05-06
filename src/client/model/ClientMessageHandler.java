package client.model;

import business.model.Customer;
import business.model.Dish;
import business.model.Order;
import business.model.Postcode;
import general.model.Message;
import general.model.MessageHandler;
import general.model.ObjectMessage;
import general.model.QuantityMap;

public class ClientMessageHandler extends MessageHandler {
	ClientModel model;

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
				//handleDishesUpdate(rx);
				break;
			case UPDATE_EXISTING_ORDERS:
				//handleExistingOrdersUpdate(rx);
				break;
			case ORDER_RESPONSE:
				//handleNewOrder(rx);
				break;
			default:
				throw new IllegalArgumentException(String.format(
						"Unsupported message type '%s'", rx.getCommand()));
		}
	}

	private void handleRegistration(Message rx) {
		model.registerResponse.write(rx.getErrorBuilder());
	}

	public void handleLogin(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		final Customer customer = rxObj.getObject("CUSTOMER", Customer.class);
		// Store message objects locally
		model.loggedInCustomer.write(customer, rx.getErrorBuilder());
	}

	public void handlePostcodeUpdate(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		final Postcode[] postcodes = rxObj.getObject("POSTCODES", Postcode[].class);
		// Store message objects locally
		model.postcodes.write(postcodes, rx.getErrorBuilder());
	}

	private void handleDishesUpdate(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		// Get dishes, use of generics means map must be reconstructed
		final QuantityMap<Dish> dishes = QuantityMap.fromObject(
				rxObj.getObject("DISHES"), Dish.class);
		// Store message objects locally
		model.dishes.write(dishes, rx.getErrorBuilder());
	}

	private void handleExistingOrdersUpdate(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		final Order[] orders = rxObj.getObject("ORDERS", Order[].class);
		// Store message objects locally
		model.orders.write(orders, rx.getErrorBuilder());
	}

	private void handleNewOrder(Message rx) {
		model.orderResponse.write(rx.getErrorBuilder());
	}

}
