package se.gotlib.bibbla.backend.model;

public class Book {
	private String title;
	private String isbn;
	private String author;
    private String desc;

    public Book(String title, String isbn, String author) {
        this(title, isbn, author, "");
    }

	public Book(String title, String isbn, String author, String desc) {
		this.title = title;
		this.isbn = isbn;
		this.author = author;
        this.desc = desc;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public String getAuthor() {
		return author;
	}

    public String getDesc() {
        return desc;
    }
}
