public class BusinessMessageHandler implements Runnable {
	BusinessApplication application;

	public BusinessMessageHandler(BusinessApplication application) {
		this.application = application;
	}

	public void handleMessage(Message message) {
		System.out.println(message);
		switch (message.getCommand()) {
		case REGISTER_NEW_CUSTOMER:
			handleRegistration(message);
			break;
		case SUBMIT_LOGIN:
			handleLogin(message);
			break;
		case GET_DISHES:
			break;
		case GET_EXISTING_ORDERS:
			break;


		case SUBMIT_ORDER:
			break;
		default:
			System.out.println("Unsupported message type!");
			break;
		}
	}
	
	public void handleRegistration(Message rxMessage) {
		Customer sentCustomer = rxMessage.getObjectAs(Customer.class);
		// Verify user account
		Message txMessage;
		if (application.findLogin(sentCustomer.getLogin()) != null) {
			txMessage = new Message(Message.Command.REJECT_LOGIN, 
					"Username already exists", null);
		}
		else {
			application.addCustomer(sentCustomer);
			txMessage = new Message(Message.Command.APPROVE_LOGIN, "", sentCustomer);
		}
		// Reply with message 
		application.getComms().sendMessage(txMessage, rxMessage.getSender());
	}
	
	public void handleLogin(Message rxMessage) {
		CustomerLogin sentLogin = rxMessage.getObjectAs(CustomerLogin.class); 
		Customer storedCustomer = application.findLogin(sentLogin);
		// Find password (null if no account stored)
		String storedPasswordHash = null;
		if (storedCustomer != null) {
			storedPasswordHash = storedCustomer.getLogin().getPasswordHash();
		}
		// Verify stored password == sent password 
		Message txMessage;
		if (sentLogin.getPasswordHash().equals(storedPasswordHash)) {
			txMessage = new Message(Message.Command.APPROVE_LOGIN, "", storedCustomer);
		}
		else {
			txMessage = new Message(Message.Command.REJECT_LOGIN, 
					"Incorrect username or password", null);
		}
		// Reply with message 
		application.getComms().sendMessage(txMessage, rxMessage.getSender());
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
