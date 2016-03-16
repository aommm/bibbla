package se.gotlib.bibbla.backend.singletons;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.CookieHandler;
import java.net.CookieManager;

/**
 * Class that extends Application so it runs when the application is started.
 * Acts as the wrapper-class for the singletons.
 * @author Jonathan Orro
 *
 */
public class Singletons {
	private Library library;
	private User user;
	private RequestQueue requestQueue;

	private static Singletons instance;
	private static Context ctx;


	public static final String API_URL = "http://192.168.1.73:3000";

	private Singletons(Context context) {
		ctx = context;
		requestQueue = getRequestQueue();
		library = new Library(ctx);
		user = new User(ctx);
	}

	public static synchronized Singletons getInstance(Context context) {
		if (instance == null) {
			instance = new Singletons(context);
		}
		return instance;
	}

	/**
	 * Getter for our instance of the library singleton.
	 * @return Library instance
	 */
	public Library getLibraryInstance() {
		return library;
	}
	
	/**
	 * Getter for out instance of the user singleton.
	 * @return User instance
	 */
	public User getUserInstance() {
		return user;
	}

	public RequestQueue getRequestQueue() {
		if (requestQueue == null) {

			// CookieStore is just an interface, you can implement it and do things like
			// save the cookies to disk or what ever.
//			CookieStore cookieStore = new MyCookieStore();
//			CookieManager manager = new CookieManager( cookieStore, CookiePolicy.ACCEPT_ALL );
//			CookieHandler.setDefault( manager  );

			// Optionally, you can just use the default CookieManager
			CookieManager manager = new CookieManager();
			CookieHandler.setDefault(manager);

			// getApplicationContext() is key, it keeps you from leaking the
			// Activity or BroadcastReceiver if someone passes one in.
			requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
		}
		return requestQueue;
	}


}
