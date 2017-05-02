package business.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map.Entry;
import java.util.Objects;

import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.Validatable;

public class Order implements Serializable, Validatable {
	private static final long serialVersionUID = 8694359833181145493L;
	private final Customer customer;
	private final QuantityMap<Dish> dishes;
	private LocalDateTime date;
	private Status status;

	public Order(Customer customer, QuantityMap<Dish> dishes) {
		this.customer = customer;
		this.dishes = dishes;
		this.date = LocalDateTime.now();
		this.status = Status.CREATED;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Customer getCustomer() {
		return customer;
	}

	public QuantityMap<Dish> getDishes() {
		return dishes;
	}

	public double getTotalPrice() {
		double price = 0;
		for (final Entry<Dish, Double> dish : dishes.entrySet()) {
			price += dish.getKey().getPrice() * dish.getValue();
		}
		return price;
	}

	public boolean isCancellable() {
		return Status.isCancellable(status);
	}

	public boolean isComplete() {
		return Status.isComplete(status);
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

	public static enum Status {
		CREATED, RECEIVED, READY_FOR_DISPATCH, DISPATCHED, DELIVERED, CANCELLED;

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

	@Override
	public ErrorBuilder validate() {
		// TODO Auto-generated method stub
		return null;
	}

}
