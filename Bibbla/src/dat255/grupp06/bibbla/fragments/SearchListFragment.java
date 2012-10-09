package dat255.grupp06.bibbla.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

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
    public void updateList(String[] listItems) {
		setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, listItems));
    }
}

