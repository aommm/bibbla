package dat255.grupp06.bibbla.frontend;

import android.app.Activity;
import android.content.Intent;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.SerializableCallback;

/**
 * Basically contains some code dealing with logging in (which could as well
 * reside in MainActivity).
 * @author arla
 *
 */
public class LoginManager {
	
	public static final String EXTRA_LOGINSUCCESS = "loginsuccess";
	public static final String EXTRA_CALLBACK = "callback";
	public static final String EXTRA_CREDENTIALS = "credentials";
	public static final int RESULT_LOGIN_FORM = 0;
	
	private Backend backend;
	
	public LoginManager(Backend backend) {
		this.backend = backend;
	}
	
	public void loginIfNeeded(Activity activity, SerializableCallback afterLoginCallback) {
		boolean loggedIn = false;//backend.isLoggedIn();
		if (!loggedIn) {
			// Show login overlay
			Intent intent = new Intent(activity, LoginOverlayActivity.class);
			intent.putExtra(EXTRA_CALLBACK, afterLoginCallback); // can not put extra callback
			activity.startActivityForResult(intent, RESULT_LOGIN_FORM);
		}
		// If logged in
		Message message = new Message();
		message.loggedIn = loggedIn;
		afterLoginCallback.handleMessage(message);
	}
	
	public void logout() {
		backend.logOut();
	}
}
