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
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.PhysicalBook;
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

		getLoginForm();
		postLoginDetails();
		getSearchResults("hej");
		// Use first hit for everything from now on.
		Book b = ((List<Book>)message.obj).get(0);
		getDetailedView(b);
		reserveBook(b);
		
		return message;
	}
	
	private void getLoginForm() throws Exception {
		System.out.println("/******************************");
		System.out.println(" * Step 1");
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
		
		// Debug printing.
		printSessionVariables();
	}
	
	private void postLoginDetails() throws Exception {
		System.out.println("/******************************");
		System.out.println(" * Step 2");
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
	    
		// Debug printing.
		printSessionVariables();
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
	    
	    // Debug printing.
		printSessionVariables();
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
	    
	    // Debug print
	    printSessionVariables();
	}
	
	/**
	 * Reserve the supplied book.
	 * TODO implement
	 */
	private void reserveBook(Book book) throws Exception {
		
		throw new UnsupportedOperationException("implementera mig pls");
		
		/**
		// Testing: printing the first PhysicalBook of book.
		PhysicalBook copy = book.getCopies().get(0);
		System.out.println(copy);
		*/
		
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
