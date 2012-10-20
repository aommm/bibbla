/**
    This file is part of Bibbla.

    Bibbla is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bibbla is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bibbla.  If not, see <http://www.gnu.org/licenses/>.    
 **/

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
public class RenewJob extends AuthorizedJob {
	private Session session;
	private Message message;
	
	private List<Book> books;
	private String userUrl;
	
	/**
	 * Creates a new RenewJob,
	 * which will try to renew all of the user's currently loaned books. 
	 */
	public RenewJob(Credentials credentials, Session session) {
		super(credentials, session);
		this.session = session;
		this.message = new Message();
	}
	
	/**
	 * Creates a new RenewJob,
	 * which will try to renew all books in the supplied list.
	 * 
	 * Note: Assumes that all books has their renewId set!
	 */
	public RenewJob(List<Book> books, Credentials credentials,
			Session session) {
		this(credentials, session);
		this.books = books;
	}
	
	/**
	 * Creates a new RenewJob,
	 * which will try to renew only the supplied book.
	 * 
	 * Note: Assumes that the book has its renewId set!
	 */
	public RenewJob(Book book, Credentials credentials, Session session) {
		this(credentials, session);
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
		login();
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
			Response response = connectAndRetry();
			System.out.println("Step 2 done! ***");
			
			System.out.println("*** Step 3: parse the results");
			parseResults(response);
			System.out.println("Step 3 done! ***");		
			
		} catch (Exception e) {
			message.error = (message.error!=null) ? message.error : Error.RENEW_FAILED;
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
	}

	@Override
	/**
	 * POSTs the form which renews our books.
	 * @throws Exception if connection failed,
	 * 		the user isn't logged in, or if the server complained.  
	 */
	protected Response connect() throws Exception {
		
	    // Prepare POST data.
	    @SuppressWarnings("serial")
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
	    Response r = Jsoup.connect(userUrl)
			    .method(Method.POST)
			    .cookies(session.getCookies())
			    .data(postData)
			    .execute();
	    return r;
	}
	
	/**
	 * Parses the results retrieved by postRenewal().
	 * 
	 * @throws Exception if we're not logged in, or if parsing otherwise failed.
	 */
	private void parseResults(Response response) throws Exception {
		
	    // Prepare parsing.
	    Document html = response.parse();

	    // Are we still logged in?
	    if (html.select("div.loginPage").size()>0) {
	    	message.error = Error.LOGIN_NEEDED;
	    	throw new Exception("Not logged in");
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
