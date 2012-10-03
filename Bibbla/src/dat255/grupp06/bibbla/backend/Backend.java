package dat255.grupp06.bibbla.backend;

import java.util.ArrayList;
import java.util.List;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.backend.tasks.SearchJob;
import dat255.grupp06.bibbla.backend.tasks.Task;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

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
		 settings = new Settings("logren","12345567643","1336");
		 session = new Session(settings.getName(), settings.getCode(), settings.getPin());
	}
	
	/**
	 * Starts the login process. Reports results using c1.
	 * @param c1 The frontend callback object.
	 */
	public void login(final Callback frontendCallback) {

		// Firstly, create a new Callback object which calls backend.
		Callback backendCallback = new Callback() {
			@Override
			// This code is run when Task finishes (on UI thread).
			public void handleMessage(Message msg) {
				Backend.this.loginDone(msg, frontendCallback);
			}};
		
		// Secondly, create a new Task and define 22its body.
		// Should call our newly created Callback when done.
		Task task = new Task(backendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				LoginJob job = new LoginJob(session);
				message = job.run();
				return null;
			}
		};
		
		// Finally, start the task.
		task.execute();
	}
	
	public void loginDone(Message msg, Callback frontendCallback) {
		// No further processing necessary. Forward to frontend.
		// TODO Can be removed?
		frontendCallback.handleMessage(msg);
	}
	
	/** Searches backend for the supplied string, and calls callback when done. **/
	public void search(final String s, final Callback frontendCallback) {
		
		// Firstly, create a new Callback object which calls backend.
		Callback backendCallback = new Callback() {
			@Override
			// This code is run when Task finishes (on UI thread).
			public void handleMessage(Message msg) {
				Backend.this.searchDone(msg, frontendCallback);
			}};
		
		// Secondly, create a new Task and define its body.
		// Should call our newly created Callback when done.
		Task task = new Task(backendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				SearchJob job = new SearchJob(s, session);
				message = job.run();
				return null;
			}
		};
		
		// Finally, start the task.
		task.execute();
	}
	
	public void searchDone(Message msg, Callback frontendCallback) {
		// No further processing necessary. Forward to frontend.
		// TODO can be removed?
		frontendCallback.handleMessage(msg);

	}
}