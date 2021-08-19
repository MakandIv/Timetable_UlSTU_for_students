package com.timetable.app;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class LoadPage extends AsyncTask<String, Void, Document> {

    protected Document doInBackground(String... urls) {
        try {
            return Jsoup.connect(urls[0]).get();
        } catch (Exception e) {
            return null;
        }
    }
}
