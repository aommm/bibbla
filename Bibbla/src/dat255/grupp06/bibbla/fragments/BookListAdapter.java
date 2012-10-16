package dat255.grupp06.bibbla.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import dat255.grupp06.bibbla.R;
import dat255.grupp06.bibbla.model.Book;

/**
 * Custom ListAdapter for book search results.
 * @author arla
 */
public class BookListAdapter extends BaseAdapter {
	
	private final List<Book> list;
	private final Activity activity;
	private final boolean showAvailable;

	/**
	 * 
	 * @param activity The activity where this adapter is applied. This is used
	 * for getting an appropriate inflater.
	 * @param list A list of books to present. 
	 */
	// TODO Bad style to pass activity?
	public BookListAdapter(Activity activity, List<Book> list, boolean showAvailable) {
		// TODO Clone needed?
		this.list = (list != null) ? list : new ArrayList<Book>();
		this.activity = activity;
		this.showAvailable = showAvailable;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	/**
	 * This should probably return some unique identifier of the specified book
	 * item, but we don't really have that (yet).
	 * @return The position , i.e. identical to the position param.
	 */
	@Override
	public long getItemId(int position) {
		return list.get(position).hashCode();
	}

	/**
	 * Inflates and modifies a view based on the search_result layout.
	 * @return In the resulting view, most fields of the concerned book are
	 * displayed in text, but availability is displayed as an image.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Book book = list.get(position);
		View view = activity.getLayoutInflater().inflate(
				R.layout.list_item_book, parent, false);
		((TextView) view.findViewById(R.id.search_result_title))
			.setText(book.getName());
		((TextView) view.findViewById(R.id.search_result_author))
			.setText(book.getAuthor());
		if (showAvailable) {
			((ImageView) view.findViewById(R.id.search_result_available))
				.setImageResource(book.getAvailable() > 0 ?
					android.R.drawable.presence_online :
					android.R.drawable.presence_busy);
		}
		return view;
	}

}
