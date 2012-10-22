/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.
    
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

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Reserves a book at the supplied library. 
 * 
 * @author Niklas Logren
 */
public class ReserveJob extends AuthorizedJob {
	private Book book;
	private Session session;
	private Message message;
	private String libraryCode;
	
	/**
	 * Creates a new ReserveJob which will try to reserve a book at the given library.
	 * 
	 * @param book - The book to reserve. Needs reserveUrl set.
	 * @param libraryCode - The code of the library to send the book to. See library-codes.txt.
	 * @param session - The session the book should be reserved using. User account is specified here.
	 */
	public ReserveJob(Book book, String libraryCode, Credentials credentials,
			Session session) {
		super(credentials, session);
		this.book = book;
		this.libraryCode = libraryCode;
		this.session = session;
		this.message = new Message();
	}

	/**
	 * Performs the reservation.
	 * 
	 * @returns a Message.
	 * If reservation was successful, error==null and obj is a string
	 * detailing which library the book will be sent to.
	 * If reservation failed, obj will be a string containing the error message.
	 */
	public Message run(){
		login();
		
		System.out.println("****** ReserveJob: ");
		try {
			System.out.println("*** Step 1: Post our reservation");
			Response response = connect();
			System.out.println("Step 1 done! ***");
			
			System.out.println("*** Step 2: Parse the results");
			parseResults(response);
			System.out.println("Step 2 done! ***");
			
		} catch (Exception e) {
			message.error = (message.error!=null) ? message.error : Error.RESERVE_FAILED;
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
		
	}
	
	@Override
	/**
	 * POSTs the reservation, and saves the response.
	 * 
	 * @throws Exception if connection failed.
	 */
	protected Response connect() throws Exception {
		
		// Define hashMap containing post data.
		@SuppressWarnings("serial")
		Map<String,String> postData = new HashMap<String,String>() {{
	    	put("locx00", libraryCode);
	    	put("needby_Year", "Year");
	    	put("needby_Month", "Month");
	    	put("needby_Day", "Day");
	    }};
	    
	    // Send request and save response.
	    Response r = Jsoup.connect(book.getReserveUrl())
			    .method(Method.POST)
			    .cookies(session.getCookies())
			    .data(postData)
			    .execute();
	    return r;
	}
	
	/**
	 * Parses the response HTML saved by postReservation().
	 * 
	 * @throws Exception if reservation failed, or if parsing otherwise failed.
	 */
	private void parseResults(Response response) throws Exception {
		
		// Prepare for parsing.
	    Document html = response.parse();

	    // All information we need lies in this div.
	    Element div = html.getElementById("singlecolumn");
	    
	    // Font tag implies error.
	    if (div.getElementsByTag("font").size() > 0) {
	    	message.error = Error.RESERVE_FAILED;
	    	// The error message is inside the font tag.
	    	String errorMessage = div.getElementsByTag("font").first().text();
	    	message.obj = errorMessage;
	    	throw new Exception("Reservation failed: "+errorMessage);
	    } 
	    // We're all set!
	    else {
	    	// Return which library the book will arrive in.
	    	message.obj = div.getElementsByTag("b").first().text();
	    }
	}

}