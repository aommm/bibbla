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

package dat255.grupp06.bibbla;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.fragments.LibraryFragment;
import dat255.grupp06.bibbla.fragments.ProfileFragment;
import dat255.grupp06.bibbla.fragments.SearchFragment;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

public class MainActivity extends SherlockFragmentActivity implements
ActionBar.TabListener {	

	Backend backend;
	
	// TODO These should probably go into a list or something.
	SearchFragment searchFragment;
	ProfileFragment profileFragment;
	LibraryFragment libraryFragment;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_Light); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        
        // We want a fancy spinner for marking progress.
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);
       
        // Hide progress bar by default.
        setSupportProgressBarIndeterminateVisibility(false);

        backend = new Backend();

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        //Create the tabs
        ActionBar.Tab searchTab = getSupportActionBar().newTab();
        ActionBar.Tab profileTab = getSupportActionBar().newTab();
        ActionBar.Tab libraryTab = getSupportActionBar().newTab();
        
        //Set tab properties
        searchTab.setContentDescription("Sök");
        searchTab.setIcon(android.R.drawable.ic_menu_search);
        searchTab.setTabListener(this);
        
        profileTab.setContentDescription("Lån");
        profileTab.setIcon(android.R.drawable.ic_menu_share);
        profileTab.setTabListener(this);
   
        libraryTab.setContentDescription("Bibliotek");
        libraryTab.setIcon(android.R.drawable.ic_menu_directions);
        libraryTab.setTabListener(this);
        
        //Add the tabs to the action bar
        getSupportActionBar().addTab(searchTab);
        getSupportActionBar().addTab(profileTab);
        getSupportActionBar().addTab(libraryTab);
        
        Log.d("Jonis", "main finished");
    }
	
	@Override
	public void onResume() {
		super.onResume();
		// Nothing atm
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Refactor to eliminate duplicate code?
		switch(tab.getPosition()) {
			case 0:
				if(searchFragment == null) {
			        searchFragment = new SearchFragment();
			        searchFragment.setBackend(backend);
			        ft.add(R.id.fragment_container, searchFragment);
				} else {
					ft.attach(searchFragment);
				}
				break;
			case 1:
				if (profileFragment == null) {
					profileFragment = new ProfileFragment();
					profileFragment.setBackend(backend);
					ft.add(R.id.fragment_container, profileFragment);
				} else {
					ft.attach(profileFragment);
				}
				break;
			case 2:
				if(libraryFragment == null) {
					libraryFragment = new LibraryFragment();
					libraryFragment.setBackend(backend);
			        ft.add(R.id.fragment_container, libraryFragment);
				} else {
					ft.attach(libraryFragment);
				}
				break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
		// If we're switching tabs, hide keyboard.
		hideKeyboard();
		
		// Detach the correct fragment.
		switch(tab.getPosition()) {
		case 0:
			ft.detach(searchFragment);
			break;
		case 1:
			ft.detach(profileFragment);
			break;
		case 2:
			ft.detach(libraryFragment);
			break;	
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	public void login(View view) {
//		EditText nameET = (EditText) findViewById(R.id.login_name_field);
//		EditText cardET = (EditText) findViewById(R.id.login_card_field);
//		EditText pinET = (EditText) findViewById(R.id.login_pin_field);
		// TODO Loading spinner
		Callback loginCallback = new Callback() {
			@Override
			public void handleMessage(Message msg) {
				MainActivity.this.loginDone(msg);
			}
		};
		// TODO Wtf, no credentials?
		backend.login(loginCallback);
	}
	
	public void loginDone(Message msg) {
		if (msg.loggedIn) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.replace(R.id.fragment_container, profileFragment);
			ft.commit();
		} else {
			Toast.makeText(this, R.string.login_fail_msg,
				Toast.LENGTH_SHORT).show();
		}
	}
	
	public void logout(View view) {
		backend.logOut();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, profileFragment);
		ft.commit();
	}
	
	/**
	 * Is called when the Search button is clicked.
	 * @param view - The view the click came from.
	 */
	public void searchClicked(View view) {
		hideKeyboard();
		searchFragment.searchClicked();
	}
	
	public void moreSearchResultsClicked(int page, String searchString) {
		searchFragment.getMoreSearchResults(page, searchString);
	}
	/**
	 * Hides the virtual keyboard.
	 */
	private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
	}
}
