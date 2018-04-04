package com.example.android.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmoviesstage2.data.FavouritesContract.FavouritesEntry;

/**
 * Created by Zsolt on 08.03.2018.
 */

public class FavouritesDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = FavouritesDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "favourites.db";

    /** Database version */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link FavouritesDbHelper}.
     * @param context of the app
     */
    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create SQL statement to create the favourites table
        final String CREATE_TABLE = "CREATE TABLE " + FavouritesEntry.TABLE_NAME + " (" +
                FavouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouritesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                FavouritesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                FavouritesEntry.COLUMN_MOVIE_RATE + " REAL NOT NULL, " +
                FavouritesEntry.COLUMN_MOVIE_DATE + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    /**
     * This is called when the database is upgraded according to changes of database schema
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavouritesEntry.TABLE_NAME);
        onCreate(db);
    }
}
