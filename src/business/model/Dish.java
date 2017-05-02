package business.model;

import java.io.Serializable;
import java.util.Objects;

import general.model.QuantityMap;
import general.utility.ErrorBuilder;
import general.utility.Validatable;

/**
 * Dish class, holds data about a dish. Name of dish defines class equality.
 *
 * Sushi Service - COMP1206 Coursework
 *
 * @author David Jones [dsj1n15]
 */
public class Dish implements Serializable, Validatable, Comparable<Dish> {
	private static final long serialVersionUID = -8693009897918595522L;
	private final String name;
	private String description;
	private Double price;
	private QuantityMap<Ingredient> ingredients;

	/**
	 * Instantiates a Dish with final fields only.
	 *
	 * @param name Name of dish
	 */
	public Dish(String name) {
		this(name, null, null, null);
	}

	/**
	 * Instantiates a Dish.
	 *
	 * @param name Name of dish
	 * @param description Initial description of dish
	 * @param price Initial price of dish
	 * @param ingredients Initial ingredients of dish
	 */
	public Dish(String name, String description, Double price,
			QuantityMap<Ingredient> ingredients) {
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
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (name == null || name.isEmpty()) {
			eb.addError("Name field is empty");
		}
		if (price < 0) {
			eb.addError("Price cannot be negative");
		}
		if (ingredients == null || ingredients.size() == 0) {
			eb.addError("The recipe uses no ingredients");
		}
		return eb;
	}

	@Override
	public String toString() {
		return String.format("SushiDish [name=%s]", name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Dish))
			return false;
		final Dish other = (Dish) obj;
		return (Objects.equals(this.name, other.name));
	}

	@Override
	public int hashCode() {
		final int prime = 7;
		return prime + (name == null ? 0 : name.hashCode());
	}

	@Override
	public int compareTo(Dish obj) {
		return name.compareTo(obj.name);
	}

}
