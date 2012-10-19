package dat255.grupp06.bibbla.utils;

/**
 * Thrown from a method in Backend when user must be logged in and there are no
 * saved credentials to use. Also thrown when login fails.
 * @author arla
 */
public class CredentialsMissingException extends RuntimeException {
	private static final long serialVersionUID = 2572442705876238218L;
	
	public CredentialsMissingException() {
		super();
	}

	public CredentialsMissingException(String string) {
		super(string);
	}

	// TODO Should implement more?
}
