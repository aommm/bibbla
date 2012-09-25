package dat255.grupp06.bibbla.backend.tasks;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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

		try { // Run the job. This updates our Message.
			this.execute(s,null,null).get();
		}
		catch (InterruptedException e) {}
		catch (ExecutionException e) {}
		
		// Send message to callback object.
		c.handleMessage(message);
	}
	
	@Override
	/** Performs the actual searching.
	 *  Puts the results into our Message object.
	 */
	protected Void doInBackground(String... arg0) {
		
		// If not logged in: (made this up myself, not standardised)
		// message.arg1 = 1;
		
		// Else, put results into list,
		ArrayList<Book> results = new ArrayList<Book>();
		results.add(new Book("Book of Life", "Niklas Logren"));
		results.add(new Book("Dassboken #39", "Jonathan Orrö"));
		// And add this list to our Message. 
		message.obj = results;
		
		return null;
	}
}
