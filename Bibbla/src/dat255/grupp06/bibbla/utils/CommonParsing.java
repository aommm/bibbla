package dat255.grupp06.bibbla.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.PhysicalBook;

/**
 * Supplies common parsing methods to other classes.
 * 
 * @author Niklas Logren
 */
public class CommonParsing {

	/**
	 * Parses the HTML from the "my loans"-view of gotlib.
	 * 
	 * @param rows - The table rows as parsed by Jsoup. Should be <tr class="trpatFuncEntry" ...> .
	 * 
	 * @returns a list of Books. Each book contains physicalBook, name, author, url and renewId.
	 * 	The physicalBook may contain an error message, retrievable by getMessage().
	 * 
	 * @throws Exception - If there is a slash in the title of the book,
	 * 	this code cannot differentiate between author and title.
	 * @throws Exception also if parsing fails in any other way. 
	 */
	public static List<Book> parseMyBooks(Elements rows) throws Exception {
		// false = parse my books; true = parse my reservations.
		return parseRows(rows, false);		
	}
	
	/**
	 * Parses the HTML from the reservations-view of gotlib.
	 * 
	 * @param rows - The table rows as parsed by Jsoup. Should be <tr class="trpatFuncEntry" ...> .
	 * 
	 * @returns a list of Books. Each book contains physicalBook, name,
	 * author, url and reservationId. The physicalBook contains a library, and 
	 * may also contain an error message, retrievable by getMessage(). 
	 * 
	 * @throws Exception - If there is a slash in the title of the book,
	 * 	this code cannot differentiate between author and title.
	 * @throws Exception also if parsing fails in any other way. 
	 */
	public static List<Book> parseMyReservations(Elements rows) throws Exception {
		// true = parse my reservations; false = parse my books.
		return parseRows(rows, true);
	}
	
	/**
	 * Parses the supplied rows and treats them in one of two ways - 
	 * As rows of the reservation view, or as rows of the My Books view. 
	 * 
	 * @param rows - the rows to be parsed.
	 * @param parseReservations - true if we should parse My reservations,
	 * 	false if we should parse My books. 
	 * @returns a list of Books. Each book contains physicalBook, name, author and url.
	 * 	Other information may be present, dependning on parsing mode.
	 * 
	 * @throws Exception - If there is a slash in the title of the book,
	 * 	this code cannot differentiate between author and title.
	 * @throws Exception also if parsing fails in any other way. 
	 */
	public static List<Book> parseRows(Elements rows, boolean parseReservations) throws Exception {
		
		List<Book> books = new ArrayList<Book>();
	    for (Element row : rows) {
	    	// Create our new Book.
	    	Book book = new Book();
	    	
	    	// Create a new PhysicalBook.
	    	PhysicalBook physicalBook = new PhysicalBook();
	    	
	    	// Does the PhysicalBook have an error?
	    	if (row.select("font").size()>0) {
	    		Element errorTag = row.select("font").first();
	    		// Save error to PhysicalBook.
	    		physicalBook.setError(true);
	    		physicalBook.setMessage(errorTag.text());
	    		// Font tag is located inside status td.
	    		// Remove, so that .text()-call below returns only status.
	    		errorTag.remove();
	    	}
	    	physicalBook.setStatus(row.select("td.patFuncStatus").first().text());
	    	
	    	if (parseReservations) { // Only reservations view displays library,
	    		physicalBook.setLibrary(row.select("td.patFuncPickup").first().text());
	    	} else { // and only My books view displays shelf.
	    		physicalBook.setShelf(row.select("td.patFuncCallNo").first().text());
	    	}
	    	book.setPhysicalBook(physicalBook);

	    	// Set name, author and URL.
	    	String[] nameAndAuthor = row.select("td.patFuncTitle").text().split("/");
	    	// TODO: can books have slashes in their titles/authors? If so, this breaks.
	    	if (nameAndAuthor.length > 2) {
	    		throw new Exception("CommonParsing: Too many slashes in title!");
	    	}
	    	book.setName(nameAndAuthor[0]);
	    	book.setAuthor(nameAndAuthor[1]);
	    	book.setUrl(row.select("td.patFuncTitle").first().attr("abs:href"));
	    	if (parseReservations) {
	    		book.setUnreserveId(row.select("td.patFuncMark").first().getElementsByTag("input").first().attr("value"));	
	    	} else {
	    		book.setRenewId(row.select("td.patFuncMark").first().getElementsByTag("input").first().attr("value"));
	    	}
	    	
	    	// Finally, add our new book to the list of results.
	    	books.add(book);
	    }
		
	    return books;
	}
	
}
