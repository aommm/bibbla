package se.gotlib.bibbla.backend.singletons;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

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
    private String gotlibName;
    private String gotlibCode;
    private String gotlibPin;

    private String token;

    private Context ctx;

//    TODO: design ctx structure between singletons better
	public User(Context ctx) {
        this.ctx = ctx;
		pcs = new PropertyChangeSupport(this);
	}
	
	public void loginGotlibAsync(String gotlibName, String gotlibCode, String gotlibPin) {
        String url = Singletons.API_URL + "/login";
        final String event = "loginGotlibDone";
        Log.d("backend", event+" called");
        this.username   = gotlibName; // TODO do not set this when login system in place
        this.gotlibName = gotlibName;
        this.gotlibCode = gotlibCode;
        this.gotlibPin  = gotlibPin;

        JSONObject data = null;
        try {
            data = new JSONObject(String.format("{name: %s, code: %s, pin: %s}", gotlibName, gotlibCode, gotlibPin));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("backend", event+" error, input");
            pcs.firePropertyChange(event, null, Error.INPUT);
            return;
        }
        JsonObjectRequest r = new JsonObjectRequest
            (Request.Method.POST, url, data, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("backend", event+" done"+User.this.username+" , "+User.this.gotlibName);
                    pcs.firePropertyChange(event, null, null);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error == null || error.networkResponse == null) {
                        Log.d("backend", event+" error");
                        pcs.firePropertyChange(event, null, Error.NETWORK);
                        return;
                    }
                    Log.d("backend", event+" error, " + error.networkResponse.statusCode);
                    switch (error.networkResponse.statusCode) {
                        case 401:
                            pcs.firePropertyChange(event, null, Error.INCORRECT_BIBBLA_CREDENTIALS);
                            break;
                        default:
                            pcs.firePropertyChange(event, null, Error.NETWORK);
                            break;
                    }
                }
            });
        r.setRetryPolicy(new DefaultRetryPolicy(5000, 0, 1));
        Singletons.getInstance(ctx).getRequestQueue().add(r);
	}


    public void loginAsync(String username, String password) {
        Log.d("backend", "loginAsync called");

        String url = Singletons.API_URL + "/me/login";
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
                            Log.d("backend", "loginAsync done"+User.this.username+" , "+User.this.gotlibName);
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
                        if (error == null || error.networkResponse == null) {
                            Log.d("backend", "loginAsync error");
                            pcs.firePropertyChange("loginDone", null, Error.NETWORK);
                            return;
                        }
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
        this.gotlibName = user.getString("gotlib_surname");
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

    public String getGotlibName() {
        return gotlibName;
    }

    public String getGotlibCode() {
        return gotlibCode;
    }

    public String getGotlibPin() {
        return gotlibPin;
    }
}
