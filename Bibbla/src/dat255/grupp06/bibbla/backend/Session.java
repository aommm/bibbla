package dat255.grupp06.bibbla.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Error;

public class Session {

	private String name, code, pin;
	private Map<String, String> cookies;
	private boolean loggedIn;
	private boolean hasCredentials;
	private Object lock; 
	
	/**
	 * Creates a new session, and saves the supplied credentials.
	 */
	public Session(String name, String code, String pin) {
		this.name = name;
		this.code = code;
		this.pin = pin;
		
		loggedIn = false;
		hasCredentials = ((name != null) && (code != null) && (pin != null));
		
		cookies = new HashMap<String, String>();
		lock = new Object();
		
	}
	/**
	 * Creates a new anonymous session.
	 */
	public Session() {
		loggedIn = false;
		hasCredentials = false;
		
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
		synchronized(cookies) {
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
	 * Checks whether all user credentials are set, and updates hasCredentials.
	 */
	private void updateHasCredentials() {
		hasCredentials = ((name != null) && (code != null) && (pin != null));
	}
	/**
	 * Returns whether all user details are correctly set.
	 */
	private boolean getHasCredentials() {
		return hasCredentials;
	}
	
	public String getName() {
		synchronized(name) {
			return name;
		}
	}
	public void setName(String name) {
		synchronized(name) {
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
		synchronized(code) {
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
		synchronized(pin) {
			this.pin = pin;
			updateHasCredentials();
		}
	}

}