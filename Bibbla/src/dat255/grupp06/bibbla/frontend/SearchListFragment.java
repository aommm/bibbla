package dat255.grupp06.bibbla.frontend;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import dat255.grupp06.bibbla.MainActivity;
import dat255.grupp06.bibbla.model.Book;

/**
 * ListFragment that is used to display the search-results after a search.
 * 
 * @author Jonathan Orrö
 *
 */
public class SearchListFragment extends SherlockListFragment {
	
	private List<Book> currentBooks;
	private int currentPage;
	private String lastSearch;
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        currentBooks = new ArrayList<Book>();
        currentPage = 0;
    }

    @Override
    /**
     * When an item is clicked in the list this method is called
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
    	if(position < currentBooks.size()) {
	    	Intent intent = new Intent(getSherlockActivity(), BookOverlayActivity.class);
	    	startActivity(intent);
    	} else {
    		currentPage++;
    		((MainActivity) getActivity()).moreSearchResultsClicked(currentPage+1, lastSearch);
    	}
    }
    
    /**
     * Receives the search-results and swaps the contents in the list with them.
     */
    public void updateList(List<Book> books) {
    	//Adds a null-book to act as a placeholder for the 
    	//"fetch more search results"-message
    	currentBooks.clear();
    	currentBooks.addAll(books);
    	books.add(null);
    	ListAdapter adapter = new SearchResultAdapter(getSherlockActivity(), books);
    	this.setListAdapter(adapter);
    }
    
    public void appendList(List<Book> books) {
    	Log.d("J", "hej");
    	currentBooks.addAll(books);
    	ListAdapter adapter = new SearchResultAdapter(getSherlockActivity(), books);
    	this.setListAdapter(adapter);
    }
    
    public void setLastSearchString(String s) {
    	lastSearch = s;
    }
}

