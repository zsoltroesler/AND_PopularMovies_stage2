package com.example.android.popularmoviesstage2.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zsolt on 26.02.2018.
 */

public class Movie implements Parcelable {

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private int movieId;
    private String movieTitle;
    private String movieImagePoster;
    private String movieImageBackdrop;
    private String movieOverview;
    private String movieDate;
    private double movieRate;

    /**
     * Constructs a new {@link Movie} object.
     *
     * @param movieId        is the id of the movie
     * @param movieTitle         is the title of the movie
     * @param movieImagePoster   is the path to URL for the movie poster image
     * @param movieImageBackdrop is the path to URL for the movie backdrop image
     * @param movieOverview      is a plot synopsis about the movie
     * @param movieDate          is the release date of the movie
     * @param movieRate          is the user rating of the movie
     */
    public Movie(int movieId, String movieTitle, String movieImagePoster, String movieImageBackdrop, String movieOverview, String movieDate, double movieRate) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.movieImagePoster = movieImagePoster;
        this.movieImageBackdrop = movieImageBackdrop;
        this.movieOverview = movieOverview;
        this.movieDate = movieDate;
        this.movieRate = movieRate;
    }

    // Parcelling part
    public Movie(Parcel in) {
        movieId = in.readInt();
        movieTitle = in.readString();
        movieImagePoster = in.readString();
        movieImageBackdrop = in.readString();
        movieOverview = in.readString();
        movieDate = in.readString();
        movieRate = in.readDouble();
    }

    public int getMovieId() { return movieId; }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieImagePosterPath() {
        return movieImagePoster;
    }

    public String getMovieImageBackDropPath() {
        return movieImageBackdrop;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public double getMovieRate() {
        return movieRate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(movieTitle);
        dest.writeString(movieImagePoster);
        dest.writeString(movieImageBackdrop);
        dest.writeString(movieOverview);
        dest.writeString(movieDate);
        dest.writeDouble(movieRate);
    }
}

