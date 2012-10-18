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

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.fragments.SearchListFragment;



public class BookOverlayActivity extends SherlockActivity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Sherlock___Theme_Light); //Used for theme switching in samples
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_overlay);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        Intent intent = getIntent();
        String title = intent.getStringExtra(SearchListFragment.BOOK_TITLE);
        String author = intent.getStringExtra(SearchListFragment.BOOK_AUTHOR);
        
        Log.d("Jonis", title + author);
        
        ((TextView)findViewById(R.id.overlay_book_title)).setText(title);
        ((TextView)findViewById(R.id.overlay_book_author)).setText(author);
    }
}
