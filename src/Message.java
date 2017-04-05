import java.io.Serializable;
import java.net.InetSocketAddress;

public class Message implements Serializable {
	private static final long serialVersionUID = -7961308398237966546L;

	final private Command command;
	final private String comment;
	final private Object object;
	private InetSocketAddress sender;

	public Message(Command command) {
		this(command, null);
	}
	
	public Message(Command command, String comment) {
		this(command, comment, null);
	}
	
	public Message(Command command, String comment, Object object) {
		this.command = command;
		this.comment = comment;
		this.object = object;
	}

	public Command getCommand() {
		return command;
	}

	public String getComment() {
		return comment;
	}
	
	public Object getObject() {
		return object;
	}
	
	public <T> T getObjectAs(Class<T> type){
		if (type.isInstance(object)){
			return type.cast(object);
		} else {
			return null;
		}
	}

	public InetSocketAddress getSender() {
		return sender;
	}

	public void setSender(InetSocketAddress sender) {
		this.sender = sender;
	}

	public static enum Command {
		// Client
		SUBMIT_LOGIN,
		REGISTER_NEW_CUSTOMER,
		GET_EXISTING_ORDERS,
		GET_DISHES,
		SUBMIT_ORDER,	
		// Business
		APPROVE_LOGIN,
		REJECT_LOGIN,
		UPDATE_EXISTING_ORDERS,
		UPDATE_DISHES,
		ORDER_ACCEPTED	
	}
}


