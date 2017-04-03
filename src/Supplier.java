import java.util.Objects;

/**
 * Supplier class, holds data about a supplier.
 * Name of supplier defines class equality. 
 * 
 * Sushi Service - COMP1206 Coursework
 * @author David Jones [dsj1n15]
 */
public class Supplier {
	private final String name;
	private int distance;
	
	/**
	 * Instantiates a supplier.
	 * @param name Name of supplier
	 * @param distance Initial distance of supplier from business
	 */
	public Supplier(String name, int distance) {
		this.name = name;
		this.distance = distance;
	}
	
	/**
	 * @return The supplier's name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return The current distance of the supplier from business
	 */
	public int getDistance() {
		return distance;
	}
	
	/**
	 * @param distance A distance of the business from the business
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}
	
	@Override
	public String toString() {
		return String.format("Supplier [name=%s]", name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Supplier)) return false;
		Supplier other = (Supplier) obj;
		return (Objects.equals(this.name, other.name));
	}               

	@Override
	public int hashCode() {
		final int prime = 11;
		return prime + (name == null ? 0 : name.hashCode());
	}
	
}
