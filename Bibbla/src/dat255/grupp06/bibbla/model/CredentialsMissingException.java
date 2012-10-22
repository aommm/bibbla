/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.
    
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

package dat255.grupp06.bibbla.model;

/**
 * Thrown from a method in Backend when user must be logged in and there are no
 * saved credentials to use. Also thrown when login fails.
 * @author arla
 */
public class CredentialsMissingException extends Exception {
	private static final long serialVersionUID = 2572442705876238218L;
	
	public CredentialsMissingException() {
		super();
	}

	public CredentialsMissingException(String string) {
		super(string);
	}

	// TODO Should implement more?
}