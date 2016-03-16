package se.gotlib.bibbla.activities;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.singletons.Singletons;
import se.gotlib.bibbla.backend.singletons.User;
import se.gotlib.bibbla.util.Error;

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

public class UserActivity extends ActionBarActivity implements PropertyChangeListener {

	private User user;

	private EditText gotlibNameEdit, gotlibCodeEdit, gotlibPinEdit;
	private Button loginButton;
	private TextView loginErrorText;


	private View.OnClickListener buttonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch(v.getId()) {
				case R.id.gotlib_save_button: {
					loginGotlib();
					break;
				}
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		user = Singletons.getInstance(getApplicationContext()).getUserInstance();
		user.addListener(this);
		gotlibCodeEdit = (EditText) findViewById(R.id.gotlib_code_edit);
		gotlibNameEdit = (EditText) findViewById(R.id.gotlib_name_edit);
		gotlibPinEdit = (EditText) findViewById(R.id.gotlib_pin_edit);
		loginErrorText = (TextView)findViewById(R.id.login_error_text);
		loginButton = (Button)findViewById(R.id.gotlib_save_button);
		loginButton.setOnClickListener(buttonListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		gotlibNameEdit.setText(user.getGotlibName());
		gotlibCodeEdit.setText(user.getGotlibCode());
		gotlibPinEdit.setText(user.getGotlibPin());
	}


	/**
	 * (Button clicked)
	 * Starts the login process
	 */
	private void loginGotlib() {
		Log.d("frontend", "UserActivity loginGotlib, ");
		String name = gotlibNameEdit.getText().toString();
		String code = gotlibCodeEdit.getText().toString();
		String pin = gotlibPinEdit.getText().toString();
		// TODO basic validation of credentials here
		user.loginGotlibAsync(name, code, pin);
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if ("loginGotlibDone".equals(e.getPropertyName())) {
			loginGotlibDone((se.gotlib.bibbla.util.Error) e.getNewValue());
		}
	}

	/**
	 * (Callback method)
	 * Is called when login task is done
	 */
	private void loginGotlibDone(Error e) {

		Log.d("frontend", "UserActivity loginGotlibDone, " + e);

		if (e == null) {
			// TODO show green success, and finish after ~300ms
			loginErrorText.setVisibility(View.GONE);
			finish();
		} else {
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
		getMenuInflater().inflate(R.menu.user, menu);
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
