package business.model;

import java.io.Serializable;

import general.utility.Validatable;

/**
 * Abstract class worker, a runnable that owns a thread and executes a task systematically.
 * Provides methods that handle thread termination and starting safely. Worker owns a thread
 * as opposed to being a thread so that thread can be restarted (a new thread is created).
 * 
 * @author David Jones [dsj1n15]
 */
public abstract class Worker implements Runnable, Serializable, Validatable {
	private static final long serialVersionUID = -3658189976773236703L;
	private static final int WAIT_TIME = 1000;

	protected final String identifier;
	private Status status;
	private String action;
	// Run fails if thread instantiated is not owned by worker
	private transient Thread thread;

	/**
	 * Instantiate a worker, stopped by default.
	 *
	 * @param identifier A string to identify the worker by
	 */
	public Worker(String identifier) {
		this.identifier = identifier;
		this.status = Status.STOPPED;
		this.action = "-";
	}

	@Override
	public void run() {
		// Verify running on the correct thread
		if (thread != null && Thread.currentThread() != thread) {
			throw new IllegalStateException("Only the thread owned by worker may do work");
		}
		// Handle run behaviour
		setStatus(Status.WORKING);
		while (getStatus() == Status.WORKING) {
			action = "IDLE";
			try {
				// Do work as defined by subclass
				doWork();
				// Wait before moving to next task
				Thread.sleep(WAIT_TIME);
			} catch (final InterruptedException e) {
				// Worker interrupted whilst working so set status to suspend
				setStatus(Status.SUSPENDED);
				// Exit loop to terminate thread terminating thread
				break;
			}
		}
		// Handle status change if stopping
		if (getStatus().equals(Status.STOPPING)) {
			action = "-";
			setStatus(Status.STOPPED);
		}
		// Thread terminating
		thread = null;
	}

	/**
	 * @return Worker's identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return What the worker is currently doing
	 */
	public synchronized String getAction() {
		return action;
	}

	/**
	 * Sets a description of the worker's current action and prints it to the command line with the
	 * worker's identifier.
	 *
	 * @param action Description to assign to action and print
	 */
	protected void actionUpdate(String action) {
		this.action = action;
		System.out.println(String.format("[%s] : %s", identifier, action));
	}

	/**
	 * @return Current status of worker
	 */
	public synchronized Status getStatus() {
		return status;
	}

	/**
	 * @param status New worker status
	 */
	protected synchronized void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @return The current thread a worker is using for doing work.
	 */
	public Thread getThread() {
		return thread;
	}

	/**
	 * If worker is deemed startable based off its current status, create a new thread of the
	 * runnable worker and start it.
	 */
	public synchronized void startWorking() {
		if (isStartable()) {
			thread = new Thread(this);
			thread.start();
		} else {
			throw new IllegalStateException("Worker already working");
		}
	}

	/**
	 * If worker is deemed stoppable based off its current status, set the status to indicate the
	 * worker should stop. The runnable should handle appropriate stopping behaviour.
	 */
	public synchronized void stopWorking() {
		if (isStoppable()) {
			setStatus(Status.STOPPING);
		} else {
			throw new IllegalStateException("Worker already stopped");
		}
	}

	/**
	 * Non-static implementation of Status' method isStartable() using current worker's status.
	 *
	 * @return Whether worker is startable
	 */
	public boolean isStartable() {
		return Status.isStartable(status);
	}

	/**
	 * Non-static implementation of Status' method isStoppable() using current worker's status.
	 *
	 * @return Whether worker is stoppable
	 */
	public boolean isStoppable() {
		return Status.isStoppable(status);
	}

	/**
	 * Method run as part of the workers thread. Functionality of a worker should be defined here.
	 *
	 * @throws InterruptedException An exception indicating that the worker should stop
	 */
	protected abstract void doWork() throws InterruptedException;

	/**
	 * Enumeration of statuses for workers.
	 *
	 * @author David Jones [dsj1n15]
	 */
	public static enum Status {
		WORKING, STOPPING, SUSPENDED, STOPPED;

		/**
		 * @param status Status to check
		 * @return Whether status should allow worker to start
		 */
		public static boolean isStartable(Status status) {
			return (status == Status.STOPPED || status == Status.SUSPENDED);
		}

		/**
		 * @param status Status to check
		 * @return Whether status should allow worker to stop
		 */
		public static boolean isStoppable(Status status) {
			return (status == Status.WORKING);
		}

	}


}
