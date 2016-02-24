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
import android.widget.Button;
import android.widget.EditText;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public class SearchActivity extends ActionBarActivity implements PropertyChangeListener {
	private ArrayList<Book> bookList;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;

	private RecyclerView bookListView;
	private Button searchButton;
	private EditText searchField;

	private Library library;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		library = Singletons.getInstance(getApplicationContext()).getLibraryInstance();
		library.addListener(this);

		bookList = new ArrayList<Book>();

		bookListView = (RecyclerView) findViewById(R.id.book_list);
		searchButton = (Button) findViewById(R.id.search_button);
		searchField = (EditText) findViewById(R.id.search_field);

		layoutManager = new LinearLayoutManager(this);
		bookListView.setLayoutManager(layoutManager);

		adapter = new BookAdapter(bookList);
		bookListView.setAdapter(adapter);

		searchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bookList.clear();
				adapter.notifyDataSetChanged();
				String search = searchField.getText().toString().trim();
				library.searchAsync(search);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
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
		if(event.getPropertyName().equals("searchDone")) {
			ArrayList<Book> books = (ArrayList<Book>)event.getNewValue();
			for(Book b : books) {
				bookList.add(b);
				adapter.notifyItemInserted(bookList.size());
			}
		}
	}
}
