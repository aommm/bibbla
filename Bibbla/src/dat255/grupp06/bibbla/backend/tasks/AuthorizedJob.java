package dat255.grupp06.bibbla.backend.tasks;

import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Job classes that need the user to be logged in extend this class.
 * @author arla
 */
public abstract class AuthorizedJob {
	// this interface intentionally left blank
	public AuthorizedJob(final boolean loggedIn, final Credentials credentials) {
		if (loggedIn)
			return;
		LoginJob loginJob = new LoginJob(credentials);
		Message message = loginJob.run();
	}
}