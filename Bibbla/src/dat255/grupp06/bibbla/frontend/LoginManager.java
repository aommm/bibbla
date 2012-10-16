package dat255.grupp06.bibbla.frontend;

import android.app.Activity;
import android.content.Intent;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

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
	
	public void loginIfNeeded(Activity activity, Callback loginDoneCallback) {
		boolean loggedIn = false;//backend.isLoggedIn();
		if (!loggedIn) {
			// Show login overlay
			Intent intent = new Intent(activity, LoginOverlayActivity.class);
			activity.startActivityForResult(intent, RESULT_LOGIN_FORM);
		} else {
			// If logged in: same should happen as when getting result from LO
			Message message = new Message();
			message.loggedIn = loggedIn;
			loginDoneCallback.handleMessage(message);
		}
	}
	
	public void logout() {
		backend.logOut();
	}
}
