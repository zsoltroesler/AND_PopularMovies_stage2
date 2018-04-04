package com.example.android.popularmoviesstage2.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Zsolt on 08.03.2018.
 */

public class FavouritesContract {

    /** ContentProvider Name */
    public static final String CONTENT_AUTHORITY = "com.example.android.popularmoviesstage2";

    /** ContentProvider base URI */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /** Possible path (appended to base content URI for possible URI's) */
    public static final String PATH_FAVOURITES = "favourites";

    /**
     * To prevent someone from accidentally instantiating the contract class,
     * give it an empty constructor.
     */
    private FavouritesContract() {}

     /* FavouritesEntry is an inner class that defines the contents of the favourites table */
    public static final class FavouritesEntry implements BaseColumns {

         /** The content URI to access the data in the provider */
         public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FAVOURITES);

         /** MIME type of the {@link #CONTENT_URI} for a list of favourite movies*/
         public static final String CONTENT_LIST_TYPE =
                 ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

         /** MIME type of the {@link #CONTENT_URI} for a single favourite movie */
         public static final String CONTENT_ITEM_TYPE =
                 ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVOURITES;

         /** Name of database table for favourites */
         public final static String TABLE_NAME = "favourites";

         /** Column names */
         public static final String COLUMN_MOVIE_ID = "movie_id";
         public static final String COLUMN_MOVIE_BACKDROP_PATH = "backdrop_path";
         public static final String COLUMN_MOVIE_POSTER_PATH = "poster_path";
         public static final String COLUMN_MOVIE_TITLE = "title";
         public static final String COLUMN_MOVIE_OVERVIEW = "overview";
         public static final String COLUMN_MOVIE_RATE = "rate";
         public static final String COLUMN_MOVIE_DATE = "date";
     }

}
