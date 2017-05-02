package general.model;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import general.utility.SerializationUtils;

/**
 * Comms class to handle interaction between two entities. Method chosen uses sockets.
 * Implementation abstracted to conform with specification so that only receiveMessage and
 * sendMessage are callable (ideal socket behaviour would keep a communication intact until
 * complete). On instantiation of class, a server is hosted on a given port number, failure to host
 * causes an exception, which should be handled externally. The hosted server runs continuously on a
 * thread receiving messages from other comms classes; these messages are stored in a message queue.
 * Receive message works by polling the message queue (it never instigates or handles a connection).
 * Send message instigates a connection with the target comms class server, sending the Message
 * object, which is stored. A default destination can be set on construction; this is provided so
 * comms classes that continually send messages to the same location need not attach the send
 * location for each message (clients). It should be noted that the comms class does not deal with
 * interpretation of messages; this should be handled by an appropriate message handler.
 *
 * @author David Jones [dsj1n15]
 */
public class Comms {
	// Locations
	private final InetSocketAddress source;
	private final InetSocketAddress defaultDestination;
	// Message receiver
	private final MessageReceiver server;

	/**
	 * Instantiate comms with a source address and no default.
	 *
	 * @param source The address on which the server is hosted and messages are tagged with
	 */
	public Comms(InetSocketAddress source) {
		this(source, null);
	}

	/**
	 * Instantiate comms with a source address and a default address.
	 *
	 * @param source The address on which the server is hosted and messages are tagged with
	 * @param defaultDestination The address to which undirected messages are sent
	 */
	public Comms(InetSocketAddress source, InetSocketAddress defaultDestination) {
		// Validate arguments
		if (source == null) {
			throw new IllegalArgumentException("No Client Address - Server not being hosted");
		}
		// Start a socket server listening for messages
		try {
			// Start a listening server
			server = new MessageReceiver(source.getPort());
			server.start();
			// Update port number of source in case of dynamic assignment
			this.source = new InetSocketAddress(source.getAddress(), server.getPort());
		} catch (BindException e) {
			throw new IllegalArgumentException("Port number in use - Server not being hosted");
		}
		// Remember default location
		this.defaultDestination = defaultDestination;
	}

	/**
	 * Send a message object to the default destination.
	 *
	 * @param message Message object initialised with content
	 * @return True if send successful, else false
	 */
	public boolean sendMessage(Message message) {
		return sendMessage(message, defaultDestination);
	}

	/**
	 * Send a message object to a target destination using an object output stream. Data is
	 * transferred using a socket connection. The message sender will be updated with the message
	 * receiver address. The address of the message receiver is defined as the source address.
	 *
	 * @param message Message object initialised with content
	 * @param destination Address of destination socket
	 * @return True if send successful, else false
	 */
	public boolean sendMessage(Message message, InetSocketAddress destination) {
		System.out.println("Sending " + message.getCommand() + " " + message);
		Socket client = null;
		try {
			// Connect to server
			client = new Socket(destination.getAddress(), destination.getPort());
			// Attach message sender to message
			message.setSender(source);
			// Write message to object output stream
			SerializationUtils.serialize(message, client.getOutputStream());
			return true;
		} catch (IOException e) {
			System.out.println("Error " + message.getCommand() + " " + message
					+ " : Could not connect to server");
			return false;
		} finally {
			if (client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// close failed, ignore
				}
			}
		}
	}

	/**
	 * Gets the first unread message from the message receiver. This will count as reading the
	 * message so the next request will receive the next message.
	 *
	 * @return First unread message
	 * @throws InterruptedException A thread interrupt was detected, stop waiting for message
	 */
	public Message receiveMessage() throws InterruptedException {
		if (server == null) {
			return null;
		}
		return server.getMessage();
	}

	/**
	 * A class which hosts a socket server, waiting for Message objects. When a Message is received
	 * it is placed in a thread safe queue ready for handling.
	 *
	 * @author David Jones [dsj1n15]
	 *
	 */
	class MessageReceiver extends Thread {
		// Socket to receive connections
		private ServerSocket serverSocket;
		// Thread safe queue of messages recieved
		private final BlockingQueue<Message> messages = new LinkedBlockingQueue<>();

		/**
		 * Start a server that expects Message objects
		 *
		 * @param port Port number to host server on (0 = dynamic)
		 * @throws BindException Port number already in use, server not instantiated
		 */
		public MessageReceiver(int port) throws BindException {
			try {
				serverSocket = new ServerSocket(port);
			} catch (BindException e) {
				throw e;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (true) {
				Socket server = null;
				try {
					// Wait for connection
					server = serverSocket.accept();
					// Receive message object
					Object object = SerializationUtils.deserialize(server.getInputStream());
					if (object != null) {
						// Verify message object - ignore if not message
						if (object instanceof Message) {
							Message message = (Message) object;
							// Add message to queue (thread-safe)
							messages.add(message);
						} else {
							System.err.println(String.format("[Server] : Expected: %s Got: %s",
									Message.class, object.getClass()));
						}
					} else {
						System.err.println("[Server] : Error receiving ");
					}
				} catch (SocketTimeoutException s) {
					System.err.println("[Server] : Socket timed out!");
					break;
				} catch (IOException e) {
					System.err.println("[Server] : Error getting connection");
					break;
				} finally {
					try {
						server.close();
					} catch (IOException e) {
						// close failed, ignore
					}
				}
			}
		}

		/**
		 * @return The actual port number on which this message reciever is listening
		 */
		public int getPort() {
			return serverSocket.getLocalPort();
		}

		/**
		 * Get the last message stored in the message receiver, removing from the queue. If no
		 * message is in the queue the thread will block until a message is received or the thread
		 * is interrupted.
		 *
		 * @return The last message received
		 * @throws InterruptedException A thread interrupt was detected, stop waiting for message
		 */
		public Message getMessage() throws InterruptedException {
			return messages.take();
		}

	}
}
