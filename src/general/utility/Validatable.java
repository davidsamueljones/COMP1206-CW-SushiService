package general.utility;

/**
 * Interface so that classes can be tagged as validatable.
 *
 * @author David Jones [dsj1n15]
 */
public interface Validatable {

	/**
	 * Check if current fields of a class are valid as defined by the class. The purpose of an
	 * implementation should be to notify of any problems using a built up error builder, not
	 * attempting to fix any issues or interrupt the flow of the program.
	 *
	 * @return A new error builder with appropriate validation fields set
	 */
	public abstract ErrorBuilder validate();

}
