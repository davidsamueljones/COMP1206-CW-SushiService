package general.model;

import java.io.Serializable;
import java.net.InetSocketAddress;

import general.utility.ErrorBuilder;

/**
 * Message class, holds data to be sent between message handlers.
 *
 * @author David Jones [dsj1n15]
 */
public class Message implements Serializable {
	private static final long serialVersionUID = -7961308398237966546L;
	// A command dictating how the message should be handled
	private final Command command;
	// An error builder; this is always available alongside object storage
	private final ErrorBuilder eb;
	// An object or collection of objects for large data transfer
	private final Object object;
	// The sender of the message, this should be the server for replies
	private InetSocketAddress sender;

	/**
	 * Initialise a message with only a command.
	 *
	 * @param command Command dictating how message should be handled
	 */
	public Message(Command command) {
		this(command, null);
	}

	/**
	 * Initialise a message with a command and comments held by an error builder.
	 *
	 * @param command Command dictating how message should be handled
	 * @param eb Comments in error builder to send
	 */
	public Message(Command command, ErrorBuilder eb) {
		this(command, eb, null);
	}

	/**
	 * Initialise a message with all final fields.
	 *
	 * @param command Command dictating how message should be handled
	 * @param eb Comments in error builder to send
	 * @param object Object or collection of objects to send
	 */
	public Message(Command command, ErrorBuilder eb, Object object) {
		this.command = command;
		this.eb = eb;
		this.object = object;
	}

	/**
	 * @return How this message should be handled
	 */
	public Command getCommand() {
		return command;
	}

	/**
	 * @return Attached comments stored in error builder
	 */
	public ErrorBuilder getErrorBuilder() {
		return eb;
	}

	/**
	 * @return Attached object
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * Get attached object as a given type using a safe cast. This method only has defined behaviour
	 * for single object instances of a single type.
	 *
	 * @param type Type to cast to
	 * @return Attached object casted to type
	 */
	public <T> T getObjectAs(Class<T> type) {
		if (type.isInstance(object)) {
			return type.cast(object);
		} else {
			return null;
		}
	}

	/**
	 * @return The sender of the message
	 */
	public InetSocketAddress getSender() {
		return sender;
	}

	/**
	 * @param sender The sender of the message
	 */
	public void setSender(InetSocketAddress sender) {
		this.sender = sender;
	}

	/**
	 * An enumeration of hard-coded command types accepted in messages.
	 *
	 * @author David Jones [dsj1n15]
	 */
	public static enum Command {
		// Client
		SUBMIT_LOGIN, REGISTER_NEW_CUSTOMER, GET_POSTCODES, GET_EXISTING_ORDERS, GET_DISH_STOCK, SUBMIT_ORDER,
		// Business
		APPROVE_LOGIN, REJECT_LOGIN, UPDATE_POSTCODES, UPDATE_EXISTING_ORDERS, UPDATE_DISH_STOCK, ORDER_ACCEPTED, ORDER_REJECTED;
	}

}
