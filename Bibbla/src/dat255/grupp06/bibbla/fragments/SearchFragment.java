package dat255.grupp06.bibbla.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

/**
 * A fragment that holds the search bar, button 
 * and a ListFragment that displays search results
 * 
 * @author Jonathan Orrö
 */

public class SearchFragment extends SherlockFragment {

	Backend backend;
	SearchListFragment fragment;
	EditText searchField;
	Button searchButton;
	
	@Override
	/**
	 * Inflates the view and sets up the ListFragment
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		searchButton = (Button)getActivity().findViewById(R.id.search_button);
		searchField = (EditText)getActivity().findViewById(R.id.search_field);

		// Get text of "search" action from xml.
		// TODO: change text of soft keyboard's next button.
		String searchText = getActivity().getResources().getString(R.string.button_search); // Get text from xml

		// Create a listener for keyboard "enter" button.
		OnEditorActionListener listener = new OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		            searchButton.performClick();
		            return true;
		        }
		        return false;
		    }
		};
		// Add our newly created listener to the text field.  
		System.out.println("exists: "+(searchField != null));
		System.out.println("instof: "+(searchField instanceof EditText));
		// searchField.setOnEditorActionListener(listener);
		// TODO: searchField doesn't exist!
		
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
		backend.search(s, 0, new Callback() {
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
