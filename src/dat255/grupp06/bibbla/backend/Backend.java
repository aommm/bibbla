package dat255.grupp06.bibbla.backend;

import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
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
		// Search is then run in a separate thread.
		new Search(s, 
				
			// Is called when searching is done.
			new Callback() {
			public boolean handleMessage(Message msg) {

				// Did we need to login? (using arg1==1 is arbitrary)
				if (msg.arg1 == 1) {
					
					// If so, login automatically.
					Login l = new Login(settings.getName(), settings.getCode(), settings.getPin());
					l.startAndFinish(); // Blocks until job is done.
					if (l.getSuccess()) {
						Backend.this.search(s, c); // Try search again.
					} else {
						// Login failed. Handle?
					}	
				}
				// Unspecified error while searching.
				if (msg.obj == null) {
					// Do we need to do anything special, or just forward?
				}

				else { // We're all clear! Forward to frontend.
					c.handleMessage(msg);
				}
				
				return true;
			}
			
		// Finally, run the search task. (starts a new thread)  
		}).execute();

	}
}