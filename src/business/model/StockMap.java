package business.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import general.model.Quantity;
import general.model.QuantityMap;

/**
 * Class StockMap that extends HashMap to allow key mappings with specific functionality. New
 * methods in this class are thread safe using the map as a locking object, any standard HashMap
 * functions should lock to the map manually to synchronise.
 *
 * Keys are generic, whereas stock levels are defined and hard-coded as StockLevels.
 *
 * @param <K> The type of keys maintained by this map
 *
 * @author David Jones [dsj1n15]
 */
public class StockMap<K> extends HashMap<K, StockLevels> {
	private static final long serialVersionUID = -7217174370857494772L;

	/**
	 * Add a new item to the stock handler with appropriate stock levels. Failure should occur if
	 * adding an item that already exists.
	 *
	 * @param key Item to add
	 * @param levels Stock levels
	 * @return Item's corresponding stock levels
	 */
	public synchronized StockLevels add(K key, StockLevels levels) {
		if (containsKey(key)) {
			throw new IllegalArgumentException("Key already exists in stock map");
		}
		return super.put(key, levels);
	}

	/**
	 * Add a new stock item to the stock handler.
	 *
	 * @param stockItem Item to add
	 * @return Item's corresponding stock levels
	 */
	public StockLevels add(StockItem<K> stockItem) {
		return add(stockItem.getItem(), stockItem.getStockLevels());
	}

	/**
	 * Add given item stocks to stock handler to be available immediately.
	 *
	 * @param items Items and the respective stocking values to add to stock handler
	 * @param available True if stock should be available immediately, else false
	 */
	public synchronized void addStock(QuantityMap<K> items) {
		for (final K item : items.keySet()) {
			final StockLevels levels = get(item);
			if (levels != null) {
				levels.addStock(items.get(item));
			}
		}
	}

	/**
	 * Add given item stock to stock handler to be available immediately.
	 *
	 * @param item Item and amount of stock to add
	 */
	public void addStock(Quantity<K> item) {
		addStock(item.toMap());
	}

	/**
	 * Remove given item stocks from stock handler. Only remove available stock. If any item of
	 * given items does not have enough stock, do not remove stock of any items.
	 *
	 * @param items Items and the respective stocking values to remove from stock handler
	 */
	public synchronized boolean removeStock(QuantityMap<K> reqItems) {
		if (!isEnoughAvailableStock(reqItems)) {
			return false;
		}
		for (final K item : reqItems.keySet()) {
			get(item).removeStock(reqItems.get(item));
		}
		return true;
	}

	/**
	 * Remove given item stock from stock handler. Only remove available stock.
	 *
	 * @param item Item and amount of stock to remove
	 */
	public boolean removeStock(Quantity<K> item) {
		return removeStock(item.toMap());
	}

	/**
	 * Add given item stocks to their respective reserved stock levels.
	 *
	 * @param items Items and their respective amount of stock to reserve
	 * @return True if stock could be reserved, else false
	 */
	public synchronized boolean reserveStock(QuantityMap<K> items) {
		if (!isEnoughAvailableStock(items)) {
			return false;
		}
		for (final K item : items.keySet()) {
			get(item).reserveStock(items.get(item));
		}
		return true;
	}

	/**
	 * Add given item stock to reserved stock level.
	 *
	 * @param item Item and amount of stock to reserve
	 * @return True if stock could be reserved, else false
	 */
	public boolean reserveStock(Quantity<K> item) {
		return reserveStock(item.toMap());
	}

	/**
	 * Remove given item stocks from their respective reserved stock levels.
	 *
	 * @param items Items and their respective amount of stock to unreserve
	 * @return True if stock could be reserved, else false
	 */
	public synchronized boolean unreserveStock(QuantityMap<K> items) {
		for (final K item : items.keySet()) {
			final StockLevels levels = get(item);
			if (levels == null || levels.getReserved() < items.get(item)) {
				return false;
			}
		}
		for (final K item : items.keySet()) {
			get(item).unreserveStock(items.get(item));
		}
		return true;
	}

	/**
	 * Removes given item stock from reserved stock level.
	 *
	 * @param item Item and amount of stock to unreserve
	 * @return True if stock could be unreserved, else false
	 */
	public boolean unreserveStock(Quantity<K> item) {
		return unreserveStock(item.toMap());
	}

