package dat255.grupp06.bibbla.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;

public class SearchListFragment extends SherlockListFragment {
	
	/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		return inflater.inflate(R.layout.search_fragment, container, false);
	}
	*/
	
	int state;
	
	public static final String[] TITLES = 
        {
        "Henry IV (1)",   
        "Henry V",
        "Henry VIII",       
        "Richard II",
        "Richard III",
        "Merchant of Venice",  
        "Othello",
        "King Lear"
        };
	
	public static final String[] Hej = 
        {
        "äpple",
        "päron",
        "banan",
        "apelsin",
        "morot"
        }; 
	
	public static final String[] då = 
        {
        "bil",
        "cykel",
        "blablabla",
        "weofhiwoef",
        "aweqwotqp",
        "popdfke"
        }; 
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        state = 3;
        setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, TITLES));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.i("FragmentList", "Item clicked: " + id);
    }
    
    public void changeListItems(String[] listItems) {
		setListAdapter(new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, listItems));

    }
}

