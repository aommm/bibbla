/**
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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;
import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.fragments.LibListFragment;



public class LibraryOverlayActivity extends SherlockActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("","onCreate done");
		//Sets up some graphical stuff
		setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
		requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library_overlay);
		getSupportActionBar().hide();

		Intent intent = getIntent();

		String name = 		intent.getStringExtra(LibListFragment.LIB_NAME);
		String openH = 		intent.getStringExtra(LibListFragment.LIB_OPENH);
		String address;
		if (intent.getStringExtra(LibListFragment.LIB_VISIT)==null){
			 address =intent.getStringExtra(LibListFragment.LIB_ADDRESS);
		}  else {
			address =intent.getStringExtra(LibListFragment.LIB_VISIT);
		}
		String phoneNr =	intent.getStringExtra(LibListFragment.LIB_PHONE);
		String email =		intent.getStringExtra(LibListFragment.LIB_EMAIL);
		String postAdr = 	intent.getStringExtra(LibListFragment.LIB_ADDRESS);
		String postInfo = 	intent.getStringExtra(LibListFragment.LIB_POSTCODE)+ " " +
		intent.getStringExtra(LibListFragment.LIB_AREA);

		((TextView)findViewById(R.id.overlay_lib_name)).setText(name);
		((TextView)findViewById(R.id.overlay_open_hours)).setText(openH);
		((TextView)findViewById(R.id.overlay_visit)).setText(address);
		((TextView)findViewById(R.id.overlay_telephone)).setText(phoneNr);
		((TextView)findViewById(R.id.overlay_email)).setText(email);
		((TextView)findViewById(R.id.overlay_address)).setText(postAdr);
		((TextView)findViewById(R.id.overlay_post_info)).setText(postInfo);
	}
}


//Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.