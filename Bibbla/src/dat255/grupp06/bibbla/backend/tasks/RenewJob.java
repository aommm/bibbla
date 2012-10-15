package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.*;

import org.jsoup.Connection.*;
import org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.*;
import dat255.grupp06.bibbla.utils.Error;

/**
 * Fetches a list of the user's currently loaned books.
 *
 * @author Niklas Logren
 */
public class RenewJob {
	private Session session;
	private Message message;
	private List<Book> books;
	private Response httpResponse;
	private String userUrl;
	
	/**
	 * Creates a new RenewJob,
	 * which will try to renew all of the user's currently loaned books. 
	 */
	public RenewJob(Session session) {
		this.session = session;
		this.message = new Message();
	}
	
	/**
	 * Creates a new RenewJob,
	 * which will try to renew all books in the supplied list.
	 * 
	 * Note: Assumes that all books has their renewId set!
	 */
	public RenewJob(Session session, List<Book> books) {
		this(session);
		this.books = books;
	}
	
	/**
	 * Creates a new RenewJob,
	 * which will try to renew only the supplied book.
	 * 
	 * Note: Assumes that the book has its renewId set!
	 */
	public RenewJob(Session session, Book book) {
		this(session);
		books = new ArrayList<Book>();
		books.add(book);
	}
	
	/**
	 * Starts the renewal process.
	 * 
	 * @returns a Message, containing a list of Books, some of which may have
	 * 	their message attribute set. 
	 */
	public Message run()  {
		System.out.println("****** RenewJob: ");
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
			userUrl += "items";
			System.out.println("Step 1 done! ***");
			
			System.out.println("*** Step 2: post our renewal");
			postRenewal();
			System.out.println("Step 2 done! ***");
			
			System.out.println("*** Step 3: parse the results");
			parseResults();
			System.out.println("Step 3 done! ***");		
			
		} catch (Exception e) {
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
	}
	
	/**
	 * POSTs the form which renews our books.
	 * @throws Exception if connection failed,
	 * 		the user isn't logged in, or if the server complained.  
	 */
	private void postRenewal() throws Exception {
		
	    // Prepare POST data.
	    Map<String,String> postData = new HashMap<String,String>() {{
	    	
	    	// No specified books? Renew everything.
	    	if (books == null) {
	    		put("renewall", "JA");
	    	}
	    	
	    	// Renew only the specified books.
	    	else {
	    		put("renewsome", "JA");
		    	// Loop through the supplied books and add to post. 
		    	for (int i=0; i<books.size(); i++) {
					put("renew"+i, books.get(i).getRenewId());
				}
	    	}
	    }};
	    
	    // Send POST request to user url and save response.
	    httpResponse = Jsoup.connect(userUrl)
			    .method(Method.POST)
			    .cookies(session.getCookies())
			    .data(postData)
			    .execute();

	}
	
	/**
	 * Parses the results retrieved by postRenewal().
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
	    	message.error = Error.RENEW_FAILED;
	    }
	    
	    // Parse our table rows into a list of Books.
	    Elements rows = html.select("tr.patFuncEntry");
	    List<Book> results = CommonParsing.parseMyBooks(rows);
	    
	    // Return list of books.
	    message.obj = results;
	}
	
}
