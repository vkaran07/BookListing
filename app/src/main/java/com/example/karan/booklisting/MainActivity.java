package com.example.karan.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getName();
    //    public  EditText text = (EditText)findViewById(R.id.search);
    public static final int book_loader_id = 1;
    public bookAdapter mAdapter;
    public TextView mempty_network;
    public TextView mempty_data;
    public ListView BookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BookListView = (ListView) findViewById(R.id.list);
        mAdapter = new bookAdapter(MainActivity.this, new ArrayList<Book>());
        BookListView.setAdapter(mAdapter);
        mempty_network = (TextView) findViewById(R.id.empty_network);
        BookListView.setEmptyView(mempty_network);
        mempty_data = (TextView) findViewById(R.id.empty_data);
        BookListView.setEmptyView(mempty_data);

        final EditText editView = (EditText) findViewById(R.id.search);
        final Button submit = (Button) findViewById(R.id.submit);
        ConnectivityManager conmgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = conmgr.getActiveNetworkInfo();
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (networkInfo != null && networkInfo.isConnected()) {
                    String newt = editView.getText().toString().trim();
                    if (editView.getText().length() == 0) {
                        mempty_data.setText(R.string.noresult);
                    }
                    String books_url = "https://www.googleapis.com/books/v1/volumes?q=" + newt;
                    Uri uri = Uri.parse(books_url);
                    final Uri.Builder ubuilder = uri.buildUpon();
                    ubuilder.appendQueryParameter("q", newt);
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute(ubuilder.toString());
                } else {
                    mempty_network.setText(R.string.noconnection);
                    submit.setVisibility(View.GONE);
                    editView.setVisibility(View.GONE);
                }
            }
        });
    }


    public class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {
        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> res = QueryUtils.extractbooks(urls[0]);
            return res;
        }

        protected void onPostExecute(List<Book> data) {

            Log.v("onPostExecute:", "executed");
            mAdapter.clear();
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
                mAdapter = new bookAdapter(getApplicationContext(), data);
            }
        }
    }
}



