package se.gotlib.bibbla.backend.model;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import se.gotlib.bibbla.R;

/**
 * Created by Master on 2014-10-20.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    private ArrayList<Book> dataSet;

    public BookAdapter(ArrayList<Book> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_book, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.v1.setText(dataSet.get(i).getTitle());
        viewHolder.v2.setText(dataSet.get(i).getAuthor());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView v1, v2;

        public ViewHolder(View v) {
            super(v);
            this.v1 = (TextView)v.findViewById(R.id.title_book);
            this.v2 = (TextView)v.findViewById(R.id.author_book);
        }
    }
}
