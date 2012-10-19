package dat255.grupp06.bibbla.backend;

import java.io.Serializable;

import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.model.CredentialsMissingException;

/** A class for storing the user's settings.
 *
 *  TODO: Save to file system.
 */
public class Settings implements Serializable {

	private static final long serialVersionUID = 6558963742577293894L;
	private String name, code, pin;

	public Settings() {}
	
	/********************************
	 * Getters/setters
	 ********************************/
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		// save to file system.
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
		// save to file system.		
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
		// save to file system.		
	}
	
	public Credentials getCredentials() throws CredentialsMissingException {
		return new Credentials(name, code, pin);
	}
	
}
