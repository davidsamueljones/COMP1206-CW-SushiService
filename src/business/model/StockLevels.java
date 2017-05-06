package business.model;

import java.io.Serializable;

/**
 * StockLevels class, holds information on available stock and restock level.
 *
 * This classes methods are thread safe for a single instance but rely on a containing structure to
 * manage synchronisation between multiple instances.
 *
 * @author David Jones [dsj1n15]
 */
public class StockLevels implements Serializable {
	private static final long serialVersionUID = 7617061460902994224L;
	// Actual stock levels
	private double stock;
	private double restocking;
	private double reserved;
	// Stocking properties
	private int restockLevel;
	private boolean stockable;

	/**
	 * Instantiate levels with defaults.
	 */
	public StockLevels() {
		this(0);
	}

	/**
	 * Instantiate levels with no initial stock and a restock level.
	 *
	 * @param restockLevel Initial restock level
	 */
	public StockLevels(int restockLevel) {
		this(0, restockLevel);
	}

	/**
	 * Instantiate levels with initial stock and a restock level. Restocking and reserved levels are
	 * defaulted to 0 with restocking enabled.
	 *
	 * @param stockLevel Starting stock level
	 * @param restockLevel Initial restock level
	 */
	public StockLevels(double stockLevel, int restockLevel) {
		setStock(stockLevel);
		setRestocking(0);
		setReserved(0);
		setRestockLevel(restockLevel);
		setStockable(true);
	}

	/**
	 * @return The current stock level, both reserved and unreserved
	 */
	public synchronized double getStock() {
		return stock;
	}

	/**
	 * @param stock A new stock level that represents the amount of of stock both reserved and
	 *        unreserved stock
	 */
	private synchronized void setStock(double stock) {
		if (stock < 0) {
			throw new IllegalArgumentException("Stock cannot be set to a negative amount");
		}
		if (stock < reserved) {
			throw new IllegalArgumentException("Cannot set stock to less than reserved");
		}
		this.stock = stock;
	}

	/**
	 * Add the given amount of stock to the current stock level.
	 *
	 * @param amount Amount to add
	 */
	public synchronized void addStock(double amount) {
		setStock(stock + amount);
	}

	/**
	 * Remove the given amount of stock from the current stock level.
	 *
	 * @param amount Amount to remove
	 */
	public synchronized void removeStock(double amount) {
		setStock(stock - amount);
	}

	/**
	 * @return The current amount of stock being restocked
	 */
	public synchronized double getRestocking() {
		return restocking;
	}

	/**
	 * @param restocking A new restocking level that represents the amount of stock being restocked
	 */
	private synchronized void setRestocking(double restocking) {
		if (restocking < 0) {
			throw new IllegalArgumentException("Restocking cannot be set to a negative amount");
		}
		this.restocking = restocking;
	}

	/**
	 * Add the given amount of stock to the amount of stock being restocked.
	 *
	 * @param amount Amount to add
	 */
	public synchronized void startRestock(double amount) {
		setRestocking(restocking + amount);
	}

	/**
	 * Transfer the given amount of stock from the amount of stock being restocked to normal stock.
	 *
	 * @param amount Amount to transfer
	 */
	public synchronized void finishRestock(double amount) {
		setRestocking(restocking - amount);
		addStock(amount);
	}

	/**
	 * @return The current amount of reserved stock that cannot be used.
	 */
	public synchronized double getReserved() {
		return reserved;
	}

	/**
	 * @param restocking A new reserved level that represents the amount of stock that cannot be
	 *        used.
	 */
	private synchronized void setReserved(double reserved) {
		if (reserved < 0) {
			throw new IllegalArgumentException("Reserved cannot be set to a negative amount");
		}
		if (reserved > stock) {
			throw new IllegalArgumentException("Cannot set reserved to more than stock");
		}
		this.reserved = reserved;
	}

	/**
	 * Add the given amount of stock to stock that is reserved.
	 *
	 * @param amount Amount to add
	 */
	public synchronized void reserveStock(double amount) {
		setReserved(reserved + amount);
	}

	/**
	 * Removes the given amount of stock from stock that is reserved.
	 *
	 * @param amount Amount to remove
	 */
	public synchronized void unreserveStock(double amount) {
		setReserved(reserved - amount);
	}

	/**
	 * @return The current stock level at which restocking should start
	 */
	public synchronized int getRestockLevel() {
		return restockLevel;
	}

	/**
	 * @param restockLevel A stock level at which restocking should start
	 */
	public synchronized void setRestockLevel(int restockLevel) {
		if (restockLevel < 0) {
			throw new IllegalArgumentException("Restock level must be zero or positive");
		}
		this.restockLevel = restockLevel;
	}

	/**
	 * @return Whether stock should be allowed to restocked; this is 
	 * not enforced but can be used as a flag.
	 */
	public boolean isStockable() {
		return stockable;
	}

	/**
	 * @param stockable Whether stock should be allowed to restocked; this will 
	 * not be enforced but should be used as a flag.
	 */
	public void setStockable(boolean stockable) {
		this.stockable = stockable;
	}
	
	/**
	 * Available stock is defined as all stock that is not reserved or being restocked.
	 *
	 * @return The amount of available stock
	 */
	public synchronized double getStockAvailable() {
		return stock - reserved;
	}

	/**
	 * @return True if stock level has fallen below restock level and stock is stockable, else false
	 */
	public synchronized boolean isStockRequired() {
		return (isStockable() && getStockRequired() > 0);
	}

	/**
	 * Check how much stock must be added to reach the restock level taking into account that which
	 * is already being restocked.
	 *
	 * @return The amount of stock required to reach restock level
	 */
	public synchronized double getStockRequired() {
		return (restockLevel - (stock + restocking));
	}

	/**
	 * Check if a given amount of stock is available, taking into account that which is already
	 * reserved.
	 *
	 * @param required The amount of stock to check for
	 * @return True if the required stock is less than available stock, else false
	 */
	public synchronized boolean isEnoughStock(double required) {
		return (getStockAvailable() >= required);
	}

}
