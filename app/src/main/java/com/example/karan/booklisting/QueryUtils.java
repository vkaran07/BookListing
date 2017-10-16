package com.example.karan.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karan on 4/9/2017.
 */
public class QueryUtils {
    private static final String TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Book> extractbooks(String rurl) {
        URL url = createUrl(rurl);
        String jsonrpse = null;
        try {
            jsonrpse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "Problem making the HTTP request", e);
        }
        List<Book> books = extractFeatureFromJson(jsonrpse);
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException except) {
            Log.e(TAG, "Error creating Url", except);
            return null;
        }
        return url;
    }

    private static List<Book> extractFeatureFromJson(String bookjson) {
        if (TextUtils.isEmpty(bookjson)) {
            return null;
        }
        List<Book> books = new ArrayList<>();
        try {
            JSONObject baseJsonresponse = new JSONObject(bookjson);
            JSONArray BookArray = baseJsonresponse.getJSONArray("items");
            for (int i = 0; i < BookArray.length(); i++) {
                JSONObject currentBook = BookArray.getJSONObject(i);
                JSONObject VolumeInfo = currentBook.getJSONObject("volumeInfo");
                String btitle = VolumeInfo.getString("title");
                Log.v("query : ", btitle);
                JSONArray info = VolumeInfo.getJSONArray("industryIdentifiers");
                String author = "";
                if (VolumeInfo.has("authors")) {
                    Log.v("Author", "author present");
                    JSONArray authors = VolumeInfo.getJSONArray("authors");
                    for (int j = 0; j < authors.length(); j++) {
                        String a = authors.getString(j);
                        author += a;
                    }
                }
                String page = "0";
                if (VolumeInfo.has("pageCount")) {
                    page = VolumeInfo.getString("pageCount");
                }
                Book book = new Book(btitle, author, page);
                books.add(book);

            }
        } catch (JSONException e) {
            Log.e(TAG, "Problem parsing the book Json results", e);
        }
        return books;
    }


    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonresponse = "";
        if (url == null) {
            return jsonresponse;
        }
        HttpURLConnection urlConnect = null;
        InputStream input = null;
        try {
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setRequestMethod("GET");
            urlConnect.setReadTimeout(10000);
            urlConnect.setConnectTimeout(12000);
            urlConnect.connect();
            if (urlConnect.getResponseCode() == 200) {
                input = urlConnect.getInputStream();
                jsonresponse = readFromStream(input);
            } else {
                Log.e(TAG, "Error Response code:" + urlConnect.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the Book json results", e);
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (input != null) {
                input.close();
            }
        }
        return jsonresponse;
    }

    private static String readFromStream(InputStream input) throws IOException {
        StringBuilder display = new StringBuilder();
        if (input != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(input, Charset.forName("UTF-8"));
            BufferedReader BufferedReader = new BufferedReader(inputStreamReader);
            String line = BufferedReader.readLine();
            while (line != null) {
                display.append(line);
                line = BufferedReader.readLine();
            }
        }
        return display.toString();

    }
}
