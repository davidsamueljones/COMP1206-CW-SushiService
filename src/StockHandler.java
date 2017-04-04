
/**
 * Interface StockHandler which defines appropriate methods for handling stock.
 * Stock being handled is generic.
 * @param <T> Item type
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public interface StockHandler<T> {
	
	/**
	 * Add a new item to the stock handler with appropriate stock levels. 
	 * Failure should occur if adding an item that already exists.
	 * @param key Item to add
	 * @param levels Stock levels
	 * @return Item's corresponding stock levels
	 */
	abstract public StockLevels add(T item, StockLevels levels);
	
	/**
	 * Remove an item from the stock handler.
	 * @param key Item to remove
	 * @return Item's corresponding stock levels, else null if item not found
	 */
	abstract public StockLevels remove(T item);
	
	/**
	 * Add given item stocks to stock handler. Make available as indicated.
	 * @param items Items and the respective stocking values to add to stock handler
	 * @param available True if stock should be available immediately, else false
	 */
	abstract public void addStock(QuantityMap<T> items, boolean available);
	
	/**
	 * Add given item stock to stock handler. Make available as indicated.
	 * @param item Item to modify stock of in stock handler
	 * @param amount Stock count to add to stock handler
	 * @param available True if stock should be available immediately, else false
	 */
	abstract public void addStock(T item, int amount, boolean available);
	
	/**
	 * Make given amount of item stock available for use. 
	 * Amount of available stock cannot be more than total stock.
	 * @param item Item to modify stock of in stock handler
	 * @param amount Amount of stock to make available
	 * @return Whether the stock was made available successfully 
	 */
	abstract public boolean makeStockAvailable(T item, int amount);
	
	/**
	 * Make given amount of item stock unavailable for use. 
	 * Amount of unavailable stock cannot be more than total stock.
	 * @param item Item to modify stock of in stock handler
	 * @param amount Amount of stock to make unavailable
	 * @return Whether the stock was made unavailable successfully 
	 */
	abstract public boolean makeStockUnavailable(T item, int amount);
	
	/**
	 * Remove given item stocks from stock handler. Only remove available stock.
	 * If any item of given items does not have enough stock, do not remove stock of
	 * any items.
	 * @param items Items and the respective stocking values to remove from stock handler
	 */
	abstract public boolean removeStock(QuantityMap<T> reqItems);
	
	/**
	 * Remove given item stock from stock handler. Only remove available stock.
	 * @param item Item to modify stock of in stock handler
	 * @param amount Stock count to remove from stock handler
	 */
	abstract public boolean removeStock(T item, int amount);
	
	/**
	 * Check if there is enough available item stock in stock handler against 
	 * a list of items and respective stock values.
	 * @param reqItems Items and the respective stock values required
	 * @return True if enough of each item, else false
	 */
	abstract public boolean isEnoughAvailableStock(QuantityMap<T> reqItems);
	
	/**
	 * Check if there is enough available item stock for an item and amount.
	 * @param item Item to check stock of in stock handler
	 * @param amount Stock count to check for in stock handler
	 * @return True if enough of item, else false
	 */
	abstract public boolean isEnoughAvailableStock(T item, int amount);
	
	
	/**
	 * Get items and counts with less stock than their respective restock level.
	 * This should take into account all stock, not just that which is available.
	 * @return Items and restock counts required
	 */
	abstract public QuantityMap<T> getStockRequired();
	
}
