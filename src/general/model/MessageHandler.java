package general.model;

import java.util.Collection;
import java.util.HashSet;

/**
 * Abstract class to handle the control flow of message handling for messages received by a single
 * instance of Comms. Implementations of MessageHandler should dictates how a received message is
 * handled.
 *
 * @author David Jones [dsj1n15]
 */
public abstract class MessageHandler implements Runnable {
	// Comms being handled
	protected final Comms comms;
	private final Collection<Thread> threads = new HashSet<>();

	/**
	 * Instantiate general message handler.
	 *
	 * @param comms Comms instance to handle
	 */
	public MessageHandler(Comms comms) {
		this.comms = comms;
	}

	@Override
	public void run() {
		// Handle all messages in message queue
		while (comms != null) {
			Message rx;
			try {
				rx = comms.receiveMessage();
			} catch (final InterruptedException e) {
				// Exit loop, stopping message handler
				break;
			}
			// Print acknowledgement
			System.out.println("[Message Handler] : Received " + rx.getCommand() + " " + rx);
			// Handle message in new thread
			final Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					handleMessage(rx);
					try {
						Thread.sleep(10000);
					} catch (final InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// Remove thread from ongoing threads once handled
					threads.remove(this);
				}
			});
			// Keep track of thread
			threads.add(thread);
			thread.start();
		}
		// Interrupt message handling threads in case of long behaviour
		for (final Thread thread : threads) {
			thread.interrupt();
		}
		// Wait for message handling threads to complete before thread finishes
		for (final Thread thread : threads) {
			try {
				thread.join();
			} catch (final InterruptedException e) {
				System.err
						.println("[Message Handler] : Unable to wait for message handling threads");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Send a new message to the return address of another message.
	 *
	 * @param rx Message that is being replied to
	 * @param tx New message to transmit
	 */
	protected void reply(Message rx, Message tx) {
		comms.sendMessage(tx, rx.getSender());
	}

	/**
	 * Process message, handling data fields accordingly. Message handling should look for expected
	 * data as opposed to interpreting given data for safety. Any unexpected message types should be
	 * discarded. Message handling behaviour should be lightweight and should finish fast if thread
	 * interrupted.
	 *
	 * @param message Message to handle
	 */
	public abstract void handleMessage(Message message);

}
