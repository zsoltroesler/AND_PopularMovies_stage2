package com.example.android.popularmoviesstage2.model;

/**
 * Created by Zsolt on 02.03.2018.
 */

public class Video {

    private String videoId;
    private String videoName;
    private String videoKey;

    /**
     * Constructs a new {@link Video} object.
     *
     * @param videoId        is the id of the video
     * @param videoName      is the name of video on Youtube
     * @param videoKey       is the path to URL for the video on Youtube
     */
    public Video(String videoId, String videoName, String videoKey) {
        this.videoId = videoId;
        this.videoName = videoName;
        this.videoKey = videoKey;
    }

    public String getVideoName() { return videoName; }

    public String getVideoKey() { return  videoKey; }
}
