package dat255.grupp06.bibbla.backend;

import java.util.ArrayList;

import org.jsoup.Jsoup;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import dat255.grupp06.bibbla.backend.tasks.Login;
import dat255.grupp06.bibbla.backend.tasks.Search;

/**
 * Performs tasks like searching, reserving and logging in.
 * Does all the heavy lifting.
 * 
 * @author Niklas Logren
 */
public class Backend {
	
	//private NetworkHandler network;
	//private Jsoup parser; // TODO: MIT license. needs to include notice?
	private Settings settings;
	
	public Backend() {
		 //network = new NetworkHandler(); // Don't need networkHandler?
		 settings = new Settings("logren","12345567643","1336");
	}
	
	/** Searches backend for the supplied string, and calls callback when done. **/
	public void search(final String s, final Callback c) {
		
		// Creates a new search.
		// This runs in a separate thread, reports via callback.
		new Search(s, new Callback() {

			// Handle the search results.
			public boolean handleMessage(Message msg) {
				// Did we need to login? (using arg1==1 for this, but it's arbitrary)
				if (msg.arg1 == 1) {
					// Create a new Login task.
					Login l = new Login(settings.getName(), settings.getCode(), settings.getPin());
					// Try to login.
					l.execute();
					// Did we succeed?
					if (l.getSuccess()) {
						// Success! Try the search again.
						Backend.this.search(s, c);
					}
					else {
						// Login failed. Handle?
					}
					
				}
				// Unspecified error.
				if (msg.obj == null) {
					// Do we need to do anything special, or just forward?
				}
				
				// We're all clear! Forward to frontend.
				else {
					c.handleMessage(msg);
				}
				
				return true; // The message is handled.
			}
			
		// Finally, run the search.  
		}).execute();

	}
}