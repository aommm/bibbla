package dat255.grupp06.bibbla;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
	
}
