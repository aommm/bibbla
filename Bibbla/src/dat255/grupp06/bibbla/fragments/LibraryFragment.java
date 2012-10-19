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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Library;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

/**
 * A fragment that holds the search bar, button 
 * and a ListFragment that displays search results
 * 
 *
 */

public class LibraryFragment extends SherlockFragment {

	Backend backend;
	LibListFragment liblistFragment;
	private ArrayList<Library> tempList = new ArrayList<Library>();
	
	@Override
	/**
	 * Inflates the view and sets up the ListFragment.
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
//		FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//		
//		if(liblistFragment == null) {
//	        liblistFragment = new LibListFragment();
//	        fragmentTransaction.add(R.id.lib_list, liblistFragment);
//		} else {
//			fragmentTransaction.attach(liblistFragment);
//		}
//
//        fragmentTransaction.commit();

		return inflater.inflate(R.layout.fragment_library, container, false);
	
//		public void onCreate(Bundle saveInstanceState) {
//			setContentView(R.layout.your_layout);
//			lv = (ListView) findViewById(R.id.your_list_view_id);
//			your_array_list.add("foo");
//			your_array_list.add("bar");
//			// This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter
//			ArrayAdapter<String> arrayAdapter =      
//				new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, your_array_list);
//			lv.setAdapter(arrayAdapter); 
//		}

	
	}
	
	@Override
	public void onDestroyView() {
		FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.detach(liblistFragment);
        fragmentTransaction.commit();
        super.onDestroyView();
	}
	
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		System.out.println("LibFrag:onActivityCreated()");
//		Log.d("Malla", "LibFrag:onActivityCreated()");
//		super.onActivityCreated(savedInstanceState);
//				
//		
//		//listAppear(tempLibList());
//		updateInfo();
//	}
	
	private void listAppear(ArrayList<Library> libs) {
		System.out.println("LibFrag:listAppear()");
		Log.d("Malla", "LibFrag:listAppear()");
		liblistFragment.updateList(libs);
		
	}

	/**
	 * Starts searching procedure in backend.
	 */
	public void updateInfo() {
		System.out.println("LibFrag:updateInfo()");
		Log.d("Malla", "LibFrag:updateInfo()");
		// Display progress bar.
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(true);
		
		// Create a new callback object, which refers to our searchDone(). 
		Callback c = new Callback() {
			public void handleMessage(Message msg) {
				LibraryFragment.this.updateDone(msg);
				System.out.println("LibFrag result"+msg.toString());
			}
		};
		
		// Call backend for update.

		backend.libInfo(c);
		System.out.println("LibFrag:updateInfo() sent to backend");
	}

	
	/** Is called when backend searching is done.**/
	public void updateDone(Message msg) {
		System.out.println("LibFrag:updateDone()");
		Log.d("Malla", "LibFrag:updateDone()");
		// Hide progress bar.
		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
		
		// Did the job fail?
		if (msg.error != null) {
			// Log,
			Log.e("searching", "Searching failed: "+msg.error);
			// And toast user.
			Toast.makeText(getSherlockActivity().getApplicationContext(), "Searching failed: "+msg.error, Toast.LENGTH_LONG).show();
			return;
		}
		// Convert results to List<Library>.  
		ArrayList<Library> libs = (ArrayList<Library>) msg.obj;
				
		
		// Update list with titles (empty or not).
		listAppear(libs);
	}
	
//	public void moreSearchDone(Message msg) {
//		// Hide progress bar.
//		getSherlockActivity().setSupportProgressBarIndeterminateVisibility(false);
//		
//		// Did the job fail?
//		if (msg.error != null) {
//			// Log,
//			Log.e("searching", "Searching failed: "+msg.error);
//			// And toast user.
//			Toast.makeText(getSherlockActivity().getApplicationContext(), "Searching failed: "+msg.error, Toast.LENGTH_LONG).show();
//			return;
//		}
//		// Convert results to List<Book>.  
//		ArrayList<Book> books = (ArrayList<Book>) msg.obj;
//				
//		// Did we get no results? 
//		if (books.size() == 0) {
//			Toast.makeText(getSherlockActivity().getApplicationContext(), "No results found.", Toast.LENGTH_LONG).show();
//		}
//		
//		// Update list with titles (empty or not).
//		listFragment.appendList(books);
//	}

	public void setBackend(Backend b) {
		backend = b;
	}
	
