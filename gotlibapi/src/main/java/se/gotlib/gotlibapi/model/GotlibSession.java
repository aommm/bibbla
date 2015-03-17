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

package se.gotlib.gotlibapi.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GotlibSession implements Serializable {

	private static final long serialVersionUID = 1290665641023286320L;

	private static final String COOKIE_LOGGED_IN = "PAT_LOGGED_IN";

	private String name;
	private String userUrl; // The URL to the user's profile page.
	private Map<String, String> cookies;

	/**
	 * Creates a new session, using the data from the supplied GotlibCredentials.
	 */
	public GotlibSession(GotlibCredentials gotlibCredentials) {
		this.name = gotlibCredentials.name;
		this.userUrl = "";
		
		cookies = new HashMap<String, String>();
	}
	
	/**
	 * Creates a new anonymous session.
	 */
	public GotlibSession() {
		this.name = "";
		this.userUrl = "";
		
		cookies = new HashMap<String, String>();
	}
	
	/**
	 * Returns the current session's cookies.
	 */
	public Map<String, String> getCookies() {
		synchronized(cookies) {
			return cookies;
		}
	}
	
	/**
	 * Overwrites the current session's cookies.
	 * @param cookies - new cookies to save.
	 */
	public void setCookies(Map<String, String> cookies) {
		synchronized(this.cookies) {
			// Save our new cookies.
			this.cookies = cookies;
		}
	}

	/**
	 * Returns the URL to the user's profile page.
	 * @returns empty string if something went wrong.
	 */
	public String getUserUrl() {
		synchronized(userUrl) {
			// set during login for sure
			return userUrl;
		}
	}
	/**
	 * Sets the URL to the user's profile page.
	 */
	public void setUserUrl(String userUrl) {
		synchronized(this.userUrl) {
			this.userUrl = userUrl;
		}
	}

	public void setName(String name) {
		synchronized(this.name) {
			this.name = name;
		}
	}
	
	public String getName() {
		synchronized(name) {
			return name;
		}
	}

}
