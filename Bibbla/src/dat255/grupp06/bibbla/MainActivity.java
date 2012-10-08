package dat255.grupp06.bibbla;

import java.util.ArrayList;

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
import dat255.grupp06.bibbla.fragments.SearchFragment;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

public class MainActivity extends SherlockFragmentActivity implements ActionBar.TabListener {	
	Backend backend;
	SearchFragment fragment;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        
        fragment = new SearchFragment();
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
        
        backend = new Backend();
        
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
		this.search("bla");
	}
	
	/** Starts searching procedure in backend. **/
	public void search(String s) {
		// Calls backend search, using callbacks.
		backend.search(s, new Callback() {
			public void handleMessage(Message msg) {
				MainActivity.this.searchDone(msg);
			}
		});
	}
	
	/** Is called when backend searching is done.**/
	public void searchDone(Message msg) {
		// Null implies an error. Todo: Display error nicely.
		if (msg.obj == null) {
			return;
		}
		
		ArrayList<Book> results = (ArrayList<Book>) msg.obj; // Can we assume type is correct?
		
		// Did we get no results? 
		if (results.size() == 0) {
			// Show special message?
			return;
		}
		
		// Otherwise, print all books.
		for (Book book : results) {
			Log.i("debug: searchDone():", book.toString());
		}
		
		
		// Display first book in label.
		//TextView tv = (TextView) findViewById(R.id.text);
		//tv.setText(results.get(0).toString());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		/*TextView mSelected = (TextView)findViewById(R.id.text);
		
		if(mSelected != null) {
			Log.d("J","Hej");
			mSelected.setText("Selected: " + tab.getContentDescription());
		}
		*/
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}
	
	public void changeList(View view) {
		Log.d("J", "main-activity 1");
		EditText editText = (EditText) findViewById(R.id.search_field);
		Log.d("J", "main-activity 2");
		fragment.changeList(editText.getText().toString());
		Log.d("J", "main-activity 3");
	}
}
