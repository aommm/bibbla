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
	private String phoneNr;
	private String allLibInfo;
//	private List<Library> allLibraries;
	
	/**
	 * Prints the book's name&author, and whether it has urls/details specified.
	 */
	@Override
	public String toString() {
		return "Library (" + name + ", " + address + ", " + phoneNr ;

	}
	
	/********************************
	 * Constructors
	 ********************************/	
//	/**
//	 * Creates a new book using the supplied information.
//	 */
//	public Library(String name, String author, String type, String url, String reserveUrl) {
//		this();
//		this.name = name;
//		this.author = author;
//		this.url = url;
//		this.reserveUrl = reserveUrl;
//	}
//
//	/**
//	 * Creates a new Book, and assigns to it a name and an author.
//	 * Should be used for debugging only, TODO remove later on.
//	 */
//	public Library(String name, String author) {
//		this();
//		this.name = name;
//		this.author = author;
//	}

	public Library() {
//		physicalBooks = new ArrayList<PhysicalBook>();
	}
	
//	@Override
//	public Object clone() {
//
//		Library newBook = new Library();
//		
//		newBook.name = name;
//		newBook.author = author;
//		newBook.url = url;
//		newBook.type = type;
//		newBook.reserveUrl = reserveUrl;
//		newBook.renewId = renewId;
//		newBook.publisher = publisher;
//		newBook.physicalDescription = physicalDescription;
//		newBook.notes = notes;
//		
//		List<PhysicalBook> newPhysicalBooks = new ArrayList<PhysicalBook>();
//		for(PhysicalBook pb : physicalBooks ) {
//			newPhysicalBooks.add((PhysicalBook)pb.clone());
//		}
//		newBook.physicalBooks = newPhysicalBooks;
//		
//		return newBook;
//	}

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

}
