package se.gotlib.bibbla.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.singletons.Singletons;
import se.gotlib.bibbla.backend.singletons.User;
import se.gotlib.bibbla.util.Error;

public class LoginActivity extends ActionBarActivity implements PropertyChangeListener {

    private View.OnClickListener buttonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.login_button: {
                    login();
                    break;
                }
            }
        }
    };

    private Button loginButton;
    private EditText usernameEdit, passwordEdit;
    private TextView loginErrorText;

    private User user;

    // Whether we are shown as logged in here or not
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initButtons();
        user = Singletons.getInstance(getApplicationContext()).getUserInstance();
        user.addListener(this);
    }

    private void initButtons() {
        loginButton = (Button)findViewById(R.id.login_button);
        usernameEdit = (EditText)findViewById(R.id.username_edit);
        passwordEdit = (EditText)findViewById(R.id.password_edit);
        loginErrorText = (TextView)findViewById(R.id.login_error_text);
        loginButton.setOnClickListener(buttonListener);
    }

    /**
     * (Button clicked)
     * Starts the login process
     */
    private void login() {
        String name = usernameEdit.getText().toString();
        String card = passwordEdit.getText().toString();
        // TODO basic validation of credentials here
        user.loginAsync(name, card);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("loginDone".equals(e.getPropertyName())) {
            loginDone((Error) e.getNewValue());
        }
    }

    /**
     * (Callback method)
     * Is called when login task is done
     */
    private void loginDone(Error e) {

        Log.d("frontend", "LoginActivity loginDone, "+e);

        if (e == null) {
            loggedIn = true;
            // TODO show green success, and finish after ~300ms
            loginErrorText.setVisibility(View.GONE);
            finish();
        } else {
            loggedIn = false;
            loginErrorText.setVisibility(View.VISIBLE);
            switch(e) {
                case INCORRECT_BIBBLA_CREDENTIALS:
                    loginErrorText.setText(R.string.incorrect_bibbla_credentials);
                    break;
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
