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
	
	/** Creates a new Login job.
	 *  Reports result via callback.
	 */
	public Login(String name, String code, String pin, Callback c) {
		super(c);
		this.name = name;
		this.code = code;
		this.pin  = pin;
	}
	
	/** Creates a new Login job, without callback.
	 *  Get results using getMessage().
	 */
	public Login(String name, String code, String pin) {
		super();
		this.name = name;
		this.code = code;
		this.pin  = pin;
	}
	
	/** Returns the result of the job.
	 *  @returns null if the task is running.
	 */
	public Boolean getSuccess() {
		// Abort if already running.
		if (running) { return null; }
		else {
			return (Boolean)message.obj;
		}
	}
	
	@Override
	/** Performs the actual login.
	 *  Puts the results into our Message object.
	 */
	protected Void doInBackground(String... params) {
		if (running) { return null; }
		running = true;
		
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
