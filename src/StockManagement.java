public class StockManagement {
	private final StockHandler<Ingredient> ingredients;
	private final StockHandler<Dish> dishes;
	
	public StockManagement() {
		// Lock stock handlers using current instance so that behaviour can be
		// synchronised between different stocks
		ingredients = new StockMap<Ingredient>();
		dishes = new StockMap<Dish>();
	}
	
	/**
	 * @return A StockHandler that allows safe viewing and modification of current dish stock.
	 */
	public StockHandler<Dish> getDishes() {
		return dishes;
	}
	
	/**
	 * @return A StockHandler that allows safe viewing and modification of current ingredient stock.
	 */
	public StockHandler<Ingredient> getIngredients() {
		return ingredients;
	}
	
	/**
	 * 
	 * @return
	 */
	public Dish getRestockableDish() {
		synchronized (ingredients) {
			synchronized (dishes) {
				for (Dish dish : dishes.getStockRequired().keySet()) {
					if (ingredients.isEnoughAvailableStock(dish.getIngredients())) {
						return dish;
					}	
				}
			}
		}
		return null;
	}
	

}
