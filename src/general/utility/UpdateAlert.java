package general.utility;

/**
 * Class UpdateAlert, class that alerts waiting threads when an update occurs. An update is defined
 * as a comment field being written to. When comments are written a flag is set to indicate new
 * comments are available. When the comments are read the flag is reset. Appropriate methods are
 * provided to allow for waiting for an update.
 *
 * @author David Jones [dsj1n15]
 */
public class UpdateAlert {
	// Status comments
	protected ErrorBuilder eb;
	// Whether an update is available
	protected boolean isNew;

	/**
	 * Instantiate an UpdateAlert.
	 */
	public UpdateAlert() {
		setErrorBuilder(null);
		// Indicate that no update is available despite set
		isNew = false;
	}

	/**
	 * Read from comments field, indicating that field has been read.
	 * 
	 * @return Stored comments as error builder
	 */
	public synchronized ErrorBuilder readComments() {
		isNew = false;
		return eb;
	}

	/**
	 * Write an error builder to the update alert, triggering those waiting for an update.
	 * 
	 * @param eb Error builder with comments regarding update alert
	 */
	public synchronized void write(ErrorBuilder eb) {
		setErrorBuilder(eb);
		// Trigger those waiting for new
		isNew = true;
		this.notify();
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
			// wait was interrupted but still check if update has occurred
		}
		// Check if new now available
		if (isNew()) {
			return true;
		} else {
			System.err.println("[UPDATE ALERT] : Timeout whilst waiting for new");
			return false;
		}
	}

	/**
	 * Clear the current state and contents
	 */
	public synchronized void clear() {
		setErrorBuilder(null);
		this.isNew = false;
	}

}
