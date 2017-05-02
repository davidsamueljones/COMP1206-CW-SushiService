package business.model;

import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * BusinessLocation class, holds server information for a location. Identifier of business location
 * defines class equality.
 *
 * @author David Jones [dsj1n15]
 */
public class BusinessLocation {
	// Name of business represented
	private final String identifier;
	// Server details
	private final String hostname;
	private final int port;

	/**
	 * Instantiate a business location.
	 *
	 * @param name Name of business represented
	 * @param hostname Hostname of server
	 * @param port Port of server
	 */
	public BusinessLocation(String identifier, String hostname, int port) {
		if (!isValidIdentifier(identifier)) {
			throw new IllegalArgumentException("Identifier is not valid");
		}
		this.identifier = identifier;
		this.hostname = hostname;
		this.port = port;
	}

	/**
	 * @return Socket address of business hostname and port
	 */
	public InetSocketAddress getAddress() {
		return new InetSocketAddress(hostname, port);
	}

	/**
	 * @return Identifier of location, defined as name
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Check if passed identifier only includes the characters A-Z, a-z, 1-9, _ and is not blank.
	 *
	 * @param identifier Identifier to validate
	 * @return Whether identifier is valid
	 */
	public static boolean isValidIdentifier(String identifier) {
		return Pattern.matches("^\\w+$", identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof BusinessLocation))
			return false;
		final BusinessLocation other = (BusinessLocation) obj;
		return (Objects.equals(this.identifier, other.identifier));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime + (identifier == null ? 0 : identifier.hashCode());
	}
}
