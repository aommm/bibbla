package se.gotlib.bibbla.backend.singletons;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Iterator;

import se.gotlib.bibbla.backend.model.Book;
import se.gotlib.bibbla.backend.tasks.TestTask;
import se.gotlib.bibbla.util.Observable;
import se.gotlib.bibbla.util.Error;

/**
 * Singleton that handles all the tasks you would want to do with the library.
 * Such as searching for books, reserving books etc..
 * 
 * All methods that end with "Async" does some kind of asynchronous call to a job.
 * @author Master
 *
 */
public class Library implements PropertyChangeListener, Observable {
	private PropertyChangeSupport pcs;
	
	/*
	 * Dummy-variabler för prototypen
	 */
	private ArrayList<Book> books;
	private ArrayList<Book> loanedBooks;
	private ArrayList<Book> reservedBooks;

	private Context ctx;

	public Library(Context ctx) {
		books = new ArrayList<Book>();
		loanedBooks = new ArrayList<Book>();
		reservedBooks = new ArrayList<Book>();

        initBooks();

		this.ctx = ctx;

		pcs = new PropertyChangeSupport(this);
	}

    private void initBooks() {
        books.add(new Book("Hej då- kalas på Miinibiblioteket", "", ""));
        books.add(new Book("Hej trottoar!", "",""));
        books.add(new Book("Hej klimakteriet - lite vallningar har väl ingen dött av, texter om livet kring 50 [Elektronisk resurs]", "","Albinsson, Åsa"));
        books.add(new Book("Hej igen! : samlade sagor / av Ulf Nilsson, Eva Eriksson", "","Nilsson, Ulf, 1948-"));
        books.add(new Book("Hej Flugo!", "","Arnold, Tedd"));
        books.add(new Book("Hej! : kreativa hem / av Moa Samuelsson & Christina Breeze ; foto: Alexander Lagergren & Bobo Olsson", "","Samuelsson, Moa, 1975-"));
        books.add(new Book("Hej och hå kläder på / Stina Kjellgren ; Lotta Geffenblad", "","Kjellgren, Stina, 1973-"));
        books.add(new Book("\"Hej och kram, Signe\" [Ljudupptagning] : en berättelse / Anne Marie Bjerg ; översättning av Sven Lindner.", "","Bjerg, Anne Marie."));
        books.add(new Book("Hej mage! [Ljudupptagning] : [för dig som vill må bättre i magen] / författare: Maria Jernsdotter Björklund.", "","Jernsdotter Björklund, Maria."));
        books.add(new Book("Hej då nappen / text av Mercè Seix och Meritxell Noguera ; illustrationer av Rocio Bonilla ; [översättning: Anna Henriksson]", "","Seix, Mercè"));
        books.add(new Book("Hej litteraturen! : en resa genom litteraturhistorien / Pia Cederholm ; [faktagranskning: Anna Nordlund]", "","Cederholm, Pia, 1970-"));

        reservedBooks.add(books.get(0));
        reservedBooks.add(books.get(4));
        reservedBooks.add(books.get(2));
        reservedBooks.add(books.get(5));
        reservedBooks.add(books.get(3));
    }

	@Override
	public void propertyChange(PropertyChangeEvent event) {
        String eventName = event.getPropertyName();
		if(eventName.equals("loginSucceeded")) {
			
		} else if(eventName.equals("loginFailed")) {

        } else if(eventName.equals("testJsoupDone")) {
            // Simply forward event to frontend
            Log.d("bibbla", "Library: testJsoupDone, result: "+event.getNewValue());
            pcs.firePropertyChange(event);

        }
    }
	
	public void getReservationsAsync() {
		String url = Singletons.API_URL +"/reservations";
		final String event = "getReservations";

		JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				Log.d("backend", event+": got "+response.length()+" books");
				try {
					getReservationsAsyncDone(response);
					pcs.firePropertyChange("getReservationsDone", null, reservedBooks);
				} catch(JSONException e) {
					Log.d("backend", event+" Exception in json");
					pcs.firePropertyChange("getReservationsDone", null, Error.PARSE);
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("backend", "errir :"+error.getLocalizedMessage());
				Log.d("backend", "errir :"+error.networkResponse);
				Log.d("backend", "errir :"+error.toString());
				Log.d("backend", "errir :"+ error.getNetworkTimeMs());
			}
		});

		r.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1));

		Log.d("backend", "just reservations bb "+ url);
		Singletons.getInstance(ctx).getRequestQueue().add(r);
	}

	public void getReservationsAsyncDone(JSONArray response) throws JSONException {
		reservedBooks = new ArrayList<>();
		for(int i=0; i<response.length(); i++) {
			// TODO: don't create Book
			JSONObject book = response.getJSONObject(i);
			Book b = new Book(book.getString("name"), "meh", "meh 2");
			reservedBooks.add(b);
		}
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
		String url = Singletons.API_URL +"/search/"+s;

		JsonArrayRequest r = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
			@Override
			public void onResponse(JSONArray response) {
				try {
					Log.d("jonis", "Got "+response.length()+" books!");
					ArrayList<Book> searchResult = new ArrayList<Book>();

					for(int i=0; i<response.length(); i++) {
						JSONObject book = response.getJSONObject(i);
						searchResult.add(new Book(book.getString("title"), "meh", book.getString("author")));
					}

					pcs.firePropertyChange("searchDone", null, searchResult);
				} catch(JSONException e) {
					Log.d("jonis", "Exception in json");
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.d("jonis", "errir :"+error.getLocalizedMessage());
				Log.d("jonis", "errir :"+error.networkResponse);
				Log.d("jonis", "errir :"+error.toString());
				Log.d("jonis", "errir :"+ error.getNetworkTimeMs());
			}
		});

		r.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1));

		Log.d("jonis", "just searched bb "+ url);
		Singletons.getInstance(ctx).getRequestQueue().add(r);
	}

    /**
     * Test method.
     * Runs async task which does some JSoupy stuff
     */
    public void testJsoupAsync() {
        TestTask tt = new TestTask();
        tt.addListener(this);
        tt.execute();
    }


    public void addListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
}
