package business.model;

import java.time.LocalDateTime;
import java.util.Set;

import general.model.Message;
import general.model.MessageHandler;
import general.model.ObjectMessage;
import general.model.QuantityMap;
import general.utility.ErrorBuilder;

/**
 * Extension of MessageHandler that handles the influence of a received message 
 * on the business side.
 * 
 * @author David Jones [dsj1n15]
 */
public class BusinessMessageHandler extends MessageHandler {
	private final BusinessModel model;

	/**
	 * Instantiate a new message handler that has influence over the business model. 
	 * 
	 * @param model Business model to interact with
	 */
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
			case MODIFY_CUSTOMER:
				handleModifyCustomer(rx);
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
				throw new IllegalArgumentException(
						String.format("Unsupported message type '%s'", rx.getCommand()));
		}
	}

	/**
	 * Handle a new registration, expect a fully defined customer object 
	 * attached to the message. Do not add new customer if customer is not fully defined
	 * or login exists.
	 * 
	 * @param rx Object message with customer attached
	 */
	private void handleRegistration(Message rx) {
		// Get expected objects from message
		ObjectMessage rxObj = expectObjectMessage(rx);
		final Customer sentCustomer = rxObj.getObject("CUSTOMER", Customer.class);

		// Validate locally
		final ErrorBuilder eb = sentCustomer.validate();
		if (!eb.isError()) {
			// Check for existence of account
			synchronized (model.customers) {
				if (!model.customers.containsKey(sentCustomer.getLogin())) {
					// Add customer to allowed logins
					model.customers.put(sentCustomer.getLogin(), sentCustomer);
				} else {
					eb.addError("Username already exists");
				}
			}
		}

		// Reply with registration response, eb contains error on failure
		reply(rx, new Message(Message.Command.REGISTER_RESPONSE, eb));
	}

	/**
	 * Handle a login attempt, expect a fully defined customer login object attached
	 * to the message. Reject login if login does not exist or password does not match
	 * expected.
	 * 
	 * @param rx Object message with customer login attached
	 */
	private void handleSubmitLogin(Message rx) {
		// Get expected objects from message
		ObjectMessage rxObj = expectObjectMessage(rx);
		final CustomerLogin sentLogin = rxObj.getObject("CUSTOMER_LOGIN", CustomerLogin.class);
		
		Customer storedCustomer;
		final ErrorBuilder eb = new ErrorBuilder();
		synchronized (model.customers) {
			// Find password (null if no account stored)
			storedCustomer = model.customers.get(sentLogin);
			String storedPasswordHash = null;
			if (storedCustomer != null) {
				storedPasswordHash = storedCustomer.getLogin().getPasswordHash();
			}
			// Verify stored password is the same as sent password
			if (!sentLogin.getPasswordHash().equals(storedPasswordHash)) {
				eb.addError("Incorrect username or password");
				// Clear customer from variable so it is not sent
				storedCustomer = null;
			}		
		}

		// Reply with login response, CUSTOMER object null on failure and eb contains error
		ObjectMessage tx = new ObjectMessage(Message.Command.LOGIN_RESPONSE, eb);
		tx.addObject("CUSTOMER", storedCustomer);
		reply(rx, tx);
	}
	
	/**
	 * Handle changes to an existing customer, expect an existing customer for reference and
	 * a fully defined customer object to replace the existing customer. Do not make changes
	 * if customer is not fully defined/valid or reference customer does not exist.
	 * 
	 * @param rx Object message with two customers attached
	 */
	private void handleModifyCustomer(Message rx) {
		// Get expected objects from message
		ObjectMessage rxObj = expectObjectMessage(rx);
		final Customer refCustomer = rxObj.getObject("EXISTING_CUSTOMER", Customer.class);
		final Customer modCustomer = rxObj.getObject("MODIFIED_CUSTOMER", Customer.class);
		
		// Validate locally
		final ErrorBuilder eb = modCustomer.validate();
		if (!eb.isError()) {
			// Check for existence of account
			synchronized (model.customers) {
				if (model.customers.containsKey(refCustomer.getLogin())) {
					// Replace existing customer with new login
					model.customers.put(modCustomer.getLogin(), modCustomer);
				} else {
					eb.addError("Reference account does not exist");
				}
			}
		}

		// Reply with login response, CUSTOMER object null on failure and eb contains error
		ObjectMessage tx = new ObjectMessage(Message.Command.LOGIN_RESPONSE, eb);
		tx.addObject("CUSTOMER", modCustomer);
		reply(rx, tx);
	}

	/**
	 * Handle a postcode request. Reply with all postcodes served.
	 * 
	 * @param rx Received request message
	 */
	private void handleGetPostcodes(Message rx) {
		// Reply with array object of postcodes served by business
		final Postcode[] postcodeArray;
		synchronized (model.postcodes) {
			postcodeArray = model.postcodes.toArray(new Postcode[model.postcodes.size()]);
		}
		final ObjectMessage tx = new ObjectMessage(Message.Command.UPDATE_POSTCODES);
		tx.addObject("POSTCODES", postcodeArray);
		reply(rx, tx);
	}

	/**
	 * Handle a dish request. Reply with all dishes and available stock.
	 * 
	 * @param rx Received request message
	 */
	private void handleGetDishStock(Message rx) {
		// Reply with map of dishes and their available stock
		final QuantityMap<Dish> dishes = model.stock.dishes.getStockAvailable();
		final ObjectMessage tx = new ObjectMessage(Message.Command.UPDATE_DISH_STOCK);
		tx.addObject("DISHES", dishes);
		reply(rx, tx);
	}

	/**
	 * Handle an order request, expect a customer login attached to the message to filter
	 * orders to return. Reply with orders found.
	 * 
	 * @param rx Received request message
	 */
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

	/**
	 * Handle a new order, expect an order object with dishes and a customer to be defined and
	 * attached. Accept order if validation passes, customer postcode is still served by the business
	 * and there is enough existing stock to accept the order.
	 * 
	 * @param rx Object message with order attached
	 */
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
		synchronized (model.postcodes){
			if (!model.postcodes.contains(customer.getPostcode())) {
				eb.addError("Customer postcode not served by business");
			}
		}

		// Attempt to reserve stock (if an error hasn't occurred)
		if (!eb.isError()) {
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
		}
		
		// Reply with order response, eb contains error on failure
		reply(rx, new Message(Message.Command.ORDER_RESPONSE, eb));
	}

}
