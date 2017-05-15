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
	protected final Command command;
	// Storage for comments and error indication
	protected final ErrorBuilder eb;
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
		this.command = command;
		this.eb = eb;
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

	@Override
	public String toString() {
		return String.format("[Message %s@%s]", command, hashCode());
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
		LOGIN_RESPONSE, REGISTER_RESPONSE, UPDATE_POSTCODES, UPDATE_EXISTING_ORDERS, UPDATE_DISH_STOCK, ORDER_RESPONSE, MODIFY_CUSTOMER;
	}

}
