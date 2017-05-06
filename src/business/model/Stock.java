package business.model;

import java.io.Serializable;

import general.model.Quantity;
import general.model.QuantityMap;

/**
 * Container class for multiple stock maps. Stock contains ingredients and dishes and functionality
 * to tie the two.
 *
 * @author David Jones [dsj1n15]
 */
public class Stock implements Serializable {
	private static final long serialVersionUID = -1556903673667706911L;
	// Stocks
	public final StockMap<Ingredient> ingredients;
	public final StockMap<Dish> dishes;

	/**
	 * Instantiate a new stock with new ingredient and dish stocks.
	 */
	public Stock() {
		ingredients = new StockMap<>();
		dishes = new StockMap<>();
	}

	/**
	 * Find a dish below its restocking level with enough stock available.
	 *
	 * @return Dish to restock if found, else null
	 */
	public Quantity<Dish> getDishToRestock() {
		// Lock both ingredients and stock to ensure a dish still requires restock
		// after further checks.
		synchronized (ingredients) {
			synchronized (dishes) {
				for (final Dish dish : dishes.getStockRequired().keySet()) {
					if (ingredients.isEnoughAvailableStock(dish.getIngredients())) {
						return new Quantity<>(dish, 1);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Find a supplier that has an ingredient that requires restocking.
	 *
	 * @return Supplier that requires restocking.
	 */
	public Supplier getSupplierToRestock() {
		synchronized (ingredients) {
			final QuantityMap<Ingredient> reqRestock = ingredients.getStockRequired();
			for (final Ingredient ingredient : reqRestock.keySet()) {
				final Supplier supplier = ingredient.getSupplier();
				if (supplier != null && !supplier.isBeingRestocked()) {
					return supplier;
				}
			}
		}
		return null;
	}

	/**
	 * Using a quantity map of ingredients and a supplier, return only those that match the target
	 * supplier.
	 *
	 * @param targetSupplier Supplier to target
	 * @param ingredients Map of ingredients to filter
	 * @return Filtered map of ingredients
	 */
	public static QuantityMap<Ingredient> getIngredientsFromSupplier(Supplier targetSupplier,
			QuantityMap<Ingredient> ingredients) {
		final QuantityMap<Ingredient> fromSupplier = new QuantityMap<>();
		for (final Ingredient ingredient : ingredients.keySet()) {
			final Supplier supplier = ingredient.getSupplier();
			if (supplier != null && supplier.equals(targetSupplier)) {
				fromSupplier.put(ingredient, ingredients.get(ingredient));
			}
		}
		return fromSupplier;
	}

}
