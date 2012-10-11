package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.PhysicalBook;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

/**
 * Logs in, searches and reserves books.
 * All in one.
 * 
 * (Am trying to use the classical db)
 * @author Niklas Logren
 */
public class ClassicJob {

	Map<String, String> sessionCookies;
	Map<String, String> sessionVariables;
	Message message;
	
	public ClassicJob() {
		message = new Message();
		sessionCookies   = new HashMap<String, String>();
		sessionVariables = new HashMap<String, String>();
	}
	
	public Message run() throws Exception {
		// LoginJob
		getLoginForm();
		postLoginDetails();
		
		// SearchJob
		getSearchResults("hej");
		
		// Use first hit for everything from now on.
		Book b = ((List<Book>)message.obj).get(0);
		
		// BookJob
		getDetailedView(b); // Updates book.
		
		// ReserveJob	
		reserveBook(b, "an"); // an = angered. See library-codes.txt
		
		return message;
	}
	
	private void getLoginForm() throws Exception {
		System.out.println("/******************************");
		System.out.println(" * Step 1.1: get login form");
		System.out.println(" ******************************/");
		String url = "https://www.gotlib.goteborg.se/iii/cas/login?service=https%3A%2F%2Fwww.gotlib.goteborg.se%3A443%2Fpatroninfo~S6*swe%2F0%2FIIITICKET&lang=swe&scope=6";

		// Create a request, and retrieve the response.
		Response response = Jsoup.connect(url)
			    .method(Method.GET)
			    .execute();

		// Get the cookies from the response.
		sessionCookies = response.cookies();
		
		// Prepare for parsing.
		Document html = response.parse();

		// Get the value of <input type="hidden" name="lt" value="" />.
		String lt = html.select("input[name=lt]").first().val();
		sessionVariables.put("lt", lt);
		
		// Tests.
		if ((sessionVariables.get("lt") == null) ||
		   (sessionCookies.get("JSESSIONID") == null) ||
		   (sessionCookies.get("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE") == null)) {
			throw new Exception("missing cookies/variables.");
		}
		
		System.out.println("login form got.");
	}
	
	private void postLoginDetails() throws Exception {
		System.out.println("/******************************");
		System.out.println(" * Step 1.2: post login details");
		System.out.println(" ******************************/");
		// Prepare POST url (spoiler: contains variables!)
	    String url= "https://www.gotlib.goteborg.se/iii/cas/login;jsessionid="+sessionVariables.get("JSESSIONID")+"?service=https%3A%2F%2Fwww.gotlib.goteborg.se%3A443%2Fpatroninfo~S6*swe%2F0%2FIIITICKET&lang="+sessionVariables.get("org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE")+"&scope=6";
	    // Prepare POST data.
	    Map<String,String> postData = new HashMap<String,String>() {{
	    	put("name", PrivateCredentials.name);
	    	put("code", PrivateCredentials.code);
	    	put("pin", PrivateCredentials.pin);
	    	put("lt", sessionVariables.get("lt"));
	    	put("_eventId", "submit");
	    }};
	    
	    // Send POST request and save response.
	    Response response = Jsoup.connect(url)
			    .method(Method.POST)
			    .data(postData)
			    .cookies(sessionCookies)
			    .execute();

	    // These new cookies are all we'll need. 
	    sessionCookies = response.cookies();
	    
	    System.out.println("login details posted.");
	}

