package general.utility;

/**
 * Class UpdateAlertObject is an implementation of UpdateAlert that also holds an object of generic
 * type. Writing of this object will trigger an update; this is alongside the default implementation
 * of a comment trigger behaviour. When the object or comment is read, the flag is reset.
 *
 * @author David Jones [dsj1n15]
 *
 * @param <T> Type of object held
 */
public class UpdateAlertObject<T> extends UpdateAlert {
	// Held objects
	private T object;

	/**
	 * Instantiate an empty UpdateAlertObject.
	 */
	public UpdateAlertObject() {
		super();
		setObject(null);
	}

	/**
	 * Read from object field, indicating that field has been read.
	 * 
	 * @return Stored object
	 */
	public synchronized T readObject() {
		isNew = false;
		return object;
	}

	/**
	 * Write an object with no error builder.
	 * 
	 * @param object Object to store
	 */
	public void write(T object) {
		write(object, null);
	}

	/**
	 * Write an object and respective error builder.
	 * 
	 * @param object Object to store
	 * @param eb Error builder with comments relating to object update
	 */
	public synchronized void write(T object, ErrorBuilder eb) {
		setObject(object);
		super.write(eb);
	}

	/**
	 * @param object The object to set
	 */
	private synchronized void setObject(T object) {
		this.object = object;
	}

	/**
	 * Clear the current state and contents
	 */
	public synchronized void clear() {
		super.clear();
		setObject(null);
	}
}
