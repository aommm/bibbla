package se.gotlib.bibbla.backend.singletons;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import se.gotlib.bibbla.backend.model.Book;

/**
 * Singleton that handles all the tasks you would want to do with the library.
 * Such as searching for books, reserving books etc..
 * 
 * All methods that end with "Async" does some kind of asynchronous call to a job.
 * @author Master
 *
 */
public class Library implements PropertyChangeListener{
	private PropertyChangeSupport pcs;
	
	/*
	 * Dummy-variabler f�r prototypen
	 */
	private ArrayList<Book> books;
	private ArrayList<Book> loanedBooks;
	private ArrayList<Book> reservedBooks;
	
	public Library() {
		books = new ArrayList<Book>();
		loanedBooks = new ArrayList<Book>();
		reservedBooks = new ArrayList<Book>();
		
		pcs = new PropertyChangeSupport(this);
	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals("loginSucceeded")) {
			
		} else if(event.getPropertyName().equals("loginFailed")) {
			
		}
	}
	
	public void getReservationsAsync() {
		pcs.firePropertyChange("getReservations", null, reservedBooks);
	}
	
	public void getLoansAsync() {
		pcs.firePropertyChange("getLoans", null, loanedBooks);
	}
	
	public void getUserInfoAsync() {
		pcs.firePropertyChange("getUserInfo", null, "user");
	}
	
	public void reserveBookAsync(String ISBN) {
		boolean reserved = false;
		for(Book b : books) {
			if(b.getIsbn().equals(ISBN)) {
				reserved = true;
				reservedBooks.add(b);
			}
		}
		
		// The last of the two values are the one we use when receiving the event.
		// They need to be opposite so that  the property change is fired.
		pcs.firePropertyChange("reserveBook", !reserved, reserved);
	}
	
	public void loanBookAsync(String ISBN) {
		boolean loaned = false;
		for(Book b : books) {
			if(b.getIsbn().equals(ISBN)) {
				loaned = true;
				loanedBooks.add(b);
			}
		}
		
		// The last of the two values are the one we use when receiving the event.
		// They need to be opposite so that the property change is fired.
		pcs.firePropertyChange("loanBook", !loaned, loaned);
	}
	
	public void searchAsync(String s) {
		ArrayList<Book> searchResult = new ArrayList<Book>();
		for(Book b : books) {
			if(b.getAuthor().contains(s) || b.getIsbn().contains(s) || b.getTitle().contains(s)) {
				searchResult.add(b);
			}
		}
		pcs.firePropertyChange("searchDone", null, searchResult);
	}
}
