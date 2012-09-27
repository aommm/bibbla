package dat255.grupp06.bibbla.utils;

/** Represents a book or any other type of media.
 *  TODO: Needs many more properties and methods. Atm just for testing. **/ 
public class Book {
	private BookType type;
	private String name, author, isbn;
	
	@Override
	public String toString() {
		return "Book [" + name + ", " + author + "]";
	}
	
	/********************************
	 * Constructors
	 ********************************/	
	public Book(String name, String author, String isbn) {
		this.name = name;
		this.author = author;
		this.isbn = isbn;
	}
	public Book(String name, String author) {
		this.name = name;
		this.author = author;
		this.isbn = null;
	}
	public Book(String name) {
		this.name = name;
		this.author = null;
		this.isbn = null;
	}
	
	public Book() {
		this.name = "Titel";
		this.author = "Författare";
		this.isbn = "abc123";
	}

	/********************************
	 * Getters/setters
	 ********************************/
	
	public BookType getType() {
		return type;
	}

	public void setType(BookType type) {
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

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
}
