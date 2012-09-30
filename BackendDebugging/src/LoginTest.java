import dat255.grupp06.bibbla.backend.tasks.LoginJob;

/**
 * A basic Java class for login debugging.
 * @author Niklas Logren
 */
public class LoginTest {

	public LoginTest() {}
	
	public void run() {
		LoginJob job = new LoginJob();
		Object result = job.run();
		System.out.println("****** Test finished. Result: "+result.toString());
	}
	
}
