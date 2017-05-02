package client.model;

import business.model.Customer;
import business.model.Dish;
import business.model.Order;
import business.model.Postcode;
import general.model.Message;
import general.model.MessageHandler;
import general.model.QuantityMap;

public class ClientMessageHandler extends MessageHandler {
	ClientModel application;

	public ClientMessageHandler(ClientModel application) {
		super(application.getComms());
		this.application = application;
	}

	@Override
	public void handleMessage(Message rx) {
		switch (rx.getCommand()) {
			case APPROVE_LOGIN:
			case REJECT_LOGIN:
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
			case ORDER_ACCEPTED:
			case ORDER_REJECTED:
				handleNewOrder(rx);
				break;
			default:
				System.out.println("Unsupported message type!");
				break;
		}
	}

	public void handleLogin(Message rx) {
		Customer customer = rx.getObjectAs(Customer.class);
		application.getCustomer().write(customer, rx.getErrorBuilder());
	}

	public void handlePostcodeUpdate(Message rx) {
		Postcode[] postcodes = rx.getObjectAs(Postcode[].class);
		application.getPostcodes().write(postcodes, rx.getErrorBuilder());
	}

	private void handleDishesUpdate(Message rx) {
		// Get all dish instances from stock handler (this must be done by reconstruction)
		QuantityMap<Dish> dishes = QuantityMap.fromObject(rx.getObject(), Dish.class);
		application.getDishes().write(dishes, rx.getErrorBuilder());
	}

	private void handleExistingOrdersUpdate(Message rx) {
		Order[] orders = rx.getObjectAs(Order[].class);
		application.getOrders().write(orders, rx.getErrorBuilder());
	}

	private void handleNewOrder(Message rx) {
		Order order = rx.getObjectAs(Order.class);
		application.getOrder().write(order, rx.getErrorBuilder());
	}

}
