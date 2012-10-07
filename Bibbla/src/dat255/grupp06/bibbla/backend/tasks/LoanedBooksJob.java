package dat255.grupp06.bibbla.backend.tasks;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.PhysicalBook;
import dat255.grupp06.bibbla.utils.Error;

/**
 * Fetches a list of the user's currently loaned books.
 *
 * @author Niklas Logren
 */
public class LoanedBooksJob {
	private Session session;
	private Message message;
	private String userUrl;
	
	public LoanedBooksJob(Session session) {
		this.session = session;
		this.message = new Message();
	}
	
	/**
	 * Fetches the user's currently loaned books.
	 * @returns a Message, containing a List of the user's current loans. 
	 */
	public Message run()  {
		System.out.println("****** LoanedBooksJob: ");
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
			
			
			System.out.println("*** Step 2: get loaned books");
			getLoanedBooks();
			System.out.println("Step 2 done! ***");
			
		} catch (Exception e) {
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
	}
	
	private void getLoanedBooks() throws Exception {

	    // Send GET request and save response.
	    Response response = Jsoup.connect(userUrl)
			    .method(Method.GET)
			    .cookies(session.getCookies())
			    .execute();
	    
	    // Prepare parsing.
	    Document html = response.parse();

	    // Are we still logged in?
	    if (html.select("div.loginPage").size()>0) {
	    	message.error = Error.LOGIN_NEEDED;
	    	message.loggedIn = false;
	    	throw new Exception("Not logged in");
	    }
	    
	    // Loop through all rows, and create a Book for each.
	    List<Book> results = new ArrayList<Book>();
	    Elements rows = html.select("tr.patFuncEntry");
	    for (Element row : rows) {
	    	// Create our new Book.
	    	Book book = new Book();
	    	
	    	// Create a new PhysicalBook, and give it a status and a shelf.
	    	PhysicalBook physicalBook = new PhysicalBook();
	    	physicalBook.setStatus(row.select("td.patFuncStatus").first().text());
	    	physicalBook.setShelf(row.select("td.patFuncCallNo").first().text());
	    	book.setPhysicalBook(physicalBook);

	    	// Set name, author and URL.
	    	String[] nameAndAuthor = row.select("td.patFuncTitle").text().split("/");
	    	// TODO: can books have slashes in their titles/authors? If so, this breaks.
	    	if (nameAndAuthor.length > 2) {
	    		throw new Exception("step1(): Too many slashes in title!");
	    	}
	    	book.setName(nameAndAuthor[0]);
	    	book.setAuthor(nameAndAuthor[1]);
	    	book.setUrl(row.select("td.patFuncTitle").first().attr("abs:href"));
	    	book.setRenewId(row.select("td.patFuncMark").first().getElementsByTag("input").first().attr("value"));
	    	
	    	// Finally, add our new book to the list of results.
	    	results.add(book);
	    }
	    
	    // Return list of loaned books.
	    message.obj = results;
	}
	
}
