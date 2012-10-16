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

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

public class Session {

	private String name, code, pin;
	private String userUrl; // The URL to the user's profile page.
	private Map<String, String> cookies;
	private boolean loggedIn;
	private boolean hasCredentials;
	private Object lock; 
	
	/**
	 * Creates a new session, and saves the supplied credentials.
	 */
	public Session(String name, String code, String pin) {
		// Set user details. If null, initalise to empty string. (for synch)
		this.name = (name == null)?"":name;
		this.code = (code == null)?"":code;
		this.pin = (pin == null)?"":pin;
		this.userUrl = "";
		
		loggedIn = false;
		updateHasCredentials();
		
		cookies = new HashMap<String, String>();
		lock = new Object();
	}
	/**
	 * Creates a new anonymous session.
	 */
	public Session() {
		loggedIn = false;
		hasCredentials = false;
		this.name = "";
		this.code = "";
		this.pin = "";
		
		cookies = new HashMap<String, String>();
		lock = new Object();
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
	private void setCookies(Map<String, String> cookies) {
		synchronized(this.cookies) {
			// Save our new cookies.
			this.cookies = cookies;
		}
	}	
	
	/**
	 * Checks if the user is logged in.
	 * If not, tries to login. If that also fails, returns false.
	 * @returns our logged in state.
	 */
	public boolean checkLogin() {
		// Do we lack the user's credentials?
		if (!hasCredentials) {
			return false;
		}
		// Not logged in?
		if (!loggedIn) {
			// Try again. 
			if (!login().loggedIn) {
				// Attempt #2 failed. Die.
				return false; 
			}
		}
		// We were logged in.
		return true;
	}
	
	/**
	 * Starts login, and waits for it to finish.
	 * Updates our member variables accordingly.
 	 * @returns the success of the login.
	 */
	public Message login() {
		Message message = new Message();

		// Do we lack the user's credentials?
		if (!hasCredentials) {
			message.error = Error.MISSING_CREDENTIALS;
			return message;
		}
		
		synchronized(lock) {
			// Create a new login job, and run it.
			LoginJob job = new LoginJob(this);
			message = job.run();
			// Did job succeed?
			loggedIn = message.loggedIn;
			if (loggedIn) {
				setCookies((Map<String, String>) message.obj);
			}
		}
		
		return message;
	}
	
	/**
	 * Tries to fetch the user's profile URL.
	 * Does this by visiting a bogus URL, and following the redirect.
	 * 
	 * @throws Exception - If redirection doesn't take place.
	 */
	private String fetchUserUrl() throws Exception {
		
		// We have need to be logged in for "user url" to make sense.
		if (!checkLogin()) {
			return "";
		}
		
		// Prepare url. (This URL will 302 redirect us)
		String url = "https://www.gotlib.goteborg.se/patroninfo~S6*swe/1/";
	    Response response = Jsoup.connect(url)
	    		.method(Method.GET)
	    		.followRedirects(false)
	    		.cookies(getCookies())
	    		.execute();
	    
	    // Were we not redirected?
	    if (response.statusCode() != 302) {
	    	return "";
	    }
	    
	    // Are we not logged in?
	    if (response.parse().select("div.loginPage").size()>0) {
	    	return "";
	    }
	    
	    // This is the URL which is unique to the user.
	    return "https://www.gotlib.goteborg.se" + response.header("Location");
	}

	/**
	 * Checks whether all user credentials are set, and updates hasCredentials.
	 */
	private void updateHasCredentials() {
		hasCredentials = ((!"".equals(name)) && (!"".equals(code)) && (!"".equals(pin)));
	}
	/**
	 * Returns whether all user details are correctly set.
	 */
	public boolean getHasCredentials() {
		return hasCredentials;
	}
	
	/**
	 * Returns the URL to the user's profile page.
	 * @returns empty string if something went wrong.
	 */
	public String getUserUrl() {
		synchronized(userUrl) {
			// If URL is empty, try to fetch a new one.
			if ("".equals(userUrl)) {
				try {
					userUrl = fetchUserUrl();
				} catch (Exception e) {}
			}
			// Return the URL, empty or not.
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
	
	public String getName() {
		synchronized(name) {
			return name;
		}
	}
	public void setName(String name) {
		synchronized(this.name) {
			this.name = name;
			updateHasCredentials();
		}
	}

	public String getCode() {
		synchronized(code) {
			return code;
		}
	}
	public void setCode(String code) {
		synchronized(this.code) {
			this.code = code;
			updateHasCredentials();
		}
	}

	public String getPin() {
		synchronized(pin) {
			return pin;
		}
	}
	public void setPin(String pin) {
		synchronized(this.pin) {
			this.pin = pin;
			updateHasCredentials();
		}
	}

}