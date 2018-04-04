package com.example.android.popularmoviesstage2.model;

/**
 * Created by Zsolt on 03.03.2018.
 */

public class Review {

    private String reviewId;
    private String reviewContent;

    /**
     * Constructs a new {@link Review} object.
     *
     * @param reviewId        is the id of the video
     * @param reviewContent      is the name of video on Youtube
     */
    public Review(String reviewId, String reviewContent) {
        this.reviewId = reviewId;
        this.reviewContent = reviewContent;
    }

    public String getReviewContent() { return reviewContent; }
}
