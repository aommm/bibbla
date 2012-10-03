package dat255.grupp06.bibbla.backend;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import dat255.grupp06.bibbla.backend.tasks.LoginJob;
import dat255.grupp06.bibbla.utils.Message;

public class Session {

	private String name, code, pin;
	private Map<String, String> cookies;
	
	public Session(String name, String code, String pin) {
		this.name = name;
		this.code = code;
		this.pin = pin;
		cookies = new HashMap<String, String>();
	}
	
	public Map<String, String> getCookies() {
		synchronized(cookies) {
			return cookies;
		}
	}
	
	public void setCookies(Map<String, String> cookies) {
		synchronized(cookies) {
			this.cookies = cookies;
		}
	}	
	
	public boolean login() {
		LoginJob job = new LoginJob(name, code, pin, this);
		Message message = new Message();
		message = job.run();
		return message.loggedIn;
	}

}