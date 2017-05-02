package general.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class QuantityMap that extends HashMap to allow key mappings with specific functionality. Keys
 * are generic, whereas quantities are defined and hard-coded as Doubles. Extended functionality
 * allows relative quantity changes and forces quantities to be positive.
 *
 * @param <K> The type of keys maintained by this map
 *
 * @author David Jones [dsj1n15]
 */
public class QuantityMap<K> extends HashMap<K, Double> implements Serializable {
	private static final long serialVersionUID = 1561977150839009960L;

	@Override
	public Double put(K key, Double quantity) {
		if (quantity != null && quantity < 0) {
			throw new IllegalArgumentException("Quantity must be zero or positive");
		}
		return super.put(key, quantity);
	}

	/**
	 * Associates the quantities item with the quantities value in the map.
	 *
	 * @param item A single quantity object
	 * @return The value associated with the new key
	 */
	public Double put(Quantity<K> item) {
		return put(item.getItem(), item.getQuantity());
	}

	/**
	 * Increment the existing quantity for a key.
	 *
	 * @param key Key to increment quantity for
	 * @return Quantity value after change
	 */
	public Double increment(K key) {
		return relativeChange(key, 1.0);
	}

	/**
	 * Decrement the existing quantity for a key.
	 *
	 * @param key Key to decrement quantity for
	 * @return Quantity value after change
	 */
	public Double decrement(K key) {
		return relativeChange(key, 1.0);
	}

	/**
	 * Get the existing quantity for a key and change the value relatively. If the key does not
	 * exist, create it.
	 *
	 * @param key Key to change quantity for
	 * @param amount Amount to change quantity by
	 * @return Quantity value after change
	 */
	public Double relativeChange(K key, Double amount) {
		Double quantity = get(key);
		if (quantity == null) {
			quantity = 0.0;
		}
		return put(key, quantity + amount);
	}

	/**
	 * @return A list of entries converted to single quantities
	 */
	public List<Quantity<K>> getList() {
		final List<Quantity<K>> list = new ArrayList<>(size());
		for (final Map.Entry<K, Double> entry : entrySet()) {
			list.add(new Quantity<>(entry.getKey(), entry.getValue()));
		}
		return list;
	}

	/**
	 * Safely recreate a quantity map getting keys of a given type from an Object.
	 *
	 * @param mapObj Object to recreate
	 * @param clazz Expected class of map keys
	 * @return A reconstructed quantity map
	 */
	public static <T> QuantityMap<T> fromObject(Object mapObj, Class<T> clazz) {
		// Check object is a quantity map
		if (mapObj instanceof QuantityMap) {
			final QuantityMap<?> mapCast = (QuantityMap<?>) mapObj;
			final QuantityMap<T> mapType = new QuantityMap<>();
			// Loop through given objects keyset
			for (final Object object : mapCast.keySet()) {
				// Store if key is of given type
				final T type = clazz.cast(object);
				if (type != null) {
					mapType.put(type, mapCast.get(type));
				}
			}
			return mapType;
		} else {
			return null;
		}
	}

}
