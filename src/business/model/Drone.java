package business.model;

import java.util.Map.Entry;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import general.model.QuantityMap;
import general.utility.ErrorBuilder;

/**
 * Drone class, holds data and appropriate methods for a drone. A drone is a type of worker that
 * does a task.
 *
 * Identifier of drone defines class equality.
 *
 * @author David Jones [dsj1n15]
 */
public class Drone extends Worker {
	private static final long serialVersionUID = 2025025460905127854L;
	// Constants used for timing
	private static final int HOUR_MS = 3600000;
	private static final double RESTOCK_MODIFIER = 1.2;
	// Drone speed
	private double speed;
	// Model being served
	private final BusinessModel model;

	/**
	 * Instantiate a drone
	 *
	 * @param identifier Drone identifier (not forced but can use Drone.generateIdentifier)
	 * @param model Business model drone is serving
	 */
	public Drone(String identifier, BusinessModel model) {
		super(identifier);
		this.model = model;
	}

	/**
	 * Instantiate a drone
	 *
	 * @param identifier Drone identifier (not forced but can use Drone.generateIdentifier)
	 * @param speed Speed of drone in km/h
	 * @param model Business model drone is serving
	 */
	public Drone(String identifier, double speed, BusinessModel model) {
		super(identifier);
		this.speed = speed;
		this.model = model;
	}

	/**
	 * @return The current speed of the drone (km/h)
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed The new speed of the drone (km/h)
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	@Override
	protected void doWork() throws InterruptedException {
		// Prioritise order delivery and only handle a single
		// task as a unit of work
		if (!deliverOrder()) {
			// If no orders attempt ingredient restock
			restockIngredients();
		}
	}

	/**
	 * Find a customer that has a delivery ready for dispatch. Collect all orders ready for dispatch
	 * for the target customer and travel to the customer. Early exit of journey to customer leads
	 * to order completion this is done as an abstraction of the drone finishing the task before
	 * stopping.
	 *
	 * @return Whether order delivery was required
	 * @throws InterruptedException A re-thrown exception indicating that the worker should stop
	 */
	public boolean deliverOrder() throws InterruptedException {
		Customer targetCustomer = null;
		Set<Order> toDeliver;
		synchronized (model.orders) {
			// Find orders ready for dispatch
			final Set<Order> reqDelivery = BusinessModel.getOrdersOfStatus(
					Order.Status.READY_FOR_DISPATCH, model.orders);
			if (reqDelivery.size() > 0) {
				// Get target customer as first in order list
				targetCustomer = reqDelivery.iterator().next().getCustomer();
				toDeliver = BusinessModel.getOrdersFromCustomer(
						targetCustomer.getLogin(), reqDelivery);
				// Start order dispatch
				for (final Order order : toDeliver) {
					// Remove reserved stock from stock
					synchronized (model.stock.dishes) {
						model.stock.dishes.unreserveStock(order.getDishes());
						model.stock.dishes.removeStock(order.getDishes());
					}
					order.setStatus(Order.Status.DISPATCHED);
				}
			} else {
				return false;
			}
		}
		// Wait (simulate drone journey to customer)
		try {
			actionUpdate(String.format("Delivering to '%s'", targetCustomer.getName()));
			doJourney(targetCustomer.getPostcode().getDistance());
		} catch(InterruptedException e) {
			// Early interruption leads to preemptive delivery
			for (final Order order : toDeliver) {
				order.setStatus(Order.Status.DELIVERED);
			}
			// Rethrow exception to propogate up worker task structure
			throw e;
		}
		for (final Order order : toDeliver) {
			order.setStatus(Order.Status.DELIVERED);
		}
		// Wait (simulate drone journey from customer to business)
		actionUpdate("Returning to Business");
		doJourney(targetCustomer.getPostcode().getDistance());
		actionUpdate("Arrived at Business");
		return true;
	}

