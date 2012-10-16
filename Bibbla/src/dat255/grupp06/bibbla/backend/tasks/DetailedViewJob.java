package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.PhysicalBook;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

public class DetailedViewJob {
	
	private Book originalBook, newBook;
	private Message message = new Message();
	
	public DetailedViewJob(Book book){
		this.originalBook = book;
		this.newBook = (Book)originalBook.clone();
	}
	
	public Message run(){
		
		try {
			getBookDetails();
		} catch (IOException e) {
			message.error = (message.error!=null) ? message.error : Error.DETAILED_VIEW_FAILED;
			System.out.println("Something went wrong dude!");
		}
		
		return message;
	}
	
	private void getBookDetails() throws IOException{
		
	    Response response = Jsoup.connect(originalBook.getUrl())
			    .method(Method.GET)
			    .execute();

	    List<PhysicalBook> physicalBooks = new ArrayList<PhysicalBook>();
	    Elements tableRows = response.parse().select("table.bibItems").select("tr.bibItemsEntry");
	    
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
	    Elements table = response.parse().select("div#orders").select("table.bibDetail").select("tr");
	    String description = "", notes = "", isbn = "";

	    // Loop through all rows.
	    for(int i=1;i<table.size();i++) {
	    	
	    	// If we find a physical description,
	    	if(table.get(i).select("td.bibInfoLabel").text().equals((String) "Fysisk beskrivning")){
	    		// Save the text present on row #1,
	    		description += (table.get(i)).select("td.bibInfoData").text();
				int n = i+1;
				// And if there are rows without labels following it,
				// they are also part of the description. 
				while((table.get(n).select("td.bibInfoLabel").size() == 0)){
					description += (table.get(n)).select("td.bibInfoData").text();
					n++;
				}
	    	}
	    	
	    	// If we find a note,
	       	if(table.get(i).select("td.bibInfoLabel").text().equals((String) "Anmärkning")) {
	       		// Save the text on row ¤1 
	    		notes += (table.get(i)).select("td.bibInfoData").text();
				int n = i+1;
				// And save text of possible following rows that has no label.
				while((table.get(n).select("td.bibInfoLabel").size() == 0)) {
					notes += (table.get(n)).select("td.bibInfoData").text();
					n++;
				}
	    	}
	       	
	       	// If we find an ISBN, save it.
	       	if(table.get(i).select("td.bibInfoLabel").text().equals((String) "ISBN")){
	    		isbn = (table.get(i)).select("td.bibInfoData").text();
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
