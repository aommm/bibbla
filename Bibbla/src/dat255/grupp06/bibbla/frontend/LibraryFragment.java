package dat255.grupp06.bibbla.frontend;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;

/**
 * A fragment that holds the search bar, button 
 * and a ListFragment that displays search results
 * 
 * @author Madeleine Appert
 */

public class LibraryFragment extends SherlockFragment {

	Backend backend;
	SearchListFragment listFragment;
	TextView text;
	ListView libList;
	
	@Override
	/**
	 * Inflates the view and sets up the ListFragment.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        listFragment = new SearchListFragment();
        fragmentTransaction.add(R.id.list_container, listFragment);
        fragmentTransaction.commit();
		
		return inflater.inflate(R.layout.fragment_library, container, false);
	}
	
	@Override
	/**
	 * Makes keyboard "enter" start searching procedure.
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Get our GUI elements.
		libList = (ListView)getSherlockActivity().findViewById(R.id.lib_list);
		text = (EditText)getSherlockActivity().findViewById(R.id.text);


	}
	

	public void setBackend(Backend b) {
		backend = b;
	}
}
