package dat255.grupp06.bibbla.backend.tasks;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.Message;

public class BookJob {
	
	private Book book;
	private Message message;
	
	public BookJob(Book book){
		
		this.book = book;
	
	}
	
	public Message run(){
		
		reserveBook();
		
		return message;
		
	}

	private void reserveBook(){
		
		
		
		
	}
}
