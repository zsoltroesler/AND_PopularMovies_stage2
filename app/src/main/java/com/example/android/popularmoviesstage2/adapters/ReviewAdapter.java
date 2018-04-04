package com.example.android.popularmoviesstage2.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.model.Review;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 03.03.2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = ReviewAdapter.class.getSimpleName();

    private ArrayList<Review> reviewList;

    /**
     * Create a new {@link ReviewAdapter} object.
     *
     * @param reviewList is the list of {@link Review}s to be displayed.
     */
    public ReviewAdapter(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }

    // Create the ViewHolder class for references
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_review)
        TextView reviewContent;

        // Public constructor, instantiate all of the references to the private variables
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ReviewAdapter.ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
        Log.i(LOG_TAG, "REVIEWADAPTER: Review position:" + position);
        // Get the {@link Review} object located at this position
        final Review currentReview = reviewList.get(position);
        holder.reviewContent.setText(currentReview.getReviewContent());
    }

    @Override
    public int getItemCount() {
        if (reviewList == null || reviewList.isEmpty()) {
            return 0;
        } else {
            return this.reviewList.size();
        }
    }

    // Helper method to set new review list or clear the previous one
    public void setReviewList(ArrayList<Review> reviewList) {
        this.reviewList = reviewList;
    }
}
