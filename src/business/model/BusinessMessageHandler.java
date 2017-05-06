package business.model;

import java.time.LocalDateTime;
import java.util.Set;

import general.model.Message;
import general.model.MessageHandler;
import general.model.ObjectMessage;
import general.model.Quantity;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;

public class BusinessMessageHandler extends MessageHandler {
	private final BusinessModel model;

	public BusinessMessageHandler(BusinessModel model) {
		super(model.getComms());
		this.model = model;
	}

	@Override
	protected void handleMessage(Message rx) {
		switch (rx.getCommand()) {
			case REGISTER_NEW_CUSTOMER:
				handleRegistration(rx);
				break;
			case SUBMIT_LOGIN:
				handleSubmitLogin(rx);
				break;
			case GET_POSTCODES:
				handleGetPostcodes(rx);
				break;
			case GET_DISH_STOCK:
				handleGetDishStock(rx);
				break;
			case GET_EXISTING_ORDERS:
				handleGetExistingOrders(rx);
				break;
			case SUBMIT_ORDER:
				handleSubmitOrder(rx);
				break;
			default:
				throw new IllegalArgumentException(String.format(
						"Unsupported message type '%s'", rx.getCommand()));
		}
	}

	private void handleRegistration(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		final Customer sentCustomer = rxObj.getObject("CUSTOMER", Customer.class);
		
		// Validate locally
		final ErrorBuilder eb = sentCustomer.validate();
		// Check for existence of account
		if (model.customers.get(sentCustomer.getLogin()) == null) {
			// Add customer to allowed logins
			model.addCustomer(sentCustomer);
		} else {
			eb.addError("Username already exists");
		}
		
		// Reply with registration response, eb contains error on failure
		reply(rx, new Message(Message.Command.REGISTER_RESPONSE, eb));
	}

	private void handleSubmitLogin(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		final CustomerLogin sentLogin = rxObj.getObject("CUSTOMER_LOGIN", CustomerLogin.class);
		
		// Find password (null if no account stored)
		Customer storedCustomer = model.customers.get(sentLogin);
		String storedPasswordHash = null;
		if (storedCustomer != null) {
			storedPasswordHash = storedCustomer.getLogin().getPasswordHash();
		}
		// Verify stored password is the same as sent password
		final ErrorBuilder eb = new ErrorBuilder();
		if (!sentLogin.getPasswordHash().equals(storedPasswordHash)) {
			eb.addError("Incorrect username or password");
			// Clear customer from variable so it is not sent
			storedCustomer = null;
		}
		
		// Reply with login response, CUSTOMER object null on failure and eb contains error
		ObjectMessage tx = new ObjectMessage(Message.Command.LOGIN_RESPONSE, eb);
		tx.addObject("CUSTOMER", storedCustomer);
		reply(rx, tx);
		
	}

	private void handleGetPostcodes(Message rx) {
		// Reply with array object of postcodes served by business
		final Set<Postcode> postcodes = model.postcodes;
		final Postcode[] postcodeArray = postcodes.toArray(new Postcode[postcodes.size()]);
		final ObjectMessage tx = new ObjectMessage(Message.Command.UPDATE_POSTCODES);
		tx.addObject("POSTCODES", postcodeArray);
		reply(rx, tx);
	}

	private void handleGetDishStock(Message rx) {
		// Reply with map of dishes and their available stock
		final QuantityMap<Dish> dishes = model.stock.dishes.getStockAvailable();
		final ObjectMessage tx = new ObjectMessage(Message.Command.UPDATE_DISH_STOCK);
		tx.addObject("DISHES", dishes);
		reply(rx, tx);
	}

	private void handleGetExistingOrders(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		final CustomerLogin sentLogin = rxObj.getObject("CUSTOMER_LOGIN", CustomerLogin.class);
		
		// Reply with set of customer orders
		final Set<Order> orders = model.getOrdersFromCustomer(sentLogin);
		final Order[] orderArray = orders.toArray(new Order[orders.size()]);
		final ObjectMessage tx = new ObjectMessage(Message.Command.UPDATE_EXISTING_ORDERS);
		tx.addObject("ORDERS", orderArray);
		reply(rx, tx);
	}

	private void handleSubmitOrder(Message rx) {
		// Get expected objects from message	
		ObjectMessage rxObj = expectObjectMessage(rx);
		final Order order = rxObj.getObject("ORDER", Order.class);
		
		// Update order time to use business time
		order.setDate(LocalDateTime.now());
		order.setStatus(Order.Status.RECEIVED);

		// Validate order locally
		ErrorBuilder eb = order.validate();
		// Check postcode of customer is being served
		Customer customer = order.getCustomer();
		if (model.postcodes.contains(customer.getPostcode())) {
			eb.addError("Customer postcode not served by business");
		}

		// Attempt to reserve stock
		if (model.stock.dishes.reserveStock(order.getDishes())) {
			// Stock reserved, make order
			order.setStatus(Order.Status.READY_FOR_DISPATCH);
			synchronized (model.orders) {
				model.orders.add(order);
			}
		} else {
			// Not enough stock
			eb.addError("Not enough stock available to complete order");
		}
		// Reply with order response, eb contains error on failure
		reply(rx, new Message(Message.Command.ORDER_RESPONSE, eb));
	}

}
