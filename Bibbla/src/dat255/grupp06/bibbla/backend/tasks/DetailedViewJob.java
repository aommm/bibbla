package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.PhysicalBook;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Fetches the detailed view of a book, and
 * saves the additional information in a new Book.
 * 
 * @author Fahad Al-Khameesi
 */
public class DetailedViewJob {
	
	private Book originalBook, newBook;
	private Message message = new Message();
	private Response httpResponse; 
	
	public DetailedViewJob(Book book){
		this.originalBook = book;
		this.newBook = (Book)originalBook.clone();
	}
	
	/**
	 * Fetches the detailed view of a book.
	 * 
	 * @returns a new Book, containing the additional information present in its detailed view.
	 */
	public Message run(){
		
		try {
			fetchBookDetails();
			parseBookDetails();
		} catch (Exception e) {
			message.error = (message.error!=null) ? message.error : Error.DETAILED_VIEW_FAILED;
			System.out.println("Something went wrong dude!");
		}
		
		return message;
	}
	
	/**
	 * Fetches the details of the supplied book.
	 * 
	 * @throws Exception if HTTP connection failed.
	 */
	private void fetchBookDetails() throws Exception {
		
	    httpResponse = Jsoup.connect(originalBook.getUrl())
			    .method(Method.GET)
			    .execute();		
	}
	
	/**
	 * Parses the HTML document fetched by fetchBookDetails().
	 * 
	 * @throws Exception if parsing fails.
	 */
	private void parseBookDetails() throws Exception{

		// Prepare HTML for parsing.
		Document html = httpResponse.parse();
		
	    List<PhysicalBook> physicalBooks = new ArrayList<PhysicalBook>();
	    Elements tableRows = html.select("table.bibItems").select("tr.bibItemsEntry");
	    
	    // Create all our PhysicalBooks.
	    for (Element row : tableRows) {
	    	Elements columns = row.getElementsByTag("td"); 
	    	String library = columns.get(0).text();
	    	String shelf = columns.get(1).text();
	    	String status = columns.get(2).text();
	    	String message = columns.get(3).text();
	    	PhysicalBook physicalBook = new PhysicalBook(library, shelf, status, message);
	    	physicalBooks.add(physicalBook);
	    }

	    // Select all rows containing detailed information.
	    Elements rows = html.select("div#orders").select("table.bibDetail").select("tr");
	    String description = "", notes = "", isbn = "";

	    // Loop through all rows.
	    for(int i=1;i<rows.size();i++) {
	    	
	    	// If we find a physical description,
	    	if(rows.get(i).select("td.bibInfoLabel").text().equals((String) "Fysisk beskrivning")){
	    		// Save the text present on row #1,
	    		description += (rows.get(i)).select("td.bibInfoData").text();
				int n = i+1;
				// And if there are rows without labels following it,
				// they are also part of the description. 
				while((rows.get(n).select("td.bibInfoLabel").size() == 0)){
					description += (rows.get(n)).select("td.bibInfoData").text();
					n++;
				}
	    	}
	    	
	    	// If we find a note,
	       	if(rows.get(i).select("td.bibInfoLabel").text().equals((String) "Anmärkning")) {
	       		// Save the text on row ¤1 
	    		notes += (rows.get(i)).select("td.bibInfoData").text();
				int n = i+1;
				// And save text of possible following rows that has no label.
				while((rows.get(n).select("td.bibInfoLabel").size() == 0)) {
					notes += (rows.get(n)).select("td.bibInfoData").text();
					n++;
				}
	    	}
	       	
	       	// If we find an ISBN, save it.
	       	if(rows.get(i).select("td.bibInfoLabel").text().equals((String) "ISBN")){
	    		isbn = (rows.get(i)).select("td.bibInfoData").text();
	    	}
	    }
	    
	    // Update newBook with this information.
	    newBook.setPhysicalBooks(physicalBooks);
	    newBook.setIsbn(isbn);
	    newBook.setNotes(notes);
	    newBook.setPhysicalDescription(description);
	    
	    message.obj = newBook;
	}
}
