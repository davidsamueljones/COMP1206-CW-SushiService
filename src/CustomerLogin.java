import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class CustomerLogin implements Serializable {
	private static final long serialVersionUID = -4963506245761340239L;
	private static final String SHA256_REGEX="^[A-Fa-f0-9]{64}$";
	private final String username;
	private String passwordHash;

	public CustomerLogin(String username, String password) {
		this.username = username;
		this.setPassword(password);
	}

	public String getUsername() {
		return username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPassword(String password) {
		if (password == null) {
			this.passwordHash = null;
		}
		else {
			this.passwordHash = hashPassword(password);			
		}
	}
	
	public Validator validate() {
		return validate(new Validator());
	}
	
	public Validator validate(Validator validator) {
		if (username == null || username.isEmpty()) {
			validator.addComment("- Username field is empty", true);
		}
		if (passwordHash == null) {
			validator.addComment("- Password field is empty", true);
		}
		else if (!isPasswordHashed(passwordHash)) {
			validator.addComment("- Password field is not hashed correctly", true);
		}
		return validator;
	}
	
	public static boolean isPasswordValid(String password) {
		return (password == null || password.isEmpty());
	}
	
	public static boolean isPasswordHashed(String password) {
	    return password.matches(SHA256_REGEX);
	}
	
	/**
	 * This should really be salted and more secure but is just put to give an idea that
	 * passwords should be encrypted.
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	 * @param password
	 */
	private static String hashPassword(String password) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
    		return sb.toString();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof CustomerLogin)) return false;
		CustomerLogin other = (CustomerLogin) obj;
		return (Objects.equals(this.username, other.username));
	}               

	@Override
	public int hashCode() {
		final int prime = 19;
		return prime + (username == null ? 0 : username.hashCode());
	}

}