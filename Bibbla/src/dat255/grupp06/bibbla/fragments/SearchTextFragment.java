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

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;

import dat255.grupp06.bibbla.MainActivity;
import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.frontend.BookOverlayActivity;
import dat255.grupp06.bibbla.model.Book;

/**
 * ListFragment that is used to display the search-results after a search.
 * 
 * @author Jonathan Orrö
 *
 */
public class SearchTextFragment extends SherlockListFragment {
	TextView textView;
	public static final String DEFAULT_TEXT = "Inga resultat funna";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Save reference to textView.
        textView = (TextView) getSherlockActivity().findViewById(R.id.text_search_status);
        // Apply default text
        System.out.println(textView!=null);
        System.out.println(DEFAULT_TEXT);
        textView.setText(DEFAULT_TEXT);
        
        System.out.println("onCreate()");
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
//		FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		System.out.println("onCreateView()");
		
		return inflater.inflate(R.layout.fragment_search_text, container, false);
	}
	
	/**
	 * Updates the label with the specified text.
	 * @param text - the text to display.
	 */
	public void setText(String text) {
		if (text != null) {
			textView.setText(text);
		}
	}

}