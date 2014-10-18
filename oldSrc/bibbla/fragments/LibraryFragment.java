/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orr�.
    
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

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.IBackend;
import dat255.grupp06.bibbla.model.Library;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

/**
 * A fragment that holds a ListFragment which holds
 *  a list of all available libraries.
 * 
 *@author Malla
 */

public class LibraryFragment extends SherlockFragment {

	IBackend backend;
	LibListFragment liblistFragment;
	private ArrayList<Library> allLibrariesList = new ArrayList<Library>();

	@Override
	/**
	 * Inflates the view and sets up the ListFragment.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

		if(liblistFragment == null) {
			liblistFragment = new LibListFragment();
			fragmentTransaction.add(R.id.lib_list, liblistFragment);

		} else {
			fragmentTransaction.attach(liblistFragment);
		}
		fragmentTransaction.commit();

		return inflater.inflate(R.layout.fragment_library, container, false);
		

	}

	@Override
	public void onDestroyView() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.detach(liblistFragment);
		fragmentTransaction.commit();
		super.onDestroyView();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		if(allLibrariesList.size() == 0) {
			updateInfo();
		}
	}

	/**
	 * Makes the list appear
	 * @param libs - list to appear
	 */
	private void listAppear(ArrayList<Library> libs) {
		liblistFragment.setLibList(libs);
	}

	/**
	 * Starts searching procedure in backend.
	 */
	public void updateInfo() {
		// Display progress bar.
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);

		// Create a new callback object, which refers to our searchDone(). 
		Callback c = new Callback() {
			public void handleMessage(Message msg) {
				LibraryFragment.this.updateDone(msg);
			}
		};
		// Call backend for update.
		backend.libInfo(c, true);
		System.out.println("LibFrag:updateInfo() sent to backend");
	}

	/** Is called when backend searching is done.**/
	@SuppressWarnings({ "unchecked" })
	public void updateDone(Message msg) {
		// Hide progress bar.
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);

		// Did the job fail?
		if (msg.error != null) {
			// Log,
			Log.e("library info fetch", "Fetch failed: "+msg.error);
			// And toast user.
			Toast.makeText(getSherlockActivity().getApplicationContext(), "Fetch failed: "+msg.error, Toast.LENGTH_LONG).show();
			return;
		}
		// Convert results to List<Library>.
		allLibrariesList = (ArrayList<Library>) msg.obj;
		
		// Update list with titles (empty or not).
		listAppear(allLibrariesList);
	}

	public void setBackend(IBackend backend2) {
		backend = backend2;
	}
}
