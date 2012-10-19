package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dat255.grupp06.bibbla.backend.login.Session;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

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
	Message message;
	
	private final Credentials credentials;
	private Session session;
	
	public LoginJob(Credentials credentials, Session session) {
		this.credentials = credentials;
		message = new Message();
		this.session = session;
		
		// Initialise maps.
		sessionVariables = new HashMap<String, String>();
		sessionCookies = new HashMap<String, String>();
	}
	
	/**
	 * Starts logging in.
	 * @returns the success of the operation.
	 */
	public Message run() {

		try {
			System.out.print("\n****** LoginJob \n");
			System.out.print("* getLoginForm(): ");
			getLoginForm();
			System.out.print("succeeded! *\n");
			System.out.print("* postLoginForm(): ");
			Response response = postLoginForm();
			System.out.print("succeeded! *\n");
			System.out.print("* loginTest(): ");
			String url = parseUserUrl(response);
			session.setUserUrl(url);
			System.out.print("succeeded! *\n");
			System.out.print("****** LoginJob done \n");
			// We made it through.
			message.obj = sessionCookies;
			message.loggedIn = true;
			session.setCookies(sessionCookies);
		}
		catch (Exception e) {
			message.loggedIn = false;
			message.error = (message.error!=null) ? message.error : Error.LOGIN_FAILED;
			System.out.print("failed: "+e.getMessage()+" ***\n");
		}

		return message;
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
		
		// Debugging
		for (Entry<String, String> c : sessionCookies.entrySet()) {
			System.out.print("\n* "+c.getKey() + ": "+c.getValue());
		} System.out.print("\n");
		
	}
	
	
	/**
	 * Step 2: POSTs login credentials.
	 * Saves new session cookies, which will be used in all requests hereafter.
	 */
	private Response postLoginForm() throws Exception {
		
		// Prepare POST url (spoiler: contains variables!)
		// TODO Um... confusing sessionVariables with sessionCookies? But it works?
	    String url = "https://www.gotlib.goteborg.se/iii/cas/login;jsessionid="+sessionVariables.get("JSESSIONID")+"?service=http%3A%2F%2Fencore.gotlib.goteborg.se%3A80%2Fiii%2Fencore%2Fj_acegi_cas_security_check&lang="+sessionVariables.get("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE");
		
	    // Prepare POST data.
	    @SuppressWarnings("serial")
	    Map<String,String> postData = new HashMap<String,String>() {{
	    	put("name", credentials.name);
	    	put("code", credentials.card);
	    	put("pin", credentials.pin);
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
	    
	    // Debugging
		for (Entry<String, String> c : sessionCookies.entrySet()) {
			System.out.print("\n* "+c.getKey() + ": "+c.getValue());
		} System.out.print("\n");
		
		return response;
	}
	
	/**
	 * Parse the user's "profile" URL in Session for later use.
	 * @param response Response from postLoginForm()
	 * @return URL for the user's personal page
	 * @throws IOException if parsing failed or the URL could't be found (for
	 * any reason)
	 */
	private static String parseUserUrl(Response response) throws IOException {
		Document html = response.parse();
		String url = html.select(".myAccountLink").attr("href");
		if (url.equals("")) throw new IOException("no link found");
		if (!url.matches("/$")) url += "/";
		return url;
	}
	
}
