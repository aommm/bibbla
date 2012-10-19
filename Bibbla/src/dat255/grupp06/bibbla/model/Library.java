package dat255.grupp06.bibbla.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a library.
 *  @author Madeleine Appert
 */ 
public class Library {
	
	// TODO Make immutable variables final
	// Immutable variables
	private String name;
	private String address;
	private String postCode;
	private String area;
	private String phoneNr;
	private String visAdr;
	private String email;
	private String openH;
//	private List<Library> allLibraries;
	
	/**
	 * Prints the book's name&author, and whether it has urls/details specified.
	 */
	@Override
	public String toString() {
		return "\n (" + name + ", \n" + address + ", \n" + postCode +", \n" + area +", \n" + phoneNr +", \n" + email +", \n" + openH +")";

	}
	
	/********************************
	 * Constructors
	 ********************************/	


	public Library() {

	}
	public Library(String name, String address, String postcode, 
			String area, String phoneNr, String visAdr, String email, String openH) {
		setName(name);
		setAddress(address);
		setPostCode(postcode);
		setArea(area);
		setPhoneNr(phoneNr);
		setVisAdr(visAdr);
		setEmail(email);
		setOpenH(openH);
	}
	


	/********************************
	 * Getters/setters
	 ********************************/
	
	public String getPhoneNr() {
		return phoneNr;
	}
	public void setPhoneNr(String phoneNr) {
		this.phoneNr = phoneNr;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getVisAdr() {
		return visAdr;
	}
	public void setVisAdr(String visAdr) {
		this.visAdr = visAdr;
	}
	public String getOpenH() {
		return openH;
	}
	public void setOpenH(String openH) {
		this.openH = openH;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email=email;
		
	}
}
