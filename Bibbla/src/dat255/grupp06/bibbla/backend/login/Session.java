
package dat255.grupp06.bibbla.backend.login;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

public class Session implements Serializable {

	private static final long serialVersionUID = 1290665641023286320L;
	private String name, code, pin;
	private String userUrl; // The URL to the user's profile page.
	private Map<String, String> cookies;
	private boolean loggedIn;
	private boolean hasCredentials;
	
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
	 * @deprecated
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
	 * Simply return whether logged in.
	 * @return true if logged in, false if not (e.g. session expired)
	 */
	// TODO This should actually check whether cookie has expired.
	public boolean isActive() {
		return loggedIn;
	}
	
	/**
	 * Starts login, and waits for it to finish.
	 * Updates our member variables accordingly.
 	 * @returns the success of the login.
 	 * @deprecated
	 */
	public Message login() {
		Message message = new Message();

		// Do we lack the user's credentials?
		if (!hasCredentials) {
			message.error = Error.MISSING_CREDENTIALS;
			return message;
		}
		
		synchronized(this) {
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
	 * Temporary debug method to examine cookies contents
	 * @param <E>
	 * @param <F>
	 */
	public static <E, F> void debugMapContents(Map<E,F> map) {
		StringBuffer buffer = new StringBuffer();
		for (Map.Entry<E,F> entry: map.entrySet()) {
			buffer.append(entry.getKey().toString()).append(" = ")
			.append(entry.getValue()).append('\n');
		}
		System.out.println(buffer.toString());
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