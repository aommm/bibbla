package dat255.grupp06.bibbla;

import java.util.ArrayList;

import android.os.Bundle;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;

import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.utils.Book;

public class MainActivity extends SherlockActivity {

	Backend backend;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		backend = new Backend();
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Sök");
		spec1.setIndicator("Sök");
		spec1.setContent(R.id.tab1);
		

		TabSpec spec2=tabHost.newTabSpec("Mitt konto");
		spec2.setIndicator("Mitt konto");
		spec2.setContent(R.id.tab2);

		TabSpec spec3=tabHost.newTabSpec("Bibliotek");
		spec3.setIndicator("Bibliotek");
		spec3.setContent(R.id.tab3);

		TabSpec spec4=tabHost.newTabSpec("Inställningar");
		spec4.setIndicator("Inställningar");
		spec4.setContent(R.id.tab4);		
		
		tabHost.addTab(spec1);
		tabHost.addTab(spec2);
		tabHost.addTab(spec3);
		tabHost.addTab(spec4);
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
		TextView tv = (TextView) findViewById(R.id.txt1);
		tv.setText(results.get(0).toString());
	}
}
