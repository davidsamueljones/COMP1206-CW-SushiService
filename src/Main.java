public class Main {
	
	public static void main(String[] args) {
		Supplier sup = new Supplier("FoodRUs", 5);
		Ingredient ing1 = new Ingredient("Carrot", "Items", sup);
		Ingredient ing2 = new Ingredient("Steak", "oz", sup);
		
		QuantityMap<Ingredient> recipe = new QuantityMap<Ingredient>();
		recipe.put(ing1, 5);
		recipe.put(ing2, 3);
		
		Dish dish1 = new Dish("Steak and Carrots", "It is what it is", 10, recipe);
		StockManagement sm = new StockManagement();
		StockHandler<Ingredient> ingredients = sm.getIngredients();
		StockHandler<Dish> dishes = sm.getDishes();
		ingredients.add(ing1, new Levels(15, 15, 20));
		ingredients.add(ing2, new Levels(9, 6, 6));
		dishes.add(dish1, new Levels(1, 1, 3));

		KitchenStaffMember ks = new KitchenStaffMember("Steph", sm, true);
		KitchenStaffMember ks2 = new KitchenStaffMember("David", sm, true);
	    Thread t = new Thread(ks);
	    Thread t2 = new Thread(ks2);
	    t.start();

		 t2.start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		 System.out.println(ingredients.removeStock(ing1, 10));
	}
	
}
