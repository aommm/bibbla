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
		String o = (obj!=null)?obj.toString():"null";
		if (error == null) {
			return "Success! loggedIn: "+loggedIn+", obj: "+o+".";	
		}
		else {
			return "Error: "+error+", loggedIn: "+loggedIn+", obj: "+o+".";
		}
		
	}
}
