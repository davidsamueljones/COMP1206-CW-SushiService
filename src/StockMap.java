/**
 * Class StockMap that extends LinkedHashMap to allow key mappings with specific functionality.
 * LinkedHashMap used so iterator is predictable and iterates from oldest to newest allowing
 * stocks to be tracked in time. 
 * 
 * New methods in this class are thread safe using a central locking object.
 * 
 * Keys are generic, whereas stock levels are defined and hard-coded as Levels.
 * 
 * @param <K> The type of keys maintained by this map
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public class StockMap<K> extends ConcurrentLinkedHashMap<K, Levels> implements StockHandler<K> {
	private static final long serialVersionUID = -7217174370857494772L;
	private final Object lock;
	
	/**
	 * Instantiates a new StockMap, using self as synchronisation lock.
	 */
	public StockMap() {
		this.lock = this;
	}
	
	/**
	 * Instantiates a new StockMap using a given object as a synchronisation lock.
	 * @param lock Object to use for locking
	 */
	public StockMap(Object lock) {
		this.lock = lock;		
	}
	
	@Override
	public Levels add(K key, Levels levels) {
		synchronized (lock) {
			if (containsKey(key)) {
				throw new IllegalArgumentException("Key already exists in stock map");
			}
			return super.put(key, levels);
		}
	}
	
	@Override
	public void addStock(QuantityMap<K> items, boolean available) {
		for (K item: items.keySet()) {
			addStock(item, items.get(item), available);
		}
	}

	@Override
	public void addStock(K item, int amount, boolean available) {
		synchronized (lock) {
			Levels curStock = get(item);
			curStock.addStock(amount, available);
		}
	}

	@Override
	public boolean makeStockAvailable(K item, int amount) {
		synchronized (lock) {
			Levels curStock = get(item);
			if (curStock.getUnavailableStock() < amount) {
				return false;
			}
			curStock.makeStockAvailable(amount);
		}
		return true;	
	}

	@Override
	public boolean makeStockUnavailable(K item, int amount) {
		synchronized (lock) {
			Levels curStock = get(item);
			if (curStock.getAvailableStock() < amount) {
				return false;
			}
			curStock.makeStockUnavailable(amount);
		}
		return true;	

	}
	
	@Override
	public boolean removeStock(QuantityMap<K> reqItems) {
		synchronized (lock) {
			// Verify enough stock before removal starts
			if (!isEnoughAvailableStock(reqItems)) {
				return false;
			}		
			for (K item: reqItems.keySet()) {
				// Do not use overloaded method to avoid second stock check
				get(item).removeStock(reqItems.get(item));
			}
		}
		return true;
	}

	@Override
	public boolean removeStock(K item, int amount) {
		synchronized (lock) {
			if (!isEnoughAvailableStock(item, amount)) {
				return false;
			}
			get(item).removeStock(amount);
		}
		return true;
	}

	@Override
	public boolean isEnoughAvailableStock(QuantityMap<K> reqItems) {
		for (K item: keySet()) {
			if (!isEnoughAvailableStock(item, reqItems.get(item))) {
				return false;
			}	
		}
		return true;
	}

	@Override
	public boolean isEnoughAvailableStock(K item, int amount) {
		synchronized (lock) {
			return get(item).isEnoughStock(amount);
		}
	}
	
	@Override
	public QuantityMap<K> getStockRequired() {
		QuantityMap<K> quantities = new QuantityMap<K>();
		synchronized (lock) {
			for (K item : keySet()) {
				if (get(item).isStockRequired()) {
					quantities.increment(item);
				}
			}
		}
		return quantities;
	}



}