package business.model;

import java.time.LocalDateTime;
import java.util.Set;

import general.model.Message;
import general.model.MessageHandler;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;

public class BusinessMessageHandler extends MessageHandler {
	private final BusinessModel application;

	public BusinessMessageHandler(BusinessModel application) {
		super(application.getComms());
		this.application = application;
	}

	@Override
	public void handleMessage(Message rx) {
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
				System.err.println("Unsupported message type : " + rx.getCommand());
				break;
		}
	}

	private void handleRegistration(Message rx) {
		final Customer sentCustomer = rx.getObjectAs(Customer.class);
		// Verify user account
		Message tx;
		if (application.findLogin(sentCustomer.getLogin()) == null) {
			application.addCustomer(sentCustomer);
			tx = new Message(Message.Command.APPROVE_LOGIN, null, sentCustomer);
		} else {
			final ErrorBuilder eb = new ErrorBuilder();
			eb.addError("Username already exists");
			tx = new Message(Message.Command.REJECT_LOGIN, eb);
		}
		reply(rx, tx);
	}

	private void handleSubmitLogin(Message rx) {
		final CustomerLogin sentLogin = rx.getObjectAs(CustomerLogin.class);
		final Customer storedCustomer = application.findLogin(sentLogin);
		// Find password (null if no account stored)
		String storedPasswordHash = null;
		if (storedCustomer != null) {
			storedPasswordHash = storedCustomer.getLogin().getPasswordHash();
		}
		// Verify stored password is the same as sent password
		Message tx;
		final ErrorBuilder eb = new ErrorBuilder();
		if (sentLogin.getPasswordHash().equals(storedPasswordHash)) {
			tx = new Message(Message.Command.APPROVE_LOGIN, eb, storedCustomer);
		} else {
			eb.addComment("Incorrect username or password", true);
			tx = new Message(Message.Command.REJECT_LOGIN, eb);
		}
		reply(rx, tx);
	}

	private void handleGetPostcodes(Message rx) {
		final Set<Postcode> postcodes = application.postcodes;
		final Postcode[] postcodeArray = postcodes.toArray(new Postcode[postcodes.size()]);
		final Message tx = new Message(Message.Command.UPDATE_POSTCODES, null, postcodeArray);
		reply(rx, tx);
	}

	private void handleGetDishStock(Message rx) {
		final QuantityMap<Dish> dishes = application.stock.dishes.getStockAvailable();
		final Message tx = new Message(Message.Command.UPDATE_DISH_STOCK, null, dishes);
		reply(rx, tx);
	}

	private void handleGetExistingOrders(Message rx) {
		final Customer sentCustomer = rx.getObjectAs(Customer.class);
		final Set<Order> orders = application.getOrdersFromCustomer(sentCustomer);
		final Order[] orderArray = orders.toArray(new Order[orders.size()]);
		final Message tx = new Message(Message.Command.UPDATE_EXISTING_ORDERS, null, orderArray);
		reply(rx, tx);
	}

	private void handleSubmitOrder(Message rx) {
		final Order order = rx.getObjectAs(Order.class);
		// Update order time to use business time
		order.setDate(LocalDateTime.now());
		order.setStatus(Order.Status.RECEIVED);

		// !!! Check order not empty (could be done other end)
		// !!! Check prices have not changed

		// Attempt to reserve stock
		Message tx;
		if (application.stock.dishes.reserveStock(order.getDishes())) {
			// Stock reserved
			order.setStatus(Order.Status.READY_FOR_DISPATCH);
			application.orders.add(order);
			tx = new Message(Message.Command.ORDER_ACCEPTED, null, order);
		} else {
			// Not enough stock
			final ErrorBuilder eb = new ErrorBuilder();
			eb.addError("Not enough stock available to complete order");
			tx = new Message(Message.Command.ORDER_REJECTED, eb);
		}
		reply(rx, tx);
	}

}
