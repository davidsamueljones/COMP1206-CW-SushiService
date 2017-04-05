import java.awt.EventQueue;
import java.net.InetSocketAddress;

public class ClientApplication {
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 23522;
	public static final InetSocketAddress ADDRESS = new InetSocketAddress(HOSTNAME, PORT);
	
	private ClientLoginFrame loginForm;
	private ClientOrderFrame orderForm;
	private ClientMessageHandler messageHandler;
	
	private Customer loggedIn;
	private String loginMessage = "";
	
	private Comms comms;
	
	public ClientApplication() {
		comms = new Comms(ClientApplication.ADDRESS, BusinessApplication.ADDRESS);
		messageHandler = new ClientMessageHandler(this);
		(new Thread(messageHandler)).start();
		
		// Show the login screen
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginForm = new ClientLoginFrame(ClientApplication.this);
					loginForm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Comms getComms() {
		return comms;
	}	
	
	/**
	 * Launch the client application.
	 */
	public static void main(String[] args) {
		new ClientApplication();
	}

	public Customer getLoggedIn() {
		synchronized (loginMessage) {
			if (loggedIn == null) {
				try {
					loginMessage.wait(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return loggedIn;
		}
		
	}

	public void setLoggedIn(Customer loggedIn) {
		synchronized (loginMessage) {
			this.loggedIn = loggedIn;
			loginMessage.notifyAll();
		}
	}

	public void login() {
		if (getLoggedIn() != null) {
			// Open new order frame
			orderForm = new ClientOrderFrame();
			orderForm.setVisible(true);
			// Close current login frame
			loginForm.dispose();
		}
		else {
			System.out.println("No user login details!");
		}
	}

	public void logout() {
		setLoggedIn(null);
		
		if (getLoggedIn() != null) {
			// Open new login frame
			loginForm = new ClientLoginFrame(this);
			loginForm.setVisible(true);
			// Close current order frame
			orderForm.dispose();
		}
		else {
			System.out.println("No user login details!");
		}
	}


}
