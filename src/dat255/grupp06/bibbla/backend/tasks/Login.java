package dat255.grupp06.bibbla.backend.tasks;

import java.util.concurrent.ExecutionException;

import android.os.Message;
import android.os.Handler.Callback;

/** Logs the user in.
 *  Reports failure/success using callback method.
 *  TOOD: should instead return cookie-stuff, to be stored in Backend?
 * @author Niklas Logren
 *
 */
public class Login extends Task {

	String name, code, pin;
	
	/** Creates and starts a new Login job.
	 *  Reports result via callback.
	 */
	public Login(String name, String code, String pin, Callback c) {
		super(c);
		this.name = name;
		this.code = code;
		this.pin  = pin;

		// Run the job.
		start();
		
		// Submit message to callback.
		c.handleMessage(message);
	}
	
	/** Creates a new Login job, but doesn't start it.
	 *  The task has to be started manually using the (blocking) method start()!
	 *  
	 *  This is the recommended approach for launching Login 'silently'.
	 *  (Is used in backend, if other tasks reports "not logged in")
	 */
	public Login(String name, String code, String pin) {
		this.name = name;
		this.code = code;
		this.pin  = pin;
	}
	
	/** Starts the login job.
	 *  @returns false if already running.
	 */
	public Boolean start() {
		// Abort if already running.
		if (running) { return false; }
		running = true;
		
		try {
			 this.execute(null,null,null).get(); // TODO timeout?
		}
		catch (InterruptedException e) {}
		catch (ExecutionException e) {} 
		
		// Return the result.
		// (returning like this is an exception; only possible since we want to be able to login silently.		
		//  usually, all data should flow through the Message object.)
		return (Boolean)message.obj;
	}
	
	@Override
	/** Performs the actual login.
	 *  Puts the results into our Message object.
	 */
	protected Void doInBackground(String... params) {

		// Actual time-consuming code.
		// Network requests,
		// parsing,
		// etc? ... Hard work.
		
		// Assume success.
		Boolean b = true;
		message.obj = b;
		// TODO: should return something cookie-like, not bool?
		
		return null;
	}

}