	private void getSearchResults(String searchPhrase) throws Exception {
		System.out.println("/******************************");
		System.out.println(" * Step 3: get search results");
		System.out.println(" ******************************/");
		// Prepare url (spoiler: contains variables!)
		String url = "http://www.gotlib.goteborg.se/search*swe/X?searchtype=X&searcharg="+searchPhrase+"&searchscope=6&SUBMIT=S%C3%B6k";
	    
	    // Send GET request and save response.
	    Response response = Jsoup.connect(url)
			    .method(Method.GET)
			    .cookies(sessionCookies)
			    .execute();

	    // Are new cookies irrelevant? 
	    // sessionCookies = response.cookies();
	    
	    // Begin parsing.
	    Document html = response.parse();
	    // Each Search result is contained in a table. 
	    Elements searchResults = html.select("table.breifCitTable");
	    List<Book> results = new ArrayList<Book>();
	    
		for(Element table : searchResults){
			String name = table.select("a").get(1).text();
			String author = table.select("strong").first().text();
			String type = table.select("td.sokresultat").get(4).getElementsByTag("img").first().attr("alt");
			String bookUrl = table.select("a").get(1).attr("abs:href"); 
			String reserveUrl = table.select("div.reserverapadding").select("a").attr("abs:href");
			Book book = new Book(name, author, type, bookUrl, reserveUrl);
			results.add(book);
		}
		// Save list of Books in our message.
	    message.obj = results;
	    
	    System.out.println("search results got.");
	}
	
	/**
	 * Fetches the detailed view of the supplied Book,
	 * and updates the Book object with new information.
	 * 
	 * Currently, updates only the list of copies.
	 */
	private void getDetailedView(Book book) throws Exception {
		System.out.println("/******************************");
		System.out.println(" * Step 4: Get detailed view");
		System.out.println(" ******************************/");
		String url = book.getUrl();
	    
	    // Send GET request and save response.
	    Response response = Jsoup.connect(url)
			    .method(Method.GET)
			    .cookies(sessionCookies)
			    .execute();
	    
		// Create a PhysicalBook for each copy available of the book.
	    List<PhysicalBook> copies = new ArrayList<PhysicalBook>();
	    // Get all table rows describing book locations.
	    Elements tableRows = response.parse().select("table.bibItems").select("tr.bibItemsEntry");
	    // Loop through rows, and create a PhysicalBook for each. 
	    for (Element row : tableRows) {
	    	Elements columns = row.getElementsByTag("td"); 
	    	String library = columns.get(0).text();
	    	String shelf = columns.get(1).text();
	    	String status = columns.get(2).text();
	    	String message = columns.get(3).text();
	    	PhysicalBook physicalBook = new PhysicalBook(library, shelf, status, message);
	    	copies.add(physicalBook);
	    }
	    // Finally, add list of PhysicalBooks to Book.
	    book.setCopies(copies);
	    
	    System.out.println("detailed view got.");
	}
	
	/**
	 * Reserves the supplied book, at the 
	 */
	private void reserveBook(Book book, final String libraryCode) throws Exception {
		System.out.println("/******************************");
		System.out.println(" * Step 5: Post reservation");
		System.out.println(" ******************************/");	    
		
		// Prepare POST data.
		Map<String,String> postData = new HashMap<String,String>() {{
	    	put("locx00", libraryCode);
	    	put("needby_Year", "Year");
	    	put("needby_Month", "Month");
	    	put("needby_Day", "Day");
	    }};
	    
	    String url = book.getReserveUrl();
	    // Send request, and save response.
	    Response response = Jsoup.connect(url)
			    .method(Method.POST)
			    .cookies(sessionCookies)
			    .data(postData)
			    .execute();
	    
	    // Prepare response for parsing.
	    Document html = response.parse();

	    // Font tag -> error. Don't ask, I don't make the rules.
	    Element div = html.getElementById("singlecolumn");
	    if (div.getElementsByTag("font").size() > 0) {
	    	System.out.println("Reservation failed.");
	    } 
	    else { // No font tag! No error! Wohoo!
	    	System.out.println("Reservation successful. Will arrive at "+
	    div.getElementsByTag("b").first().text()+" sometime before easter. Probably?");
	    }
		
	}
	
	/**
	 * Prints all our cookies and variables (for debugging).
	 */
	private void printSessionVariables() {
		// Debug printing all cookies.
		for (Entry<String,String>c : sessionCookies.entrySet())
			System.out.println(c.getKey()+": "+c.getValue());
		// Debug printing all variables.
		for (Entry<String,String>c : sessionVariables.entrySet())
			System.out.println(c.getKey()+": "+c.getValue());
	}
	
}
