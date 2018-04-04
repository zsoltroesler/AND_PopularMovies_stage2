package com.example.android.popularmoviesstage2.utils;

/*
  Created by Zsolt on 26.02.2018.
  This class is used from my previous Udacity`s ABND projects
 */

import android.util.Log;

import com.example.android.popularmoviesstage2.model.Movie;
import com.example.android.popularmoviesstage2.model.Review;
import com.example.android.popularmoviesstage2.model.Video;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper class related to requesting and receiving movie list from themoviedb.org API
 */
public class NetworkUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link NetworkUtils} object.
     * This class is only meant to hold static variables and methods.
     */
    private NetworkUtils() {
    }

    /**
     * Query the themoviedb.org API data set and return an {@link Movie} object to represent a list of movies.
     */
    public static ArrayList<Movie> fetchMovieData(String requestUrl) throws JSONException {
        Log.i(LOG_TAG, "TEST: fetchMovieData() called...");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Return the {@link Movie}
        return JsonUtils.parseMovieJson(jsonResponse);
    }

    /**
     * Query the themoviedb.org API data set and return an {@link Video} object to represent a list
     * of videos of the particular movie.
     */
    public static ArrayList<Video> fetchVideoData(String requestUrl) throws JSONException {
        Log.i(LOG_TAG, "TEST: fetchVideoData() called...");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Return the {@link Video}
        return JsonUtils.parseVideoJson(jsonResponse);
    }

    /**
     * Query the themoviedb.org API data set and return an {@link Review} object to represent a list
     * of reviews of the particular movie.
     */
    public static ArrayList<Review> fetchReviewData(String requestUrl) throws JSONException {
        Log.i(LOG_TAG, "TEST: fetchVideoData() called...");

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Return the {@link Review}
        return JsonUtils.parseReviewJson(jsonResponse);
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the movie JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}

