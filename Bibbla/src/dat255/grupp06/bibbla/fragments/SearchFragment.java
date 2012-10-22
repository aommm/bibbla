/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.
    
    This file is part of Bibbla.

    Bibbla is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bibbla is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bibbla.  If not, see <http://www.gnu.org/licenses/>.    
 **/

package dat255.grupp06.bibbla.fragments;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

/**
 * A fragment that holds the search bar, button 
 * and a ListFragment that displays search results
 * 
 * @author Jonathan Orrö
 */

public class SearchFragment extends SherlockFragment {

	SearchListFragment listFragment;
	SearchTextFragment textFragment;
	
	EditText searchEdit;
	Button searchButton;
	ProgressBar searchProgress;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create our two children.
		listFragment  = new SearchListFragment();
		textFragment  = new SearchTextFragment();
		
		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // Add children to container,
        fragmentTransaction.add(R.id.list_container, listFragment);
        fragmentTransaction.add(R.id.list_container, textFragment);
        // but hide them for now.
        fragmentTransaction.detach(listFragment);
		fragmentTransaction.detach(textFragment);
		
		fragmentTransaction.commit();
	}
	
	@Override
	/**
	 * Inflates the view and sets up the ListFragment.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
        fragmentTransaction.detach(textFragment);
        fragmentTransaction.detach(listFragment);
        
        // If list is empty, show textFragment.
		if (listFragment.isEmpty()) {
			System.out.println("Attaching textFragment.");
			fragmentTransaction.attach(textFragment);
		} else { // Else, show listFragment.
			System.out.println("Attaching listFragment.");
			fragmentTransaction.attach(listFragment);
		}
        fragmentTransaction.commit();
		
        return inflater.inflate(R.layout.fragment_search, container, false);
	}
	
	@Override
	/**
	 * Detaches textFragment and listFragment.
	 */
	public void onDestroyView() {
		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(textFragment);
        fragmentTransaction.detach(listFragment);
        fragmentTransaction.commit();
        super.onDestroyView();
	}
	
	@Override
	/**
	 * Makes keyboard "enter" start searching procedure.
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		// Get our GUI elements.
		searchButton = (Button)getSherlockActivity().findViewById(R.id.button_search);
		searchEdit = (EditText)getSherlockActivity().findViewById(R.id.edit_search);
		searchProgress = (ProgressBar)getSherlockActivity().findViewById(R.id.progress_search);
		
		// Create a listener for keyboard "enter" button.
		OnEditorActionListener listener = new OnEditorActionListener() {
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	// Simulate click on the search button.
		            searchButton.performClick();
		            return true;
		        }
		        return false;
		    }
		};
		// Add our newly created listener to the text field.
		searchEdit.setOnEditorActionListener(listener);
	}
	
	/**
	 * Starts searching procedure in backend.
	 */
	public void searchClicked() {
		
		// Convert search string to UTF-8.
		String searchString = searchEdit.getText().toString();
		try {
			searchString = URLEncoder.encode(searchString, "UTF-8");
		} catch (UnsupportedEncodingException e) {}

		// Display progress bar.
		setProgressBarVisibility(true);
		
		// No input? No need to search.
		if("".equals(searchString)) {
			listFragment.updateList(new ArrayList<Book>());
			return;
		}
		
		// Create a new callback object, which refers to our searchDone(). 
		Callback c = new Callback() {
			public void handleMessage(Message msg) {
				SearchFragment.this.searchDone(msg);
			}
		};
		
		// Call backend search.
		Backend.getBackend().search(searchString, 0, c);
		listFragment.setLastSearchString(searchString);
	}
	
	public void getMoreSearchResults(int page, String searchString) {
		
		setProgressBarVisibility(true);
		
		// Create a new callback object, which refers to our searchDone(). 
		Callback c = new Callback() {
			public void handleMessage(Message msg) {
				SearchFragment.this.moreSearchDone(msg);
			}
		};
		
		// Call backend search.
		Log.d("Jonis", "searching..... on page    "+page);
		Backend.getBackend().search(searchString, page, c);
	}
	
	/** Is called when backend searching is done.**/
	public void searchDone(Message msg) {
		// Hide progress bar.
		setProgressBarVisibility(false);
		
		// Did the job fail?
		if (msg.error != null) {
			// Log,
			Log.e("searching", "Searching failed: "+msg.error);
			// And toast user.
			Toast.makeText(getSherlockActivity().getApplicationContext(), "Searching failed: "+msg.error, Toast.LENGTH_LONG).show();
			return;
		}
		// Convert results to ArrayList<Book>.  
		ArrayList<Book> books = (ArrayList<Book>) msg.obj;
				
		// Did we get no results? 
		if (books.size() == 0) {
			Toast.makeText(getSherlockActivity().getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
		}
		
		// Update list with titles (empty or not).
		listFragment.updateList(books);
	}
	
	public void moreSearchDone(Message msg) {
		// Hide progress bar.
		setProgressBarVisibility(false);
		
		// Did the job fail?
		if (msg.error != null) {
			// Log,
			Log.e("searching", "Searching failed: "+msg.error);
			// And toast user.
			Toast.makeText(getSherlockActivity().getApplicationContext(), "Searching failed: "+msg.error, Toast.LENGTH_LONG).show();
			return;
		}
		// Convert results to List<Book>.  
		ArrayList<Book> books = (ArrayList<Book>) msg.obj;
				
		// Did we get no results? 
		if (books.size() == 0) {
			Toast.makeText(getSherlockActivity().getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
		}
		
		// Update list with titles (empty or not).
		listFragment.appendList(books);
	}
	
	/**
	 * Changes the visibility of the progress bar.
	 * @param visible - should we show or hide the progress bar? 
	 */
	private void setProgressBarVisibility(boolean visible) {
		// Do only this if searchProgress exists.
		if (searchProgress != null) {
			// setVisibility takes an int.
			int visibilityCode = (visible) ? View.VISIBLE : View.GONE;
			searchProgress.setVisibility(visibilityCode);
		}
	}
}