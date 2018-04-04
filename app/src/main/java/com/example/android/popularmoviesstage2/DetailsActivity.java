package com.example.android.popularmoviesstage2;

import android.app.LoaderManager;
import android.content.ActivityNotFoundException;

import android.content.ContentValues;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesstage2.model.Movie;
import com.example.android.popularmoviesstage2.model.Review;
import com.example.android.popularmoviesstage2.adapters.ReviewAdapter;
import com.example.android.popularmoviesstage2.loaders.ReviewLoader;
import com.example.android.popularmoviesstage2.model.Video;
import com.example.android.popularmoviesstage2.adapters.VideoAdapter;
import com.example.android.popularmoviesstage2.loaders.VideoLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmoviesstage2.BuildConfig.API_KEY;
import com.example.android.popularmoviesstage2.data.FavouritesContract.FavouritesEntry;

/**
 * Created by Zsolt on 26.02.2018.
 * Code snippets were used from this Stack Overflow post in terms of multiple loader in same activity
 * https://stackoverflow.com/questions/15643907/multiple-loaders-in-same-activity
 */

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = DetailsActivity.class.getSimpleName();

    // Base URLs as string for backdrop and poster images
    private static final String BASE_BACKDROP_URL = "http://image.tmdb.org/t/p/w780/";
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    // Base URL of the film for sharing
    private static final String BASE_MOVIE_URL = "https://www.themoviedb.org/movie/";

    // Base URL for the videos or reviews
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie/";

    // Base URLs for the Youtube video to watch via app and web
    private static final String YOUTUBE_APP_URL = "vnd.youtube:";
    private static final String YOUTUBE_WEB_URL = "http://www.youtube.com/watch?v=";

    private Movie movie;
    private int movieId;

    private ArrayList<Video> videoList = new ArrayList<>();
    private VideoAdapter videoAdapter;
    private static final int VIDEO_LOADER_ID = 2;

    private ArrayList<Review> reviewList = new ArrayList<>();
    private ReviewAdapter reviewAdapter;
    private static final int REVIEW_LOADER_ID = 3;

    // Identify whether the movie is in our favourite database or not
    private boolean isFavourite;

    // Binding the views with Butter Knife
    @BindView(R.id.sv_details)
    ScrollView scrollView;
    @BindView(R.id.tv_title)
    TextView movieTitle;
    @BindView(R.id.iv_backdrop)
    ImageView movieBackdrop;
    @BindView(R.id.iv_poster_details)
    ImageView moviePoster;
    @BindView(R.id.tv_release_date)
    TextView movieDate;
    @BindView(R.id.tv_rate)
    TextView movieRate;
    @BindView(R.id.tv_overview)
    TextView movieOverview;
    @BindView(R.id.rv_video_list)
    RecyclerView recyclerViewVideo;
    @BindView(R.id.rv_review_list)
    RecyclerView recyclerViewReview;
    @BindView(R.id.tv_empty_review)
    TextView emptyReview;
    @BindView(R.id.tv_empty_video)
    TextView emptyVideo;
    @BindView(R.id.tv_favourite)
    TextView favourite;
    @BindView(R.id.ib_favourite)
    ImageButton buttonFavourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = this.getSupportActionBar();
        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        // Get the data from the intent
        Bundle data = getIntent().getExtras();
        movie = data.getParcelable(MainActivity.DETAILS_INTENT_KEY);

        movieId = movie.getMovieId();

        isFavourite = isInFavouriteDb(movieId);

        if(isFavourite){
            favourite.setText(R.string.favorites_added);
            buttonFavourite.setImageResource(R.drawable.ic_star);
        }

        // Set the movie title on the view
        movieTitle.setText(movie.getMovieTitle());

        // Set the backdrop image on the view
        String finalBackdropUrl = getImageUrl(BASE_BACKDROP_URL, movie.getMovieImageBackDropPath());
        Picasso.with(this)
                .load(finalBackdropUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(movieBackdrop);

        // Set the poster image on the view
        String finalPosterUrl = getImageUrl(BASE_POSTER_URL, movie.getMovieImagePosterPath());
        Picasso.with(this)
                .load(finalPosterUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(moviePoster);

        // Set the release date on the view
        movieDate.setText(movie.getMovieDate());

        // Set the movie rate on the view
        movieRate.setText(String.valueOf(movie.getMovieRate()));

        // Set the movie overview on the view
        movieOverview.setText(movie.getMovieOverview());

        // Attach a LayoutManager to this RecyclerView
        recyclerViewVideo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Set vertical divider among list items
        recyclerViewVideo.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL));

        videoAdapter = new VideoAdapter(videoList, new VideoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Video video) {
                String videoKey = video.getVideoKey();
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE_APP_URL + videoKey));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(YOUTUBE_WEB_URL + videoKey));
                try {
                    startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    startActivity(webIntent);
                }
            }
        });
        recyclerViewVideo.setAdapter(videoAdapter);

        // Attach a LayoutManager to this RecyclerView
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));

        // Set vertical divider among list items
        recyclerViewReview.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        reviewAdapter = new ReviewAdapter(reviewList);

        recyclerViewReview.setAdapter(reviewAdapter);

        // Set emptyReview invisible
        emptyReview.setVisibility(View.INVISIBLE);

        // Set emptyVideo invisible
        emptyVideo.setVisibility(View.INVISIBLE);

        // Get a reference to the LoaderManager, in order to interact with loaders.
        LoaderManager loaderManager = getLoaderManager();

        // Initialize the video loader
        loaderManager.initLoader(VIDEO_LOADER_ID, null, this);

        // Initialize the review loader
        loaderManager.initLoader(REVIEW_LOADER_ID, null, this);
    }


    // Helper method to identify whether the movie is already in our favourite database or not
    private boolean isInFavouriteDb(int movieId) {

        Cursor cursor = getContentResolver().query(
                FavouritesEntry.CONTENT_URI,
                null,
                FavouritesEntry.COLUMN_MOVIE_ID + "=?",
                new String[]{String.valueOf(movieId)},
                null);

        if(cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "TEST: Details Activity onCreateLoader() called...");
        if (id == VIDEO_LOADER_ID) {
            String videos = "videos";
            Log.i(LOG_TAG, "TEST: DetailsActivity videos URL: " + createUrl(movieId, videos));
            return new VideoLoader(this, createUrl(movieId, videos));
        } else if (id == REVIEW_LOADER_ID) {
            String reviews = "reviews";
            Log.i(LOG_TAG, "TEST: DetailsActivity reviews URL: " + createUrl(movieId, reviews));
            return new ReviewLoader(this, createUrl(movieId, reviews));
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object data) {
        Log.i(LOG_TAG, "TEST: Details Activity onLoadFinished() called...");

        switch (loader.getId()) {
            case VIDEO_LOADER_ID:
                videoList = (ArrayList<Video>) data;
                if (videoList != null && !videoList.isEmpty()) {
                    videoAdapter.setVideoList(videoList);
                    videoAdapter.notifyDataSetChanged();
                    emptyVideo.setVisibility(View.GONE);
                } else emptyVideo.setVisibility(View.VISIBLE);
                break;
            case REVIEW_LOADER_ID:
                reviewList = (ArrayList<Review>) data;
                if (reviewList != null && !reviewList.isEmpty()) {
                    reviewAdapter.setReviewList(reviewList);
                    reviewAdapter.notifyDataSetChanged();
                    emptyReview.setVisibility(View.GONE);
                } else emptyReview.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {
        Log.i(LOG_TAG, "TEST: DetailsActivity onLoadReset() called...");
        switch (loader.getId()) {
            case VIDEO_LOADER_ID:
                videoAdapter.setVideoList(null);
                break;
            case REVIEW_LOADER_ID:
                reviewAdapter.setReviewList(null);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sharing) {
                String sharedUrl = BASE_MOVIE_URL + movieId;
                ShareCompat.IntentBuilder.from(this)
                        .setChooserTitle("Share this movie")
                        .setType("text/plain")
                        .setText(sharedUrl)
                        .startChooser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Helper method to get the full image URL as a string for poster and backdrop
    private String getImageUrl(String baseUrl, String imagePath) {
        Uri baseUri = Uri.parse(baseUrl);
        Uri.Builder imageUri = baseUri.buildUpon();
        imageUri.appendEncodedPath(imagePath);

        return imageUri.toString();
    }

    // Helper method to create URL in order to get the videos or the reviews of the movie
    private String createUrl(int movieId, String option) {
        // Create the query URL and pass it into VideoLoader as String
        Uri baseUri = Uri.parse(BASE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(String.valueOf(movieId));
        uriBuilder.appendPath(option);
        uriBuilder.appendQueryParameter("api_key", API_KEY);

        return uriBuilder.toString();
    }

    public void onClickFavourite(View view) {
        if(isFavourite) {
            String stringId = Integer.toString(movieId);
            Uri uri = FavouritesEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            // Delete a single row of data using a ContentResolver
            getContentResolver().delete(uri, null, null);
            // Change the text to "Add to favourites:"
            favourite.setText(R.string.favorites_add);
            buttonFavourite.setImageResource(R.drawable.ic_star_border);
            isFavourite = false;
            Toast.makeText(DetailsActivity.this, R.string.toast_removed_db, Toast.LENGTH_SHORT).show();
        } else {
            // Create new empty ContentValues object
            ContentValues contentValues = new ContentValues();
            // Put all movie related info into the ContentValues
            contentValues.put(FavouritesEntry.COLUMN_MOVIE_ID, movie.getMovieId());
            contentValues.put(FavouritesEntry.COLUMN_MOVIE_BACKDROP_PATH, movie.getMovieImageBackDropPath());
            contentValues.put(FavouritesEntry.COLUMN_MOVIE_POSTER_PATH, movie.getMovieImagePosterPath());
            contentValues.put(FavouritesEntry.COLUMN_MOVIE_TITLE, movie.getMovieTitle());
            contentValues.put(FavouritesEntry.COLUMN_MOVIE_OVERVIEW, movie.getMovieOverview());
            contentValues.put(FavouritesEntry.COLUMN_MOVIE_RATE, movie.getMovieRate());
            contentValues.put(FavouritesEntry.COLUMN_MOVIE_DATE, movie.getMovieDate());
            // Insert the movie as a new row into favourites table
            getContentResolver().insert(FavouritesEntry.CONTENT_URI, contentValues);
            // Change the text to "Added as favorite:"
            favourite.setText(R.string.favorites_added);
            buttonFavourite.setImageResource(R.drawable.ic_star);
            isFavourite = true;
            Toast.makeText(DetailsActivity.this, R.string.toast_added_db, Toast.LENGTH_SHORT).show();
        }
    }
}
