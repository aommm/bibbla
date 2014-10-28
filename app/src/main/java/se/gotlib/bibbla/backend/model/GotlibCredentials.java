/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orr�.
    
    This file is part of Bibbla.

    Bibbla is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bibbla is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bibbla.  If not, see <http://www.gnu.org/licenses/>.    
 **/

package se.gotlib.bibbla.backend.model;

/**
 * Holds a set of credentials used to login with Gotlib. GotlibCredentials are
 * supplied with registration of library cards.
 * @author arla
 */
public final class GotlibCredentials {
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
	 */
	public GotlibCredentials(String name, String card, String pin) {
		this.name = name;
		this.card = card;
		this.pin = pin;
	}

	public static boolean areLegalCredentials(String name, String card,
			String pin) {
		return isLegalName(name) && isLegalCard(card) && isLegalPin(pin);
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

    /**
     * helper method. Checks if string is null or of length 0.
     */
    private static boolean stringEmpty(String string) {
        return (string == null || string.length() == 0);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GotlibCredentials{" +
                "name='" + name + '\'' +
                ", card='" + card + '\'' +
                ", pin='" + pin + '\'' +
                '}';
    }
}
