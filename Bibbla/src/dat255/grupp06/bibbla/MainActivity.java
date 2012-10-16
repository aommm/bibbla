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
import dat255.grupp06.bibbla.frontend.LoginManager;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

public class MainActivity extends SherlockFragmentActivity implements
ActionBar.TabListener {	

	private Backend backend;
	public static final String EXTRA_BACKEND = "backend";
	
	SearchFragment searchFragment;
	ProfileFragment profileFragment;
	LoginManager loginManager;
	private Callback loginDoneCallback;
	
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
        loginManager = new LoginManager(backend);

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
	 * This is used to get data from the Login Overlay Activity.
	 * @param data in our case should contain a callback to decide what happens
	 * when login is successful. Use putExtra(LoginManager.EXTRA_CALLBACK,
	 * myCallback).
	 * 
	 * @see http://stackoverflow.com/questions/449484/android-capturing-the-return-of-an-activity
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LoginManager.RESULT_LOGIN_FORM) {
			Credentials cred = (Credentials) data.getSerializableExtra(LoginManager.EXTRA_CREDENTIALS); // TODO What if null?
			backend.saveCredentials(cred);
			backend.arildLogin(loginDoneCallback);
		}
    	// Read return value from overlay
    	
		//   if login fails, show error and overlay persists
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
				// Specify what to do when logged in
				loginDoneCallback = new Callback() {
					@Override
					public void handleMessage(Message msg) {
						// Must not use instance of anything non-serializable
						MainActivity.this.showProfileTab(tab, ft, msg);
					}
				};
				// Prompt login
				loginManager.loginIfNeeded(this, loginDoneCallback);
		}
	}
	
	public void showProfileTab(final Tab tab, final FragmentTransaction ft,
			Message msg) {
		if (msg.error != null) {
			if (profileFragment == null) {
				profileFragment = new ProfileFragment();
				profileFragment.setBackend(backend);
				ft.add(R.id.fragment_container, profileFragment);
			} else {
				ft.attach(profileFragment);
			}
		}
		else System.out.println("Not showing profile tab because msg had errors: " + msg.error);
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
	
	public void logout(View view) {
		loginManager.logout();
		// ?
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, profileFragment);
		ft.commit();
	}
	
	public void searchClicked(View view) {
		searchFragment.searchClicked();
	}
}
