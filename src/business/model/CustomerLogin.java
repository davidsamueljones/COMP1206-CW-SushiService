package business.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import general.utility.ErrorBuilder;
import general.utility.Validatable;

/**
 * CustomerLogin class, holds login information for a customer. The login can be verified using a
 * checksum consisting of username and the password hash. It should be noted that this is not a
 * hugely secure implementation but shows some of the methods that may be required in data storage.
 *
 * Username of customer login defines class equality.
 *
 * @author David Jones [dsj1n15]
 */
public class CustomerLogin implements Serializable, Validatable {
	private static final long serialVersionUID = -4963506245761340239L;
	// Password validation
	private static final String VALID_PASSWORD_REGEX =
			"^(?=\\S*?[A-Z])(?=\\S*?[a-z])(?=\\S*?[0-9]).{6,}$";
	private static final String SHA256_REGEX = "^[A-Fa-f0-9]{64}$";

	// Login properties
	private final String username;
	private String passwordHash;
	// Integrity check
	private String checksum;

	/**
	 * Instantiate a login with only final fields.
	 *
	 * @param username Unique identifier of login
	 */
	public CustomerLogin(String username) {
		this(username, null);
	}

	/**
	 * Instantiate a login with all fields.
	 *
	 * @param username Unique identifier of login
	 * @param password Initial password
	 */
	public CustomerLogin(String username, String password) {
		this.username = username;
		this.setPassword(password);
	}

	/**
	 * @return Unique identifier attached to login
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return The stored password hash
	 */
	public String getPasswordHash() {
		return passwordHash;
	}

	/**
	 * Set the password using a raw string, hash it for storage. Do not enforce password validity,
	 * passwords should be validated prior to set.
	 *
	 * @param password
	 */
	public void setPassword(String password) {
		if (password == null) {
			this.passwordHash = null;
			this.checksum = null;
		} else {
			this.passwordHash = createHash(password);
			refreshChecksum();
		}
	}

	/**
	 * Remake checksum and verify it against that held in the login object.
	 *
	 * @return Whether the checksums are the same.
	 */
	public boolean isChecksumValid() {
		final String newChecksum = makeChecksum();
		return checksum.equals(newChecksum);
	}

	/**
	 * Remake the checksum and set it to the login object.
	 */
	public void refreshChecksum() {
		checksum = makeChecksum();
	}

	/**
	 * @return A hash of username and the hashed password
	 */
	private String makeChecksum() {
		return createHash(username + passwordHash);
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (username == null || username.isEmpty()) {
			eb.addError("Username field is empty");
		} else if (!isUsernameValid(username)) {
			eb.addError("Username is not valid");
		}
		if (passwordHash == null) {
			eb.addError("Password field is empty");
		} else if (!isPasswordHashed(passwordHash)) {
			eb.addError("Password field is not hashed correctly");
		}
		return eb;
	}

	/**
	 * Verify if a given string conforms to username rules (not blank, no whitespace).
	 *
	 * @param username Username to verify
	 * @return Whether username follows rules
	 */
	public static boolean isUsernameValid(String username) {
		final Matcher matcher = Pattern.compile("\\s").matcher(username);
		return (username != null && !username.isEmpty() && !matcher.find());
	}

	/**
	 * Verify if a given string conforms to password rules (not blank, 6+ characters, uppercase,
	 * lowercase, digit).
	 *
	 * @param password Password to verify (non-hashed)
	 * @return Whether password follows rules
	 */
	public static boolean isPasswordValid(String password) {
		return (password != null && password.matches(VALID_PASSWORD_REGEX));
	}

	/**
	 * Verify the structure of a string to see if it matches that of a SHA-256 regex.
	 *
	 * @param password Hashed password
	 * @return Whether structure implies hash
	 */
	public static boolean isPasswordHashed(String password) {
		return password.matches(SHA256_REGEX);
	}

	/**
	 * Function to hash a String using SHA-256. This is a one way encryption and would not be secure
	 * enough for a final implementation, at the very least it should be salted. It may also be
	 * better to have two way encryption implemented so that password verification can happen
	 * business side. The code to create the hash was sourced from:
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	 *
	 * @param string String to hash
	 */
	private static String createHash(String string) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(string.getBytes());
			final byte[] bytes = md.digest();
			final StringBuilder sb = new StringBuilder();
			for (final byte b : bytes) {
				sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
			}
			return sb.toString();
		} catch(NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof CustomerLogin))
			return false;
		final CustomerLogin other = (CustomerLogin) obj;
		return (Objects.equals(this.username, other.username));
	}

	@Override
	public int hashCode() {
		final int prime = 19;
		return prime + (username == null ? 0 : username.hashCode());
	}

}
