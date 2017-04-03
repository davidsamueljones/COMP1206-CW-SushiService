class Levels {
	private int stock;
	private int availableStock;
	private int restockLevel;
	
	/**
	 * Instantiate levels with no stock and a restocking level.
	 * @param restockLevel
	 */
	public Levels(int restockLevel) {
		this(0, 0, restockLevel);
	}
	
	/**
	 * Instantiate levels.
	 * @param restockLevel
	 */
	public Levels(int stockLevel, int availableStock, int restockLevel) {
		setStock(stockLevel);
		setAvailableStock(availableStock);
		setRestockLevel(restockLevel);
	}

	/**
	 * @return The current stock level, both available and unavailable
	 */
	public int getStock() {
		return stock;
	}

	/**
	 * @param stock A new stock level
	 */
	private void setStock(int stock) {
		if (stock < 0) {
			throw new IllegalArgumentException("Total stock count must be zero or positive");
		}
		this.stock = stock;
	}

	/**
	 * Add an amount of stock to stock. Also add to available stock as indicated.
	 * @param amount Amount of stock to add
	 * @param available Whether stock should be available immediately.
	 */
	public void addStock(int amount, boolean available) {
		setStock(stock+amount);
		if (available) {
			makeStockAvailable(amount);
		}
	}
	
	/**
	 * Removes stock from total stock and available stock.
	 * Will fail if not enough stock available.
	 * @param amount Amount of stock to remove
	 */
	public void removeStock(int amount) {
		makeStockUnavailable(amount);
		setStock(stock-amount);
	}
	
	/**
	 * @return The amount of available stock
	 */
	public int getAvailableStock() {
		return availableStock;
	}
	
	/**
	 * @return The amount of stock that is unavailable out of total stock
	 */
	public int getUnavailableStock() {
		return stock - availableStock;
	}
	
	/**
	 * @param availableStock Amount of stock to be available
	 */
	private void setAvailableStock(int availableStock) {
		if (availableStock < 0) {
			throw new IllegalArgumentException("Stock available must be zero or positive");
		}
		if (availableStock > stock) {
			throw new IllegalArgumentException("Stock available must be less or equal to stock that exists");
		}
		this.availableStock = availableStock;
	}
	
	/**
	 * Increment the current amount of available stock by a given amount
	 * @param amount Amount to increment by
	 */
	public void makeStockAvailable(int amount) {
		setAvailableStock(availableStock+amount);
	}
	
	/**
	 * Decrement the current amount of available stock by a given amount
	 * @param amount Amount to decrement by
	 */
	public void makeStockUnavailable (int amount) {
		setAvailableStock(availableStock-amount);
	}
	
	/**
	 * @return The current level at which restocking should start
	 */
	public int getRestockLevel() {
		return restockLevel;
	}

	/**
	 * @param restockLevel The level at which restocking should start
	 */
	public void setRestockLevel(int restockLevel) {
		if (restockLevel < 0) {
			throw new IllegalArgumentException("Restock level must be zero or positive");
		}
		this.restockLevel = restockLevel;
	}

	/**
	 * 
	 * @return True if stock level has fallen below restock level, else false
	 */
	public boolean isStockRequired() {
		return (stockRequired() > 0);
	}

	/**
	 * @return The amount of stock required to reach restock level
	 */
	public int stockRequired() {
		return (restockLevel - stock);
	}

	/**
	 * Check if a given amount of stock is available
	 * @param required The amount of stock to check for
	 * @return True if the required stock is less than available stock, else false
	 */
	public boolean isEnoughStock(int required) {
		return (availableStock >= required);
	}
}