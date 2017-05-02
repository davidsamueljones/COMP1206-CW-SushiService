package business.model;

import java.io.Serializable;

public class Postcode implements Comparable<Postcode>, Serializable {
	private static final long serialVersionUID = 3854958387837476583L;
	private final String postcode;
	private double distance;

	public Postcode(String postcode, double distance) {
		this.postcode = postcode;
		this.setDistance(distance);
	}

	public String getPostcode() {
		return postcode;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public boolean isValid() {
		return true;
	}

	@Override
	public String toString() {
		if (isValid()) {
			return String.format(postcode);
		} else {
			return "Invalid Postcode Format";
		}
	}

	@Override
	public int compareTo(Postcode other) {
		// !!! This isn't really compared properly
		return postcode.compareTo(other.getPostcode());
	}

}
