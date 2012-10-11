package dat255.grupp06.bibbla.model;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents a book or any other type of media.
 *  TODO: Needs many more properties and methods. Atm just for testing.
 *  @author Niklas Logren
 */ 
public class Book {
	
	// TODO Make immutable variables final
	// Immutable variables
	private String name;
	private String author;
	private String url;
	private int available;
	
	// Mutable variables
	private String type;
	//private BookType type; // TODO Do we even want enum for this?
	private String reserveUrl;
	private String renewId;
	// Details
	private String publisher; 
	private String physicalDescription;
	private String notes;
	private List<PhysicalBook> copies;
	
	/**
	 * Prints the book's name&author, and whether it has urls/details specified.
	 */
	@Override
	public String toString() {
		return "Book [" + name + ", " + author + ", urls: " +
			((url!=null)&&(reserveUrl!=null)) +", details: " + ((publisher!=null) 
			|| (physicalDescription!=null) || (notes!=null) || (copies!=null)) +"]";
	}
	
	/********************************
	 * Constructors
	 ********************************/	
	/**
	 * Creates a new book using the supplied information.
	 */
	public Book(String name, String author, String type, String url, String reserveUrl) {
		this();
		this.name = name;
		this.author = author;
		this.url = url;
		this.reserveUrl = reserveUrl;
	}

	/**
	 * Creates a new Book, and assigns to it a name and an author.
	 * Should be used for debugging only, TODO remove later on.
	 */
	public Book(String name, String author) {
		this();
		this.name = name;
		this.author = author;
	}

	public Book() {
		copies = new ArrayList<PhysicalBook>();
	}
	
	@Override
	public Object clone() {

		Book newBook = new Book();
		
		newBook.name = name;
		newBook.author = author;
		newBook.url = url;
		newBook.type = type;
		newBook.reserveUrl = reserveUrl;
		newBook.renewId = renewId;
		newBook.publisher = publisher;
		newBook.physicalDescription = physicalDescription;
		newBook.notes = notes;
		
		List<PhysicalBook> newCopies = new ArrayList<PhysicalBook>();
		for(PhysicalBook pb : copies) {
			newCopies.add((PhysicalBook)pb.clone());
		}
		newBook.copies = newCopies;
		
		return newBook;
	}

	/********************************
	 * Getters/setters
	 ********************************/
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getReserveUrl() {
		return reserveUrl;
	}
	public void setReserveUrl(String reserveUrl) {
		this.reserveUrl = reserveUrl;
	}
	
	/**
	 * Returns the renew ID for this book.
	 * Is used to renew it if possible, from within RenewJob.
	 */
	public String getRenewId() {
		return renewId;
	}
	/**
	 * Sets the renew id for this book.
	 * (Renew ID is only available from "loaned books")
	 */
	public void setRenewId(String renewId) {
		this.renewId = renewId;
	}	
	
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getPhysicalDescription() {
		return physicalDescription;
	}
	public void setPhysicalDescription(String physicalDescription) {
		this.physicalDescription = physicalDescription;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<PhysicalBook> getCopies() {
		return copies;
	}
	public void setCopies(List<PhysicalBook> copies) {
		this.copies = copies;
	}
	
	/**
	 * Sets the supplied PhysicalBook to be the only copy of this book.
	 */
	public void setPhysicalBook(PhysicalBook physicalBook) {
		List<PhysicalBook> copies = new ArrayList<PhysicalBook>();
		copies.add(physicalBook);
		this.copies = copies;
	}
	/**
	 * Returns the first PhysicalBook tied to this book. If none, returns null.
	 */
	public PhysicalBook getPhysicalBook() {
		if (copies.size()>0) {
			return copies.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Set how many copies of this book are available to loan in any library. 
	 * @param Number of available copies.
	 * @author arla
	 */
	public void setAvailable(int available) {
		this.available = available;
	}
	
	/**
	 * Number of copies of this book that are available to loan in any library.
	 * @author arla
	 */
	public int getAvailable() {
		return available;
	}
}
