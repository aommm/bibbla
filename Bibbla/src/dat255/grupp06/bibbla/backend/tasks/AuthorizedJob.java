package dat255.grupp06.bibbla.backend.tasks;

import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.model.CredentialsMissingException;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Job classes that need the user to be logged in extend this class.
 * The constructor runs a LoginJob to ensure that every run of ancestor jobs
 * should be carried out with user logged-in.
 * @author arla
 */
public abstract class AuthorizedJob extends Job {
	
	protected final Credentials credentials;
	protected final Session session;
	
	/**
	 * Construct a job that requires being logged in.
	 * @param credentials Which credentials to use if not logged in.
	 * @param session cookies to detect whether logged in.
	 */
	public AuthorizedJob(Credentials credentials, Session session) {
		if (credentials == null);
		this.credentials = credentials;
		this.session = session;
	}
	
	/**
	 * Log in if needed (which is determined by the session object supplied in
	 * constructor). Should be called in ancestor's run() before
	 * login-dependent connections are made.
	 * @throws RuntimeException if login fails
	 * TODO Replace with something semantically correct
	 */
	public void login() {
		if (session.isActive())
			return;
		LoginJob loginJob = new LoginJob(credentials, session);
		Message message = loginJob.run();
		if (!session.isActive()) {
			throw new RuntimeException("Login failed.");
		}
	}
}