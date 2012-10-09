package dat255.grupp06.bibbla.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import dat255.grupp06.bibbla.R;

import com.actionbarsherlock.app.SherlockListFragment;

import dat255.grupp06.bibbla.utils.Book;

/**
 * ListFragment that is used to display the search-results after a search.
 * 
 * @author Jonathan Orrö
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
    	
    	// Create a list of maps, which contains name and author.
    	List<Map<String, String>> nameAuthor = new ArrayList<Map<String, String>>();
    	for (Book book : books) {
    		Map<String, String> map = new HashMap<String,String>();
    		map.put("name", book.getName());
    		map.put("author", book.getAuthor());
    		nameAuthor.add(map);
    	}

    	// Tell the list to use this data.
    	SimpleAdapter adapter = new SimpleAdapter(getSherlockActivity(), nameAuthor,
    			R.layout.search_result, new String[] { "name","author" },
    			new int[] {R.id.search_result_text1, R.id.search_result_text2} );
    	 this.setListAdapter(adapter);
    	
    }
}

