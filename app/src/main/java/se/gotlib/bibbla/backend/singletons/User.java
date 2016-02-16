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

    private boolean loggedIn; // Logged in or not?

    private String username;
    private String password;
    private String gotlibSurname;
    private String gotlibCode;
    private String gotlibPin;

    private String token;

    private Context ctx;

//    TODO: design ctx structure between singletons better
	public User(Context ctx) {
        this.ctx = ctx;
		pcs = new PropertyChangeSupport(this);
	}
	
	public void loginAsync(String username, String password) {
        Log.d("backend", "loginAsync called");

        String url = Singletons.API_URL + "/login";
        JSONObject data = null;
        try {
            data = new JSONObject(String.format("{username: %s, password: %s}", username, password));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("backend", "loginAsync error, input");
            pcs.firePropertyChange("loginDone", null, Error.INPUT);
            return;
        }
        JsonObjectRequest r = new JsonObjectRequest
            (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        parseJson(response);
                        Log.d("backend", "loginAsync done"+User.this.username+" , "+User.this.gotlibSurname);
                        pcs.firePropertyChange("loginDone", null, null);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("backend", "loginAsync failed");
                        pcs.firePropertyChange("loginDone", null, Error.PARSE);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("backend", "loginAsync error, " + error.networkResponse.statusCode);
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

    private void parseJson(JSONObject response) throws JSONException {

        this.token = response.getString("token");
        JSONObject user = response.getJSONObject("user");

        this.username = user.getString("username");
        this.password = user.getString("password");
        this.gotlibSurname = user.getString("gotlib_surname");
        this.gotlibCode = user.getString("gotlib_code");
        this.gotlibPin = user.getString("gotlib_pin");

        this.loggedIn = true;
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
        return username;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGotlibSurname() {
        return gotlibSurname;
    }

    public String getGotlibCode() {
        return gotlibCode;
    }

    public String getGotlibPin() {
        return gotlibPin;
    }
}
