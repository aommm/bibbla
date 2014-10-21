package se.gotlib.bibbla.activities;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.model.Book;
import se.gotlib.bibbla.backend.model.BookAdapter;
import se.gotlib.bibbla.backend.singletons.Library;
import se.gotlib.bibbla.backend.singletons.Singletons;
import se.gotlib.bibbla.fragments.BookFragment;
import se.gotlib.bibbla.fragments.RecyclerFragment;

import android.support.v4.app.FragmentTransaction;
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

public class ReservationsActivity extends ActionBarActivity implements RecyclerFragment.OnBookSelectedListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Book> myDataset;

    private Library library;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        library = ((Singletons)getApplication()).getLibraryInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        RecyclerFragment newFragment = new RecyclerFragment();
        transaction.add(R.id.fragment_container, newFragment);
        transaction.commit();
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
    public void onBookSelected(String isbn) {

        BookFragment newFragment = new BookFragment();

        Bundle args = new Bundle();
        args.putString("isbn", isbn);
        newFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.fragment_container, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    public void push(View v) {
        RecyclerFragment rf = (RecyclerFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        rf.push(v);
    }
}
