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

import java.io.IOException;
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

	static final String KEY_LIBCODE = "locx00";
	static final String KEY_YEAR = "needby_Year", VAL_YEAR = "Year";
	static final String KEY_MONTH = "needby_Month", VAL_MONTH = "Month";
	static final String KEY_DAY = "needby_Day", VAL_DAY = "Day";

	private Book book;
	private Session session;
	private String libraryCode;
	private Map<String, String> postData;
	
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
	}

	/**
	 * Performs the reservation.
	 * 
	 * @returns a Message.
	 * If reservation was successful, error==null and obj is a string
	 * detailing which library the book will be sent to.
	 * If reservation failed, obj will be a string containing the error message.
	 */
	public Message run() {
		login();

		// Prepare field for paramless connect()
		postData = createPostData(libraryCode);

		// Attempt reservation
		Response reserveResponse = null;
		try {
			reserveResponse = connect();
		} catch (IOException e) {
			Message errorMessage = new Message();
			errorMessage.error = Error.RESERVE_FAILED;
			return errorMessage;
		}
		
		// Parse html from response
		Document html;
		try {
			html = reserveResponse.parse();
		} catch (IOException e) {
			Message errorMessage = new Message();
			errorMessage.error = Error.RESERVE_FAILED;
			return errorMessage;
		}
		
		// Identify 
		String library;
		try {
			library = identifyReceivingLibrary(html);
		} catch (JobFailedException e) {
			Message errorMessage = new Message();
			errorMessage.error = Error.RESERVE_FAILED;
			return errorMessage;
		}
		
		// Prepare a result message
		Message successMessage = new Message();
		successMessage.obj = library;
		return successMessage;
	}

	/**
	 * Creates a map of post data to send with the reservation request.
	 * @param libraryCode code identifying a library
	 * @return a map modelled by { "locx00" = <var>libraryCode</var>;
	 * "needby_Year" = "Year"; "needby_Month" = "Month"; "needby_Day" = "Day" }
	 */
	static Map<String, String> createPostData(final String libraryCode) {
		Map<String,String> postData = new HashMap<String,String>() {
			private static final long serialVersionUID = 5883265540089660691L;
			{
		    	put(KEY_LIBCODE, libraryCode);
		    	put(KEY_YEAR, VAL_YEAR);
		    	put(KEY_MONTH, VAL_MONTH);
		    	put(KEY_DAY, VAL_DAY);
			}
		};
		return postData;
	}

	@Override
	/**
	 * POSTs the reservation, and saves the response.
	 * @throws IOException if connection failed.
	 */
	protected final Response connect() throws IOException {
	    
	    // Send request and return response.
	    Response httpResponse = Jsoup.connect(book.getReserveUrl())
			    .method(Method.POST)
			    .cookies(session.getCookies())
			    .data(postData)
			    .execute();
	    return httpResponse;
	}

	/**
	 * Find the name of the library where the book will arrive.
	 * @param html produced from the reservation connection
	 * @return the name of a library
	 * @throws JobFailedException if the element of the library name could not
	 * be found. Assume the reservation failed.
	 */
	static String identifyReceivingLibrary(Document html)
	throws JobFailedException {
		Element div = html.getElementById("singlecolumn");
		try {
			return div.getElementsByTag("b").first().text();
		} catch (NullPointerException e) {
			throw new JobFailedException(e);
		}
	}
}
