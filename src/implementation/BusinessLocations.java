package implementation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import business.model.BusinessLocation;

/**
 * BusinessLocations class that holds a static list of business locations. These are addressable
 * through a public immutable map and should only be added through this class.
 *
 * @author David Jones [dsj1n15]
 */
public class BusinessLocations {
	// Possible business locations
	public static final Map<String, BusinessLocation> LOCATIONS;

	/**
	 * Do not allow this class to be instantiated
	 */
	public BusinessLocations() {}

	/**
	 * Create an unmodifiable map of locations. Valid locations are defined here.
	 */
	static {
		// Initialise a modifiable map of locations
		final Map<String, BusinessLocation> locs = new HashMap<>();
		// Add locations
		addLocation(locs, "Southampton_1", "localhost", 23534);
		addLocation(locs, "Southampton_2", "localhost", 23536);
		addLocation(locs, "Portsmouth", "localhost", 23537);
		// Make public map an unmodifiable copy
		LOCATIONS = Collections.unmodifiableMap(locs);
	}

	/**
	 * Add location to a given map using name as a key.
	 *
	 * @param locations Map to add location to
	 * @param identifier Name of location (using only word characters [A-Za-z_])
	 * @param hostname Hostname of server
	 * @param port Port of server
	 */
	static private void addLocation(Map<String, BusinessLocation> locations, String identifier,
			String hostname, int port) {
		// Validate identifier
		if (!BusinessLocation.isValidIdentifier(identifier)) {
			throw new IllegalArgumentException("Location identifier not valid");
		}
		if (locations.containsKey(identifier)) {
			throw new IllegalArgumentException("Location already exists");
		}
		// Add location to map using identifier as key
		locations.put(identifier, new BusinessLocation(identifier, hostname, port));
	}

}
