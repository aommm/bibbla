package dat255.grupp06.bibbla.backend.tasks;

import java.util.ArrayList;
import java.util.List;

import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;

public class SearchJob {
	
	private Message message;
	
	public SearchJob(String s) {
		message = new Message();
	}
	
	public Message run() {
		List<Book> results = new ArrayList<Book>();
		results.add(new Book("Book of Life", "Niklas Logren"));
		
		message.loggedIn = false;
		message.obj = results;
		return message;
	}

}
