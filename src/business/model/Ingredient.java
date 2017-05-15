package business.model;

import java.io.Serializable;
import java.util.Objects;

import general.utility.ErrorBuilder;
import general.utility.Validatable;

/**
 * Ingredient class, holds data about an ingredient. Name of ingredient defines class equality.
 *
 * @author David Jones [dsj1n15]
 */
public class Ingredient implements Serializable, Validatable, Comparable<Ingredient> {
	private static final long serialVersionUID = -2561118496190324325L;
	private final String name;
	private final Unit unit;
	private Supplier supplier;

	/**
	 * Instantiates an Ingredient with final fields only.
	 *
	 * @param name Name of ingredient
	 * @param unit Initial unit type of ingredient
	 */
	public Ingredient(String name, Unit unit) {
		this(name, unit, null);
	}

	/**
	 * Instantiates an Ingredient.
	 *
	 * @param name Name of ingredient
	 * @param unit Initial unit type of ingredient
	 * @param supplier Initial supplier of ingredient
	 */
	public Ingredient(String name, Unit unit, Supplier supplier) {
		this.name = name;
		this.unit = unit;
		this.supplier = supplier;
	}

	/**
	 * @return The ingredient's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return The ingredient's current unit type
	 */
	public Unit getUnit() {
		return unit;
	}

	/**
	 * @return The supplier object that is supplying the ingredient
	 */
	public Supplier getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier A supplier object that can supply an ingredient
	 */
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (name == null || name.isEmpty()) {
			eb.addError("Name field is empty");
		}
		if (unit == null) {
			eb.addError("Unit field is empty");
		}
		return eb;
	}

	@Override
	public String toString() {
		return String.format("%s (%s)", name, unit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Ingredient))
			return false;
		final Ingredient other = (Ingredient) obj;
		return (Objects.equals(this.name, other.name));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + (name == null ? 0 : name.hashCode());
	}

	@Override
	public int compareTo(Ingredient obj) {
		return name.compareTo(obj.name);
	}

	/**
	 * List of units allowed by ingredient.
	 *
	 * @author David Jones [dsj1n15]
	 */
	public static enum Unit {
		g, kg, ml, l, oz, lb, items
	}
}
