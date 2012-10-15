package dat255.grupp06.bibbla.backend.tasks;

import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.CommonParsing;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Fetches a list of the user's current reservations.
 *
 * @author Niklas Logren
 */
public class MyReservationsJob {
	private Session session;
	private Message message;
	
	private Response httpResponse;
	private String userUrl;
	
	public MyReservationsJob(Session session) {
		this.session = session;
		this.message = new Message();
	}
	
	/**
	 * Fetches the user's currentl reservations.
	 * @returns a Message, containing a List of the user's current reservations. 
	 */
	public Message run()  {
		System.out.println("****** MyReservationsJob: ");
		try {
			// Get user URL.
			System.out.println("*** Step 1: get user's url");
			userUrl = session.getUserUrl();
			// Did it fail?
			if ("".equals(userUrl)) {
				message.error = Error.FETCHING_USER_URL_FAILED;
				throw new Exception("Fetching user URL failed.");
			}
			// Append "items" to user URL.
			userUrl += "holds";
			System.out.println("Step 1 done! ***");
			
			System.out.println("*** Step 2: fetch reservations");
			fetchReservations();
			System.out.println("Step 2 done! ***");
			
			System.out.println("*** Step 3: parse reservations");
			parseLoanedBooks();
			System.out.println("Step 3 done! ***");
			
		} catch (Exception e) {
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
	}
	
	/**
	 * Connects to gotlib, and downloads the HTML of the reservations page.
	 * 
	 * @throws Exception - If http connection fails.
	 */
	private void fetchReservations() throws Exception {

	    // Send GET request and save response.
	    httpResponse = Jsoup.connect(userUrl)
			    .method(Method.GET)
			    .cookies(session.getCookies())
			    .execute();   
	}
	
	/**
	 * Parses the results saved by fetchReservations().
	 * 
	 * @throws Exception - If we're not logged in, or if parsing otherwise failed. 
	 */
	private void parseLoanedBooks() throws Exception {
	    // Prepare parsing.
	    Document html = httpResponse.parse();

	    // Are we still logged in?
	    if (html.select("div.loginPage").size()>0) {
	    	message.error = Error.LOGIN_NEEDED;
	    	message.loggedIn = false;
	    	throw new Exception("Not logged in");
	    }
	    
	    // Parse our table rows into a list of Books.
	    Elements rows = html.select("tr.patFuncEntry");
	    List<Book> results = CommonParsing.parseMyReservations(rows);
	    
	    // Return list of loaned books.
	    message.obj = results;
	}
	
}
