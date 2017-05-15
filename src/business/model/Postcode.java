package business.model;

import java.io.Serializable;
import java.util.Objects;

import general.utility.ErrorBuilder;
import general.utility.Validatable;

/**
 * Postcode class, holds data about a postcode. Postcode defines equality.
 *
 * @author David Jones [dsj1n15]
 */
public class Postcode implements Serializable, Validatable, Comparable<Postcode> {
	private static final long serialVersionUID = 3854958387837476583L;
	// Postcode properties
	private final String postcode;
	private double distance;

	/**
	 * Instantiate postcode with only final fields.
	 *
	 * @param postcode Postcode represented by object
	 */
	public Postcode(String postcode) {
		this(postcode, 0);
	}

	/**
	 * Instantiate postcode with all fields.
	 *
	 * @param postcode Postcode represented by object
	 * @param distance Distance of postcode from business (km)
	 */
	public Postcode(String postcode, double distance) {
		this.postcode = postcode;
		this.setDistance(distance);
	}

	/**
	 * @return Postcode held by object
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @return Current distance of postcode from business (km)
	 */
	public double getDistance() {
		return distance;
	}

	/**
	 * @param distance New distance of postcode from business (km)
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (postcode == null) {
			eb.addError("Postcode field is empty");
		} else {
			if (!isPostcodeValid(postcode)) {
				eb.addError("Postcode structure is not valid");
			}
		}
		if (distance <= 0) {
			eb.addError("Distance must be positive");
		}
		return eb;
	}

	/**
	 * Verify structure of postcode using a simplified rule, structure of postcodes is verified as
	 * opposed to the character makeup. General postcodes are also allowed; this is where a postcode
	 * does not have to be complete meaning many postcodes falling under it. An example of this is
	 * PO7 7** would include PO7 7NH and PO7 8ED as valid options. An initial area code is the only
	 * enforced part. Valid for postcodes: A9 9AA, A9A 9AA, A99 9AA, AA9 9AA, AA9A 9AA, AA99 9AA
	 *
	 * @param postcode Postcode to verify
	 * @return Whether postcode is valid
	 */
	public static boolean isPostcodeValid(String postcode) {
		final String VALID_POSTCODE_REGEX =
				"^[A-Z]{1,2}[0-9\\*][0-9A-Z\\*]?\\s?[0-9\\*][A-Z\\*]{2}$";
		// Verify initial structure of postcode
		if (postcode.matches(VALID_POSTCODE_REGEX)) {
			// Verify that any generalisation is not made specific at any point
			// i.e. Allow: AA* *** but not AA* *AA
			final int generalStart = postcode.indexOf("*");
			if (generalStart >= 0) {
				return postcode.substring(generalStart).matches("^(\\*+|\\s)$");
			}
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return postcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Postcode))
			return false;
		final Postcode other = (Postcode) obj;
		return (Objects.equals(this.postcode, other.postcode));
	}

	@Override
	public int hashCode() {
		final int prime = 7;
		return prime + (postcode == null ? 0 : postcode.hashCode());
	}

	@Override
	public int compareTo(Postcode other) {
		// This should be an alphanumeric sort but not required
		return postcode.compareTo(other.getPostcode());
	}

}
