package com.example.android.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.android.popularmoviesstage2.data.FavouritesContract.FavouritesEntry.TABLE_NAME;
import com.example.android.popularmoviesstage2.data.FavouritesContract.FavouritesEntry;

/**
 * Created by Zsolt on 08.03.2018.
 */

public class FavouritesContentProvider extends ContentProvider {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = FavouritesContentProvider.class.getSimpleName();

    /**
     * URI matcher code for the content URI for the products table
     */
    private static final int FAVOURITES = 100;

    /**
     * URI matcher code for the content URI for a single product in the products table
     */
    private static final int FAVOURITES_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // Access to the whole favourites table
        sUriMatcher.addURI(FavouritesContract.CONTENT_AUTHORITY, FavouritesContract.PATH_FAVOURITES, FAVOURITES);

        // Access to one single row in favourites table
        sUriMatcher.addURI(FavouritesContract.CONTENT_AUTHORITY, FavouritesContract.PATH_FAVOURITES + "/#", FAVOURITES_ID);
    }

    /**
     * Database helper object
     */
    private FavouritesDbHelper favouritesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        favouritesDbHelper = new FavouritesDbHelper(context);
        return true;
    }

    // Implement query to handle requests for data by URI
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        // Get access to underlying database (read-only for query)
        final SQLiteDatabase db = favouritesDbHelper.getReadableDatabase();

        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch (match) {
            // Query for the favourites directory
            case FAVOURITES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVOURITES_ID:
                selection = FavouritesEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // Return Cursor containing that row of the table, which was queried
                retCursor = db.query(FavouritesEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    // Implement insert() to handle requests to insert a single new row of data
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        // Get access to the task database (to write new data to)
        final SQLiteDatabase db = favouritesDbHelper.getWritableDatabase();

        // Write URI matching code to identify the match for the tasks directory
        int match = sUriMatcher.match(uri);
        Uri returnUri; // URI to be returned

        switch (match) {
            case FAVOURITES:
                // Insert new values into the database
                long id = db.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(FavouritesEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    // Implement delete(). We only delete a single row of data not the whole table
    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = favouritesDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int movieDeleted;

        switch (match) {
            case FAVOURITES_ID:
                // Delete a single row given by the ID in the URI
                selection = FavouritesEntry.COLUMN_MOVIE_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
                movieDeleted = db.delete(FavouritesEntry.TABLE_NAME, selection, selectionArgs);
                Log.i(LOG_TAG, "Movie deleted: " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (movieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return movieDeleted;
    }

    // Not implemented as there would be no update on a single movie only inserting or deleting
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case FAVOURITES:
                return FavouritesEntry.CONTENT_LIST_TYPE;
            case FAVOURITES_ID:
                return FavouritesEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
