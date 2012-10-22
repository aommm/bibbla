/**
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

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.model.Library;

/**
 * Custom ListAdapter for book search results.
 * @author Malla
 */
public class LibListAdapter extends BaseAdapter {

	private final List<Library> list;
	private final Activity activity;

	/**
	 * 
	 * @param activity The activity where this adapter is applied. This is used
	 * for getting an appropriate inflater.
	 * @param list A list of librarries to present. 
	 */
	// TODO Bad style to pass activity?
	@SuppressWarnings("unchecked")
	public LibListAdapter(Activity activity, List<Library> list, boolean showAvailable) {
		// TODO Clone needed?
		this.list = (List<Library>) ((ArrayList<Library>) list).clone();
		this.activity = activity;
	}

	@Override
	public int getCount() {
		if (list != null) {
			return list.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if ((list != null) && (list.size()>position)) {
			return list.get(position);
		} else {
			return null;
		}
	}

	/**
	 * This should probably return some unique identifier of the specified book
	 * item, but we don't really have that (yet).
	 * @return The position , i.e. identical to the position param.
	 */
	@Override
	public long getItemId(int position) {
		return list.get(position).hashCode();
	}

	/**
	 * Inflates and modifies a view based on the search_result layout.
	 * @return In the resulting view, most fields of the concerned book are
	 * displayed in text, but availability is displayed as an image.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Library lib = list.get(position);

		View view;

		if(position < list.size()-1 || list.size() < 50) {

			view = activity.getLayoutInflater().inflate(
					R.layout.list_item_library, parent, false);
			((TextView) view.findViewById(R.id.lib_name))
								.setText(lib.getName());
			((TextView) view.findViewById(R.id.lib_area))
								.setText(lib.getArea());

		} 
		else {
			view = activity.getLayoutInflater().inflate(R.layout.more_search_results, parent, false);
		}

		return view;
	}

}


//Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.