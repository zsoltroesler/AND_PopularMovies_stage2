package com.example.android.popularmoviesstage2.loaders;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.android.popularmoviesstage2.utils.NetworkUtils;
import com.example.android.popularmoviesstage2.model.Video;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Zsolt on 02.03.2018.
 */

public class VideoLoader extends AsyncTaskLoader<ArrayList<Video>> {

    /** Tag for log messages */
    private static final String LOG_TAG = VideoLoader.class.getName();

    /** Query URL */
    private String url;

    /**
     * Constructs a new {@link VideoLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public VideoLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "TEST: Video onStartLoading() called...");
        forceLoad();
    }

    @Override
    public ArrayList<Video> loadInBackground() {
        Log.i(LOG_TAG, "TEST: Video loadInBackground() called...");

        // Don't perform the request if there are no URLs, or the first URL is null.
        if (url == null) {
            return null;
        }

        // Perform the HTTP request for video data and process the response.
        ArrayList<Video> result = null;
        try {
            result = NetworkUtils.fetchVideoData(url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}

