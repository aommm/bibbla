/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orr�.
    
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

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Fetches the user's current debt.
 *
 * @author Niklas Logren
 */
public class MyDebtJob extends AuthorizedJob {
	private Message message;
	
	private String userUrl;
	private Integer debt;
	
	/**
	 * Creates a new MyDebtJob which will fetch the user's current debt.
	 * 
	 * @param session - the session to use. Is required since this is for logged-in users only.
	 */
	public MyDebtJob(Credentials credentials, Session session) {
		super(credentials, session);
		this.message = new Message();
	}
	
	/**
	 * Fetches the user's current debt.
	 * @returns a float, which is the user's current total debt. 
	 */
	public Message run()  {
		login();
		System.out.println("****** MyDebtJob: ");
		try {
			// Get user URL.
			System.out.println("*** Step 1: get user's url");
			userUrl = session.getUserUrl();
			// Did it fail?
			if ("".equals(userUrl)) {
				message.error = Error.FETCHING_USER_URL_FAILED;
				throw new Exception("Fetching user URL failed.");
			}
			System.out.println("Step 1 done! ***");
			
			System.out.println("*** Step 2: fetch user page");
			Response response = connectAndRetry();
			System.out.println("Step 2 done! ***");
			
			System.out.println("*** Step 3: parse debt");
			parseDebt(response);
			System.out.println("Step 3 done! ***");
			
		} catch (Exception e) {
			message.error = (message.error!=null) ? message.error : Error.MY_DEBT_FAILED;
			System.out.println("Failed: "+e.getMessage()+" ***");
		}
		
		return message;
	}
	
	@Override
	/**
	 * Connects to gotlib, and downloads the HTML of the user's profile page.
	 * 
	 * @throws Exception - If http connection fails.
	 */
	protected Response connect() throws Exception {

	    // Send GET request and save response.
	    Response r = Jsoup.connect(userUrl)
			    .method(Method.GET)
			    .cookies(session.getCookies())
			    .execute();
	    return r;
	}
	
	/**
	 * Parses the debt number from the HTML saved by fetchUserPage().
	 * 
	 * @throws Exception if we're not logged in, or if parsing otherwise failed. 
	 */
	private void parseDebt(Response response) throws Exception {
	    // Prepare parsing.
	    Document html = response.parse();

	    // Are we still logged in?
	    if (html.select("div.loginPage").size()>0) {
	    	message.error = Error.LOGIN_NEEDED;
	    	throw new Exception("Not logged in");
	    }
	    
	    // Get the text saying "x,xx kr i obetalda avgifter".
	    String text = html.select("div#leftcolumn").first().
	    		select("p").get(1).select("a").first().text();
	    // Split it at the comma, and parse first part into int.
	    debt = Integer.parseInt(text.split(",")[0]);
	    
	    // Save it in our message object.
	    message.obj = debt;
	}
	
}