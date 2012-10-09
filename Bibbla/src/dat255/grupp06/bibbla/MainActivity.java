package dat255.grupp06.bibbla;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.fragments.LoginFragment;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {	

	Backend backend;
	LoginFragment fragment;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
        backend = new Backend();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        fragment = new LoginFragment();
        fragment.setBackend(backend);
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        //Create the tabs
        ActionBar.Tab searchTab = getSupportActionBar().newTab();
        ActionBar.Tab profileTab = getSupportActionBar().newTab();
        ActionBar.Tab librariesTab = getSupportActionBar().newTab();
        
        //Set tab properties
        searchTab.setContentDescription("S�k");
        searchTab.setIcon(android.R.drawable.ic_menu_search);
        searchTab.setTabListener(this);
        
        profileTab.setContentDescription("L�n");
        profileTab.setIcon(android.R.drawable.ic_menu_share);
        profileTab.setTabListener(this);
        
        librariesTab.setContentDescription("Bibliotek");
        librariesTab.setIcon(android.R.drawable.ic_menu_gallery);
        librariesTab.setTabListener(this);
        
        //Add the tabs to the action bar
        getSupportActionBar().addTab(searchTab);
        getSupportActionBar().addTab(profileTab);
        getSupportActionBar().addTab(librariesTab);      
    }
	
	@Override
	public void onResume() {
		super.onResume();
		// Nothing atm
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	public void search(View view) {
		EditText editText = (EditText) findViewById(R.id.search_field);
//		fragment.search(editText.getText().toString());
	}
	
	public void login(View vieww) {
		EditText nameET = (EditText) findViewById(R.id.login_name_field);
		EditText cardET = (EditText) findViewById(R.id.login_card_field);
		EditText pinET = (EditText) findViewById(R.id.login_pin_field);
		fragment.login(nameET.getText().toString(), cardET.getText().toString(),
				pinET.getText().toString());
	}
}
