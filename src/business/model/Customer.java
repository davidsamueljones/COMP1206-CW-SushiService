package business.model;

import java.io.Serializable;
import java.util.Objects;

import general.utility.ErrorBuilder;
import general.utility.Validatable;

public class Customer implements Serializable, Validatable {
	private static final long serialVersionUID = 5915055766113455685L;

	private final String name;
	private final String address;
	private final Postcode postcode;
	private final CustomerLogin login;

	public Customer(String name, String address, Postcode postcode, CustomerLogin login) {
		this.name = name;
		this.address = address;
		this.postcode = postcode;
		this.login = login;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public Postcode getPostcode() {
		return postcode;
	}

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
