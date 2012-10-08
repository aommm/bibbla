package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;

public class ReserveJob {
	
	private Book book;
	private Map<String, String> sessionCookies;
	private Message message = new Message();
	private String libraryCode = null;
	
	public ReserveJob(Book book, final String libraryCode, Session session){
		
		this.book = book;
		sessionCookies = session.getCookies();
		this.libraryCode = libraryCode;		
	}

	public Message run(){
		
		
		try {
			message.obj = reserveBook();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return message;
		
	}
	
	private boolean reserveBook() throws IOException{
		
		boolean jobDone = false;
		
		Map<String,String> postData = new HashMap<String,String>() {/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

		{
	    	put("locx00", libraryCode);
	    	put("needby_Year", "Year");
	    	put("needby_Month", "Month");
	    	put("needby_Day", "Day");
	    }};
	    
	    Response response = Jsoup.connect(book.getUrl())
			    .method(Method.POST)
			    .cookies(sessionCookies)
			    .data(postData)
			    .execute();
	    
	    Document responseDocument = response.parse();

	    
	    Element div = responseDocument.getElementById("singlecolumn");
	    if (div.getElementsByTag("font").size() > 0) {
	    	System.out.println("Reservation failed.");
	    } 
	    else {
	    	System.out.println("Reservation successful. Will arrive at "+
	    	div.getElementsByTag("b").first().text()+" sometime before easter. Probably?");
	    	
	    	jobDone = true;
	    }
	    
	    
	    return jobDone;
	}

	

}
