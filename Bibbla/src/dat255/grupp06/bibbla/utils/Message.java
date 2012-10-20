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

package dat255.grupp06.bibbla.utils;

/**
 * A class used for communication all throughout the project.
 * Can contain an object, an error and a loggedIn boolean.
 * 
 * @author Niklas Logren
 */
public class Message {
	public Object obj;
	public boolean loggedIn;
	public Error error;
	
	@Override
	public String toString() {
		String o = (obj!=null)?obj.toString():"null";
		if (error == null) {
			return "Success! loggedIn: "+loggedIn+", obj: "+o+".";	
		}
		else {
			return "Error: "+error+", loggedIn: "+loggedIn+", obj: "+o+".";
		}
		
	}
}
