package se.gotlib.bibbla.backend;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import se.gotlib.bibbla.util.Observable;

/**
 * Singleton that handles all the async tasks you would want to do for a user.
 * Such as logging in, adding accounts, changing accounts.
 * @author Master
 *
 */
public class User implements PropertyChangeListener, Observable {
	private PropertyChangeSupport pcs;

    //private GotlibCredentials gotlibCredentials; // Gotlib login credentials (name, card, pin)
    //private GotlibSession gotlibSession;         // Gotlib session (cookies etc)

	private String name;      // User's name
    private boolean loggedIn; // Logged in or not?

	public User() {
		pcs = new PropertyChangeSupport(this);
	}
	
	public void loginAsync(String name, String cardNumber, String pinNumber) {
/*
        // Replace credentials object
        gotlibCredentials = new GotlibCredentials(name, cardNumber, pinNumber);
        // Start login task
        LoginTask lt = new LoginTask();
        lt.addListener(this);
        lt.execute(gotlibCredentials);
*/
	}

    public void loginDone(/*Message<GotlibSession> message*/) {
/*
        // Save session (if any)
        if (message.error == null && message.obj != null) {
            gotlibSession = message.obj;
            // TODO save timeout, and  create Timer using it which re-launches login here
        }
        // Forward event to frontend
        pcs.firePropertyChange("loginDone", null, message);
*/
    }
	
	public boolean isLoggedIn() {
		return loggedIn;
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

    @Override
    /**
     * Receive events, e.g. from LoginTask
     */
    public void propertyChange(PropertyChangeEvent e) {
/*
        if ("loginDone".equals(e.getPropertyName())) {
            loginDone((Message<GotlibSession>)e.getNewValue());
        }
*/
    }

    /**
     * Returns the name of the currently logged in user
     */
    public String getName() {
        // TODO return full name, if fetched sometime
        /*return gotlibCredentials.getName();*/
        return null;
    }

}
