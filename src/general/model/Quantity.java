package general.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class holding a quantity for a single item of generic type. Quantity is stored as a double but
 * methods can expect quantities to often stick to integer values. The equality criteria of
 * quantities is the item for which the quantity is being stored and not the actual quantity level.
 *
 * @author David Jones [dsj1n15]
 *
 * @param <T> The type of item
 */
public class Quantity<T> implements Serializable {
	private static final long serialVersionUID = -6885572530739842517L;
	// Properties
	private final T item;
	private double quantity;

	/**
	 * Instantiate a quantity with a given item and amount.
	 *
	 * @param item The item for which a quantity is being recorded
	 * @param quantity An initial quantity
	 */
	public Quantity(T item, double quantity) {
		this.item = item;
		this.setQuantity(quantity);
	}

	/**
	 * @return The item for which a quantity is recorded
	 */
	public T getItem() {
		return item;
	}

	/**
	 * @return The current quantity value
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity A new quantity value (> 0)
	 */
	public void setQuantity(double quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("Quantity must be zero or positive");
		}
		this.quantity = quantity;
	}

	/**
	 * @return Get a new map containing this quantity
	 */
	public QuantityMap<T> toMap() {
		final QuantityMap<T> map = new QuantityMap<>();
		map.put(this);
		return map;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Quantity<?>))
			return false;
		final Quantity<?> other = (Quantity<?>) obj;
		return (Objects.equals(this.item, other.item));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + (item == null ? 0 : item.hashCode());
	}

}
