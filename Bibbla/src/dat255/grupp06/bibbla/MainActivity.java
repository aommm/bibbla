package dat255.grupp06.bibbla;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.fragments.LoginFragment;
import dat255.grupp06.bibbla.fragments.ProfileFragment;
import dat255.grupp06.bibbla.frontend.SearchFragment;

public class MainActivity extends SherlockFragmentActivity implements
ActionBar.TabListener, LoginFragment.OnLoginListener {	

	Backend backend;
	
	// TODO These should probably go into a list or something.
	SearchFragment searchFragment;
	LoginFragment loginFragment;
	ProfileFragment profileFragment;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backend = new Backend();
        
        searchFragment = new SearchFragment();
        loginFragment = new LoginFragment();
        profileFragment = new ProfileFragment();
        searchFragment.setBackend(backend);
        loginFragment.setBackend(backend);
        profileFragment.setBackend(backend);

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
	
	public void search(View view) {
		EditText editText = (EditText) findViewById(R.id.search_field);
		searchFragment.search(editText.getText().toString());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.d("J", "Selecting tab "+tab.getPosition());
		switch(tab.getPosition()) {
			case 0:
				ft.replace(R.id.fragment_container, searchFragment);
				break;
			case 1:
				// TODO Check if logged in here or in login fragment?
				ft.replace(R.id.fragment_container, loginFragment);
				break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	public void login(View vieww) {
		EditText nameET = (EditText) findViewById(R.id.login_name_field);
		EditText cardET = (EditText) findViewById(R.id.login_card_field);
		EditText pinET = (EditText) findViewById(R.id.login_pin_field);
		loginFragment.login(nameET.getText().toString(), cardET.getText().toString(),
				pinET.getText().toString());
	}

	@Override
	public void onLoginSuccessful() {
		System.out.println("onLoginSuccessful");
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.fragment_container, profileFragment);
		ft.commit();
	}
}
