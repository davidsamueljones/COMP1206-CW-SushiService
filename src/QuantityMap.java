import java.util.HashMap;

/**
 * Class QuantityMap that extends HashMap to allow key mappings with specific functionality.
 * Keys are generic, whereas quantities are defined and hard-coded as Integers.
 * Extended functionality allows relative quantity changes and forces quantities to be positive.
 * @param <K> The type of keys maintained by this map
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public class QuantityMap<K> extends HashMap<K, Integer> {
	private static final long serialVersionUID = 1561977150839009960L;

	@Override
	public Integer put(K key, Integer quantity) {
		if (quantity < 0) {
			throw new IllegalArgumentException("Quantity must be zero or positive");
		}
		return super.put(key, quantity);
	}

	/**
	 * Increment the existing quantity for a key.
	 * @param key Key to increment quantity for
	 * @return Quantity value after change
	 */
	public Integer increment(K key) {
		return relativeChange(key, 1);
	}
	
	/**
	 * Decrement the existing quantity for a key.
	 * @param key Key to decrement quantity for
	 * @return Quantity value after change
	 */
	public Integer decrement(K key) {
		return relativeChange(key, 1);
	}
	
	/**
	 * Get the existing quantity for a key and change the value relatively.
	 * If the key does not exist, create it.
	 * @param key Key to change quantity for
	 * @param amount Amount to change quantity by 
	 * @return Quantity value after change
	 */
	public Integer relativeChange(K key, Integer amount) {
		Integer quantity = get(key);
		if (quantity == null) {
			quantity = 0;
		}
		return put(key, quantity+amount);
	}
	
}
