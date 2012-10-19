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
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

public class SearchJob {
	
	private String searchPhrase = null;
	private Message message = new Message();
	private Response httpResponse;
	private int pageNumber;

	/**
	 * Creates a new SearchJob, which returns the first page of the search results.
	 * @param s - The string to search for.
	 */
	public SearchJob(String s){
		this(s, 0);
	}
	
	/**
	 * Creates a new SearchJob, which returns the specified page of search results.
	 * @param s - The string to search for.
	 * @param pageNumber - which page to fetch. 0=page1, 1=page1, 2=page2 ...
	 */
	public SearchJob(String s, int pageNumber){
		searchPhrase = s;
		System.out.println("s: "+s);
		message = new Message();
		this.pageNumber = pageNumber;
	}
	
	/**
	 * Starts the searching procedure.
	 * @returns a Message, which has a List<Book> as its object if successful.
	 */
	public Message run(){
					
		try {
			System.out.print("\n****** SearchJob\n");
			System.out.print("* step 1: fetch search results ");
			fetchSearchResults();
			System.out.print("succeeded! *\n");
			System.out.print("* step 2: parse search results ");
			parseSearchResults();
			System.out.print("succeeded! *\n*");
			System.out.print("****** SearchJob done \n");
		}
		catch (Exception e) {
			message.error = (message.error!=null) ? message.error : Error.SEARCH_FAILED;
			System.out.print("failed: "+e.getMessage()+" *** \n");
		}

		return message;
	}

	/**
	 * Fetches and saves the HTML of the search results.
	 *   
	 * @throws Exception if HTTP connection fails.
	 */
	private void fetchSearchResults() throws Exception {
		
		String url;
		// Fetch first page?
		if (pageNumber == 0) {
			url = "http://www.gotlib.goteborg.se/search*swe/X?searchtype=X&searcharg="+searchPhrase+"&searchscope=6&SUBMIT=S%C3%B6k";
		} // Fetch any other page.
		else {
			url = "http://www.gotlib.goteborg.se/search~S6*swe?/X"+searchPhrase+"&searchscope=6&SORT=D/X"+searchPhrase+"&searchscope=6&SORT=D&SUBKEY="+searchPhrase+"/"+(((pageNumber-1)*50)+ 1)+"%2C1000000%2C1000000%2CB/browse";
		}
		
		// Send HTTP request.
		httpResponse = Jsoup.connect(url)
					.method(Method.GET)
					.execute();
	}
	
	/**
	 * Parses the HTML fetched by fetchSearchResults().
	 * 
	 * @throws Exception if parsing fails. 
	 */
	private void parseSearchResults() throws Exception {		
	
		List<Book> results = new ArrayList<Book>();
		
		// Prepare HTML for parsing.
		Document html = httpResponse.parse();
		
		// Each result lies in its own table - loop through them all.
		Elements tables = html.select("table.breifCitTable");
		for(Element e : tables) {
			Book book = new Book();
			book.setName(e.select("a").get(1).text());
			book.setAuthor(e.select("strong").first().text());
			book.setType(e.select("td.sokresultat").get(4).getElementsByTag("img").first().attr("alt"));
			book.setUrl(e.select("a").get(1).attr("abs:href")); 
			book.setReserveUrl(e.select("div.reserverapadding").select("a").attr("abs:href"));
			book.setAvailable(availableParse(e.select("td.sokresultat").select("em").text()));
			results.add(book);
		}
		
		message.obj = results;
	}

	/**
	 * Parse the first word of string as int.
	 * @param A string whose first word possibly is a number.
	 * @return An int which defaults to 0 if parse fails.
	 */
	private int availableParse(String text) {
		try {
			return Integer.parseInt(text.split(" ")[0]);
		} catch (NumberFormatException exc) {
			return 0;
		}
	}
}
