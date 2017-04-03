import java.util.Objects;


/**
 * Ingredient class, holds data about an ingredient.
 * Name of ingredient defines class equality. 
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public class Ingredient {
	private final String name;
	private String unit;
	private Supplier supplier;

	/**
	 * Instantiates an Ingredient.
	 * @param name Name of ingredient
	 * @param unit Initial unit type of ingredient
	 * @param supplier Initial supplier of ingredient
	 */
	public Ingredient(String name, String unit, Supplier supplier) {
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
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit A unit type
	 */
	public void setUnit(String unit) {
		this.unit = unit;
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
	public String toString() {
		return String.format("Ingredient [name=%s]", name);
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Ingredient)) return false;
		Ingredient other = (Ingredient) obj;
		return (Objects.equals(this.name, other.name));
	}               

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + (name == null ? 0 : name.hashCode());
	}
	
}
