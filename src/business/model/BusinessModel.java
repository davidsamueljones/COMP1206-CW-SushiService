package business.model;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import general.model.Comms;
import general.model.MessageHandler;

/**
 * Main business model class that ties together all objects held by a business. This
 * model class is designed to be completely independent of any interface. The model expects
 * to handle connections to clients and therefore includes a communication layer. The model
 * provides suspension and resume behaviour for safe storage.
 * 
 * @author David Jones [dsj1n15]
 */
public class BusinessModel implements Serializable {
	private static final long serialVersionUID = 2670571685446749898L;
	// Speed modifier to use throughout business - used to speed up simulations
	public static final double SPEED_MODIFIER = 0.10;
	
	// Business data (Persistent)
	public final Map<CustomerLogin, Customer> customers = new HashMap<>();
	public final Stock stock = new Stock();
	public final Set<Supplier> suppliers = new HashSet<>();
	public final Set<Order> orders = new LinkedHashSet<>();
	public final Set<KitchenStaffMember> kitchenStaff = new HashSet<>();
	public final Set<Drone> drones = new HashSet<>();
	public final Set<Postcode> postcodes = new HashSet<>();

	// Message handling
	private transient Comms comms;
	private transient Thread messageHandlerThread;

	/**
	 * @return Current comms instance
	 */
	public Comms getComms() {
		return comms;
	}

	/**
	 * Create a new instance of comms if it isn't already instantiated. This will create an
	 * accompanying message handler.
	 *
	 * @param source The address on which the server is hosted
	 * @return True if server hosted, else false
	 */
	public boolean startComms(InetSocketAddress source) {
		if (comms != null) {
			throw new IllegalStateException("[MODEL] : Comms already instantiated");
		}
		try {
			System.out.println("[MODEL] : Comms starting...");
			// Start comms
			comms = new Comms(source);
			// Create associated message handler
			final MessageHandler messageHandler = new BusinessMessageHandler(this);
			messageHandlerThread = new Thread(messageHandler);
			messageHandlerThread.start();
			System.out.println("[MODEL] : Comms started");
			return true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return false;
	}

	/**
	 * Put model objects into a state of suspension, safely stopping threads that have influence
	 * over the model; this includes message handling and communications. This method should block
	 * until all threads have completed.
	 */
	public void suspend() {
		System.out.println("[MODEL] : Suspending...");
		// Safely stop theads that have influence over the model
		try {
			// Stop message handler thread
			messageHandlerThread.interrupt();
			messageHandlerThread.join();
			System.out.println("[MODEL] : Message handler thread finished");
		} catch(InterruptedException e) {
			System.err.println("[MODEL] : Unable to wait for message handler thread completion");
		}
		try {
			// Stop worker threads (workers handle tidy up)
			interruptWorkerThreads(true);
			System.out.println("[MODEL] : Worker threads finished");
		} catch(InterruptedException e) {
			System.err.println("[MODEL] : Unable to wait for worker thread completion");
		}
		System.out.println("[MODEL] : Suspended");
	}

	/**
	 * Stop worker threads in model using an interrupt. Workers handle their own suspension
	 * behaviour.
	 *
	 * @param wait Whether the calling thread should block until all worker threads join
	 * @throws InterruptedException Thrown if blocking joins are interrupted
	 */
	private void interruptWorkerThreads(boolean wait) throws InterruptedException {
		// Build up array list of worker threads
		final List<Thread> threads = new ArrayList<>();
		// Get worker threads
		for (final Worker kitchenStaffMember : kitchenStaff) {
			threads.add(kitchenStaffMember.getThread());
		}
		for (final Worker drone : drones) {
			threads.add(drone.getThread());
		}
		// Interrupt threads
		for (final Thread thread : threads) {
			if (thread != null) {
				thread.interrupt();
			}
		}
		if (wait) {
			// Wait for all threads to be completed
			for (final Thread thread : threads) {
				if (thread != null) {
					thread.join();
				}
			}
		}
	}


	/**
	 * Restart suspended model objects; this does not include comms and message handling.
	 */
	public void resume() {
		System.out.println("[MODEL] : Resuming...");
		// Start suspended workers
		resumeWorkers();
		System.out.println("[MODEL] : Resumed");
	}

	/**
	 * Restart suspended workers.
	 */
	private void resumeWorkers() {
		// Resume workers that are suspended
		for (final Worker kitchenStaffMember : kitchenStaff) {
			if (kitchenStaffMember.getStatus() == Worker.Status.SUSPENDED) {
				kitchenStaffMember.startWorking();
			}
		}
		for (final Worker drone : drones) {
			if (drone.getStatus() == Worker.Status.SUSPENDED) {
				drone.startWorking();
			}
		}
	}

	/**
	 * Filter the models set of orders to get only those with a particular status.
	 * 
	 * @param status Status to filter by
	 * @return Filtered set of orders
	 */
	public Set<Order> getOrdersOfStatus(Order.Status status) {
		synchronized (orders) {
			return getOrdersOfStatus(status, orders);
		}
	}

	/**
	 * Filter a set of orders to only those with a particular status.
	 * 
	 * @param status Status to filter by
	 * @param orders Orders to filter
	 * @return Filtered set of orders
	 */
	public static Set<Order> getOrdersOfStatus(Order.Status status, Set<Order> orders) {
		final Set<Order> reqDelivery = new LinkedHashSet<>();
		for (final Order order : orders) {
			if (order.getStatus().equals(status)) {
				reqDelivery.add(order);
			}
		}
		return reqDelivery;
	}

	/**
	 * Filter the models set of orders to get only those with a particular customer login.
	 * 
	 * @param login Customer login to filter by
	 * @return Filtered set of orders
	 */
	public Set<Order> getOrdersFromCustomer(CustomerLogin login) {
		synchronized (orders) {
			return getOrdersFromCustomer(login, orders);
		}
	}

	/**
	 * Filter a set of orders to only those with a particular customer login.
	 * 
	 * @param login Customer login to filter by
	 * @param orders Orders to filter
	 * @return Filtered set of orders
	 */
	public static Set<Order> getOrdersFromCustomer(CustomerLogin login, Set<Order> orders) {
		final Set<Order> fromCustomer = new HashSet<>();
		for (final Order order : orders) {
			if (order.getCustomer().getLogin().equals(login)) {
				fromCustomer.add(order);
			}
		}
		return fromCustomer;
	}

}
