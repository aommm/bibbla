package dat255.grupp06.bibbla.backend.tasks;

import junit.framework.TestCase;

public class LoginJobTest extends TestCase {

	public void testRun() {
		LoginJob job = new LoginJob();
		Object result = job.run();
		assertTrue((Boolean) result); // What would happen if job returned a different type? 
	}

}
