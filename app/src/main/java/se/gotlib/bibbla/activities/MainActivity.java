package se.gotlib.bibbla.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.R.id;
import se.gotlib.bibbla.backend.singletons.Library;
import se.gotlib.bibbla.backend.singletons.Singletons;

public class MainActivity extends ActionBarActivity implements PropertyChangeListener {

	private Button searchButton, reservationsButton, loansButton, userButton, librariesButton, settingsButton, testJsoupButton;

    private Library library;

	private OnClickListener buttonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
                case R.id.search_button: {
                    Intent i = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(i);
                    break;
                }
                case R.id.reservations_button: {
                    Intent i = new Intent(MainActivity.this, ReservationsActivity.class);
                    startActivity(i);
                    break;
                }
                case R.id.loans_button: {
                    Intent i = new Intent(MainActivity.this, LoansActivity.class);
                    startActivity(i);
                    break;
                }
                case R.id.user_button: {
                    Intent i = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(i);
                    break;
                }
                case R.id.libraries_button:
                    break;

                case R.id.settings_button:
                    break;

                case id.test_jsoup_button: {
                    library.testJsoupAsync();
                    break;
                }
            }
		}
	};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    	initButtons();
        library = ((Singletons)getApplication()).getLibraryInstance();
        library.addListener(this);
    }

    private void initButtons() {
    	searchButton = (Button)findViewById(R.id.search_button);
    	reservationsButton = (Button)findViewById(R.id.reservations_button);
    	loansButton = (Button)findViewById(R.id.loans_button);
    	userButton = (Button)findViewById(R.id.user_button);
    	librariesButton = (Button)findViewById(R.id.libraries_button);
    	settingsButton = (Button)findViewById(R.id.settings_button);
    	settingsButton = (Button)findViewById(R.id.settings_button);
        testJsoupButton = (Button) findViewById(id.test_jsoup_button);
    	searchButton.setOnClickListener(buttonListener);
    	reservationsButton.setOnClickListener(buttonListener);
    	loansButton.setOnClickListener(buttonListener);
    	userButton.setOnClickListener(buttonListener);
    	librariesButton.setOnClickListener(buttonListener);
    	settingsButton.setOnClickListener(buttonListener);
    	testJsoupButton.setOnClickListener(buttonListener);
    }


    @Override
    /**
     * Test method - receives JsoupTestDone
     */
    public void propertyChange(PropertyChangeEvent pcs) {
        if (pcs.getPropertyName().equals("testJsoupDone")) {
            testJsoupDone((String)pcs.getNewValue());
        }
    }

    private void testJsoupDone(String result) {
        // Log.d("bibbla", "MainActivity: testJsoupDone, result: " + pcs.getNewValue());
        testJsoupButton.setText(testJsoupButton.getText().toString() + " - "+result);
        AsyncTask<Void, Void, Void> timer = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(1500);
                } catch(Exception e) {}
                return null;
            }
            @Override
            protected void onPostExecute(Void e) {
                String text = testJsoupButton.getText().toString();
                testJsoupButton.setText(text.substring(0, text.length() - 5));
            }
        };
        timer.execute();
    }
}
