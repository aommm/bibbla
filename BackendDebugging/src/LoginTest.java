import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

/**
 * A basic Java class for login debugging.
 * @author Niklas Logren
 */
public class LoginTest {

	public LoginTest() {}
	
	public void run() {
		// PrivateCredentials is an unpublished class in the tasks-package, for debugging purposes.
		Session session = new Session(PrivateCredentials.name, PrivateCredentials.code, PrivateCredentials.pin);
		LoginJob job = new LoginJob(PrivateCredentials.name, PrivateCredentials.code, PrivateCredentials.pin, session);
		Object result = job.run();
		System.out.println("****** Test finished. Result: "+result.toString());
	}
	
}
