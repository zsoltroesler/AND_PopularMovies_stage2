package com.example.android.popularmoviesstage2;

import android.app.SearchManager;
import android.content.ComponentName;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.support.v7.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.adapters.FavouriteCursorAdapter;
import com.example.android.popularmoviesstage2.model.Movie;
import com.example.android.popularmoviesstage2.adapters.MovieAdapter;
import com.example.android.popularmoviesstage2.loaders.MovieLoader;
import com.example.android.popularmoviesstage2.data.FavouritesContract.FavouritesEntry;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Object> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * API key to be able to run query on themoviedb.org data set
     */
    private static final String API_KEY = BuildConfig.API_KEY;

    /**
     * base URL for movie list data from the themoviedb.org data set
     */
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";

    /**
     * Loaders
     */
    private static final int MOVIE_LOADER_ID = 11;
    private static final int FAVOURITES_LOADER_ID = 12;

    private static final String CURRENT_LOADER_ID = "currentLoaderId";
    public static final String DETAILS_INTENT_KEY = "movieDetails";

    private ArrayList<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private FavouriteCursorAdapter favouriteCursorAdapter;
    private GridLayoutManager layoutManager;
    private String listBy = "";
    private int currentLoaderId;

    @BindView(R.id.rv_movie_list)
    RecyclerView recyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.tv_empty_state)
    TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "TEST: MainActivity onCreate() called...");

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        layoutManager = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(layoutManager);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        listBy = sharedPrefs.getString(
                getString(R.string.pref_key_list),
                getString(R.string.pref_default_list));

        // Set up the app title
        switch (listBy) {
            case "top_rated":
                setTitle(getString(R.string.title_top_rated));
                break;
            case "now_playing":
                setTitle(getString(R.string.title_now_playing));
                break;
            case "favourites":
                setTitle(getString(R.string.title_favourites));
                break;
            default:
                setTitle(getString(R.string.title_popular));
                break;
        }

        if (savedInstanceState == null) {
            selectLoader();
        } else {
            if (selectLoader()) {
                currentLoaderId = savedInstanceState.getInt(CURRENT_LOADER_ID);
                getSupportLoaderManager().restartLoader(currentLoaderId, null, this);
            } else {
                progressBar.setVisibility(View.GONE);
                emptyStateTextView.setText(getString(R.string.no_internet_connection));
            }
        }
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: MainActivity onCreateLoader() called...");
        if (id == MOVIE_LOADER_ID) {
            Log.i(LOG_TAG, "TEST: onCreateLoader() URL: " + movieUrl());
            return new MovieLoader(this, movieUrl());
        } else if (id == FAVOURITES_LOADER_ID) {
            String[] projection = new String[]{
                    FavouritesEntry.COLUMN_MOVIE_ID,
                    FavouritesEntry.COLUMN_MOVIE_BACKDROP_PATH,
                    FavouritesEntry.COLUMN_MOVIE_POSTER_PATH,
                    FavouritesEntry.COLUMN_MOVIE_TITLE,
                    FavouritesEntry.COLUMN_MOVIE_OVERVIEW,
                    FavouritesEntry.COLUMN_MOVIE_RATE,
                    FavouritesEntry.COLUMN_MOVIE_DATE
            };
            Log.i(LOG_TAG, "TEST: onCreateLoader(): FAVOURITES_LOADER_ID");
            return new CursorLoader(this, FavouritesEntry.CONTENT_URI, projection, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        Log.i(LOG_TAG, "TEST: MainActivity onLoadFinished() called...");

        // Hide loading indicator because the data has been loaded
        progressBar.setVisibility(View.GONE);

        // Clear the adapter with previous data
        recyclerView.setAdapter(null);

        switch (loader.getId()) {
            case MOVIE_LOADER_ID:
                if (data == null) {
                    emptyStateTextView.setText(getString(R.string.no_movies));
                } else {
                    movieList = (ArrayList<Movie>) data;
                    movieAdapter = new MovieAdapter(movieList, new MovieAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Movie movie) {
                            Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
                            detailsIntent.putExtra(DETAILS_INTENT_KEY, movie);
                            startActivity(detailsIntent);
                        }
                    });
                    recyclerView.setAdapter(movieAdapter);
                    movieAdapter.setMovieList(movieList);
                    movieAdapter.notifyDataSetChanged();
                }
                break;
            case FAVOURITES_LOADER_ID:
                Cursor cursor = (Cursor) data;
                if (cursor.getCount() == 0) {
                    emptyStateTextView.setText(getString(R.string.empty_favourites));
                } else {
                    favouriteCursorAdapter = new FavouriteCursorAdapter(this, new FavouriteCursorAdapter.FavouriteOnItemClickListener() {
                        @Override
                        public void onItemClick(Movie movie) {
                            Intent detailsIntent = new Intent(MainActivity.this, DetailsActivity.class);
                            detailsIntent.putExtra(DETAILS_INTENT_KEY, movie);
                            startActivity(detailsIntent);
                        }
                    });
                    recyclerView.setAdapter(favouriteCursorAdapter);
                    favouriteCursorAdapter.swapCursor((Cursor) data);
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
        Log.i(LOG_TAG, "TEST: MainActivity onLoadReset() called...");
        switch (loader.getId()) {
            case MOVIE_LOADER_ID:
                getSupportLoaderManager().destroyLoader(MOVIE_LOADER_ID);
                break;
            case FAVOURITES_LOADER_ID:
                getSupportLoaderManager().destroyLoader(FAVOURITES_LOADER_ID);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(MainActivity.this, SearchResultsActivity.class)));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
                startActivity(startSettingsActivity);
                return true;

            case R.id.action_refresh:
                selectLoader();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_LOADER_ID, currentLoaderId);
    }

    // Helper method to initialize the corresponding loader
    private boolean selectLoader(){
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // First identify which loader should be used. MOVIE_LOADER_ID is responsible for all lists
        // except favourites
        if (listBy.equals(getString(R.string.pref_value_favourites))) {
            currentLoaderId = FAVOURITES_LOADER_ID;
            getSupportLoaderManager().initLoader(FAVOURITES_LOADER_ID, null, this);
            return true;
        } else {
            if (networkInfo != null && networkInfo.isConnected()) {
                currentLoaderId = MOVIE_LOADER_ID;
                getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
                // Hide this TextView for the scenario if we refresh data after lost internet connection.
                emptyStateTextView.setVisibility(View.GONE);
                return true;
            } else {
                progressBar.setVisibility(View.GONE);
                emptyStateTextView.setText(getString(R.string.no_internet_connection));
                return false;
            }
        }
    }

    // Helper method to calculate dynamically to number of columns of gridlayout
    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    // Helper method to get the movie URL to the data base
    private String movieUrl() {
        // Create the query URL and pass it into MovieLoader as String
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(listBy);
        uriBuilder.appendQueryParameter("api_key", API_KEY);

        return uriBuilder.toString();
    }
}

