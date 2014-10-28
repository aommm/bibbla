package se.gotlib.bibbla.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.model.GotlibSession;
import se.gotlib.bibbla.backend.singletons.Singletons;
import se.gotlib.bibbla.backend.singletons.User;
import se.gotlib.bibbla.util.Message;

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
    private EditText nameEdit, cardEdit, pinEdit;
    private TextView loginErrorText;

    private User user;

    // Whether we are shown as logged in here or not
    private boolean loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initButtons();
        user = ((Singletons)getApplication()).getUserInstance();
        user.addListener(this);
    }

    private void initButtons() {
        loginButton = (Button)findViewById(R.id.login_button);
        nameEdit = (EditText)findViewById(R.id.name_edit);
        cardEdit = (EditText)findViewById(R.id.card_edit);
        pinEdit = (EditText)findViewById(R.id.pin_edit);
        loginErrorText = (TextView)findViewById(R.id.login_error_text);
        loginButton.setOnClickListener(buttonListener);
    }

    /**
     * (Button clicked)
     * Starts the login process
     */
    private void login() {
        String name = nameEdit.getText().toString();
        String card = cardEdit.getText().toString();
        String pin = pinEdit.getText().toString();
        // TODO basic validation of credentials here
        user.loginAsync(name, card, pin);
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        if ("loginDone".equals(e.getPropertyName())) {
            loginDone((Message<GotlibSession>) e.getNewValue());
        }
    }

    /**
     * (Callback method)
     * Is called when login task is done
     */
    private void loginDone(Message<GotlibSession> message) {
        if (message.error == null) {
            loggedIn = true;
            // TODO show green success, and finish after ~300ms
            loginErrorText.setVisibility(View.GONE);
            finish();
        } else {
            loggedIn = false;
            loginErrorText.setVisibility(View.VISIBLE);
            switch(message.error) {
                case INCORRECT_LOGIN_CREDENTIALS:
                    loginErrorText.setText(R.string.incorrect_login_credentials);
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
