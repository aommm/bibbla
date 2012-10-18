package dat255.grupp06.bibbla.backend.tasks;

import java.util.List;

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
public class MyBooksJob extends AuthorizedJob {
	private Session session;
	private Message message;
	
	private Response httpResponse;
	private String userUrl;
	
	public MyBooksJob(boolean loggedIn, Credentials credentials, Session session) {
		super(loggedIn, credentials);
		this.session = session;
		this.message = new Message();
	}
	
	/**
	 * Fetches the user's currently loaned books.
	 * @returns a Message, containing a List of the user's current loans. 
	 */
	public Message run()  {
		System.out.println("****** MyBooksJob: ");
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
			
			System.out.println("*** Step 2: fetch loaned books");
			fetchLoanedBooks();
			System.out.println("Step 2 done! ***");
			
			System.out.println("*** Step 3: parse loaned books");
			parseLoanedBooks();
			System.out.println("Step 3 done! ***");
			
		} catch (Exception e) {
			message.error = (message.error!=null) ? message.error : Error.MY_BOOKS_FAILED;
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
	}
	
	/**
	 * Connects to gotlib, and downloads the HTML of 'loaned books'.
	 * 
	 * @throws Exception - If http connection fails.
	 */
	private void fetchLoanedBooks() throws Exception {

	    // Send GET request and save response.
	    httpResponse = Jsoup.connect(userUrl)
			    .method(Method.GET)
			    .cookies(session.getCookies())
			    .execute();   
	}
	
	/**
	 * Parses the results saved by fetchLoanedBooks().
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
	    List<Book> results = CommonParsing.parseMyBooks(rows);
	    
	    // Return list of loaned books.
	    message.obj = results;
	}
	
}
