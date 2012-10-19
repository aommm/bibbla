package dat255.grupp06.bibbla.backend;

import dat255.grupp06.bibbla.backend.login.Session;
import dat255.grupp06.bibbla.backend.tasks.DetailedViewJob;
import dat255.grupp06.bibbla.backend.tasks.MyBooksJob;
import dat255.grupp06.bibbla.backend.tasks.MyReservationsJob;
import dat255.grupp06.bibbla.backend.tasks.SearchJob;
import dat255.grupp06.bibbla.backend.tasks.Task;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Performs tasks like searching, reserving and logging in.
 * Does all the heavy lifting.
 * 
 * @author Niklas Logren
 */
public final class Backend {
	
	private Settings settings;
	private Session session;
	
	/**
	 * Creates a new instance of our Backend.
	 * Initialises a new session and fetches settings.
	 */
	public Backend() {
		settings = new Settings();
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
	public void getUserDebt(Callback callback) {
		Message message = new Message();
		message.obj = (int)(Math.random()*500);
		callback.handleMessage(message);
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
	 *  
	 *  @param frontendCallback - the callback object which will be called when searching is done.
	 */
	public void fetchReservations(Callback frontendCallback) {
		final MyReservationsJob job = new MyReservationsJob(
				settings.getCredentials(), session);
		Task task = new Task(frontendCallback) {
			@Override
			protected Void doInBackground(String... arg0) {
				message = job.run();
				return null;
			}
		};
		task.execute();
	}

	/**
	 *  Fetches a list of the user's currently loaned books. Returns it using callback.
	 *  
	 *  @param frontendCallback - the callback object which will be called when searching is done.
	 */
	public void fetchLoans(Callback frontendCallback) {
		final MyBooksJob job = new MyBooksJob(settings.getCredentials(), session);
		// Create a new Task and define its body.
		Task task = new Task(frontendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
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
	
	public void saveCredentials(Credentials cred) {
		if (cred != null) {
			settings.setName(cred.name);
			settings.setCode(cred.card);
			settings.setPin(cred.pin);
		}
	}
}