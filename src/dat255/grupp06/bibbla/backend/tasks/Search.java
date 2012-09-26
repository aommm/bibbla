package dat255.grupp06.bibbla.backend.tasks;

import java.util.ArrayList;

import android.os.Handler.Callback;
import dat255.grupp06.bibbla.utils.Book;

/** Searches for a book.
 *  Returns results using callback method.
 * @author Niklas Logren
 */
public class Search extends Task {
	String searchString;
	
	public Search(String s, Callback c) {
		super(c);
		searchString = s;
	}
	
	@Override
	/** Performs the actual searching.
	 *  Puts the results into our Message object.
	 */
	protected Void doInBackground(String... arg0) {
		if (running) { return null; }
		running = true;
		
		// If not logged in: (made this up myself, not standardised)
		// message.arg1 = 1;
		
		// Else, put results into list,
		ArrayList<Book> results = new ArrayList<Book>();
		results.add(new Book("Book of Life", "Niklas Logren"));
		results.add(new Book("Dassboken #39", "Jonathan Orrö"));
		// And add this list to our Message. 
		message.obj = results;
		
		// Trying if threading works.
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}
