package dat255.grupp06.bibbla;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.frontend.SearchFragment;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {	

	Backend backend;
	SearchFragment searchFragment;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        backend = new Backend();
        
        searchFragment = new SearchFragment();
        searchFragment.setBackend(backend);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        //Create the tabs
        ActionBar.Tab searchTab = getSupportActionBar().newTab();
        ActionBar.Tab profileTab = getSupportActionBar().newTab();
        
        //Set tab properties
        searchTab.setContentDescription("S�k");
        searchTab.setIcon(android.R.drawable.ic_menu_search);
        searchTab.setTabListener(this);
        
        profileTab.setContentDescription("L�n");
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
		Log.d("J", tab.getPosition()+"select");
		switch(tab.getPosition()) {
			case 0:
				ft.add(R.id.fragment_container, searchFragment);
				Log.d("J", "select 1    "+tab.getPosition());
				break;
			case 1:
				//ft.add(R.id.fragment_container, testFragment);
				Log.d("J", "select 2    "+tab.getPosition());
				break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		Log.d("J", tab.getPosition()+"unselect");
		switch(tab.getPosition()) {
		case 0:
			ft.remove(searchFragment);
			Log.d("J", "unselect 1       "+tab.getPosition());
			break;
		case 1:
			//ft.remove(testFragment);
			Log.d("J", "unselect 2       "+tab.getPosition());
			break;
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
