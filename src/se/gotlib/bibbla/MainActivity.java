package se.gotlib.bibbla;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.bibblatemp.R;

public class MainActivity extends Activity {

	private Button searchButton, reservationsButton, loansButton, userButton, librariesButton, settingsButton;
	
	private OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			case R.id.search_button:
				Intent i = new Intent(this, SearchActivity.class);
				break;
				
			case R.id.reservations_button:
				Intent i = new Intent(this, ReservationsActivity.class);
				break;
			
			case R.id.loans_button:
				Intent i = new Intent(this, LoansActivity.class);
				break;
				
			case R.id.user_button:
				Intent i = new Intent(this, UserActivity.class);
				break;
				
			case R.id.libraries_button:
				break;
				
			case R.id.settings_button:
				break;
			}
		}
	};
	
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
    	searchButton.setOnClickListener(buttonListener);
    	reservationsButton.setOnClickListener(buttonListener);
    	loansButton.setOnClickListener(buttonListener);
    	userButton.setOnClickListener(buttonListener);
    	librariesButton.setOnClickListener(buttonListener);
    	settingsButton.setOnClickListener(buttonListener);
    }
}
