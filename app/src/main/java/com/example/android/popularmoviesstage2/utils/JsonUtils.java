package com.example.android.popularmoviesstage2.utils;

/*
  Created by Zsolt on 26.02.2018.
 */

import android.text.TextUtils;
import android.util.Log;

import com.example.android.popularmoviesstage2.model.Movie;
import com.example.android.popularmoviesstage2.model.Review;
import com.example.android.popularmoviesstage2.model.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Utility functions to handle JSON data from themoviedb.org API.
 */
public class JsonUtils {

    /** Tag for the log messages */
    private static final String LOG_TAG = JsonUtils.class.getSimpleName();

    /** Keys for parsing Movie JSON response */
    public static final String KEY_RESULTS = "results";
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_POSTER_PATH = "poster_path";
    public static final String KEY_BACKDROP_PATH = "backdrop_path";
    public static final String KEY_OVERVIEW = "overview";
    public static final String KEY_DATE = "release_date";
    public static final String KEY_RATE = "vote_average";

    /** Keys for parsing Video JSON response */
    public static final String KEY_VIDEO_RESULTS = "results";
    public static final String KEY_VIDEO_ID = "id";
    public static final String KEY_VIDEO_NAME = "name";
    public static final String KEY_VIDEO_KEY = "key";

    /** Keys for parsing Review JSON response */
    public static final String KEY_REVIEW_RESULTS = "results";
    public static final String KEY_REVIEW_ID = "id";
    public static final String KEY_REVIEW_CONTENT = "content";

    /**
     * Return a list of {@link Movie} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Movie> parseMovieJson(String json) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding movies to
        ArrayList<Movie> movieList = new ArrayList<>();

        // Try to parse the JSON response.
        try {
            JSONObject rootJsonObject = new JSONObject(json);
            JSONArray results = rootJsonObject.optJSONArray(KEY_RESULTS);

            // For each movie in the results, create a {@link Movie} object
            for (int i = 0; i < results.length(); i++) {

                // Get a single movie at position i within the results array
                JSONObject singleMovie = results.getJSONObject(i);

                // Extract the value for the key called "id"
                int movieId = singleMovie.optInt(KEY_ID);

                // Extract the value for the key called "title"
                String movieTitle = singleMovie.optString(KEY_TITLE);

                // Extract the value for the key called "poster_path"
                String movieImagePoster = singleMovie.optString(KEY_POSTER_PATH);

                // Extract the value for the key called "backdrop_path"
                String movieImageBackdrop = singleMovie.optString(KEY_BACKDROP_PATH);

                // Extract the value for the key called "overview"
                String movieOverview = singleMovie.optString(KEY_OVERVIEW);

                // Extract the value for the key called "release_date"
                String movieDate = singleMovie.optString(KEY_DATE);

                // Extract the value for the key called "vote_average"
                double movieRate = singleMovie.optDouble(KEY_RATE);

                Movie movie = new Movie(movieId, movieTitle, movieImagePoster, movieImageBackdrop, movieOverview, movieDate, movieRate);

                // Add the Movie object to the list
                movieList.add(movie);
            }
        }
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON response", e);
        }
        return movieList;
    }

    /**
     * Return a list of {@link Video} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Video> parseVideoJson(String json) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding videos to
        ArrayList<Video> videoList = new ArrayList<>();

        // Try to parse the JSON response.
        try {
            JSONObject rootJsonVideoObject = new JSONObject(json);
            JSONArray resultsVideo = rootJsonVideoObject.optJSONArray(KEY_VIDEO_RESULTS);

            // For each video in the resultsVideo, create a {@link Video} object
            for (int i = 0; i < resultsVideo.length(); i++) {

                // Get a single movie at position i within the results array
                JSONObject singleVideo = resultsVideo.getJSONObject(i);

                // Extract the value for the key called "id"
                String videoId = singleVideo.optString(KEY_VIDEO_ID);

                // Extract the value for the key called "name"
                String videoName = singleVideo.optString(KEY_VIDEO_NAME);

                // Extract the value for the key called "key"
                String videoKey = singleVideo.optString(KEY_VIDEO_KEY);

                Video video = new Video(videoId, videoName, videoKey);

                // Add the Video object to the list
                videoList.add(video);
            }
        }
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON response", e);
        }
        return videoList;
    }

    /**
     * Return a list of {@link Review} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Review> parseReviewJson(String json) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding videos to
        ArrayList<Review> reviewList = new ArrayList<>();

        // Try to parse the JSON response.
        try {
            JSONObject rootJsonReviewObject = new JSONObject(json);
            JSONArray resultsReview = rootJsonReviewObject.optJSONArray(KEY_REVIEW_RESULTS);

            // For each video in the resultsVideo, create a {@link Video} object
            for (int i = 0; i < resultsReview.length(); i++) {

                // Get a single review at position i within the results array
                JSONObject singleReview = resultsReview.getJSONObject(i);

                // Extract the value for the key called "id"
                String reviewId = singleReview.optString(KEY_REVIEW_ID);

                // Extract the value for the key called "content"
                String reviewContent = singleReview.optString(KEY_REVIEW_CONTENT);

                Review review = new Review(reviewId, reviewContent);

                // Add the Video object to the list
                reviewList.add(review);
            }
        }
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the JSON response", e);
        }
        return reviewList;
    }
}
