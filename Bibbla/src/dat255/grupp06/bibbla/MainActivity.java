package dat255.grupp06.bibbla;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.frontend.SearchFragment;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {	

	Backend backend;
	SearchFragment searchFragment;
	
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

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
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
				//ft.add(R.id.fragment_container, testFragment);
				break;
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		switch(tab.getPosition()) {
		case 0:
			ft.detach(searchFragment);
			break;
		case 1:
			//ft.remove(testFragment);
			break;
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}
	
	public void searchClicked(View view) {
		searchFragment.searchClicked();
	}
	
	public void moreSearchResultsClicked(int page, String searchString) {
		Log.d("J", "hej");
		searchFragment.getMoreSearchResults(page, searchString);
	}
}
