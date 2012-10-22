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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;

/**
 * Fragment which holds a nice-looking TextView.
 * @author Niklas Logren
 */
public class SearchTextFragment extends SherlockFragment {
	TextView textView;
	String text = "bobi";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search_text, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        textView = (TextView) getSherlockActivity().findViewById(R.id.text_search_status);
        textView.setText(text);
        textView.setTextColor(0xff0000ff);
        textView.setBackgroundColor(0xffffffff);
	}
	
	/**
	 * Updates the label with the specified text.
	 * @param text - the text to display.
	 */
	public void setText(String text) {
		if (text != null) {
			this.text = text;
		}
	}

}