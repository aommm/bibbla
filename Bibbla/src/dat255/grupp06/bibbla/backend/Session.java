package dat255.grupp06.bibbla.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.utils.Message;

public class Session {

	private String name, code, pin;
	private Map<String, String> cookies;
	private boolean loggedIn;
	private Object lock; 
	
	public Session(String name, String code, String pin) {
		this.name = name;
		this.code = code;
		this.pin = pin;
		
		cookies = new HashMap<String, String>();
		loggedIn = false;
		lock = new Object();
		
	}
	
	public Map<String, String> getCookies() {
		synchronized(cookies) {
			return cookies;
		}
	}
	
	private void setCookies(Map<String, String> cookies) {
		synchronized(cookies) {
			// Save our new cookies.
			this.cookies = cookies;
		}
	}	
	
	public boolean checkLogin() {
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

	public String getName() {
		synchronized(name) {
			return name;
		}
	}
	public void setName(String name) {
		synchronized(name) {
			this.name = name;
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
		}
	}

}