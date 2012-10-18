package dat255.grupp06.bibbla;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.fragments.ProfileFragment;
import dat255.grupp06.bibbla.fragments.SearchFragment;
import dat255.grupp06.bibbla.frontend.LoginOverlayActivity;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Message;

public class MainActivity extends SherlockFragmentActivity implements
ActionBar.TabListener {	

	private Backend backend;
	public static final int RESULT_LOGIN_FORM = 0;
	public static final String EXTRA_CREDENTIALS = "credentials";
	
	SearchFragment searchFragment;
	ProfileFragment profileFragment;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
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
        
        //Set tab properties
        searchTab.setContentDescription("Sök");
        searchTab.setIcon(android.R.drawable.ic_menu_search);
        searchTab.setTabListener(this);
        
        profileTab.setContentDescription("Lån");
        profileTab.setIcon(android.R.drawable.ic_menu_share);
        profileTab.setTabListener(this);
   
        //Add the tabs to the action bar
        getSupportActionBar().addTab(searchTab);
        getSupportActionBar().addTab(profileTab);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		// Nothing atm
	}

	/**
	 * {@inheritDoc}
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
			// TODO Check format of input
			if (cred == null) {
				// Retry login form (recursive)
				Intent intent = new Intent(this, LoginOverlayActivity.class);
				startActivityForResult(intent, RESULT_LOGIN_FORM);
			} else {
				backend.saveCredentials(cred);
			}
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
		}
	}
	
	/** @deprecated*/
	public void showProfileTab(Message msg) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if (msg.error == null) {
			if (profileFragment == null) {
				profileFragment = new ProfileFragment();
				profileFragment.setBackend(backend);
				ft.add(R.id.fragment_container, profileFragment);
				ft.commit();
			} else {
				ft.attach(profileFragment);
				ft.commit();
			}
		} else {
//			loginManager.promptIfNotLoggedIn(this, loginDoneCallback);
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		switch(tab.getPosition()) {
		case 0:
			ft.detach(searchFragment);
			break;
		case 1:
			ft.detach(profileFragment);
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
			searchFragment.setBackend(backend);
			ft.add(R.id.fragment_container, searchFragment);
		} else {
			ft.attach(searchFragment);
			// detach...?
		}
		ft.commit();
	}
	
	public void searchClicked(View view) {
		searchFragment.searchClicked();
	}
}
