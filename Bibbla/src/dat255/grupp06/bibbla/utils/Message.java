package dat255.grupp06.bibbla.utils;

public class Message {
	public Object obj;
	public boolean loggedIn;
	public Error error;
	
	
	public Message() {
		obj = null;
		loggedIn = false;
		error = null;
	}
	
	@Override
	public String toString() {
		return "error: "+error+", loggedIn: "+loggedIn+", obj: "+obj+".";
	}
}
