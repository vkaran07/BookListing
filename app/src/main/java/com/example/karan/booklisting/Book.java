package com.example.karan.booklisting;

/**
 * Created by karan on 4/9/2017.
 */
public class Book {
    private String mTitle;
    private String mAuthor;
    private String mpage;

    public Book(String Title, String Author, String Page) {
        mTitle = Title;
        mAuthor = Author;
        mpage = Page;
    }

    public String gettitle() {
        return mTitle;
    }

    public String getauthor() {
        return mAuthor;
    }

    public String getpage() {
        return mpage;
    }
}
