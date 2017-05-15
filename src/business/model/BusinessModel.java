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
import general.model.QuantityMap;

public class BusinessModel implements Serializable {
	private static final long serialVersionUID = 2670571685446749898L;

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

	// List of orders?
	public BusinessModel() {
		CREATE_TEST();
	}

	// !!! TEST
	public void CREATE_TEST() {
		// !!! Test postcodes
		final Postcode postcode1 = new Postcode("PO7", 4);
		postcodes.add(postcode1);
		postcodes.add(new Postcode("PO8", 10));
		postcodes.add(new Postcode("SO15", 15));

		// !!! Test customer
		final CustomerLogin login = new CustomerLogin("David", "Test");
		final Customer customer =
				new Customer("David Jones", "18 Stirling Avenue", postcode1, login);
		customers.put(login, customer);

		// !!! Test Supplier
		final Supplier sup = new Supplier("FoodRUs", 5.0);
		suppliers.add(sup);

		// !!! Test Ingredient
		final Ingredient ing1 = new Ingredient("Carrot", Ingredient.Unit.items, sup);
		final Ingredient ing2 = new Ingredient("Steak", Ingredient.Unit.g, sup);

		// !!! Test Dish
		final QuantityMap<Ingredient> recipe = new QuantityMap<>();
		recipe.put(ing1, 5.3);
		recipe.put(ing2, 3.0);
		final QuantityMap<Ingredient> recipe1 = new QuantityMap<>();
		recipe1.put(ing1, 4.5);
		recipe1.put(ing2, 0.5);
		final QuantityMap<Ingredient> recipe2 = new QuantityMap<>();
		recipe2.put(ing1, 5.33);
		recipe2.put(ing2, 21.2);
		final QuantityMap<Ingredient> recipe3 = new QuantityMap<>();
		recipe3.put(ing1, 23.2);
		final QuantityMap<Ingredient> recipe4 = new QuantityMap<>();
		recipe4.put(ing2, 1.5);

		final Dish dish1 = new Dish("Steak and Carrots", "It is what it is", 10.5, recipe);
		final Dish dish2 = new Dish("Tomato Soup", "Toemaytoe-tomarto", 4.0, recipe1);
		final Dish dish3 = new Dish("Fish & Chips", "Why ask?", 12.2, recipe2);
		final Dish dish4 = new Dish("Fishy Fingers", "It is what it is", 8.0, recipe3);
		final Dish dish5 = new Dish("Custard Tart", "It is what it is", 4.0, recipe4);

		// !!! Test stocks
		stock.ingredients.add(ing1, new StockLevels(15, 20));
		stock.ingredients.add(ing2, new StockLevels(9, 6));
		stock.dishes.add(dish1, new StockLevels(4, 8));
		stock.dishes.add(dish2, new StockLevels(2, 4));
		stock.dishes.add(dish3, new StockLevels(4, 4));
		stock.dishes.add(dish4, new StockLevels(8, 6));
		stock.dishes.add(dish5, new StockLevels(5, 5));

		// !!! Test kitchen staff
		final KitchenStaffMember ks = new KitchenStaffMember("Steph", stock);
		final KitchenStaffMember ks2 = new KitchenStaffMember("Kirtan", stock);
		kitchenStaff.add(ks);
		kitchenStaff.add(ks2);
		ks.startWorking();
		ks2.startWorking();

		// !!! Test drone
		final Drone drone1 = new Drone("BB8", 2, this);
		drones.add(drone1);
		drone1.startWorking();
	}

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
		} catch (final Exception e) {
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
		} catch (final InterruptedException e) {
			System.err.println("[MODEL] : Unable to wait for message handler thread completion");
		}
		try {
			// Stop worker threads (workers handle tidy up)
			interruptWorkerThreads(true);
			System.out.println("[MODEL] : Worker threads finished");
		} catch (final InterruptedException e) {
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
