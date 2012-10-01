package dat255.grupp06.bibbla;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockActivity;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;

public class MainActivity extends SherlockActivity implements ActionBar.TabListener {
	private TextView mSelected;
	
	Backend backend;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        super.onCreate(savedInstanceState);

        backend = new Backend();
        
        setContentView(R.layout.activity_main);
        mSelected = (TextView)findViewById(R.id.text);

        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab searchTab = getSupportActionBar().newTab();
        ActionBar.Tab profileTab = getSupportActionBar().newTab();
        ActionBar.Tab librariesTab = getSupportActionBar().newTab();
        
        searchTab.setText("Sök");
        searchTab.setTabListener(this);
        profileTab.setText("Lån & Reservationer");
        profileTab.setTabListener(this);
        librariesTab.setText("Bibliotek");
        librariesTab.setTabListener(this);
        
        getSupportActionBar().addTab(searchTab);
        getSupportActionBar().addTab(profileTab);
        getSupportActionBar().addTab(librariesTab);
        
        /*
        for (int i = 1; i <= 3; i++) {
            ActionBar.Tab tab = getSupportActionBar().newTab();
            tab.setText("Tab " + i);
            tab.setTabListener(this);
            getSupportActionBar().addTab(tab);
        }
        */
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
		TextView tv = (TextView) findViewById(R.id.text);
		tv.setText(results.get(0).toString());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mSelected.setText("Selected: " + tab.getText());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}
