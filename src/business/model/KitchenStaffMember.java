package business.model;

import java.util.Objects;
import java.util.Random;

import general.model.Quantity;
import general.utility.ErrorBuilder;

/**
 * KitchenStaffMember class, holds data and appropriate methods for a member of kitchen staff. A
 * kitchen staff member is a type of worker that does a task.
 *
 * Name of kitchen staff member defines class equality.
 *
 * Sushi Service - COMP1206 Coursework
 *
 * @author David Jones [dsj1n15]
 */
public class KitchenStaffMember extends Worker {
	private static final long serialVersionUID = 8752641082220001781L;
	// Working times
	private static final int PREP_TIME_LOWER_BOUND = 20000;
	private static final int PREP_TIME_UPPER_BOUND = 40000;
	// Stock being managed
	private final Stock stock;

	/**
	 * Instantiate a kitchen staff member.
	 *
	 * @param name Name of kitchen staff member.
	 * @param stock
	 */
	public KitchenStaffMember(String name, Stock stock) {
		super(name);
		this.stock = stock;
	}

	@Override
	protected void doWork() throws InterruptedException {
		// Possible work includes finding dish for preparation
		findDishAndPrepare();
	}

	/**
	 * Find a dish that requires restocking and has enough ingredients available to restock. Process
	 * a single restock. Early exit of dish preparation finishes restock as opposed to discarding;
	 * this is done as an abstraction of someone finishing their task before stopping their work.
	 *
	 * @return Whether a dish was found to prepare
	 * @throws InterruptedException A re-thrown exception indicating that the worker should stop
	 */
	private boolean findDishAndPrepare() throws InterruptedException {
		Quantity<Dish> toRestock;
		// Lock both ingredients and stock so no changes can be made between check
		// and start of preparation (remove chance of restocking the same dish twice
		// or no longer being able to prepare dish)
		synchronized (stock.ingredients) {
			synchronized (stock.dishes) {
				toRestock = stock.getDishToRestock();
				if (toRestock == null) {
					return false;
				}
				startDishPreparation(toRestock);
			}
		}
		// Wait (simulate dish preparation)
		try {
			randomWait(PREP_TIME_LOWER_BOUND, PREP_TIME_UPPER_BOUND);
		} catch (final InterruptedException e) {
			// Early interruption leads to preemptive restock finishing
			finishDishPreparation(toRestock);
			// Rethrow exception to propogate up worker task structure
			throw e;
		}
		// Finish preparation normally
		finishDishPreparation(toRestock);
		return true;
	}

	/**
	 * Remove ingredient stock and increment dish restocking level.
	 *
	 * @param toRestock Dish and amount to start preparation for
	 */
	private void startDishPreparation(Quantity<Dish> toRestock) {
		final Dish dish = toRestock.getItem();
		// Lock both ingredients and stock to ensure ingredients are decremented and
		// dishes are incremented without any other method interferring
		synchronized (stock.ingredients) {
			synchronized (stock.dishes) {
				if (stock.ingredients.removeStock(dish.getIngredients())) {
					actionUpdate(String.format("Preparing '%s'", dish.getName()));
					stock.dishes.startRestock(toRestock);
				}
			}
		}
	}

	/**
	 * Transfer restocking stock to available stock.
	 *
	 * @param toRestock Dish and amount to finish preparation for
	 */
	private void finishDishPreparation(Quantity<Dish> toRestock) {
		stock.dishes.finishRestock(toRestock);
		actionUpdate(String.format("Prepared '%s'", toRestock.getItem().getName()));
	}

	/**
	 * Sleep the thread for a random amount of time between two bounds.
	 *
	 * @param lowerBound Lowest amount of time to wait (ms)
	 * @param upperBound Highest amount of time to wait (ms)
	 * @throws InterruptedException A re-thrown exception indicating that the worker should stop
	 */
	private static void randomWait(int lowerBound, int upperBound) throws InterruptedException {
		final int time = (new Random()).nextInt(lowerBound + upperBound) + lowerBound;
		Thread.sleep(time);
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (identifier == null || identifier.isEmpty()) {
			eb.addError("Name field is empty");
		}
		if (stock == null) {
			eb.addError("Stock is not set");
		}
		return eb;
	}

	@Override
	public String toString() {
		return String.format("Kitchen Staff Member [name=%s]", identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof KitchenStaffMember))
			return false;
		final KitchenStaffMember other = (KitchenStaffMember) obj;
		return (Objects.equals(this.identifier, other.identifier));
	}

	@Override
	public int hashCode() {
		final int prime = 13;
		return prime + (identifier == null ? 0 : identifier.hashCode());
	}

}
