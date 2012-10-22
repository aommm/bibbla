/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.
    
    This file is part of Bibbla.

    Bibbla is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bibbla is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bibbla.  If not, see <http://www.gnu.org/licenses/>.    
 **/

package dat255.grupp06.bibbla.frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.MainActivity;
import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.model.Credentials;

/**
 * A form for login credentials. Start this with startActivityForResult();
 * pressing submit/login will hand the input back. 
 * @author arla
 *
 */
// TODO Make sure back button does something swell.
public class LoginOverlayActivity extends SherlockActivity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_overlay);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().hide();
	}
	
	/**
	 * This sets a result on an intent containing the user's input as a
	 * Credentials object. The Credentials object is put as an extra on the
	 * string specified by LoginManager.EXTRA_CREDENTIALS. 
	 * @param view Unused.
	 */
	public void submit(View view) {
		// Take care of input
		EditText nameET = (EditText) findViewById(R.id.login_name_field);
		EditText cardET = (EditText) findViewById(R.id.login_card_field);
		EditText pinET = (EditText) findViewById(R.id.login_pin_field);
		Credentials cred;
		cred = new Credentials(nameET.getText().toString(),
				cardET.getText().toString(), pinET.getText().toString());
		if (!Credentials.areLegalCredentials(cred.name, cred.card, cred.pin) ||
				cred.name.length()*cred.card.length()*cred.pin.length() == 0) {
			Toast.makeText(this, R.string.login_fail_msg, Toast.LENGTH_SHORT)
					.show();
			return;
		}
		// Send it back to whoever asked
		Intent resultIntent = new Intent();
		resultIntent.putExtra(MainActivity.EXTRA_CREDENTIALS, cred);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}