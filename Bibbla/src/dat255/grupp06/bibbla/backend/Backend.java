package dat255.grupp06.bibbla.backend;

import java.util.Map;

import dat255.grupp06.bibbla.backend.login.Session;
import dat255.grupp06.bibbla.backend.tasks.DetailedViewJob;
import dat255.grupp06.bibbla.backend.tasks.LoginJob;
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
		settings = new Settings(/*PrivateCredentials.name,PrivateCredentials.code,PrivateCredentials.pin*/);
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
	 * Starts the login process. Reports results using callback.
	 * 
	 * @param frontendCallback - the callback object which will be called when logging in is done.
	 * @deprecated
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
	 * New version of login, weird name in order not to break things with
	 * previous login method.
	 * @deprecated
	 */
	public void arildLogin(final Callback frontendCallback) {
		// Check if already logged in
		if (isLoggedIn2()) {
			// Call back with success
			Message message = new Message();
			message.loggedIn = true;
			frontendCallback.handleMessage(message);
		} else {
			// Log in through LoginJob, using credentials from settings
			Task task = new Task(frontendCallback) {
				@SuppressWarnings("unchecked")
				@Override
				protected Void doInBackground(String... params) {
					LoginJob job = new LoginJob(settings.getCredentials());
					message = job.run();
					if (message.loggedIn) {
						try {
							session.setCookies((Map<String,String>) message.obj);
						} catch (ClassCastException e) {
							session.setCookies(null);
						}
					}
					return null;
				}
			};
			task.execute();
		}
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
		Task task = new Task(frontendCallback) {
			@Override
			protected Void doInBackground(String... arg0) {
				MyReservationsJob job = new MyReservationsJob(isLoggedIn2(),
						settings.getCredentials(), session);
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
		// Create a new Task and define its body.
		Task task = new Task(frontendCallback) {
			@Override
			// The code that's run in the Task (on new thread).
			protected Void doInBackground(String... params) {
				MyBooksJob job = new MyBooksJob(isLoggedIn2(),
						settings.getCredentials(), session);
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
	
	/** @deprecated */
	public boolean isLoggedIn() {
		return session.checkLogin();
	}
	
	public boolean isLoggedIn2() {
		return session.isActive();
	}

	// TODO Change settings to use Credentials
	public void saveCredentials(String name, String card, String pin) {
		settings.setName(name);
		settings.setCode(card);
		settings.setPin(pin);
	}
	
	public void saveCredentials(Credentials cred) {
		if (cred != null)
			saveCredentials(cred.name, cred.card, cred.pin);
	}
}