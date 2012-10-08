package dat255.grupp06.bibbla.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.MainActivity;
import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.backend.Backend;

/**
 * A fragment that holds the search bar, button 
 * and a ListFragment that displays search results
 * 
 * @author Jonathan Orrö
 */

public class SearchFragment extends SherlockFragment {

	Backend backend;
	SearchListFragment fragment;
	
	@Override
	/**
	 * Inflates the view and sets up the ListFragment
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        fragment = new SearchListFragment();
        fragmentTransaction.add(R.id.list_container, fragment);
        fragmentTransaction.commit();
		
		return inflater.inflate(R.layout.search_fragment, container, false);
	}
	
	/** Starts searching procedure in backend. **/
	public void search(String s) {
		// Calls backend search, using callbacks.
		backend.search(s, new Callback() {
			public void handleMessage(Message msg) {
				SearchFragment.this.searchDone(msg);
			}
		});
	}
	
	/** Is called when backend searching is done.**/
	public void searchDone(Message msg) {
		// Null implies an error. Todo: Display error nicely.
		if (msg.obj == null) {
			return;
		}
		
		ArrayList<Book> results = (ArrayList<Book>) msg.obj; // Can we assume type is correct?
		
		// Did we get no results? 
		if (results.size() == 0) {
			// Show special message?
			return;
		}
		
		String[] searchItems = new String[results.size()];
		int i=0;
		
		// Otherwise, print all books.
		for (Book book : results) {
			searchItems[i] = book.getName();
			i++;
		}
		
		fragment.changeListItems(searchItems);
	}

	public void setBackend(Backend b) {
		backend = b;
	}
}
