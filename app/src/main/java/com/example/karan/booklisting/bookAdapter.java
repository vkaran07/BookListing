package com.example.karan.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by karan on 3/29/2017.
 */

public class bookAdapter extends ArrayAdapter<Book> {

    public bookAdapter(Context context, List<Book> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View ListItemView = convertView;
        if (ListItemView == null) {
            Log.d("getview : ", "view was null ");
            ListItemView = LayoutInflater.from(getContext()).inflate(R.layout.book, parent, false);
        }
        Book currentbook = getItem(position);
        TextView txt = (TextView) ListItemView.findViewById(R.id.Title);
        txt.setText(currentbook.gettitle());
        TextView txt1 = (TextView) ListItemView.findViewById(R.id.Author);
        txt1.setText(currentbook.getauthor());
        TextView txt2 = (TextView) ListItemView.findViewById(R.id.Page);
        txt2.setText(currentbook.getpage());
        return ListItemView;
    }

}

