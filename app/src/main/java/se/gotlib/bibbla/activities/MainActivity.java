package se.gotlib.bibbla.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.R.id;
import se.gotlib.bibbla.backend.singletons.Library;
import se.gotlib.bibbla.backend.singletons.Singletons;
import se.gotlib.bibbla.backend.singletons.User;

public class MainActivity extends ActionBarActivity implements PropertyChangeListener {

	private Button searchButton, reservationsButton, loansButton, userButton, librariesButton, settingsButton, testJsoupButton;
    private Button loginButton;
    private Library library;
    private User user;

    // Whether we display the user as logged in or not
    private boolean loggedIn;

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

                case id.test_jsoup_button:
                    library.testJsoupAsync();
                    break;

                case id.login_button:
                    login();
                    break;

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
        user = ((Singletons)getApplication()).getUserInstance();
        user.addListener(this);
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
        loginButton = (Button) findViewById(id.login_button);

    	searchButton.setOnClickListener(buttonListener);
    	reservationsButton.setOnClickListener(buttonListener);
    	loansButton.setOnClickListener(buttonListener);
    	userButton.setOnClickListener(buttonListener);
    	librariesButton.setOnClickListener(buttonListener);
    	settingsButton.setOnClickListener(buttonListener);
    	testJsoupButton.setOnClickListener(buttonListener);
    	loginButton.setOnClickListener(buttonListener);
    }


    @Override
    /**
     * Test method - receives JsoupTestDone
     */
    public void propertyChange(PropertyChangeEvent pcs) {
        String eventName = pcs.getPropertyName();
        if ("testJsoupDone".equals(eventName)) {
            testJsoupDone((String)pcs.getNewValue());
        }
        else if ("loginDone".equals(eventName)) {
//            loginDone((Message<GotlibSession>)pcs.getNewValue());
        }
    }

    /**
     * Test method
     * @param result
     */
    private void testJsoupDone(String result) {
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

    /**
     * Called when login button is clicked.
     * If user is not logged in: trigger login
     * If user is logged in:     show profile
     */
    private void login() {
        if (loggedIn) {
            Log.d("bibbla", "MainActivity: already logged in");
        } else {
            Log.d("bibbla", "MainActivity: log in, switching to LoginActivity");
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }
    }

    /**
     * Callback method, called when login is done.
     * May have failed or succeeded
     */
    private void loginDone(/*Message<GotlibSession> message*/) {
/*        if (message.error == null) {
            loggedIn = true;
            // TODO there's no way to change theme dynamically :(
            String name = user.getName();
            loginButton.setText(getString(R.string.logged_in)+" - "+name);
            loginButton.setBackgroundColor(getResources().getColor(R.color.logged_in_background));
            loginButton.setTextColor(getResources().getColor(R.color.logged_in_text));
        } else {
            loggedIn = false;
            loginButton.setText(getString(R.string.not_logged_in));
            loginButton.setBackgroundColor(getResources().getColor(R.color.logged_out_background));
            loginButton.setTextColor(getResources().getColor(R.color.logged_out_text));
            *//*
            Don't care about the specific error
            switch(message.error) {
                case se.gotlib.gotlibapi.util.Error.INCORRECT_LOGIN_CREDENTIALS:

                    break;
            }
            *//*
        }*/
    }
}
