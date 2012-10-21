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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.fragments.LibraryFragment;
import dat255.grupp06.bibbla.fragments.ProfileFragment;
import dat255.grupp06.bibbla.fragments.SearchFragment;
import dat255.grupp06.bibbla.frontend.LoginCallbackHandler;
import dat255.grupp06.bibbla.frontend.LoginOverlayActivity;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

public class MainActivity extends SherlockFragmentActivity implements
ActionBar.TabListener, LoginCallbackHandler {	

	private Backend backend;
	
	public static final int RESULT_LOGIN_FORM = 0;
	public static final String EXTRA_CREDENTIALS = "credentials";
	
	SearchFragment searchFragment;
	ProfileFragment profileFragment;
	LibraryFragment libraryFragment;
	private Callback loginDoneCallback;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        
        // We want a fancy spinner for marking progress.
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);
       
        backend = Backend.getBackend();
        
        // Hide progress bar by default.
        setSupportProgressBarIndeterminateVisibility(false);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        //Create the tabs
        ActionBar.Tab searchTab = getSupportActionBar().newTab();
        ActionBar.Tab profileTab = getSupportActionBar().newTab();
        ActionBar.Tab libraryTab = getSupportActionBar().newTab();
        
        //Set tab properties
        searchTab.setContentDescription("Sök");
        searchTab.setIcon(R.drawable.search_icon);
        searchTab.setTabListener(this);
        
        profileTab.setContentDescription("Lån");
        profileTab.setIcon(R.drawable.profile_icon);
        profileTab.setTabListener(this);
   
        libraryTab.setContentDescription("Bibliotek");
        libraryTab.setIcon(R.drawable.lib_icon);
        libraryTab.setTabListener(this);
        
        //Add the tabs to the action bar
        getSupportActionBar().addTab(searchTab);
        getSupportActionBar().addTab(profileTab);
        getSupportActionBar().addTab(libraryTab);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		// Nothing atm
	}

	/**
	 * {@inheritdoc}
	 * 
	 * This is called when a user is done with LoginOverlayActivity. Saves the
	 * specified credentials and tells Backend to log in.
	 * @param data should contain a Credentials object as an Extra, identified
	 * by LoginManager.EXTRA_CREDENTIALS
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RESULT_LOGIN_FORM:
			// Get credentials
			Credentials cred = (Credentials) data.getSerializableExtra(EXTRA_CREDENTIALS);
			if (cred == null) {
				// Retry login form (recursive)
				showCredentialsDialog(loginDoneCallback);
			} else {
				backend.saveCredentials(cred);
			}
			loginDoneCallback.handleMessage(new Message());
			// The callback should not be handled more than once.
			loginDoneCallback = null;
			break;
		default:
			throw new IllegalArgumentException("onActivityResult was called "+
					"with an unknown request code");
		}
	}
	
	@Override
	public void onTabSelected(final Tab tab, final FragmentTransaction ft) {
		// TODO Refactor to eliminate duplicate code?
		switch(tab.getPosition()) {
			case 0:
				if(searchFragment == null) {
			        searchFragment = new SearchFragment();
			        ft.add(R.id.fragment_container, searchFragment);
				} else {
					ft.attach(searchFragment);
				}
				break;
			case 1:
				if (profileFragment == null) {
					profileFragment = new ProfileFragment();
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
		
		// Hide keyboard and spinner
		hideKeyboard();
		setSupportProgressBarIndeterminateVisibility(false);
		
		// Detach the correct fragment.
		switch(tab.getPosition()) {
		case 0:
			ft.detach(searchFragment);
			break;
		case 1:
			ft.detach(profileFragment);
			profileFragment.cancelUpdate();
			break;
		case 2:
			ft.detach(libraryFragment);
			break;	
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	/**
	 * Logs out and switches to the Search fragment.
	 * @param view
	 */
	public void logout(View view) {
		backend.logOut();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (searchFragment == null) {
			searchFragment = new SearchFragment();
			ft.add(R.id.fragment_container, searchFragment);
		} else {
			ft.attach(searchFragment);
			// TODO detach...?
		}
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

	@Override
	public void showCredentialsDialog(Callback callback) {
		this.loginDoneCallback = callback;
		Intent intent = new Intent(this, LoginOverlayActivity.class);
		startActivityForResult(intent, RESULT_LOGIN_FORM);
	}
}
