package dat255.grupp06.bibbla.fragments;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import dat255.grupp06.bibbla.model.Book;

/**
 * ListFragment that is used to display the search-results after a search.
 * 
 * @author Jonathan Orr�
 *
 */
public class SearchListFragment extends SherlockListFragment {
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    /**
     * When an item is clicked in the list this method is called
     */
    public void onListItemClick(ListView l, View v, int position, long id) {
        //Do something
    }
    
    /**
     * Receives the search-results and swaps the contents in the list with them.
     */
    public void updateList(List<Book> books) {
    	ListAdapter adapter = new BookListAdapter(getSherlockActivity(), books);
    	this.setListAdapter(adapter);
    	
    }
}

