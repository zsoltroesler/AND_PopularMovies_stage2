package com.example.android.popularmoviesstage2.loaders;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.model.Movie;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Zsolt on 26.02.2018.
 */

public class MovieLoader extends AsyncTaskLoader<ArrayList<Movie>> {

    /** Tag for log messages */
    private static final String LOG_TAG = MovieLoader.class.getName();

    /** Query URL */
    private String url;

    /**
     * Constructs a new {@link MovieLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public MovieLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: Movie onStartLoading() called...");
        forceLoad();
    }

    @Override
    public ArrayList<Movie> loadInBackground() {
        Log.i(LOG_TAG, "TEST: Movie loadInBackground() called...");

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url == null) {
            return null;
        }

        // Perform the HTTP request for movie data and process the response.
        ArrayList<Movie> result = null;
        try {
            result = NetworkUtils.fetchMovieData(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}

