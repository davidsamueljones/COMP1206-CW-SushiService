package general.utility;

/**
 * Class MonitoredObject, holds an object and an error builder to indicate issues. When object is
 * updated (written) a flag is set to indicate a new object is available. When the object is read
 * the flag is reset. Reading the error builder will not cause the flag to be reset as it is assumed
 * an object read is done first. Appropriate methods are provided to allow for waiting for an
 * update.
 *
 * @author David Jones [dsj1n15]
 *
 * @param <T> Type of object held
 */
public class MonitoredObject<T> {
	// Held objects
	private T object;
	private ErrorBuilder eb;
	// Whether an update is available
	private boolean isNew;

	/**
	 * Instantiate an empty monitored object.
	 */
	public MonitoredObject() {
		setObject(null);
		setErrorBuilder(null);
		// Indicate that no object is available despite set
		isNew = false;
	}

	/**
	 * Read from object field, indicating that field has been read.
	 * 
	 * @return Stored object
	 */
	public synchronized T read() {
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
		setErrorBuilder(eb);
		// Trigger those waiting for new
		isNew = true;
		this.notify();
	}

	/**
	 * @param object The object to set
	 */
	private synchronized void setObject(T object) {
		this.object = object;
	}

	/**
	 * @return Attached error builder
	 */
	public synchronized ErrorBuilder getErrorBuilder() {
		return eb;
	}

	/**
	 * @param eb The error builder to set, null will create a new error builder.
	 */
	private void setErrorBuilder(ErrorBuilder eb) {
		if (eb == null) {
			eb = new ErrorBuilder();
		} else {
			this.eb = eb;
		}
	}

	/**
	 * @return Non-waiting check if update available
	 */
	public synchronized Boolean isNew() {
		return isNew;
	}

	/**
	 * Wait for new update with no timeout.
	 * 
	 * @return Whether there was an update
	 */
	public boolean waitForNew() {
		return waitForNew(0);
	}

	/**
	 * Wait for a new update, timing out after the given time.
	 * 
	 * @param timeout How long to wait before timeout (ms where 0 = no timeout)
	 * @return Whether there was an update
	 */
	public synchronized boolean waitForNew(int timeout) {
		try {
			// If new update is available, there is no need to wait
			if (!isNew()) {
				this.wait(timeout);
			}
		} catch (final InterruptedException e) {
			// wait was interrupted but still check (do nothing)
		}
		// Check if new now available
		if (isNew()) {
			return true;
		} else {
			return false;
		}
	}

}
