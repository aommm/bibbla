package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.*;
import org.jsoup.Connection.*;
import org.jsoup.*;
import org.jsoup.nodes.*;

import dat255.grupp06.bibbla.backend.Session;
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
	 * TODO: doesn't report errors correctly.
	 * 
	 * Current behaviour:
	 * Even if the returned message indicates RENEW_FAILED,
	 * it's still possible that some of the renewals were successful.
	 * 
	 * Even if no error is indicated, some of the renewals may have failed.
	 * Be aware. 
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
	    Response response = Jsoup.connect(userUrl)
			    .method(Method.POST)
			    .cookies(session.getCookies())
			    .data(postData)
			    .execute();
	    
	    // Prepare parsing.
	    Document html = response.parse();

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
	    	// Something went wrong,
	    	// but some of the renewals may still have been applied.
	    	message.error = Error.RENEW_FAILED;
	    	
	    	/**
	    	 * TODO:
	    	 * Get text of each font tag- it is the error message.
	    	 * Tell the world about every error in some fancy way.
	    	 * 
	    	 * Should we return List<Book>? If so, where do we put the error?
	    	 * In what situations do we create RenewJobs? What do we want then?
	    	 * So many questions. So few answers. 
	    	 */
	    }
	    
	}
	
}
