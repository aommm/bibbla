package se.gotlib.bibbla.backend.singletons;

import android.app.Application;

/**
 * Class that extends Application so it runs when the application is started.
 * Acts as the wrapper-class for the singletons.
 * @author Jonathan Orro
 *
 */
public class Singletons extends Application {
	private Library library;
	private User user;
	
	/**
	 * Initializes all the singletons
	 */
	@Override
	public void onCreate() {
		library = new Library();
		user = new User();
	}
	
	/**
	 * Getter for our instance of the library singleton.
	 * @return Library instance
	 */
	public Library getLibraryInstance() {
		return library;
	}
	
	/**
	 * Getter for out instance of the user singleton.
	 * @return User instance
	 */
	public User getUserInstance() {
		return user;
	}
}
