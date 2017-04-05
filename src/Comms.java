import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class Comms {
	private InetSocketAddress source; 
	private InetSocketAddress defaultDestination;

	private MessageReceiver server;

	public Comms(InetSocketAddress source) {
		this(source, null);
	}
	
	public Comms(InetSocketAddress source, InetSocketAddress defaultDestination) {
		// Remember setup
		this.source = source;
		this.defaultDestination = defaultDestination;
		// Start server
		startClientServer();
	}

	private void startClientServer() {
		if (source == null) {
			server = null;
			System.err.println("No Client Address - Server not being hosted"); 
		}
		else {
			server = new MessageReceiver(source.getPort());
			(new Thread(server)).start();			
		}
	}

	private void stopClientServer() {
		if (server == null) {
			System.err.println("Server not hosted - Server cannot be closed"); 
		}
		else {
			server.stopServer();
		}
	}

	public void sendMessage(Message message) {
		sendMessage(message, defaultDestination);
	}
	
	public void sendMessage(Message message, InetSocketAddress destination) {
		try {
			// Connect to server
			Socket client = new Socket(destination.getAddress(), destination.getPort());
			// Attach message sender to message
			message.setSender(source);
			// Write message to object output stream
			OutputStream outToServer = client.getOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(outToServer);
			oos.writeObject(message);
			// Close object writer
			oos.flush();
			oos.close();
			// Close connection to client
			client.close();
		} catch (IOException e) {
			System.err.println("[Client] : Couldn't connect to server");
		}
	}

	public Message receiveMessage() {
		return server.getMessage();
	}

	/**
	 * !!! Listening class 
	 * @author David
	 *
	 */
	class MessageReceiver implements Runnable {
		private ServerSocket serverSocket;
		private BlockingQueue<Message> messages = new LinkedBlockingQueue<Message>();
		private Boolean serverOn;

		public MessageReceiver(int port) {
			try {
				serverOn = true;
				serverSocket = new ServerSocket(port);
				//serverSocket.setSoTimeout(20000);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			while (isServerOn()) {
				try {
					// Wait for connection
					Socket server = serverSocket.accept();
					// Receive message object
					ObjectInputStream ois = new ObjectInputStream(server.getInputStream());
					Object object = ois.readObject();
					if (object instanceof Message) {
						Message message = (Message) object;
						// Add message to queue (thread-safe)
						messages.add(message);
					}
					// Close connection to server
					ois.close();
					server.close();
				}
				catch (ClassNotFoundException e) {
					System.err.println("[Server] : Object not recieved");
				}
				catch (SocketTimeoutException s) {
					System.err.println("[Server] : Socket timed out!");
					break;
				}
				catch(IOException e) {
					System.err.println("[Server] : IO Exception");
					break;
				} 
			}
		}
		
		public Message getMessage() {
			try {
				return messages.take();
			} catch (InterruptedException e) {
				return null;
			}
		}

		public boolean isServerOn() {
			synchronized (serverOn) {
				return serverOn;
			}
		}

		public void stopServer() {
			synchronized (serverOn) {
				serverOn = false;
			}
		}

		
	}
}
