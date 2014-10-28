package se.gotlib.bibbla.backend.singletons;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import se.gotlib.bibbla.util.Observable;

/**
 * Singleton that handles all the async tasks you would want to do for a user.
 * Such as logging in, adding accounts, changing accounts.
 * @author Master
 *
 */
public class User implements Observable {
	private PropertyChangeSupport pcs;
	
	private String name, cardNumber, pinNumber;
	
	private boolean loggedIn;
	
	public User() {
		pcs = new PropertyChangeSupport(this);
	}
	
	public void loginAsync(String name, String cardNumber, String pinNumber) {
        // TODO start LoginTask here
        /*
		if(this.cardNumber.equals(cardNumber) && this.pinNumber.equals(pinNumber)) {
			pcs.firePropertyChange("loginSucceeded", true, false);
		} else {
			pcs.firePropertyChange("loginFailed", true, false);
		}
		*/
	}
	
	public boolean isLoggedIn() {
		return loggedIn;
	}

    /**
     * @deprecated what do we use this for?
     */
	public void addAccountAsync(String cardNumber, String pinNumber) {
		this.cardNumber = cardNumber;
		this.pinNumber = pinNumber;
		
		pcs.firePropertyChange("accountAdded", true, false);
	}
	
	public void changeAccountAsync(String newPin) {
		pinNumber = newPin;
		
		pcs.firePropertyChange("accountChanged", true, false);
	}
	
	public void addListener(PropertyChangeListener pcl) {
		pcs.addPropertyChangeListener(pcl);
	}
}
