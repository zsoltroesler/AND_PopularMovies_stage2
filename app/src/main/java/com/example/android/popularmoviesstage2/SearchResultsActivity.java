package com.example.android.popularmoviesstage2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.adapters.MovieAdapter;
import com.example.android.popularmoviesstage2.loaders.MovieLoader;
import com.example.android.popularmoviesstage2.model.Movie;
import com.example.android.popularmoviesstage2.MainActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 14.03.2018.
 * This activity presents the search result from SearchView in MainActivity
 */

public class SearchResultsActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = SearchResultsActivity.class.getSimpleName();

    /**
     * API key to be able to run query on themoviedb.org data set
     */
    private static final String API_KEY = BuildConfig.API_KEY;

    /**
     * base URL for movie list data from the themoviedb.org data set
     */
    private static final String BASE_URL = "https://api.themoviedb.org/3/search/movie";

    private static final String SAVED_RESULT_LIST = "results";

    private static final int SEARCH_LOADER_ID = 16;

    private ArrayList<Movie> resultList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private GridLayoutManager layoutManager;
    private String searchUrl;

    @BindView(R.id.rv_movie_list)
    RecyclerView recyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar progressBar;
    @BindView(R.id.tv_empty_state)
    TextView emptyStateTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleIntent(getIntent());

        ButterKnife.bind(this);

        progressBar.setVisibility(View.VISIBLE);

        layoutManager = new GridLayoutManager(this, numberOfColumns());
        recyclerView.setLayoutManager(layoutManager);

        movieAdapter = new MovieAdapter(resultList, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Movie movie) {
                Intent detailsIntent = new Intent(SearchResultsActivity.this, DetailsActivity.class);
                detailsIntent.putExtra(MainActivity.DETAILS_INTENT_KEY, movie);
                startActivity(detailsIntent);
            }
        });
        recyclerView.setAdapter(movieAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (savedInstanceState == null) {
            if (networkInfo != null && networkInfo.isConnected()) {
                getSupportLoaderManager().initLoader(SEARCH_LOADER_ID, null, this);
            } else {
                progressBar.setVisibility(View.GONE);
                emptyStateTextView.setText(getString(R.string.no_internet_connection));
            }
        } else {
            resultList = savedInstanceState.getParcelableArrayList(SAVED_RESULT_LIST);
            movieAdapter.setMovieList(resultList);
            progressBar.setVisibility(View.GONE);
            if (resultList == null || resultList.isEmpty()){
                emptyStateTextView.setText(getString(R.string.no_movies));
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: SearchResultsActivity onCreateLoader() URL: " + searchUrl);
        return new MovieLoader(this, searchUrl);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        // Hide loading indicator because the data has been loaded
        progressBar.setVisibility(View.GONE);

        // If there is a valid list of {@link Movie}, then add them to the adapter's
        // data set. This will trigger the RecyclerView to update.
        if (data != null && !data.isEmpty()) {
            resultList = data;
            movieAdapter.setMovieList(data);
            movieAdapter.notifyDataSetChanged();
        } else {
            emptyStateTextView.setText(getString(R.string.no_movies));
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {
        movieAdapter.setMovieList(null);
    }

    // Handling the received intent from SearchView in MainActivity
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            // Create the query URL and pass it into MovieLoader as String
            Uri baseUri = Uri.parse(BASE_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("api_key", API_KEY);
            uriBuilder.appendQueryParameter("query", query);

            searchUrl = uriBuilder.toString();
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_RESULT_LIST, resultList);
    }
}
