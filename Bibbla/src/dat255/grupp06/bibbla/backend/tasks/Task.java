package dat255.grupp06.bibbla.backend.tasks;

import android.os.AsyncTask;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Callback;

/** 
 * An abstract class, representing a task which is run in a new thread.
 * 
 * Any subclass should:
 * 1. Perform the heavy operations,
 * 2. fill the message object with meaningful information,
 * 3. Submit the Message using callback.handleMessage(message).
 *   
 * @author Niklas Logren
 */
public abstract class Task extends AsyncTask<String, String, Void> {
	
	protected boolean running; // Not actually needed globally?
	protected Callback callback;
	protected Message message;
	
	/** Creates a new Task.
	 *  Saves the supplied callback, and initialises the internal message.
	 */
	public Task(Callback c) {
		// Save callback object.
		callback = c;
		// Initialise message.
		message = new Message();
		running = false;
	}
	
	public Task() {
		message = new Message();
		running = false;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		// Send message to callback object.
		if (callback != null) {
			callback.handleMessage(message);
		}
	}
	
}
