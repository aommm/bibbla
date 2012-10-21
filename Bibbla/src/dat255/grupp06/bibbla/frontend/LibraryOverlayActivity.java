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
import android.view.WindowManager;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.fragments.LibListFragment;
import dat255.grupp06.bibbla.fragments.SearchListFragment;
import dat255.grupp06.bibbla.model.Library;



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


//    	setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_Light); //Used for theme switching in samples
//        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_library_overlay);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        Intent intent = getIntent();
		Log.d("","get the intent");
//        String title = intent.getStringExtra(SearchListFragment.BOOK_TITLE);
//        String author = intent.getStringExtra(SearchListFragment.BOOK_AUTHOR);
    	String name = intent.getStringExtra(LibListFragment.LIB_NAME);
    	String address;
    	String postCode;
    	String area;
    	String phoneNr;
    	String visAdr;
    	String email;
    	String openH;
        
        ((TextView)findViewById(R.id.overlay_lib_name)).setText(name);
		Log.d("","set the name");
//        ((TextView)findViewById(R.id.overlay_book_author)).setText(author);
    }
}
