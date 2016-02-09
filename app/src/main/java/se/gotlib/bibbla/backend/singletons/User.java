package se.gotlib.bibbla.backend.singletons;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import se.gotlib.bibbla.util.Observable;
import se.gotlib.bibbla.util.Error;

/**
 * Singleton that handles all the async tasks you would want to do for a user.
 * Such as logging in, adding accounts, changing accounts.
 * @author Master
 *
 */
public class User implements Observable {
	private PropertyChangeSupport pcs;

	private String name;      // User's name
    private boolean loggedIn; // Logged in or not?

    private Context ctx;

//    TODO: design ctx structure between singletons better
	public User(Context ctx) {
        this.ctx = ctx;
		pcs = new PropertyChangeSupport(this);
	}
	
	public void loginAsync(String username, String password) {
        Log.d("login", "loginAsync called");
        this.name = username;

        String url = Singletons.API_URL + "/login";
        JSONObject data = null;
        try {
            data = new JSONObject(String.format("{username: %s, password: %s}", username, password));
        } catch (JSONException e) {
            e.printStackTrace();
            pcs.firePropertyChange("loginDone", null, Error.INPUT);
            return;
        }
        JsonObjectRequest r = new JsonObjectRequest
            (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("login", "loginAsync done");
                    pcs.firePropertyChange("loginDone", null, null);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("login", "loginAsync error, " + error.networkResponse.statusCode);
                    switch (error.networkResponse.statusCode) {
                        case 401:
                            pcs.firePropertyChange("loginDone", null, Error.INCORRECT_BIBBLA_CREDENTIALS);
                            break;
                        default:
                            pcs.firePropertyChange("loginDone", null, Error.NETWORK);
                            break;
                    }
                }
            });
        Singletons.getInstance(ctx).getRequestQueue().add(r);
	}

    /**
     * @deprecated what do we use this for?
     */
	public void addAccountAsync(String cardNumber, String pinNumber) {
		/*
        this.cardNumber = cardNumber;
		this.pinNumber = pinNumber;
		
		pcs.firePropertyChange("accountAdded", true, false);
		*/
	}
	
	public void changeAccountAsync(String newPin) {
		/*
        pinNumber = newPin;
		
		pcs.firePropertyChange("accountChanged", true, false);
		*/
	}
	
	public void addListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}


    /**
     * Returns the name of the currently logged in user
     */
    public String getName() {
        // TODO return full name, if fetched sometime
        return name;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

}
