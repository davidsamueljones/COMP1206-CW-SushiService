package business.model;

import java.io.Serializable;
import java.util.Objects;

import general.utility.ErrorBuilder;
import general.utility.Validatable;

/**
 * Customer class, holds data about a customer. Login object of customer defines class equality.
 *
 * @author David Jones [dsj1n15]
 */
public class Customer implements Serializable, Validatable {
	private static final long serialVersionUID = 5915055766113455685L;
	// Customer properties
	private String name;
	private String address;
	private Postcode postcode;
	// Login owned by customer
	private final CustomerLogin login;

	/**
	 * Instantiate a customer with final fields only.
	 *
	 * @param login Login owned by customer
	 */
	public Customer(CustomerLogin login) {
		this(null, null, null, login);
	}

	/**
	 * Instantiate a customer with all fields.
	 *
	 * @param name Full name or nickname for customer (not used for equality)
	 * @param address Customers address, addressable when combined with postcode
	 * @param postcode Postcode/area customer is located in
	 * @param login Login owned by customer
	 */
	public Customer(String name, String address, Postcode postcode, CustomerLogin login) {
		this.name = name;
		this.address = address;
		this.postcode = postcode;
		this.login = login;
	}

	/**
	 * @return Current name of customer
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name New name for customer
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Customer's address to be used in conjunction with postcode
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address New address for customer
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return Customer's postcode/area
	 */
	public Postcode getPostcode() {
		return postcode;
	}

	/**
	 *
	 * @param postcode New postcode/area for customer
	 */
	public void setPostcode(Postcode postcode) {
		this.postcode = postcode;
	}

	/**
	 * @return Customer's respective login object
	 */
	public CustomerLogin getLogin() {
		return login;
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (name == null || name.isEmpty()) {
			eb.addError("Name field is empty");
		}
		if (address == null || address.isEmpty()) {
			eb.addError("Address field is empty");
		}
		if (postcode == null) {
			eb.addError("Postcode field is empty");
		}
		if (login == null) {
			eb.addError("Customer has no login object");
		} else {
			eb.append(login.validate());
		}
		return eb;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Customer))
			return false;
		final Customer other = (Customer) obj;
		return (Objects.equals(this.login, other.login));
	}

	@Override
	public int hashCode() {
		final int prime = 23;
		return prime + (login == null ? 0 : login.hashCode());
	}

}
