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

/**
 * A listFragment used to hold books in the profile tab.
 * @author Jonathan Orrö
 */
public class BookListFragment extends SherlockListFragment {
	public final static String BOOK = "dat255.grupp06.bibbla.BOOK";
	public final static String RESERVED = "dat255.grupp06.bibbla.RESERVED";
	public final static String LOANED = "dat255.grupp06.bibbla.LOANED";
	private ArrayList<Book> currentBooks;
	private boolean isReservedList;
	private boolean isLoanedList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentBooks = new ArrayList<Book>();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	
    	Intent intent = new Intent(getSherlockActivity(), BookOverlayActivity.class);
    	
    	Log.d("Jonis", "url :"+currentBooks.get(position).getUrl());
    	
    	intent.putExtra(BOOK, currentBooks.get(position));
    	intent.putExtra(RESERVED, isReservedList);
    	intent.putExtra(LOANED, isLoanedList);
    	
    	startActivity(intent);
    }
    
    /**
     * Receives the search-results and swaps the contents in the list with them.
     * @param books - list of books to replace the current list with.
     */
    public void updateList(List<Book> books) {
    	//Adds a book to act as a placeholder for the 
    	//"fetch more search results"-message
    	currentBooks.clear();
    	currentBooks.addAll(books);

    	ListAdapter adapter = new BookListAdapter(getSherlockActivity(), books, false);

    	this.setListAdapter(adapter);
    }
    
    /**
     * Sets if the list is a list of reserved books or not
     * @param reserved  - true or false
     */
    public void setReservedListStatus(boolean reserved) {
    	isReservedList = reserved;
    }
    
    /**
     * Sets if the list is a list of loaned books or not
     * @param loaned - true or false
     */
    public void setLoanedListStatus(boolean loaned) {
    	isLoanedList = loaned;
    }
}
