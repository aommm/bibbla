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

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

import dat255.grupp06.bibbla.frontend.LibraryOverlayActivity;
import dat255.grupp06.bibbla.model.Library;

/**
 * ListFragment that is used to display the available Libraries.
 * 
 * 
 *@author Malla
 */
public class LibListFragment extends SherlockListFragment {
	public final static String LIB_NAME = 		"dat255.grupp06.bibbla.LIB_NAME";
	public final static String LIB_ADDRESS = 	"dat255.grupp06.bibbla.LIB_ADDRESS";
	public final static String LIB_POSTCODE = 	"dat255.grupp06.bibbla.LIB_POSTCODE";
	public final static String LIB_AREA = 		"dat255.grupp06.bibbla.LIB_AREA";
	public final static String LIB_PHONE = 		"dat255.grupp06.bibbla.LIB_PHONE";
	public final static String LIB_VISIT =		"dat255.grupp06.bibbla.LIB_VISIT";
	public final static String LIB_EMAIL =		"dat255.grupp06.bibbla.LIB_EMAIL";
	public final static String LIB_OPENH = 		"dat255.grupp06.bibbla.LIB_OPENH";

	private ArrayList<Library> allLibInfo;

	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
        allLibInfo = new ArrayList<Library>();
    }

    @Override
    /**
     * When an item is clicked in the list this method is called
     */
    public void onListItemClick(ListView l, View v, int position, long id) {

	    	Intent intent = new Intent(getSherlockActivity(), LibraryOverlayActivity.class);

	    	intent.putExtra(LIB_NAME, allLibInfo.get(position).getName());
	    	intent.putExtra(LIB_ADDRESS, allLibInfo.get(position).getAddress());
	    	intent.putExtra(LIB_POSTCODE, allLibInfo.get(position).getPostCode());
	    	intent.putExtra(LIB_AREA, allLibInfo.get(position).getArea());
	    	intent.putExtra(LIB_PHONE, allLibInfo.get(position).getPhoneNr());
	    	intent.putExtra(LIB_VISIT, allLibInfo.get(position).getVisAdr());
	    	intent.putExtra(LIB_EMAIL, allLibInfo.get(position).getEmail());
	    	intent.putExtra(LIB_OPENH, allLibInfo.get(position).getOpenH());
	    	startActivity(intent);

    }
    
    /**
     * Receives the search-results and swaps the contents in the list with them.
     */
    public void updateList(List<Library> libs) {
    	allLibInfo.clear();
    	allLibInfo.addAll(libs);
    	libs.add(new Library());

    	ListAdapter adapter = new LibListAdapter(getSherlockActivity(), libs, true);

    	this.setListAdapter(adapter);
    }    
}

//Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.