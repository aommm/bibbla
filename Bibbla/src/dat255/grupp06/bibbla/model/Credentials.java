package dat255.grupp06.bibbla.model;

import java.io.Serializable;


/**
 * Holds a set of credentials used to login with Gotlib. Credentials are
 * supplied with registration of library cards.
 * @author arla
 */
public final class Credentials implements Serializable {
	private static final long serialVersionUID = -5535338463939136870L;
	/** Card number regexp: 9 numbers and possibly a letter; spaces ignored. */
	public static final String CARD_REGEXP = "^\\s*([0-9]\\s*){9,10}[A-Za-z]?\\s*$";
	/** Pin regexp: 4 numbers, trailing spaces ignored */
	public static final String PIN_REGEXP = "^[0-9]{4}\\s*$";

	/** Name of user as registered with library card. */
	public final String name;
	/** Card number as printed on library card. */
	public final String card;
	/** PIN code assigned to library account. */
	public final String pin;
	
	/**
	 * Creates a set of credentials.
	 * @param name Name of card holder
	 * @param card Card number
	 * @param pin PIN code
	 * @throws CredentialsMissingException if the credentials are empty or
	 * don't follow the expected pattern.
	 */
	public Credentials(String name, String card, String pin)
	throws CredentialsMissingException {
		if (stringEmpty(name) || stringEmpty(card) || stringEmpty(pin)) {
			throw new CredentialsMissingException("Credentials are empty: "+
					"('"+name+"','"+card+"','"+pin+"')");
		}
		if (!isLegalName(name) || !isLegalCard(card) || !isLegalPin(pin)) {
			throw new CredentialsMissingException("Credentials don't follow "+
					"the required format.");
		}

		this.name = name;
		this.card = card;
		this.pin = pin;
	}
	
	private static boolean stringEmpty(String string) {
		return (string == null || string.length() == 0);
	}

	/**
	 * Check if a name is not empty. 
	 * @param name Name of user.
	 * @return whether the name is longer than 0 chars.
	 */
	public static boolean isLegalName(String name) {
		return (!stringEmpty(name));
	}
	
	/**
	 * Check if a card number follows the expected pattern.
	 * @param card Card number, should have 9 digits and possibly a letter.
	 */
	public static boolean isLegalCard(String card) {
		return (card != null && card.matches(CARD_REGEXP));
	}
	
	/**
	 * Check if a PIN code follows the expected pattern.
	 * @param pin PIN code, should have four digits.
	 */
	public static boolean isLegalPin(String pin) {
		return (pin != null && pin.matches(PIN_REGEXP));
	}

	// TODO Implement clone, equals, etc.
}
