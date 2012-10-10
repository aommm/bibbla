package dat255.grupp06.bibbla.utils;

public class Message {
	public Object obj;
	public boolean loggedIn;
	public Error error;
	public int resultsNumber;
	
	
	public Message() {
		obj = null;
		loggedIn = false;
		error = null;
		resultsNumber = 0;
	}
	
	@Override
	public String toString() {
		if (error == null) {
			return "Success! loggedIn: "+loggedIn+", obj: "+obj+".";	
		}
		else {
			return "Error: "+error+", loggedIn: "+loggedIn+", obj: "+obj+".";
		}
		
	}
}
