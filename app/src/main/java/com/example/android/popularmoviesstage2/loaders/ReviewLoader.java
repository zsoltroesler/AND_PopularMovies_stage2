package com.example.android.popularmoviesstage2.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.model.Review;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Zsolt on 03.03.2018.
 */

public class ReviewLoader extends AsyncTaskLoader<ArrayList<Review>> {

    /** Tag for log messages */
    private static final String LOG_TAG = ReviewLoader.class.getName();

    /** Query URL */
    private String url;

    /**
     * Constructs a new {@link ReviewLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public ReviewLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: Review onStartLoading() called...");
        forceLoad();
    }

    @Override
    public ArrayList<Review> loadInBackground() {
        Log.i(LOG_TAG, "TEST: Review loadInBackground() called...");

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url == null) {
            return null;
        }

        // Perform the HTTP request for review data and process the response.
        ArrayList<Review> result = null;
        try {
            result = NetworkUtils.fetchReviewData(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}


