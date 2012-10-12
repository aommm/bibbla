package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.PhysicalBook;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Error;

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
			System.out.println("Something went wrong dude!");
			message.error = Error.DETAILED_VIEW_FAILED;
			e.printStackTrace();
		}
		
		return message;
		
	}
	
	private void getBookDetails() throws IOException{
		
	    Response response = Jsoup.connect(originalBook.getUrl())
			    .method(Method.GET)
			    .execute();
	    
	    List<PhysicalBook> physicalBooks = new ArrayList<PhysicalBook>();
	    Elements tableRows = response.parse().select("table.bibItems").select("tr.bibItemsEntry");
	    
	    for (Element row : tableRows) {
	    	Elements columns = row.getElementsByTag("td"); 
	    	String library = columns.get(0).text();
	    	String shelf = columns.get(1).text();
	    	String status = columns.get(2).text();
	    	String message = columns.get(3).text();
	    	PhysicalBook physicalBook = new PhysicalBook(library, shelf, status, message);
	    	physicalBooks.add(physicalBook);
	    }
	    // TODO:
	    // Fill other fields as well
	    
	    newBook.setPhysicalBooks(physicalBooks);
	}
}
