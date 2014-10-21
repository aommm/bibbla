package se.gotlib.bibbla.fragments;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.model.Book;
import se.gotlib.bibbla.backend.model.BookAdapter;
import se.gotlib.bibbla.backend.singletons.Library;
import se.gotlib.bibbla.backend.singletons.Singletons;

/**
 *
 */
public class RecyclerFragment extends Fragment implements PropertyChangeListener {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Book> myDataset;


    private Library library;

    public RecyclerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        library = ((Singletons)getActivity().getApplication()).getLibraryInstance();
        library.addListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);
        mRecyclerView = ((RecyclerView) rootView.findViewById(R.id.books_recycler));

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myDataset = new ArrayList<Book>();

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BookAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        library.getReservationsAsync();
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if(event.getPropertyName().equals("getReservations")) {
            ArrayList<Book> books = (ArrayList<Book>)event.getNewValue();
            for(Book b : books) {
                myDataset.add(b);
                mAdapter.notifyItemInserted(myDataset.size());
            }
        }
    }

    //TODO
    /*
    Replace with handling of pressing the elements in the list. You get it.
     */
    public void push(View v) {
        //Heheheheheheheheheheheheheheeeehe :):):):):):):):):)
        ((OnBookSelectedListener)getActivity()).onBookSelected(myDataset.get(Integer.parseInt((((TextView)getActivity().findViewById(R.id.edittext))).getText().toString())).getIsbn());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnBookSelectedListener {
        // TODO: Update argument type and name
        public void onBookSelected(String isbn);
    }

}
