package dat255.grupp06.bibbla.backend;

/** A class for storing the user's settings.
 *
 *  TODO: Save to file system.
 */
public class Settings {

	private String name, code, pin;

	public Settings(String name, String code, String pin) {
		this.name = name;
		this.code = code;
		this.pin = pin;
	}
	
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
	
}
