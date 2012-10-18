package dat255.grupp06.bibbla.model;

import java.io.Serializable;

public final class Credentials implements Serializable {
	private static final long serialVersionUID = -5535338463939136870L;

	public final String name, card, pin;
	
	public Credentials(String name, String card, String pin) {
		this.name = name;
		this.card = card;
		this.pin = pin;
	}
	
	// TODO Implement clone, equals, etc.
}
