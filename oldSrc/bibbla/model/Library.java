/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.
    
    This file is part of Bibbla.

    Bibbla is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bibbla is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bibbla.  If not, see <http://www.gnu.org/licenses/>.    
 **/

package dat255.grupp06.bibbla.model;

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
