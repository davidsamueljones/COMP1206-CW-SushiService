package business.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import general.utility.ErrorBuilder;
import general.utility.Validatable;

public class CustomerLogin implements Serializable, Validatable {
	private static final long serialVersionUID = -4963506245761340239L;
	private static final String SHA256_REGEX = "^[A-Fa-f0-9]{64}$";

	private final String username;
	private String passwordHash;
	private String checksum;

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
			this.checksum = null;
		} else {
			this.passwordHash = createHash(password);
			refreshChecksum();
		}
	}

	public boolean isChecksumValid() {
		final String newChecksum = makeChecksum();
		if (checksum.equals(newChecksum)) {
			return true;
		}
		return false;
	}

	public void refreshChecksum() {
		checksum = makeChecksum();
	}

	private String makeChecksum() {
		return createHash(username + passwordHash);
	}

	@Override
	public ErrorBuilder validate() {
		final ErrorBuilder eb = new ErrorBuilder();
		if (username == null || username.isEmpty()) {
			eb.addError("Username field is empty");
		}
		if (passwordHash == null) {
			eb.addError("Password field is empty");
		} else if (!isPasswordHashed(passwordHash)) {
			eb.addError("Password field is not hashed correctly");
		}
		return eb;
	}

	public static boolean isPasswordValid(String password) {
		return (password != null && !password.isEmpty());
	}

	public static boolean isPasswordHashed(String password) {
		return password.matches(SHA256_REGEX);
	}

	/**
	 * This should really be salted and more secure but is just put to give an idea that passwords
	 * should be encrypted.
	 * http://howtodoinjava.com/security/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
	 *
	 * @param password
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
		} catch (final NoSuchAlgorithmException e1) {
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
