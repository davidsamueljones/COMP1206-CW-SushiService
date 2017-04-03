public class StockManagement {
	private final StockHandler<Ingredient> ingredients;
	private final StockHandler<Dish> dishes;
	
	public StockManagement() {
		ingredients = new StockMap<Ingredient>(this);
		dishes = new StockMap<Dish>(this);
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
	
	

	
	public synchronized Dish getRestockableDish() {
		for (Dish dish : dishes.getStockRequired().keySet()) {
			if (ingredients.isEnoughAvailableStock(dish.getIngredients())) {
				return dish;
			}	
		}
		return null;
	}
	

}
