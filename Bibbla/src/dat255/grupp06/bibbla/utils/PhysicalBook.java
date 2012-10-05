package dat255.grupp06.bibbla.utils;

/**
 * A class modeling a physical copy of a book.
 * (Each Book has several PhysicalBooks)
 * @author Niklas Logren
 */
public class PhysicalBook {
	// TODO: Represent library with a separate class? Has name and specific code.
	private String library;
	// TODO: Represent shelf with an enum? Necessary?
	private String shelf;
	// TODO: Represent status with a separate class?
	// (Idea: Can parse status and possible return date.)  
	private String status;
	private String message;
	
	public PhysicalBook(String library, String shelf, String status, String message) {
		this.library = library;
		this.shelf = shelf;
		this.status = status;
		this.message = message;
	}

	@Override
	public String toString() {
		return "PhysicalBook [library=" + library + ", shelf=" + shelf
				+ ", status=" + status + ", message=" + message + "]";
	}
	
	public String getLibrary() {
		return library;
	}
	public String getShelf() {
		return shelf;
	}
	public String getStatus() {
		return status;
	}
	public String getMessage() {
		return message;
	}
	
}