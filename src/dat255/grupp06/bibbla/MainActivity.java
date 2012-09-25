package dat255.grupp06.bibbla;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.Log;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import dat255.grupp06.bibbla.backend.Backend;

public class MainActivity extends Activity {

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
		spec2.setIndicator("Mina konto");
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
			public boolean handleMessage(Message msg) {
				MainActivity.this.searchDone(msg.getData().getStringArrayList("results"));
				return true; // TODO ?
			}
		});
	}
	/** Is called when backend searching is done. **/
	public void searchDone(ArrayList<String> results) {
		Log.i("debug", results.get(0));
		TextView tv = (TextView) findViewById(R.id.txt1);
		tv.setText(results.get(0));
	}
}
