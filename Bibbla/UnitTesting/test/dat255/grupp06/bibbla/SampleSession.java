package dat255.grupp06.bibbla;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

public class SampleSession {

	// TODO: Turn into Singleton
	public static Session getSession() {
		// Note:
		// PrivateCredentials (used below) is an unpublished class in the tasks-package.
		// Is for debugging purposes. Create your own!
		
		// Create a new Session, which keeps track of our session cookies.
		return new Session(PrivateCredentials.name, PrivateCredentials.code, PrivateCredentials.pin);
	}

}
