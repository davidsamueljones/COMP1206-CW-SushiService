package general.model;

import java.util.HashMap;
import java.util.Map;

import general.utility.ErrorBuilder;

/**
 * Extension of message class to send a map of objects with a standard message. Map is addition and
 * view only meaning objects cannot be removed once added.
 *
 * @author David Jones [dsj1n15]
 */
public class ObjectMessage extends Message {
	private static final long serialVersionUID = 9014642389601797656L;
	private final Map<String, Object> objects;

	/**
	 * Initialise a message with only a command.
	 *
	 * @param command Command dictating how message should be handled
	 */
	public ObjectMessage(Command command) {
		this(command, null, null);
	}

	/**
	 * Initialise a message with a command and comments held by an error builder.
	 *
	 * @param command Command dictating how message should be handled
	 * @param eb Comments in error builder to send
	 */
	public ObjectMessage(Command command, ErrorBuilder eb) {
		this(command, eb, null);
	}

	/**
	 * Initialise a message with a command and a map of objects.
	 *
	 * @param command Command dictating how message should be handled
	 * @param objects Map of identifiers and objects to send, null will cause a new map to be
	 *        created
	 */
	public ObjectMessage(Command command, Map<String, Object> objects) {
		this(command, null, objects);
	}

	/**
	 * Initialise a message with a command, comments held by an error builder and a map of objects.
	 *
	 * @param command Command dictating how message should be handled
	 * @param eb Comments in error builder to send
	 * @param objects Map of identifiers and objects to send, null will cause a new map to be
	 *        created
	 */
	public ObjectMessage(Command command, ErrorBuilder eb, Map<String, Object> objects) {
		super(command, eb);
		// Handle object argument
		if (objects == null) {
			objects = new HashMap<>();
		}
		this.objects = objects;
	}

	/**
	 * Get an attached object from the stored map. If the identifier is not found an exception is
	 * thrown as this indicates a structural problem with the message.
	 *
	 * @param identifier Key for object to get
	 * @return Attached object
	 */
	public Object getObject(String identifier) {
		if (!objects.containsKey(identifier)) {
			throw new IllegalArgumentException("Message does not contain message with identifier");
		}
		return objects.get(identifier);
	}

	/**
	 * Get an attached object from the stored map as a given type using a safe cast. This method
	 * only has defined behaviour for single object instances of a single type. Failure to cast will
	 * result in null being returned.
	 *
	 * @param identifier Key for object to get
	 * @param type Type to cast to
	 * @return Attached object casted to type, null on failure
	 */
	public <T> T getObject(String identifier, Class<T> type) {
		// Get object
		final Object object = getObject(identifier);
		// Accept if null, otherwise attempt cast
		if (object == null || type.isInstance(object)) {
			return type.cast(object);
		} else {
			throw new ClassCastException("Message object not of expected type");
		}
	}

	/**
	 * Add an object to the messages stored map.
	 *
	 * @param identifier Key for object to be assigned to, must not exist in map or be null
	 * @param object Object to attach (value assigned to key)
	 */
	public void addObject(String identifier, Object object) {
		// Validate identifier
		if (identifier == null) {
			throw new IllegalArgumentException("Identifier cannot be null");
		}
		if (objects.containsKey(identifier)) {
			throw new IllegalArgumentException("Identifier already exists");
		}
		// Store
		objects.put(identifier, object);
	}

}
