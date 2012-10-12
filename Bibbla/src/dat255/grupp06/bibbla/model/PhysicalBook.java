package dat255.grupp06.bibbla.model;

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
	private boolean error;
	
	/**
	 * Create a PhysicalBook using the supplied information.
	 */
	public PhysicalBook(String library, String shelf, String status, String message) {
		this.library = library;
		this.shelf = shelf;
		this.status = status;
		this.message = message;
	}
	/**
	 * Create an empty PhysicalBook.
	 */
	public PhysicalBook() {
		
	}

	@Override
	public Object clone() {
		
		PhysicalBook pb = new PhysicalBook();
		pb.library = library;
		pb.shelf = shelf;
		pb.status = status;
		pb.message = message;
		return pb;
	}
	
	@Override
	public String toString() {
		return "PhysicalBook [library=" + library + ", shelf=" + shelf
				+ ", status=" + status + ", error=" + error + ", message=" + message + ")";
	}
	
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	}
	
	public String getShelf() {
		return shelf;
	}
	public void setShelf(String shelf) {
		this.shelf = shelf;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean getError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
}