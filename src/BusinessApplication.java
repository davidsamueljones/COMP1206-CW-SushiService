import java.awt.EventQueue;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.HashSet;

public class BusinessApplication {
	
	private static final String HOSTNAME = "localhost";
	private static final int PORT = 23532;
	public static final InetSocketAddress ADDRESS = new InetSocketAddress(HOSTNAME, PORT);
	
	// Message handling
	private BusinessMessageHandler messageHandler;
	private Comms comms;
	
	private HashMap<CustomerLogin, Customer> customers = new HashMap<CustomerLogin, Customer>();
	private StockManagement stock = new StockManagement();
	private HashSet<Supplier> suppliers = new HashSet<Supplier>();
	private HashSet<KitchenStaffMember> kitchenStaff = new HashSet<KitchenStaffMember>();
	private HashSet<Drone> drone = new HashSet<Drone>();
	
	
	// List of orders?
	
	public BusinessApplication() {
		comms = new Comms(BusinessApplication.ADDRESS);
		messageHandler = new BusinessMessageHandler(this);
		(new Thread(messageHandler)).start();
		
		//!!! Test customer
		CustomerLogin login = new CustomerLogin("David", "Test");
		Customer customer = new Customer("David Jones", "18 Stirling Avenue", "PO7 7NH", login);
		customers.put(login, customer);
	}
	
	public Comms getComms() {
		return comms;
	}	
	
	public Customer findLogin(CustomerLogin login) {
		return customers.get(login);
	}	

	public void addCustomer(Customer customer) {
		synchronized (customers) {
			customers.put(customer.getLogin(), customer);
		}
	}
	
	public static void main(String[] args) {
		new BusinessApplication();
//		Object msgObject = "Gareth is a tool ~ from the other server!";
//		Customer customer = new Customer();
//		Message message = new Message(Message.Command.APPROVE_LOGIN, customer);
//		comms.sendMessage(message);
	}
	

}
