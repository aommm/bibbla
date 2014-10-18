package se.gotlib.bibbla;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.example.bibblatemp.R;

public class MainActivity extends Activity {

	private Button searchButton, reservationsButton, loansButton, userButton, librariesButton, settingsButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	
    	initButtons();
    }
    
    private void initButtons() {
    	searchButton = (Button)findViewById(R.id.search_button);
    	reservationsButton = (Button)findViewById(R.id.reservations_button);
    	loansButton = (Button)findViewById(R.id.loans_button);
    	userButton = (Button)findViewById(R.id.user_button);
    	librariesButton = (Button)findViewById(R.id.libraries_button);
    	settingsButton = (Button)findViewById(R.id.settings_button);
    }
}
