package business.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Objects;

import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.Validatable;

/**
 * Order class, holds data about an order. Combination of customer and date define 
 * equality. Date is not immutable so it is suggested that this is only ever changed
 * when it is known that the order is not held in any collections. 
 *
 * @author David Jones [dsj1n15]
 */
public class Order implements Serializable, Validatable {
	private static final long serialVersionUID = 8694359833181145493L;
	private final Customer customer;
	private final QuantityMap<Dish> dishes;
	private LocalDateTime date;
	private Status status;

	/**
	 * Instantiate a new order from a customer with given dishes.
	 * @param customer Customer who made order
	 * @param dishes Dishes in order
	 */
	public Order(Customer customer, QuantityMap<Dish> dishes) {
		this.customer = customer;
		this.dishes = dishes;
		this.date = LocalDateTime.now();
		this.status = Status.CREATED;
	}

	/**
	 * @return Customer who made the order
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * @return Dishes and their respective quantities in the order
	 */
	public QuantityMap<Dish> getDishes() {
		return dishes;
	}
	
	/**
	 * Calculate the price of all dishes in the order.
	 * @return Total price
	 */
	public double getTotalPrice() {
		double price = 0;
		for (final Entry<Dish, Double> dish : dishes.entrySet()) {
			price += dish.getKey().getPrice() * dish.getValue();
		}
		return price;
	}
	
	/**
	 * @return The date/time when the order was created
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**
	 * Set the date/time of the order; this can be set so a business can update the 
	 * time to be in respect to the server clock.
	 * @param date The date/time of the order
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	/**
	 * @return Status of order
	 */
	public Status getStatus() {
		return status;
	}

	/**
	 * @param status The new status of the order
	 */
	public void setStatus(Status status) {
		this.status = status;
	}


	/**
	 * Non-static implementation of Status.isCancellable.
	 * @return Whether status implies order is cancellable
	 */
	public boolean isCancellable() {
		return Status.isCancellable(status);
	}

	/**
	 * Non-static implementation of Status.isComplete.
	 * @return Whether status implies order is complete
	 */
	public boolean isComplete() {
		return Status.isComplete(status);
	}
	
	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (customer == null) {
			eb.addError("Order is not owned by a customer");
		}
		if (dishes == null || dishes.size() == 0) {
			eb.addError("The order contains no dishes");
		}
		if (status == null) {
			eb.addError("No status has been set");
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Order))
			return false;
		final Order other = (Order) obj;
		return (Objects.equals(this.customer, other.customer)
				&& Objects.equals(this.date, other.date));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customer == null) ? 0 : customer.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	/**
	 * Enumeration of statuses for an order.
	 * 
	 * @author David Jones [dsj1n15]
	 */
	public static enum Status {
		CREATED, RECEIVED, READY_FOR_DISPATCH, DISPATCHED, DELIVERED, CANCELLED;

		/**
		 * @param status Status to verify
		 * @return Whether status implies can cancel
		 */
		public static boolean isCancellable(Status status) {
			switch (status) {
				case CREATED:
				case RECEIVED:
				case READY_FOR_DISPATCH:
					return true;
				default:
					return false;
			}
		}

		/**
		 * @param status Status to verify
		 * @return Whether status implies is complete
		 */
		public static boolean isComplete(Status status) {
			switch (status) {
				case DELIVERED:
				case CANCELLED:
					return true;
				default:
					return false;
			}
		}

	}

}
