package dat255.grupp06.bibbla.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Error;

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
		this.name = name;
		this.code = code;
		this.pin = pin;
		this.userUrl = ""; // Initialisation needed for synchronisation
		
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
		hasCredentials = ((name != null) && (code != null) && (pin != null));
	}
	/**
	 * Returns whether all user details are correctly set.
	 */
	private boolean getHasCredentials() {
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