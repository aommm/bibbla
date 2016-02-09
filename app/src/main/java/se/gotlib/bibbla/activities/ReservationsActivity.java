package se.gotlib.bibbla.activities;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.model.Book;
import se.gotlib.bibbla.backend.model.BookAdapter;
import se.gotlib.bibbla.backend.singletons.Library;
import se.gotlib.bibbla.backend.singletons.Singletons;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class ReservationsActivity extends ActionBarActivity implements PropertyChangeListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Book> myDataset;

    private Library library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        library = Singletons.getInstance(getApplicationContext()).getLibraryInstance();
        library.addListener(this);

        myDataset = new ArrayList<Book>();

        mRecyclerView = (RecyclerView) findViewById(R.id.reserved_books_recycler);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BookAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        Log.d("jonis", "1");
        library.getReservationsAsync();
        Log.d("jonis", "2");
    }

    public void push(View v) {
        int pos = Integer.parseInt(((TextView) findViewById(R.id.pos_text)).getText().toString());
        String tit = ((TextView)findViewById(R.id.tit_text)).getText().toString();
        String auth = ((TextView)findViewById(R.id.auth_text)).getText().toString();
        myDataset.add(pos, new Book(tit, Math.random()+"auth", auth));
        mAdapter.notifyItemInserted(pos);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reservations, menu);
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
        Log.d("jonis", "belo");
        if(event.getPropertyName().equals("getReservations")) {
            Log.d("jonis", "helo");
            ArrayList<Book> books = (ArrayList<Book>)event.getNewValue();
            Log.d("jonis", books.size()+"");
            for(Book b : books) {
                Log.d("jonis", "data"+myDataset);
                Log.d("jonis", "adapter"+mAdapter);
                myDataset.add(b);
                mAdapter.notifyItemInserted(myDataset.size());
            }
        }
    }
}
