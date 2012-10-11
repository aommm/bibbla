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
	 * @returns a list of Books. (Each book contains physicalBook, name, author, url and renewId)
	 * @throws Exception - If there is a slash in the title of the book,
	 * 						this code cannot differentiate between author and title. TODO or can it? 
	 */
	public static List<Book> parseMyBooks(Elements rows) throws Exception {
		
		/**
		 * TODO:
		 * Split up into several functions, some of which saves the name,
		 * some of which creates PhysicalBooks etc. 
		 * 
		 * .. Or just copy-paste code slightly across Jobs,
		 * if the code differs too much.
		 */
		
		List<Book> books = new ArrayList<Book>();
		
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
	    		throw new Exception("CommonParsing: Too many slashes in title!");
	    	}
	    	book.setName(nameAndAuthor[0]);
	    	book.setAuthor(nameAndAuthor[1]);
	    	book.setUrl(row.select("td.patFuncTitle").first().attr("abs:href"));
	    	book.setRenewId(row.select("td.patFuncMark").first().getElementsByTag("input").first().attr("value"));
	    	
	    	// Finally, add our new book to the list of results.
	    	books.add(book);
	    }
		
	    return books;
	}
	
}
