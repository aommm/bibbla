package dat255.grupp06.bibbla.backend.tasks;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import dat255.grupp06.bibbla.utils.Message;
//import java.net.CookieManager;

/**
 * Logs the user into gotlib.
 * 
 * Contains the actual implementation of network/parsing.
 * Is in separate class to simplify testing (no android dependencies).
 * 
 * @author Niklas Logren
 */
public class LoginJob {
	// TODO use this somehow?
    //CookieManager cookieManager;

	public LoginJob() {}
	
	/**
	 * Starts logging in.
	 * @returns the success of the operation.
	 */
	public Message run() {
		
		Message msg = new Message();
		
		try {
			step1();
		} catch (Exception e) {
			System.out.println("*** step1() failed: "+e.getMessage()+"***");
		}
		try {
			step2();
		} catch (Exception e) {
			System.out.println("*** step2() failed: "+e.getMessage()+"***");
		}		
		
		// Will eventually return success/failure.
		msg.loggedIn = true;
		
		return msg;
	}
	
	/**
	 * Step 1: Gets login form.
	 * Saves the initial session cookies, and retrieves three values.  
	 */
	private void step1() throws Exception {

		// Define new GET request.
		URL url = new URL("https://www.gotlib.goteborg.se/iii/cas/login?service=http%3A%2F%2Fencore.gotlib.goteborg.se%3A80%2Fiii%2Fencore%2Fj_acegi_cas_security_check&lang=swe");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// Send request.
		conn.connect();
		
		// Get content.
		String html = null;
		InputStream in = new BufferedInputStream(conn.getInputStream());
		html = readStream(in);

		// Terminate connection. Surely we can do this here?
		conn.disconnect();
		
		/**
		 * TODO: Save cookies.
		 * Save all session cookies, and save the values of 
		 * "org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE"
		 * and "JSESSIONID" manually.
		 * 
		 * How to save session cookies? Look up CookieManager.
		 * How long can it keep up the show? Can it do it across threads?
		 */ 
		
		/**
		 * TODO: Parse HTML.
		 * Save value of <input type="hidden" name="lt" value="xxx" />.
		 */
		
		System.out.println("*** Login form: "+html);
		System.out.println("End login form. ***"); // temporary debug text.
	}
	
	
	/**
	 * Step 2: POSTs login credentials.
	 */
	private void step2() throws Exception {
		// TODO: implement
		throw new UnsupportedOperationException("implement me pls");
	}	
	
	
	/**
	 * Reads the supplied stream until the end.
	 * @returns the String contained in the stream. 
	 */
	private String readStream(InputStream is) {
	    try {
	      ByteArrayOutputStream bo = new ByteArrayOutputStream();
	      int i = is.read();
	      while(i != -1) {
	        bo.write(i);
	        i = is.read();
	      }
	      return bo.toString();
	    } catch (IOException e) {
	      return "";
	    }
	}

	
}
