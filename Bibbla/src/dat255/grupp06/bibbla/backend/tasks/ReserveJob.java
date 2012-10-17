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

import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Reserves a book at the supplied library. 
 * 
 * @author Niklas Logren
 */
public class ReserveJob {

	/**
	 * Performs the reservation.
	 * 
	 * @returns a Message.
	 * If reservation was successful, error==null and obj is a string
	 * detailing which library the book will be sent to.
	 * If reservation failed, obj will be a string containing the error message.
	 */
	public static Message run(String reserveUrl, final String libraryCode, Map<String,String> cookies){
		Message message = new Message();
		
		// Define hashMap containing post data.
		Map<String, String> postData = createPostData(libraryCode);

		// Attempt reservation
		Response reserveResponse;
		try {
			reserveResponse = postReservation(reserveUrl, libraryCode, cookies,
					postData);
		} catch (IOException e) {
			message.error = Error.RESERVE_FAILED;
			return message;
		}
		// Assert success
		Message parseReport = parseResults(reserveResponse);
		message.obj = parseReport;
		return message;
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
		    	put("locx00", libraryCode);
		    	put("needby_Year", "Year");
		    	put("needby_Month", "Month");
		    	put("needby_Day", "Day");
			}
		};
		return postData;
	}
	
	/**
	 * POSTs the reservation, and saves the response.
	 * @throws IOException if connection failed.
	 */
	static Response postReservation(String reserveUrl, final String
			libraryCode, Map<String,String> cookies, Map<String,String>
			postData) throws IOException {
	    
	    // Send request and return response.
	    Response httpResponse = Jsoup.connect(reserveUrl)
			    .method(Method.POST)
			    .cookies(cookies)
			    .data(postData)
			    .execute();
	    return httpResponse;
	}
	
	/**
	 * Parses the response HTML to see if everything went fine.
	 * @return the library where the book will arrive if successful, or error
	 * message otherwise
	 */
	static Message parseResults(Response reserveResponse) {
		// Identfy the div element with our data.
		Element div = findDiv(reserveResponse);
		if (div == null) {
			Message parseFailedMessage = new Message();
			parseFailedMessage.error = Error.RESERVE_FAILED;
			return parseFailedMessage;
		}

		// Font tag implies error.
		if (div.getElementsByTag("font").size() > 0) {
			Message failedMessage = new Message();
			failedMessage.error = Error.RESERVE_FAILED;
			failedMessage.obj = div.getElementsByTag("font").first().text();
			return failedMessage;
		} else {
			// We're all set! Return which library the book will arrive in.
			Message successMessage = new Message();
			successMessage.obj = div.getElementsByTag("b").first().text();
			return successMessage;
		}
	}

	/**
	 * Finds the relevant data in the response generated from a book
	 * reservation.
	 * @param reserveResponse 
	 * @return a div element containing information about the result, or null
	 * if it was not found.
	 */
	static Element findDiv(Response reserveResponse) {
		try {
			Document html = reserveResponse.parse();
			Element div = html.getElementById("singlecolumn");
			return div;
		} catch (Exception e) { // IO or NullPointer
			return null;
		}
	}

}
