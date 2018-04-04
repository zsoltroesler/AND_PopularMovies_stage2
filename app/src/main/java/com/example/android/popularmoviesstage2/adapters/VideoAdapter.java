package com.example.android.popularmoviesstage2.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage2.R;
import com.example.android.popularmoviesstage2.model.Video;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 02.03.2018.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = VideoAdapter.class.getSimpleName();

    public interface OnItemClickListener {
        void onItemClick(Video video);
    }

    public static final String IMAGE_URL_FIRST_PART =  "https://img.youtube.com/vi/";
    public static final String IMAGE_URL_THIRD_PART = "mqdefault.jpg";

    private ArrayList<Video> videoList;
    private final OnItemClickListener listener;

    /**
     * Create a new {@link VideoAdapter} object.
     *
     * @param videoList is the list of {@link Video}s to be displayed.
     * @param listener  is an object of OnItemClickListener.
     */
    public VideoAdapter(ArrayList<Video> videoList, OnItemClickListener listener) {
        this.videoList = videoList;
        this.listener = listener;
    }

    // Create the ViewHolder class for references
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_video)
        ImageView videoImage;
        @BindView(R.id.tv_video_name)
        TextView videoName;

        // Public constructor, instantiate all of the references to the private variables
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final Video video, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(video);
                }
            });
        }
    }

    @Override
    public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(VideoAdapter.ViewHolder holder, int position) {
        // Get the {@link Video} object located at this position
        final Video currentVideo = videoList.get(position);
        holder.videoName.setText(currentVideo.getVideoName());

        String videoKey = currentVideo.getVideoKey();
        Picasso.with(holder.videoImage.getContext())
                .load(createYoutubeImageUrl(videoKey))
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_error_placeholder)
                .into(holder.videoImage);

        holder.bind(currentVideo, listener);
    }

    @Override
    public int getItemCount() {
        if (videoList == null || videoList.isEmpty()) {
            return 0;
        } else {
            return this.videoList.size();
        }
    }

    // Helper method to set new video list or clear the previous one
    public void setVideoList(ArrayList<Video> videoList) {
        this.videoList = videoList;
    }

    // Helper method to create URL in order to get the videos or the reviews of the movie
    private String createYoutubeImageUrl(String videoKey) {
        // Create the query URL and pass it into VideoLoader as String
        Uri baseUri = Uri.parse(IMAGE_URL_FIRST_PART);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendPath(videoKey);
        uriBuilder.appendPath(IMAGE_URL_THIRD_PART);
        Log.i(LOG_TAG, "TEST: VideoAdapter Youtube URL: " + uriBuilder.toString());
        return uriBuilder.toString();
    }
}
