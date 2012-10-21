package dat255.grupp06.bibbla.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import dat255.grupp06.bibbla.frontend.BookOverlayActivity;
import dat255.grupp06.bibbla.model.Book;

public class BookListFragment extends SherlockListFragment {
	public final static String BOOK = "dat255.grupp06.bibbla.BOOK";
	public final static String RESERVED = "dat255.grupp06.bibbla.RESERVED";
	public final static String LOANED = "dat255.grupp06.bibbla.LOANED";
	private ArrayList<Book> currentBooks;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentBooks = new ArrayList<Book>();
    }

    @Override
    /**
     * When an item is clicked in the list this method is called
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
    	
    	Intent intent = new Intent(getSherlockActivity(), BookOverlayActivity.class);
    	
    	Log.d("Jonis", currentBooks.get(position).getUnreserveId()+"");
    	Book book = (Book) currentBooks.get(position).clone();
    	
    	intent.putExtra(BOOK, book);
    	intent.putExtra(RESERVED, true);
    	intent.putExtra(LOANED, false);
    	
    	startActivity(intent);
    }
    
    /**
     * Receives the search-results and swaps the contents in the list with them.
     */
    public void updateList(List<Book> books) {
    	//Adds a book to act as a placeholder for the 
    	//"fetch more search results"-message
    	currentBooks.clear();
    	currentBooks.addAll(books);

    	ListAdapter adapter = new BookListAdapter(getSherlockActivity(), books, false);

    	this.setListAdapter(adapter);
    }
}
