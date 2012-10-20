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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Window;

import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.backend.Backend;
import dat255.grupp06.bibbla.fragments.SearchFragment;
import dat255.grupp06.bibbla.fragments.SearchListFragment;

import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.CredentialsMissingException;
import dat255.grupp06.bibbla.utils.Callback;
import dat255.grupp06.bibbla.utils.Message;



public class BookOverlayActivity extends SherlockActivity {
	
	Book book;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_overlay);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        Intent intent = getIntent();
        book =(Book)intent.getSerializableExtra(SearchListFragment.BOOK);
        ((TextView)findViewById(R.id.overlay_book_title)).setText(book.getName());
        ((TextView)findViewById(R.id.overlay_book_author)).setText(book.getAuthor());
        
        Callback c = new Callback() {
			public void handleMessage(Message msg) {
				//BookOverlayActivity.this.setDetails(msg);
			}
		};
    }
    
    public void reserveDone(Message msg) {
    	((TextView)findViewById(R.id.text_reserve_book)).setText("Klar!");
    	setSupportProgressBarIndeterminateVisibility(false);
    }
    
    
    public void reserveBook(View view) {
    	setSupportProgressBarIndeterminateVisibility(true);
    	Callback c = new Callback() {
			public void handleMessage(Message msg) {
				BookOverlayActivity.this.reserveDone(msg);
			}
		};
		
		try {
			Backend.getBackend().reserve(book, "An", c);
			((TextView)findViewById(R.id.text_reserve_book)).setText("Reserverar bok...");
		} catch (CredentialsMissingException e) {
			setSupportProgressBarIndeterminateVisibility(false);
		}
    }

    
}
