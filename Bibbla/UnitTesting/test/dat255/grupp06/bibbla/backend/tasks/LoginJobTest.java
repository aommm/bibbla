package dat255.grupp06.bibbla.backend.tasks;

import junit.framework.TestCase;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

public class LoginJobTest extends TestCase {

	public void testRun() {
		// PrivateCredentials is an unpublished class in the tasks-package, for debugging purposes.
		LoginJob job = new LoginJob(PrivateCredentials.name, PrivateCredentials.code, PrivateCredentials.pin);
		Message result = job.run();
		assertTrue(result.loggedIn); // What would happen if job returned a different type? 
	}

}
