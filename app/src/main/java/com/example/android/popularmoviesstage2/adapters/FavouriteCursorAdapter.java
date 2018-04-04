package com.example.android.popularmoviesstage2.adapters;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.data.FavouritesContract.FavouritesEntry;
import com.example.android.popularmoviesstage2.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 12.03.2018.
 */

public class FavouriteCursorAdapter extends RecyclerView.Adapter<FavouriteCursorAdapter.ViewHolder> {

    public interface FavouriteOnItemClickListener {
        void onItemClick(Movie movie);
    }

    private Context context;
    private Cursor cursor;
    private final FavouriteOnItemClickListener favouriteOnItemClickListener;
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    /**
     * Create a new {@link FavouriteCursorAdapter} object.
     *
     * @param context                      bridge between UI and app resources
     * @param favouriteOnItemClickListener is an object of FavouriteOnItemClickListener.
     */
    public FavouriteCursorAdapter(Context context, FavouriteOnItemClickListener favouriteOnItemClickListener) {
        this.context = context;
        this.favouriteOnItemClickListener = favouriteOnItemClickListener;
    }

    @Override
    public FavouriteCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FavouriteCursorAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (cursor == null || cursor.getCount() == 0) {
            return;
        }

        cursor.moveToPosition(position);

        final Movie movie = favouriteMovie(position);

        String finalPosterUrl = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_POSTER_PATH));

        Picasso.with(context)
                .load(getImageUrl(BASE_POSTER_URL, finalPosterUrl))
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(holder.posterView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favouriteOnItemClickListener.onItemClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor == null) return 0;
        return cursor.getCount();
    }

    // Helper method to swap new favourites list or reset the previous one.
    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        if (cursor != null) {
            notifyDataSetChanged();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_poster)
        ImageView posterView;

        // Public constructor, instantiate all of the references to the private variables
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Helper method to create a Movie object from cursor data
    private Movie favouriteMovie(int position) {

        cursor.moveToPosition(position);

        String stringId = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_ID));
        int movieId = Integer.valueOf(stringId);

        String movieTitle = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_TITLE));

        String movieImagePoster = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_POSTER_PATH));

        String movieImageBackdrop = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_BACKDROP_PATH));

        String movieOverview = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_OVERVIEW));

        String movieDate = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_DATE));

        String stringRate = cursor.getString(cursor.getColumnIndex(FavouritesEntry.COLUMN_MOVIE_RATE));
        double movieRate = Double.valueOf(stringRate);

        return new Movie(movieId, movieTitle, movieImagePoster, movieImageBackdrop,
                movieOverview, movieDate, movieRate);
    }

    // Helper method to get the full image URL as a string
    private String getImageUrl(String baseUrl, String posterPath) {

        Uri baseUri = Uri.parse(baseUrl);
        Uri.Builder posterUri = baseUri.buildUpon();
        posterUri.appendEncodedPath(posterPath);

        return posterUri.toString();
    }
}
