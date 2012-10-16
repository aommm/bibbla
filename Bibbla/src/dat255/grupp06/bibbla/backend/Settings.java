/**
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

package dat255.grupp06.bibbla.backend;

/** A class for storing the user's settings.
 *
 *  TODO: Save to file system.
 */
public class Settings {

	private String name, code, pin;

	public Settings(String name, String code, String pin) {
		this.name = name;
		this.code = code;
		this.pin = pin;
	}
	
	/********************************
	 * Getters/setters
	 ********************************/
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		// save to file system.
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		// save to file system.		
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
		// save to file system.		
	}
	
}
