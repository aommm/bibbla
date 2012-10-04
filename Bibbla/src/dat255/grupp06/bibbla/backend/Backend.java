package dat255.grupp06.bibbla.backend;

import java.util.ArrayList;
import java.util.List;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.backend.tasks.SearchJob;
import dat255.grupp06.bibbla.backend.tasks.Task;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

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
	private Session session;
	
	public Backend() {
		 //network = new NetworkHandler(); // Don't need networkHandler? 
		settings = new Settings(PrivateCredentials.name,PrivateCredentials.code,PrivateCredentials.pin);
		 session = new Session(settings.getName(), settings.getCode(), settings.getPin());
	}
	
	/**
	 * Starts the login process. Reports results using callback.
	 */
	public void login(final Callback frontendCallback) {

		// Create a new Task and define its body.
		Task task = new Task(frontendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				message = session.login();
				return null;
			}
		};
		// Start the task.
		task.execute();
	}
	
	/**
	 *  Searches backend for the supplied string, and reports results using callback.
	 */
	public void search(final String s, final Callback frontendCallback) {
		
		// Create a new Task and define its body.
		Task task = new Task(frontendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				SearchJob job = new SearchJob(s, session);
				message = job.run();
				return null;
			}
		};
		// Start the task.
		task.execute();
	}
	
	public void logOut(final Callback frontendCallback){
		
		session = new Session();
		
	}
}