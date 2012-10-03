import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.backend.tasks.SearchJob;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

/**
 * A basic Java class for login debugging.
 * @author Niklas Logren
 */
public class LoginTest {

	public LoginTest() {}
	
	public void run() {
		// Note:
		// PrivateCredentials (used below) is an unpublished class in the tasks-package.
		// Is for debugging purposes. Create your own!
		
		// Create a new Session, which keeps track of our session cookies.
		Session session = new Session(PrivateCredentials.name, PrivateCredentials.code, PrivateCredentials.pin);
		
		SearchJob searchJob = new SearchJob("hej", session);
		Message result = searchJob.run();
		System.out.println(">>> SearchJob done: "+result);
		
		LoginJob loginJob = new LoginJob(session);
		result = loginJob.run();
		System.out.println(">>> LoginJob done: "+result);
	}
	
}