	/**
	 * Find an ingredient that requires restocking where the supplier is not already being
	 * restocked. Travel to the ingredients supplier. On arrival determine all ingredients that need
	 * restocking from that supplier and restock them. Early exit of journey to supplier cancels
	 * restock. Early exit of journey back finishes restock as opposed to discarding; this is done
	 * as an abstraction of the drone finishing the task before stopping.
	 *
	 * @return Whether ingredient restock was required
	 * @throws InterruptedException A re-thrown exception indicating that the worker should stop
	 */
	public boolean restockIngredients() throws InterruptedException {
		Supplier targetSupplier;
		// Find a supplier that requires restocking
		synchronized (model.stock.ingredients) {
			targetSupplier = model.stock.getSupplierToRestock();
			if (targetSupplier != null) {
				targetSupplier.setBeingRestocked(true);
				actionUpdate(String.format("Restocking from '%s'", targetSupplier.getName()));
			} else {
				return false;
			}
		}
		// Wait (simulate drone journey to supplier)
		try {
			doJourney(targetSupplier.getDistance());
		} catch(InterruptedException e) {
			// Early interruption leads to cancelling of restock
			targetSupplier.setBeingRestocked(false);
			// Rethrow exception to propogate up worker task structure
			throw e;
		}
		// Find ingredients that require restock, start restock
		QuantityMap<Ingredient> toRestock = null;
		synchronized (model.stock.ingredients) {
			QuantityMap<Ingredient> reqStock = model.stock.ingredients.getStockRequired();
			reqStock = Stock.getIngredientsFromSupplier(targetSupplier, reqStock);
			toRestock = getRestockAmount(reqStock);
			model.stock.ingredients.startRestock(toRestock);
			targetSupplier.setBeingRestocked(false);
		}
		// Wait (simulate drone journey from supplier to business)
		try {
			actionUpdate("Returning to Business");
			doJourney(targetSupplier.getDistance());
		} catch(InterruptedException e) {
			// Early interruption leads to preemptive restock finishing
			model.stock.ingredients.finishRestock(toRestock);
			// Rethrow exception to propogate up worker task structure
			throw e;
		}
		// Finish restock
		model.stock.ingredients.finishRestock(toRestock);
		actionUpdate("Arrived at Business");
		return true;
	}

	/**
	 * Apply restock modifier and round up any decimal value to an integer to every ingredient 
	 * in a quantity map; this simulates the purchase of extra stock to avoid continuous restocking
	 * and the purchase of only whole units only.
	 *
	 * @param ingredients Map of ingredients to calculate restock amount for
	 * @return Scaled map of ingredients
	 */
	private static QuantityMap<Ingredient> getRestockAmount(QuantityMap<Ingredient> ingredients) {
		final QuantityMap<Ingredient> restockAmounts = new QuantityMap<>();
		for (final Entry<Ingredient, Double> entry : ingredients.entrySet()) {
			restockAmounts.put(entry.getKey(), Math.ceil(entry.getValue() * RESTOCK_MODIFIER));
		}
		return restockAmounts;
	}

	/**
	 * Sleep the thread for the time it takes the drone to cover the given distance. A constant
	 * speed modifier is taken into account.
	 *
	 * @param distance Distance that must be travelled (km)
	 * @throws InterruptedException A re-thrown exception indicating that the worker should stop
	 */
	private void doJourney(double distance) throws InterruptedException {
		final int time = (int) Math.round(HOUR_MS * distance / speed * BusinessModel.SPEED_MODIFIER);
		Thread.sleep(time);
	}

	/**
	 * Generate an identifier using valid drone identifier characters. This is not enforced but is
	 * suggested for all drones.
	 *
	 * @param length Length of identifier
	 * @return Generated identifier
	 */
	public static String generateIdentifier(int length) {
		final char[] letters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		final Random r = new Random(System.currentTimeMillis());
		final char[] identifier = new char[length];
		for (int i = 0; i < length; i++) {
			identifier[i] = letters[r.nextInt(letters.length)];
		}
		return String.valueOf(identifier);
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (identifier == null || identifier.isEmpty()) {
			eb.addError("Name field is empty");
		}
		if (speed <= 0) {
			eb.addError("Speed must be positive");
		}
		if (model == null) {
			eb.addError("Model is not set");
		}
		return eb;
	}

	@Override
	public String toString() {
		return String.format("Drone [identifier=%s]", identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Drone))
			return false;
		final Drone other = (Drone) obj;
		return (Objects.equals(this.identifier, other.identifier));
	}

	@Override
	public int hashCode() {
		final int prime = 13;
		return prime + (identifier == null ? 0 : identifier.hashCode());
	}

}