	public ArrayList<Library> tempLibList(){
	tempList.add(new Library("Name1", "Address1", "Numbers1", "Area1", "PhoneNr1", "VisAdr1", "Email1", "OpenH1"));
	tempList.add(new Library("Name2", "Address2", "Numbers2", "Area2", "PhoneNr2", "VisAdr2", "Email2", "OpenH2"));
	tempList.add(new Library("Name3", "Address3", "Numbers3", "Area3", "PhoneNr3", "VisAdr3", "Email3", "OpenH3"));
	tempList.add(new Library("Name4", "Address4", "Numbers4", "Area4", "PhoneNr4", "VisAdr4", "Email4", "OpenH4"));
	tempList.add(new Library("Name5", "Address5", "Numbers5", "Area5", "PhoneNr5", "VisAdr5", "Email5", "OpenH5"));
	tempList.add(new Library("Name6", "Address6", "Numbers6", "Area6", "PhoneNr6", "VisAdr6", "Email6", "OpenH6"));
	tempList.add(new Library("Name7", "Address7", "Numbers7", "Area7", "PhoneNr7", "VisAdr7", "Email7", "OpenH7"));
	tempList.add(new Library("Name8", "Address8", "Numbers8", "Area8", "PhoneNr8", "VisAdr8", "Email8", "OpenH8"));
	tempList.add(new Library("Name9", "Address9", "Numbers9", "Area9", "PhoneNr9", "VisAdr9", "Email9", "OpenH9"));
	tempList.add(new Library("Name10", "Address10", "Numbers10", "Area10", "PhoneNr10", "VisAdr10", "Email10", "OpenH10"));
	tempList.add(new Library("Name11", "Address11", "Numbers11", "Area11", "PhoneNr11", "VisAdr11", "Email11", "OpenH11"));
	tempList.add(new Library("Name12", "Address12", "Numbers12", "Area12", "PhoneNr12", "VisAdr12", "Email12", "OpenH12"));
	tempList.add(new Library("Name13", "Address13", "Numbers13", "Area13", "PhoneNr13", "VisAdr13", "Email13", "OpenH13"));
	tempList.add(new Library("Name14", "Address14", "Numbers14", "Area14", "PhoneNr14", "VisAdr14", "Email14", "OpenH14"));
	tempList.add(new Library("Name15", "Address15", "Numbers15", "Area15", "PhoneNr15", "VisAdr15", "Email15", "OpenH15"));
	tempList.add(new Library("Name16", "Address16", "Numbers16", "Area16", "PhoneNr16", "VisAdr16", "Email16", "OpenH16"));
	tempList.add(new Library("Name17", "Address17", "Numbers17", "Area17", "PhoneNr17", "VisAdr17", "Email17", "OpenH17"));
	tempList.add(new Library("Name18", "Address18", "Numbers18", "Area18", "PhoneNr18", "VisAdr18", "Email18", "OpenH18"));
	tempList.add(new Library("Name19", "Address19", "Numbers19", "Area19", "PhoneNr19", "VisAdr19", "Email19", "OpenH19"));
	tempList.add(new Library("Name20", "Address20", "Numbers20", "Area20", "PhoneNr20", "VisAdr20", "Email20", "OpenH20"));
	tempList.add(new Library("Name21", "Address21", "Numbers21", "Area21", "PhoneNr21", "VisAdr21", "Email21", "OpenH21"));
	tempList.add(new Library("Name22", "Address22", "Numbers22", "Area22", "PhoneNr22", "VisAdr22", "Email22", "OpenH22"));
	return tempList;
}
}
//package dat255.grupp06.bibbla.fragments;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//
//import com.actionbarsherlock.app.SherlockFragment;
//
//import dat255.grupp06.bibbla.R;
//import dat255.grupp06.bibbla.backend.Backend;
//import dat255.grupp06.bibbla.model.Book;
//import dat255.grupp06.bibbla.model.Library;
//import dat255.grupp06.bibbla.utils.Callback;
//import dat255.grupp06.bibbla.utils.Message;
//
///**
// * A fragment that holds the search bar, button 
// * and a ListFragment that displays search results
// * 
// * @author Madeleine Appert
// */
//
//public class LibraryFragment extends SherlockFragment {
//
//	Backend backend;
//	private List<Library> allLibInfo;
//	TextView textSomething;
//	ListView libList;
//	private List<Library> tempList = new ArrayList<Library>();
//
//	
//	@Override
//	/**
//	 * Inflates the view and sets up the ListFragment.
//	 */
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
//		
//		//libList = getPrevLibInfo; To get the previous shown info of the libraries.
//		
//		
//		
//		
//		// Create a new callback object, which refers to our searchDone(). 
//		Callback c = new Callback() {
//			public void handleMessage(Message msg) {
//				LibraryFragment.this.updateLibInfo(msg);
//			}
//		};
//		// Call backend and start update info.
//		Log.d("Jonis", "searching..... on page    1");
//		backend.libInfo( c);
//		
//		return inflater.inflate(R.layout.fragment_library, container, false);
//		//start updating the lib info?
//		
//	}
//	public void setBackend(Backend b) {
//		backend = b;
//	}
//	
//	private void listToGUI(){
//		//Call backend to get the info. 
//		setAllLibraries(tempLibList());
//		
//	}
//	
//	public void setAllLibraries(List<Library> list){
//		allLibInfo = list;
//	}
//	
//	public List<Library> tempLibList(){
//		tempList.add(new Library("Name1", "Address1", "Numbers1", "Area1", "PhoneNr1", "VisAdr1", "Email1", "OpenH1"));
//		tempList.add(new Library("Name2", "Address2", "Numbers2", "Area2", "PhoneNr2", "VisAdr2", "Email2", "OpenH2"));
//		tempList.add(new Library("Name3", "Address3", "Numbers3", "Area3", "PhoneNr3", "VisAdr3", "Email3", "OpenH3"));
//		tempList.add(new Library("Name4", "Address4", "Numbers4", "Area4", "PhoneNr4", "VisAdr4", "Email4", "OpenH4"));
//		tempList.add(new Library("Name5", "Address5", "Numbers5", "Area5", "PhoneNr5", "VisAdr5", "Email5", "OpenH5"));
//		tempList.add(new Library("Name6", "Address6", "Numbers6", "Area6", "PhoneNr6", "VisAdr6", "Email6", "OpenH6"));
//		tempList.add(new Library("Name7", "Address7", "Numbers7", "Area7", "PhoneNr7", "VisAdr7", "Email7", "OpenH7"));
//		tempList.add(new Library("Name8", "Address8", "Numbers8", "Area8", "PhoneNr8", "VisAdr8", "Email8", "OpenH8"));
//		tempList.add(new Library("Name9", "Address9", "Numbers9", "Area9", "PhoneNr9", "VisAdr9", "Email9", "OpenH9"));
//		tempList.add(new Library("Name10", "Address10", "Numbers10", "Area10", "PhoneNr10", "VisAdr10", "Email10", "OpenH10"));
//		tempList.add(new Library("Name11", "Address11", "Numbers11", "Area11", "PhoneNr11", "VisAdr11", "Email11", "OpenH11"));
//		tempList.add(new Library("Name12", "Address12", "Numbers12", "Area12", "PhoneNr12", "VisAdr12", "Email12", "OpenH12"));
//		tempList.add(new Library("Name13", "Address13", "Numbers13", "Area13", "PhoneNr13", "VisAdr13", "Email13", "OpenH13"));
//		tempList.add(new Library("Name14", "Address14", "Numbers14", "Area14", "PhoneNr14", "VisAdr14", "Email14", "OpenH14"));
//		tempList.add(new Library("Name15", "Address15", "Numbers15", "Area15", "PhoneNr15", "VisAdr15", "Email15", "OpenH15"));
//		tempList.add(new Library("Name16", "Address16", "Numbers16", "Area16", "PhoneNr16", "VisAdr16", "Email16", "OpenH16"));
//		tempList.add(new Library("Name17", "Address17", "Numbers17", "Area17", "PhoneNr17", "VisAdr17", "Email17", "OpenH17"));
//		tempList.add(new Library("Name18", "Address18", "Numbers18", "Area18", "PhoneNr18", "VisAdr18", "Email18", "OpenH18"));
//		tempList.add(new Library("Name19", "Address19", "Numbers19", "Area19", "PhoneNr19", "VisAdr19", "Email19", "OpenH19"));
//		tempList.add(new Library("Name20", "Address20", "Numbers20", "Area20", "PhoneNr20", "VisAdr20", "Email20", "OpenH20"));
//		tempList.add(new Library("Name21", "Address21", "Numbers21", "Area21", "PhoneNr21", "VisAdr21", "Email21", "OpenH21"));
//		tempList.add(new Library("Name22", "Address22", "Numbers22", "Area22", "PhoneNr22", "VisAdr22", "Email22", "OpenH22"));
//		return tempList;
//	}
//	
//	public void updateLibInfo(Message msg){//List<Library> lib){
//		//Fix all the things tat need be done when LibInfo is updated.
////    	allLibInfo.clear();
////    	allLibInfo.addAll(lib);
//	}
//	
//	public void adaptToList(List<Library> lib) {
//    	ListAdapter adapter = new LibListAdapter(getSherlockActivity(), lib, true);
//    	this.setListAdapter(adapter);
//    }
//}
