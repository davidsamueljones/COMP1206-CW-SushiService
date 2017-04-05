import java.io.Serializable;

public class Customer implements Serializable {
	private static final long serialVersionUID = 5915055766113455685L;
	
	private final String name;
	private String address;
	private String postcode;
	private final CustomerLogin login;
	
	
	public Customer(String name, String address, String postcode, CustomerLogin login) {
		// Verify arguments for finals		
		this.name = name;
		this.address = address;
		this.postcode = postcode;
		this.login = login;
	}

	public Validator validate() {
		return validate(new Validator());
	}
	
	public Validator validate(Validator validator) {
		if (name == null || name.isEmpty()) {
			validator.addComment("- Name field is empty", true);
		}
		if (address == null || address.isEmpty()) {
			validator.addComment("- Address field is empty", true);
		}
		if (postcode == null || postcode.isEmpty()) {
			validator.addComment("- Postcode field is empty", true);
		}
		if (login == null) {
			validator.addComment("- Customer has no login object", true);
		}
		else {
			login.validate(validator);
		}
		return validator;
	}
	
	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPostcode() {
		return postcode;
	}


	public CustomerLogin getLogin() {
		return login;
	}
		
}
