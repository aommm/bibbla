package se.gotlib.bibbla.backend.singletons;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

import se.gotlib.bibbla.backend.model.Book;

/**
 * Singleton that handles all the tasks you would want to do with the library.
 * Such as searching for books, reserving books etc..
 * 
 * All methods that end with "Async" does some kind of asynchronous call to a job.
 * @author Master
 *
 */
public class Library implements PropertyChangeListener, Observer{
	private PropertyChangeSupport pcs;
	
	/*
	 * Dummy-variabler för prototypen
	 */
	private ArrayList<Book> books;
	private ArrayList<Book> loanedBooks;
	private ArrayList<Book> reservedBooks;
	
	public Library() {
		books = new ArrayList<Book>();
		loanedBooks = new ArrayList<Book>();
		reservedBooks = new ArrayList<Book>();

        initBooks();

		pcs = new PropertyChangeSupport(this);
	}

    private void initBooks() {
        books.add(new Book("Hej då- kalas på Miinibiblioteket", "1", ""));
        books.add(new Book("Hej trottoar!", "2",""));
        books.add(new Book("Hej klimakteriet - lite vallningar har väl ingen dött av, texter om livet kring 50 [Elektronisk resurs]", "3","Albinsson, Åsa", "\"Hjälp, jag har kommit i klimakteriet!\" Försvinner min kvinnlighet nu? Får jag pinsamma vallningar? Tappar sexlusten? Blir sur och grinig? Kvinnors klimakterium omges fortfarande av fördomar och motstridiga känslor. Men det är hög tid att slå hål på myten om \"surkärringen\". Betrakta klimakteriet som början på något nytt! I Hej klimakteriet gör journalisterna Åsa Albinsson och Maria Fröjdh upp med många föreställningar om kvinnors klimakterium. Den senaste forskningen varvas med personliga erfarenheter. Det är dags att släppa fram nya röster om livet runt 50! Flera kvinnor i sin bästa ålder, som Sissela Kyle, Nedjma Chaouche Liljedahl, Emma Hamberg, Karin Björkegren Jones, Nina Lekander, Mian Lodalen, Pia Sundhage och Camilla Thulin bidrar också med roliga, tankeväckande och inspirerande berättelser om livet runt menopausen. Sant är att mensen upphör, att en del kvinnor svettas mycket, får svårt att sova, blir deppiga. Och att möjligheten att bli gravid försvinner, något som både kan kännas som en sorg och en lättnad. Men långt ifrån alla kvinnor får medicinska besvär. För många blir klimakteriet en frihetstid. Bort med PMS och alla hopplösa mensskydd. Klimakteriet är en övergång, inte en slutpunkt! Hej klimakteriet! [Elib]"));
        books.add(new Book("Hej igen! : samlade sagor / av Ulf Nilsson, Eva Eriksson", "4","Nilsson, Ulf, 1948-"));
        books.add(new Book("Hej Flugo!", "5","Arnold, Tedd"));
        books.add(new Book("Hej! : kreativa hem / av Moa Samuelsson & Christina Breeze ; foto: Alexander Lagergren & Bobo Olsson", "6","Samuelsson, Moa, 1975-"));
        books.add(new Book("Hej och hå kläder på / Stina Kjellgren ; Lotta Geffenblad", "7","Kjellgren, Stina, 1973-"));
        books.add(new Book("\"Hej och kram, Signe\" [Ljudupptagning] : en berättelse / Anne Marie Bjerg ; översättning av Sven Lindner.", "8","Bjerg, Anne Marie."));
        books.add(new Book("Hej mage! [Ljudupptagning] : [för dig som vill må bättre i magen] / författare: Maria Jernsdotter Björklund.", "9","Jernsdotter Björklund, Maria."));
        books.add(new Book("Hej då nappen / text av Mercè Seix och Meritxell Noguera ; illustrationer av Rocio Bonilla ; [översättning: Anna Henriksson]", "10","Seix, Mercè"));
        books.add(new Book("Hej litteraturen! : en resa genom litteraturhistorien / Pia Cederholm ; [faktagranskning: Anna Nordlund]", "11","Cederholm, Pia, 1970-"));

        reservedBooks.add(books.get(0));
        reservedBooks.add(books.get(4));
        reservedBooks.add(books.get(2));
        reservedBooks.add(books.get(5));
        reservedBooks.add(books.get(3));
    }

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals("loginSucceeded")) {
			
		} else if(event.getPropertyName().equals("loginFailed")) {
			
		}
	}
	
	public void getReservationsAsync() {
		pcs.firePropertyChange("getReservations", null, reservedBooks);
	}
	
	public void getLoansAsync() {
		pcs.firePropertyChange("getLoans", null, loanedBooks);
	}
	
	public void getUserInfoAsync() {
		pcs.firePropertyChange("getUserInfo", null, "user");
	}
	
	public void reserveBookAsync(String ISBN) {
		boolean reserved = false;
		for(Book b : books) {
			if(b.getIsbn().equals(ISBN)) {
				reserved = true;
				reservedBooks.add(b);
			}
		}
		
		// The last of the two values are the one we use when receiving the event.
		// They need to be opposite so that  the property change is fired.
		pcs.firePropertyChange("reserveBook", !reserved, reserved);
	}
	
	public void loanBookAsync(String ISBN) {
		boolean loaned = false;
		for(Book b : books) {
			if(b.getIsbn().equals(ISBN)) {
				loaned = true;
				loanedBooks.add(b);
			}
		}
		
		// The last of the two values are the one we use when receiving the event.
		// They need to be opposite so that the property change is fired.
		pcs.firePropertyChange("loanBook", !loaned, loaned);
	}
	
	public void searchAsync(String s) {
		ArrayList<Book> searchResult = new ArrayList<Book>();
		for(Book b : books) {
			if(b.getAuthor().contains(s) || b.getIsbn().contains(s) || b.getTitle().contains(s)) {
				searchResult.add(b);
			}
		}
		pcs.firePropertyChange("searchDone", null, searchResult);
	}

    public void getBookAsync(String isbn) {
        for(Book b : books) {
            if(b.getIsbn().equals(isbn)) {
                pcs.firePropertyChange("getBook", null, b);
            }
        }
    }

    public void addListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
}
