package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dat255.grupp06.bibbla.backend.Session;
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

	// Global throughout the whole program. In Session-class later on.
	Map<String, String> sessionCookies;
	// Needed only in LoginJob.
	Map<String, String> sessionVariables;
	Session session;
	
	String name, code, pin;
	
	public LoginJob(Session session) {
		this.session = session;
		name = session.getName();
		code = session.getCode();
		pin = session.getPin();
		
		// Initialise maps.
		sessionVariables = new HashMap<String, String>();
		sessionCookies = new HashMap<String, String>();
	}
	
	/**
	 * Starts logging in.
	 * @returns the success of the operation.
	 */
	public Message run() {
		
		// Create a response object.
		Message msg = new Message();
		
		try {
			System.out.print("\n****** LoginJob \n");
			System.out.print("* getLoginForm(): ");
			getLoginForm();
			System.out.print("succeeded! *\n");
			System.out.print("* postLoginForm(): ");
			postLoginForm();
			System.out.print("succeeded! *\n");
			System.out.print("* loginTest(): ");
			loginTest("hej");
			System.out.print("succeeded! *\n");
			System.out.print("****** LoginJob done \n");
			// We made it through.
			session.setCookies(sessionCookies);
			msg.loggedIn = true;
		}
		catch (Exception e) {
			System.out.print("failed: "+e.getMessage()+" ***\n");
		}

		return msg;
	}
	
	/**
	 * Step 1: Gets login form.
	 * Saves the initial session cookies, and retrieves three values.  
	 */
	private void getLoginForm() throws Exception {

		String url = "https://www.gotlib.goteborg.se/iii/cas/login?service=http%3A%2F%2Fencore.gotlib.goteborg.se%3A80%2Fiii%2Fencore%2Fj_acegi_cas_security_check&lang=swe";

		// Create a request, and retrieve the response.
		Response response = Jsoup.connect(url)
			    .method(Method.GET)
			    .execute();

		// Get the cookies from the response.
		sessionCookies = response.cookies();
		
		// Prepare for parsing.
		Document html = response.parse();

		// Get the value of <input type="hidden" name="lt" value="" />.
		String lt = html.select("input[name=lt]").first().val();
		sessionVariables.put("lt", lt);
		
		// Tests.
		if ((sessionVariables.get("lt") == null) ||
		   (sessionCookies.get("JSESSIONID") == null) ||
		   (sessionCookies.get("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE") == null)) {
			throw new Exception("missing cookies/variables.");
		}
	}
	
	
	/**
	 * Step 2: POSTs login credentials.
	 * Saves new session cookies, which will be used in all requests hereafter.
	 */
	private void postLoginForm() throws Exception {
		
		// Prepare POST url (spoiler: contains variables!)
	    String url = "https://www.gotlib.goteborg.se/iii/cas/login;jsessionid="+sessionVariables.get("JSESSIONID")+"?service=http%3A%2F%2Fencore.gotlib.goteborg.se%3A80%2Fiii%2Fencore%2Fj_acegi_cas_security_check&lang="+sessionVariables.get("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE");
		
	    // Prepare POST data.
	    Map<String,String> postData = new HashMap<String,String>() {{
	    	put("name", name);
	    	put("code", code);
	    	put("pin", pin);
	    	put("lt", sessionVariables.get("lt"));
	    	put("_eventId", "submit");
	    }};
	    
	    // Send POST request and save response.
	    Response response = Jsoup.connect(url)
			    .method(Method.POST)
			    .data(postData)
			    .cookies(sessionCookies)
			    .execute();

	    // These new cookies are all we'll need. 
	    sessionCookies = response.cookies();
	}
	
	/**
	 * Performs a test search, to see if login was successful.
	 */
	private void loginTest(String searchString) throws Exception {
		
	    String url = "http://encore.gotlib.goteborg.se/iii/encore/search/C__S"+searchString+"__Orightresult__U1?lang=swe&suite=pearl";
	    
	    // Send a GET request and save response.
	    Response response = Jsoup.connect(url)
	    		.method(Method.GET)
	    		.cookies(sessionCookies)
	    		.execute();
	    
	    // Prepare for parsing.
	    Document html = response.parse();
	    
	    // Is login link present?
	    if (html.select("a[id=loginLinkComponent]").size() > 0) {
	    	// Yep. Login failed.
	    	System.out.println(html.select("a[id=loginLinkComponent]").first().text());
	    	throw new Exception("Login test failed.");
	    }

	    // We made it here without exceptions? Yay!
	}
	
}
