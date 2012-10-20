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
import android.view.WindowManager;
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


/**
 * An activity that will display more details about a selected book.
 * @author Jonathan Orrö
 *
 */
public class BookOverlayActivity extends SherlockActivity {
	
	Book book;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Sets up some graphical stuff
    	setTheme(com.actionbarsherlock.R.style.Theme_Sherlock); //Used for theme switching in samples
        requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_overlay);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        
        //Get all of the data the intent sends
        Intent intent = getIntent();
        book =(Book)intent.getSerializableExtra(SearchListFragment.BOOK);
        ((TextView)findViewById(R.id.overlay_book_title)).setText(book.getName());
        ((TextView)findViewById(R.id.overlay_book_author)).setText(book.getAuthor());
        
        //Then get the rest from Gotlib's servers
        Callback c = new Callback() {
			public void handleMessage(Message msg) {
				BookOverlayActivity.this.setDetails(msg);
			}
		};
		
		Backend.getBackend().fetchDetailedView(book, c);
    }
    
    /**
     * This method will be called by a callback object when a reservation is done.
     * @param msg - A message object that will contain any eventual error messages
     */
    public void reserveDone(Message msg) {
    	((TextView)findViewById(R.id.text_reserve_book)).setText("Klar!");
    	setSupportProgressBarIndeterminateVisibility(false);
    }
    
    /**
     * This method is called when the reserve book button is pressed on the
     * book overlay.
     * @param view
     */
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
    
    
    /**
     * Method that is called from a callback object when the backend is done fetching
     * more details about a book.
     * @param msg - A message object that contains the data collected by the backend and
     * 				any eventual error messages.
     */
    public void setDetails(Message msg) {
    	// Hide progress bar.
    	setSupportProgressBarIndeterminateVisibility(false);
    	// Did the job fail?
		if (msg.error != null) {
			// Log,
			Log.e("Jonis", "Details fetcher failed: "+msg.error);
			// And toast user.
			Toast.makeText(getApplicationContext(), "Details fetcher failed: "+msg.error, Toast.LENGTH_LONG).show();
			return;
		}
		
		book = (Book)msg.obj;
		
		((TextView)findViewById(R.id.overlay_book_isbn)).setText(book.getIsbn());
        ((TextView)findViewById(R.id.overlay_book_physical)).setText(book.getPhysicalDescription());
    }

    
}
