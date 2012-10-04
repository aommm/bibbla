package dat255.grupp06.bibbla;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

public class MainActivity extends SherlockActivity implements ActionBar.TabListener {
	private TextView tabText;
	private TextView statusText;
	
	Backend backend;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        backend = new Backend();
        
        setContentView(R.layout.activity_main);
        tabText = (TextView)findViewById(R.id.tabText);
        statusText = (TextView)findViewById(R.id.statusText);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        //Create the tabs
        ActionBar.Tab searchTab = getSupportActionBar().newTab();
        ActionBar.Tab profileTab = getSupportActionBar().newTab();
        ActionBar.Tab librariesTab = getSupportActionBar().newTab();
        
        //Set tab properties
        searchTab.setContentDescription("Sök");
        searchTab.setIcon(android.R.drawable.ic_menu_search);
        searchTab.setTabListener(this);
        
        profileTab.setContentDescription("Lån");
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

	public void buttonClicked(View v) {
		search("hej");
	}
	
	/**
	 * Starts searching procedure in backend, and changes status label.
	 */
	public void search(String s) {
		// Calls backend search, using callbacks.
		statusText.setText("Söker...");
		backend.search(s, new Callback() {
			public void handleMessage(Message msg) {
				MainActivity.this.searchDone(msg);
			}
		});
	}
	
	/** Is called when backend searching is done.**/
	public void searchDone(Message msg) {
		
		if (msg.error != null) {
			statusText.setText("Fel: "+msg.error);
			return;
		}
		
		ArrayList<Book> results = (ArrayList<Book>) msg.obj; // Can we assume type is correct?
		// Did we get no results? 
		if (results.size() == 0) {
			// Show special message?
			statusText.setText("Inga resultat.");
			return;
		}
		
		// Otherwise, display all books in status label.
		String txt = "";
		for (Book book : results) {
			txt += ". "+book.toString();
		}
		statusText.setText(txt);
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		tabText.setText("Selected: " + tab.getContentDescription());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
}
