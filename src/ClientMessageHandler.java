public class ClientMessageHandler implements Runnable {
	ClientApplication application;

	public ClientMessageHandler(ClientApplication application) {
		this.application = application;
	}

	public void handleMessage(Message message) {
		System.out.println(message);
		switch (message.getCommand()) {
		case APPROVE_LOGIN:
		case REJECT_LOGIN:
			System.out.println(message.getCommand());
			handleLogin(message);
			break;
		case UPDATE_DISHES:
			break;
		case UPDATE_EXISTING_ORDERS:
			break;
		case ORDER_ACCEPTED:
			break;
		default:
			System.out.println("Unsupported message type!");
			break;
		}
	}

	public void handleLogin(Message message) {
		Customer customer = message.getObjectAs(Customer.class);
		application.setLoggedIn(customer);
	}
	
	@Override
	public void run() {
		// Get communications
		Comms comms = application.getComms();
		// Handle all messages in message queue
		while (true) {
			handleMessage(comms.receiveMessage());
		}	
	}

}
