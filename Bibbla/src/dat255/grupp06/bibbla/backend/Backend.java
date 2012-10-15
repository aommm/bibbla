package dat255.grupp06.bibbla.backend;

import java.util.List;

import dat255.grupp06.bibbla.backend.tasks.DetailedViewJob;
import dat255.grupp06.bibbla.backend.tasks.MyBooksJob;
import dat255.grupp06.bibbla.backend.tasks.SearchJob;
import dat255.grupp06.bibbla.backend.tasks.Task;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

/**
 * Performs tasks like searching, reserving and logging in.
 * Does all the heavy lifting.
 * 
 * @author Niklas Logren
 */
public class Backend {
	
	private Settings settings;
	private Session session;
	
	/**
	 * Creates a new instance of our Backend.
	 * Initialises a new session and fetches settings.
	 */
	public Backend() {
		settings = new Settings(PrivateCredentials.name,PrivateCredentials.code,PrivateCredentials.pin);
		session = new Session(settings.getName(), settings.getCode(), settings.getPin());
	}
	
	/**
	 *  @returns the user's username.
	 *  If no name is saved, returns empty string.
	 */
	public String getUserName() {
		return session.getName();
	}
	
	/**
	 *  @returns the user's current debt.
	 *  TODO: Implement.
	 */
	public int getUserDebt() {
		return (int)(Math.random()*500);
	}
	
	/**
	 * Starts the login process. Reports results using callback.
	 * 
	 * @param frontendCallback - the callback object which will be called when logging in is done.
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
	 *  
	 *  @param s - The string to search for.
	 *  @param page - which page of search results to fetch. If too high, returns empty list.
	 *  @param frontendCallback - the callback object which will be called when searching is done.
	 */
	public void search(final String s, final int page, final Callback frontendCallback) {
		// Create a new Task and define its body.
		Task task = new Task(frontendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				SearchJob job = new SearchJob(s,0);
				message = job.run();
				return null;
			}
		};
		// Start the task.
		task.execute();
	}
	
	/**
	 *  Fetches the DetailedView of the supplied book.
	 *  Sends a new book with all the additional information to the callback.
	 */
	public void fetchDetailedView(final Book book, final Callback frontendCallback) {
		// Create a new Task and define its body.
		Task task = new Task(frontendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				DetailedViewJob job = new DetailedViewJob(book);
				message = job.run();
				return null;
			}
		};
		// Start the task.
		task.execute();
	}
	
	/**
	 *  Fetches a list of the user's current reservations.
	 *  Returns it using callback.
	 *  TODO: implement.
	 *  
	 *  @param frontendCallback - the callback object which will be called when searching is done.
	 */
	public void fetchReservations(Callback frontendCallback) {
		throw new UnsupportedOperationException("implement pls");
	}

	/**
	 *  Fetches a list of the user's currently loaned books. Returns it using callback.
	 *  
	 *  @param frontendCallback - the callback object which will be called when searching is done.
	 */
	public void fetchLoans(Callback frontendCallback) {
		// Create a new Task and define its body.
		Task task = new Task(frontendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				MyBooksJob job = new MyBooksJob(session);
				message = job.run();
				return null;
			}
		};
		// Start the task.
		task.execute();
	}
	
	/**
	 * Logs the user out.
	 */
	public void logOut(){
		session = new Session();	
	}
	
	public boolean isLoggedIn() {
		return session.checkLogin();
	}
}