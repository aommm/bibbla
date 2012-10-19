package dat255.grupp06.bibbla.backend.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.backend.login.Session;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.CommonParsing;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Fetches a list of the user's currently loaned books.
 *
 * @author Niklas Logren
 */
public class UnreserveJob extends AuthorizedJob {
	private Session session;
	private Message message;
	private List<Book> books;
	private Response httpResponse;
	private String userUrl;
	
	/**
	 * Creates a new UnreserveJob,
	 * which will try to unreserve all of the user's current reservations. 
	 */
	public UnreserveJob(boolean loggedIn, Credentials credentials,
			Session session) {
		super(loggedIn, credentials, session);
		this.session = session;
		this.message = new Message();
	}
	
	/**
	 * Creates a new UnreserveJob,
	 * which will try to unreserve all books in the supplied list.
	 * 
	 * Note: Assumes that all books has their unreserveId set!
	 */
	public UnreserveJob(boolean loggedIn, Credentials credentials,
			Session session, List<Book> books) {
		this(loggedIn, credentials, session);
		this.books = books;
	}
	
	/**
	 * Creates a new UnreserveJob,
	 * which will try to unreserve only the supplied book.
	 * 
	 * Note: Assumes that the book has its unreserveId set!
	 */
	public UnreserveJob(boolean loggedIn, Credentials credentials,
			Session session, Book book) {
		this(loggedIn, credentials, session);
		books = new ArrayList<Book>();
		books.add(book);
	}
	
	/**
	 * Starts the unreserve process.
	 * 
	 * @returns a Message, containing a list of Books, some of which may have
	 * 	their message attribute set. 
	 */
	public Message run()  {
		login();
		System.out.println("****** UnreserveJob: ");
		try {
			// Get user URL.
			System.out.println("*** Step 1: get user's url");
			userUrl = session.getUserUrl();
			// Did it fail?
			if ("".equals(userUrl)) {
				message.error = Error.FETCHING_USER_URL_FAILED;
				throw new Exception("Fetching user URL failed.");
			}
			// Append "holds" to user URL.
			userUrl += "holds";
			System.out.println("Step 1 done! ***");
			
			System.out.println("*** Step 2: post our unreservation");
			postUnreservation();
			System.out.println("Step 2 done! ***");
			
			System.out.println("*** Step 3: parse the results");
			parseResults();
			System.out.println("Step 3 done! ***");		
			
		} catch (Exception e) {
			message.error = (message.error!=null) ? message.error : Error.UNRESERVE_FAILED;
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
	}
	
	/**
	 * POSTs the form which unreserves our books.
	 * @throws Exception if connection failed,
	 * 		the user isn't logged in, or if the server complained.  
	 */
	private void postUnreservation() throws Exception {
		
	    // Prepare POST data.
	    @SuppressWarnings("serial")
		Map<String,String> postData = new HashMap<String,String>() {{
	    	
	    	// No specified books? Unreserve everything.
	    	if (books == null) {
	    		put("cancelall", "JA");
	    	}
	    	
	    	// Unreserve only the specified books.
	    	else {
	    		put("updateholdssome", "JA");
		    	// Loop through the supplied books and add to post. 
		    	for (int i=0; i<books.size(); i++) {
					put(books.get(i).getUnreserveId(), "on");
					put(books.get(i).getFreezeId(), "off");
					//put("loci4076577x00", "");
				}
	    	}
	    }};
	    
	    System.out.println(postData);
	    
	    // Send POST request to user url and save response.
	    httpResponse = Jsoup.connect(userUrl)
			    .method(Method.POST)
			    .cookies(session.getCookies())
			    .data(postData)
			    .execute();
	}
	
	/**
	 * Parses the results retrieved by postUnreservation().
	 * 
	 * @throws Exception if we're not logged in, or if parsing otherwise failed.
	 */
	private void parseResults() throws Exception {
		
	    // Prepare parsing.
	    Document html = httpResponse.parse();

	    // Are we still logged in?
	    if (html.select("div.loginPage").size()>0) {
	    	message.error = Error.LOGIN_NEEDED;
	    	message.loggedIn = false;
	    	throw new Exception("Not logged in");
	    } else {
	    	message.loggedIn = true;
	    }
	    
	    // Just checking - font tags implies trouble.
	    if (html.select("font").size()>0) {
	    	// Something went wrong, but not necessarily everything.
	    	message.error = Error.UNRESERVE_FAILED;
	    }
	    
	    // Parse our table rows into a list of Books.
	    Elements rows = html.select("tr.patFuncEntry");
	    List<Book> results = CommonParsing.parseMyReservations(rows);
	    
	    // Return list of books.
	    message.obj = results;
	}
	
}
