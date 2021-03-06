/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orr�.
    
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a book or any other type of media.
 *  TODO: Needs many more properties and methods. Atm just for testing.
 *  @author Niklas Logren
 */ 
public class Book implements Serializable {
	
	private String name;
	private String author;
	private int available;
	private String type;
	
	private String url;
	private String reserveUrl;
	
	// Id's, used for renewing and unreserving.
	private String renewId;
	private String unreserveId;
	private String freezeId;
	
	// Details
	private String publisher; 
	private String physicalDescription;
	private String notes;
	private String isbn;
	private List<PhysicalBook> physicalBooks;
	
	@Override
	/**
	 * Returns the hashcode of this book.
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	/**
	 * Compares this book to the supplied one.
	 * Compares author, name and type.
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	/**
	 * Prints the book's name&author, and whether it has urls/details specified.
	 */
	@Override
	public String toString() {
		String physicalBook = ((physicalBooks.size()>0) && ((physicalBooks.get(0) != null))
				? physicalBooks.get(0).toString() : "no");
		
		String details = "publisher: "+((publisher!=null)?publisher:"no")+
				", physicalDescription: "+((physicalDescription!=null)?physicalDescription:"no")+
				", notes: "+((notes!=null)?notes:"no")+
				", isbn: "+((isbn!=null)?isbn:"no");
		
		return "Book (" + name + ", " + author + ", urls: " +
			((url!=null)&&(reserveUrl!=null)) +", details: ["+details+
			"], physicalBook: "+physicalBook+")\n";
	}
	
	/********************************
	 * Constructors
	 ********************************/	
	/**
	 * Creates a new book using the supplied information.
	 * @param name - The name of the book.
	 * @param author - The author of the book.
     * @param type - The type of the book (e-book, audio-book, daisy, etc)
	 * @param url - The url to the book's detailed view.
	 * @param reserveUrl - The url used to reserve the book.
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

	/**
	 * Creates a new, empty book.
	 */
	public Book() {
		physicalBooks = new ArrayList<PhysicalBook>();
	}
	
	@Override
	/**
	 * @returns a copy of this book.
	 * Performs deep copying of physicalBooks.
	 */
	public Object clone() {

		Book newBook = new Book();
		
		newBook.name = name;
		newBook.author = author;
		newBook.url = url;
		newBook.type = type;
		newBook.reserveUrl = reserveUrl;
		newBook.renewId = renewId;
		newBook.unreserveId = unreserveId;
		newBook.freezeId = freezeId;
		newBook.publisher = publisher;
		newBook.physicalDescription = physicalDescription;
		newBook.notes = notes;
		newBook.isbn = isbn;
		
		List<PhysicalBook> newPhysicalBooks = new ArrayList<PhysicalBook>();
		for(PhysicalBook pb : physicalBooks ) {
			newPhysicalBooks.add((PhysicalBook)pb.clone());
		}
		newBook.physicalBooks = newPhysicalBooks;
		
		return newBook;
	}

	/********************************
	 * Getters/setters
	 ********************************/
	
	/**
	 * Returns the type of the book (e-book, audio-book, daisy, etc).
	 */
	public String getType() {
		return type;
	}
	/**
	 * Sets the type of the book.
	 * @param type - the type of the book (e-book, audio-book, daisy, etc). 
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Returns the name of the book.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Sets the name of the book.
	 * @param name - the book's name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the author of the book.
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * Sets the author of the book.
	 * @param author - the book's author.
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * Returns the URL to the book's detailed view.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * Sets the URL of the book. 
	 * @param url -  the URL to the book's detailed view.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	
	/**
	 * Returns the URL used to reserve the book.
	 */
	public String getReserveUrl() {
		return reserveUrl;
	}
	/**
	 * Set the reservation URL.
	 * @param reserveUrl - the URL used to reserve the book.
	 */
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
	
	/**
	 * Sets the unreserve id for this book.
	 */
	public void setUnreserveId(String unreserveId) {
		this.unreserveId = unreserveId;
	}
	/**
	 * Returns the unreserve ID for this book.
	 * Is used to unreserve it if possible, from within UnreserveJob.
	 */
	public String getUnreserveId() {
		return unreserveId;
	}
	
	/**
	 * Sets the freeze id for this book.
	 */
	public void setFreezeId(String freezeId) {
		this.freezeId = freezeId;
	}
	/**
	 * Returns the freeze ID for this book.
	 * Can be used to freeze its reservation if possible; used from within UnreserveJob.
	 */
	public String getFreezeId() {
		return freezeId;
	}
	
	/**
	 * Returns the publisher of the book.
	 */
	public String getPublisher() {
		return publisher;
	}
	/**
	 * Sets the publisher of this book.
	 * @param publisher - the book's publisher.
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * Returns the physical description of this book -
	 * How large it is, how many pages etc.
	 */
	public String getPhysicalDescription() {
		return physicalDescription;
	}
	/**
	 * Sets the physical description of this book.
	 * @param physicalDescription - Describes how large the book is, how many pages etc.
	 */
	public void setPhysicalDescription(String physicalDescription) {
		this.physicalDescription = physicalDescription;
	}

	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * Returns a list of all physical books tied to this book.
	 * @returns empty list if none present. 
	 */
	public List<PhysicalBook> getPhysicalBooks() {
		return physicalBooks;
	}
	/**
	 * Sets a list of all physical books tied to this book.
	 * @param physicalBooks - a list of the book's physicalBooks.
	 */	
	public void setPhysicalBooks(List<PhysicalBook> physicalBooks) {
		this.physicalBooks = physicalBooks;
	}
	
	/**
	 * Sets the supplied PhysicalBook to be the only copy of this book.
	 */
	public void setPhysicalBook(PhysicalBook physicalBook) {
		List<PhysicalBook> physicalBooks = new ArrayList<PhysicalBook>();
		physicalBooks.add(physicalBook);
		this.physicalBooks = physicalBooks;
	}
	/**
	 * Returns the first PhysicalBook tied to this book. If none, returns null.
	 */
	public PhysicalBook getPhysicalBook() {
		if (physicalBooks.size()>0) {
			return physicalBooks.get(0);
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
	
	/**
	 * Returns the message of our first PhysicalBook. If no physicalBooks, returns null.
	 */
	public String getMessage() {
		if (physicalBooks.size()>0) {
			return physicalBooks.get(0).getMessage();
		} else {
			return null;
		}
	}
	/**
	 * Returns the error status of our first PhysicalBook. If no physicalBooks, returns false.
	 */
	public boolean getError() {
		if (physicalBooks.size()>0) {
			return physicalBooks.get(0).getError();
		} else {
			return false;
		}
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	/**
	 * Returns the library of our first PhysicalBook. If no physicalBooks, returns false.
	 */
	public String getLibrary() {
		if (physicalBooks.size()>0) {
			return physicalBooks.get(0).getLibrary();
		} else {
			return null;
		}
	}
}