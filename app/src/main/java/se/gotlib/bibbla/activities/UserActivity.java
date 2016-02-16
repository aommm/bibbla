package se.gotlib.bibbla.activities;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.singletons.Singletons;
import se.gotlib.bibbla.backend.singletons.User;
import se.gotlib.bibbla.util.*;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class UserActivity extends ActionBarActivity implements PropertyChangeListener {

	private User user;
	private EditText gotlibSurnameEdit, gotlibCodeEdit, gotlibPinEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		user = Singletons.getInstance(getApplicationContext()).getUserInstance();
		user.addListener(this);
		gotlibCodeEdit = (EditText) findViewById(R.id.gotlib_code_edit);
		gotlibSurnameEdit = (EditText) findViewById(R.id.gotlib_surname_edit);
		gotlibPinEdit = (EditText) findViewById(R.id.gotlib_pin_edit);
	}

	@Override
	protected void onResume() {
		super.onResume();
		gotlibSurnameEdit.setText(user.getGotlibSurname());
		gotlibCodeEdit.setText(user.getGotlibCode());
		gotlibPinEdit.setText(user.getGotlibPin());
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

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		// don't care
//		if ("loginDone".equals(event.getPropertyName())) {
//			loginDone((se.gotlib.bibbla.util.Error) pcs.getNewValue());
//		}
	}
}
