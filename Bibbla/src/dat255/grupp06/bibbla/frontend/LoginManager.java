package dat255.grupp06.bibbla.frontend;

import android.app.Activity;
import android.content.Intent;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.model.Credentials;
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
	
	/**
	 * Shows LoginOverlay prompt if not logged in.
	 * @param activity 
	 * @param loginDoneCallback will be called if the user was already logged
	 * in; otherwise will be passed to the activity's onActivityResult method.
	 */
	public void promptIfNotLoggedIn(Activity activity, Callback loginDoneCallback) {
		boolean loggedIn = backend.isLoggedIn2();
		if (!loggedIn) {
			// Show login overlay
			prompt(activity);
		} else {
			// If logged in: same should happen as when getting result from LO
			Message message = new Message();
			message.loggedIn = loggedIn;
			loginDoneCallback.handleMessage(message);
		}
	}
	
	public void prompt(Activity activity) {
		Intent intent = new Intent(activity, LoginOverlayActivity.class);
		activity.startActivityForResult(intent, RESULT_LOGIN_FORM);
	}
	
	public void login(Credentials cred, Callback loginDoneCallback) {
		backend.saveCredentials(cred);
		backend.arildLogin(loginDoneCallback);
	}
	
	public void logout() {
		backend.logOut();
	}
}
