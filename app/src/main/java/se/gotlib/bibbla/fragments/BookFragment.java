package se.gotlib.bibbla.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import se.gotlib.bibbla.R;
import se.gotlib.bibbla.backend.model.Book;
import se.gotlib.bibbla.backend.singletons.Library;
import se.gotlib.bibbla.backend.singletons.Singletons;

public class BookFragment extends Fragment implements PropertyChangeListener {

    private TextView titleText, authorText, isbnText, descText;

    private Library library;

    public BookFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);

        titleText = (TextView)rootView.findViewById(R.id.book_fragment_title);
        authorText = (TextView)rootView.findViewById(R.id.book_fragment_author);
        isbnText = (TextView)rootView.findViewById(R.id.book_fragment_isbn);
        descText = (TextView)rootView.findViewById(R.id.book_fragment_desc);

        library.getBookAsync(getArguments().getString("isbn"));

        // Inflate the layout for this fragment
        return rootView;
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
        if(event.getPropertyName().equals("getBook")) {
            Book b = (Book)event.getNewValue();
            titleText.setText(b.getTitle());
            authorText.setText(b.getAuthor());
            isbnText.setText(b.getIsbn());
            descText.setText(b.getDesc());
        }
    }
}