	/**
	 * Add given item stocks to their respective restocking levels.
	 *
	 * @param items Items and their respective amount of stock to start restocking
	 */
	public synchronized void startRestock(QuantityMap<K> items) {
		for (final K item : items.keySet()) {
			final StockLevels levels = get(item);
			if (levels != null) {
				levels.startRestock(items.get(item));
			}
		}
	}

	/**
	 * Add given item stock to restocking levels.
	 *
	 * @param item Item and amount of stock to start restocking
	 */
	public void startRestock(Quantity<K> item) {
		startRestock(item.toMap());
	}

	/**
	 * Transfer given item stocks from restocking stock to available stock.
	 *
	 * @param items Items and their respective amount of stock to transfer
	 * @return True if stock could be transferred, else false
	 */
	public synchronized boolean finishRestock(QuantityMap<K> items) {
		for (final K item : items.keySet()) {
			final StockLevels levels = get(item);
			if (levels != null) {
				if (levels.getRestocking() < items.get(item)) {
					return false;
				}
			}
		}
		for (final K item : items.keySet()) {
			final StockLevels levels = get(item);
			if (levels != null) {
				levels.finishRestock(items.get(item));
			}
		}
		return true;
	}

	/**
	 * Transfer given item stock from restocking stock to available stock.
	 *
	 * @param item Item and amount of stock to transfer
	 * @return True if stock could be transferred, else false
	 */
	public boolean finishRestock(Quantity<K> item) {
		return finishRestock(item.toMap());
	}

	/**
	 * Sets stockable for a list of items.
	 *
	 * @param item Item to set stockable for
	 * @param stockable True if should be stockable, else false
	 */
	public synchronized void setStockable(Collection<K> items, boolean stockable) {
		for (final K item : items) {
			setStockable(item, stockable);
		}
	}

	/**
	 * Set stockable for a single item.
	 *
	 * @param item Item to set stockable for
	 * @param stockable True if should be stockable, else false
	 */
	public synchronized void setStockable(K item, boolean stockable) {
		final StockLevels levels = get(item);
		if (levels != null) {
			levels.setStockable(stockable);
		}
	}

	/**
	 * Check if there is enough available item stock in stock handler against a list of items and
	 * respective stock values.
	 *
	 * @param reqItems Items and the respective stock values required
	 * @return True if enough of each item, else false
	 */
	public synchronized boolean isEnoughAvailableStock(QuantityMap<K> reqItems) {
		for (final K item : reqItems.keySet()) {
			final StockLevels levels = get(item);
			if (levels == null || !levels.isEnoughStock(reqItems.get(item))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Check if there is enough available item stock for an item and amount.
	 *
	 * @param item Item and amount of stock to check if is available
	 * @return True if enough of item, else false
	 */
	public boolean isEnoughAvailableStock(Quantity<K> item) {
		return isEnoughAvailableStock(item.toMap());
	}

	/**
	 * Get items with less stock than their respective restock level. This should take into account
	 * all stock, not just that which is available.
	 *
	 * @return Items and restock counts required
	 */
	public synchronized QuantityMap<K> getStockRequired() {
		final QuantityMap<K> quantities = new QuantityMap<>();
		for (final K item : keySet()) {
			if (get(item).isStockRequired()) {
				quantities.put(item, get(item).getStockRequired());
			}
		}
		return quantities;
	}

	/**
	 * Get all items with the amount of stock they have available for use.
	 *
	 * @return Items and available stock count
	 */
	public QuantityMap<K> getStockAvailable() {
		final QuantityMap<K> quantities = new QuantityMap<>();
		for (final K item : keySet()) {
			quantities.put(item, get(item).getStockAvailable());
		}
		return quantities;
	}

	/**
	 * @return List of all stock items (unpredictable order)
	 */
	public synchronized List<StockItem<K>> getList() {
		final List<StockItem<K>> list = new ArrayList<>();
		for (final Entry<K, StockLevels> entry : entrySet()) {
			list.add(new StockItem<>(entry.getKey(), entry.getValue()));
		}
		return list;
	}

}
