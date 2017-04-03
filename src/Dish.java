import java.util.Objects;

/**
 * Dish class, holds data about a dish.
 * Name of dish defines class equality. 
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public class Dish {
	private final String name;
	private String description;
	private double price;
	private QuantityMap<Ingredient> ingredients;

	/**
	 * Instantiates a Dish.
	 * @param name Name of dish
	 * @param description Initial description of dish
	 * @param price Initial price of dish
	 * @param ingredients Initial ingredients of dish
	 */
	public Dish(String name, String description, double price, QuantityMap<Ingredient> ingredients) {
		this.name = name;
		this.description = description;
		this.price = price;
		this.ingredients = ingredients;
	}
	
	/**
	 * @return The dish's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The dish's current description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description A description of a dish
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return The dish's current price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price A price
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return The dish's ingredients and quantities
	 */
	public QuantityMap<Ingredient> getIngredients() {
		return ingredients;
	}

	/**
	 * @param ingredients Ingredients and respective quantities
	 */
	public void setIngredients(QuantityMap<Ingredient> ingredients) {
		this.ingredients = ingredients;
	}
	
	@Override
	public String toString() {
		return String.format("SushiDish [name=%s]", name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Dish)) return false;
		Dish other = (Dish) obj;
		return (Objects.equals(this.name, other.name));
	}               

	@Override
	public int hashCode() {
		final int prime = 7;
		return prime + (name == null ? 0 : name.hashCode());
	}
	
}
