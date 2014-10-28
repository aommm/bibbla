package se.gotlib.bibbla.backend.tasks;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import se.gotlib.bibbla.backend.model.GotlibCredentials;
import se.gotlib.bibbla.backend.model.GotlibSession;
import se.gotlib.bibbla.util.Constants;

/**
 * Logs the user into gotlib.
 * 
 * Contains the actual implementation of network/parsing.
 * Is in separate class to simplify testing (no android dependencies).
 * 
 * @author Niklas Logren
 */
public class LoginJob {

	// Global throughout the whole program. In GotlibSession-class later on.
	Map<String, String> sessionCookies;
	// Needed only in LoginJob.
	Map<String, String> sessionVariables;
    // User name, card and pin
    GotlibCredentials gotlibCredentials;
    // Session
    GotlibSession gotlibSession;

	public LoginJob(GotlibCredentials gotlibCredentials) {
        this.gotlibCredentials = gotlibCredentials;
		// Initialise maps.
		sessionVariables = new HashMap<String, String>();
		sessionCookies = new HashMap<String, String>();
	}
	
	/**
	 * Starts logging in.
	 * @return the success of the operation.
	 */
	public GotlibSession login() {

		// Retry login job a specified number of times. 
		int failureCounter = 0;
		while(failureCounter < Constants.MAX_CONNECTION_ATTEMPTS) {
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
                gotlibSession.setUserUrl(url);
				String name = parseUserName(response);
                gotlibSession.setUserName(name);
				System.out.print("succeeded! *\n");
				System.out.print("****** LoginJob done \n");
				// We made it through.
                gotlibSession.setCookies(sessionCookies);
				break; // Break if we succeed.
			} catch (Exception e) {
				failureCounter++;
				System.out.print("failed. retrying... ");
			}
		}
		// Did we fail?
		if (failureCounter >= Constants.MAX_CONNECTION_ATTEMPTS) {
			System.out.print("\n****** LoginJob failed.\n");
            return null;
		}
		else { // Nope!
			System.out.print("\n****** LoginJob done.\n");
		}

		return gotlibSession;
	}

	/**
	 * Step 1: Gets login form.
	 * Saves the initial session cookies, and retrieves three values.  
	 */
	public void getLoginForm() throws Exception {

		String url = "https://www.gotlib.goteborg.se/iii/cas/login?service=" +
				"http%3A%2F%2Fencore.gotlib.goteborg.se%3A80%2Fiii%2Fencore" +
				"%2Fj_acegi_cas_security_check&lang=swe";

        // Create a request, and retrieve the response.
        Response response = Jsoup.connect(url)
                .method(Method.GET)
                .timeout(Constants.CONNECTION_TIMEOUT)
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
                (sessionCookies.get("org.springframework.web.servlet." +
                        "i18n.CookieLocaleResolver.LOCALE") == null)) {
            throw new Exception("cookies/variables missing");
        }
	}
	
	/**
	 * Step 2: POSTs login gotlibCredentials.
	 * Saves new session cookies, which will be used in all requests hereafter.
	 */
	private Response postLoginForm() throws Exception {
		
		// Prepare POST url (spoiler: contains variables!)
	    String url = "https://www.gotlib.goteborg.se/iii/cas/login;jsessionid="
		+sessionVariables.get("JSESSIONID")+"?service=http%3A%2F%2Fencore.got" +
				"lib.goteborg.se%3A80%2Fiii%2Fencore%2Fj_acegi_cas_security_c" +
				"heck&lang="+sessionVariables.get("org.springframework.web.se" +
						"rvlet.i18n.CookieLocaleResolver.LOCALE");
		
	    // Prepare POST data.
	    @SuppressWarnings("serial")
	    Map<String,String> postData = new HashMap<String,String>() {{
	    	put("name", gotlibCredentials.name);
	    	put("code", gotlibCredentials.card);
	    	put("pin", gotlibCredentials.pin);
	    	put("lt", sessionVariables.get("lt"));
	    	put("_eventId", "submit");
	    }};
	    
	    // Send POST request and save response.
	    Response response = Jsoup.connect(url)
			    .method(Method.POST)
			    .timeout(Constants.CONNECTION_TIMEOUT)
			    .data(postData)
			    .cookies(sessionCookies)
			    .execute();

	    // These new cookies are all we'll need. 
	    sessionCookies = response.cookies();
	    
		// Tests.
		if ((sessionVariables.get("lt") == null) ||
		   (sessionCookies.get("JSESSIONID") == null) ||
		   (sessionCookies.get("III_SESSION_ID") == null)) {
			throw new Exception("missing cookies/variables.");
		}
		return response;
	}
	
	/**
	 * Parse the user's "profile" URL in GotlibSession for later use.
	 * @param response Response from postLoginForm()
	 * @return URL for the user's personal page
	 * @throws java.io.IOException if parsing failed or the URL could't be found (for
	 * any reason)
	 */
	private static String parseUserUrl(Response response) throws IOException {
		Document html = response.parse();
		String url = html.select(".myAccountLink").attr("href");
		if (url.equals("")) throw new IOException("no link found");
		// Replace http://...:80/... by https://.../...
		return "https://www.gotlib.goteborg.se" +
				url.split(":80")[1] + "/";
	}
	
	/**
	 * 
	 * @throws java.io.IOException if parsing went wrong
	 */
	private String parseUserName(Response response) throws IOException {
		Document html = response.parse();
		// Find name on form "Family-name, First-name"
		try {
			String accountInfo = html.select(".myAccountInfo").first().text();
			String[] splitGreeting = accountInfo.split(" \\|")[0].split(", ");
            return splitGreeting[2]+" "+splitGreeting[1];
		} catch (NullPointerException e) {
			return "";
		}
	}
	
}
