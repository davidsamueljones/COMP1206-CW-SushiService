package business.model;

import java.io.Serializable;
import java.util.Objects;

import general.utility.ErrorBuilder;
import general.utility.Validatable;

/**
 * Supplier class, holds data about a supplier. Name of supplier defines class equality.
 *
 * Sushi Service - COMP1206 Coursework
 *
 * @author David Jones [dsj1n15]
 */
public class Supplier implements Serializable, Validatable, Comparable<Supplier> {
	private static final long serialVersionUID = 4458598135397412806L;
	private final String name;
	private double distance;
	private boolean beingRestocked;

	/**
	 * Instantiates a Supplier with final fields only.
	 *
	 * @param name Name of supplier
	 */
	public Supplier(String name) {
		this(name, 0);
	}

	/**
	 * Instantiates a supplier.
	 *
	 * @param name Name of supplier
	 * @param distance Initial distance of supplier from business
	 */
	public Supplier(String name, double distance) {
		this.name = name;
		this.setDistance(distance);
		this.setBeingRestocked(false);
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
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance A distance of the business from the business
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	public synchronized boolean isBeingRestocked() {
		return beingRestocked;
	}

	public synchronized void setBeingRestocked(boolean beingRestocked) {
		this.beingRestocked = beingRestocked;
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (name == null || name.isEmpty()) {
			eb.addError("Name field is empty");
		}
		if (distance <= 0) {
			eb.addError("Distance must be positive");
		}
		return eb;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Supplier))
			return false;
		final Supplier other = (Supplier) obj;
		return (Objects.equals(this.name, other.name));
	}

	@Override
	public int hashCode() {
		final int prime = 11;
		return prime + (name == null ? 0 : name.hashCode());
	}

	@Override
	public int compareTo(Supplier obj) {
		return name.compareTo(obj.name);
	}

}
