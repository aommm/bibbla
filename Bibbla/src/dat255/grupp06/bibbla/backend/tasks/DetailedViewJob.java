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
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.PhysicalBook;

public class DetailedViewJob {
	
	private Book book;
	private Message message = new Message();
	private Map<String,String> sessionCookies;
	
	public DetailedViewJob(Book book, Session session){
		
		this.book = book;
		sessionCookies = session.getCookies();
	
	}
	
	public Message run(){
		
		try {
			getBookDetails();
		} catch (IOException e) {
			System.out.println("Something went wrong dude!");
			e.printStackTrace();
		}
		
		message.obj = book;
		return message;
		
	}
	
	private void getBookDetails() throws IOException{
		
	    Response response = Jsoup.connect(book.getUrl())
			    .method(Method.GET)
			    .cookies(sessionCookies)
			    .execute();
	    
	    List<PhysicalBook> copies = new ArrayList<PhysicalBook>();
	    Elements tableRows = response.parse().select("table.bibItems").select("tr.bibItemsEntry");
	    
	    for (Element row : tableRows) {
	    	Elements columns = row.getElementsByTag("td"); 
	    	String library = columns.get(0).text();
	    	String shelf = columns.get(1).text();
	    	String status = columns.get(2).text();
	    	String message = columns.get(3).text();
	    	PhysicalBook physicalBook = new PhysicalBook(library, shelf, status, message);
	    	copies.add(physicalBook);
	    }
	    
	    book.setCopies(copies);
		
	}
}
