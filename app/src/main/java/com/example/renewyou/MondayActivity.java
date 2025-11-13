package com.example.renewyou; // change to your package

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class MondayActivity extends AppCompatActivity {

    private ImageButton btnBackMonday;
    private VideoView videoCatCow, videoChildPose, videoBreathing, videoMeditation, videoWalking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monday);

        btnBackMonday = findViewById(R.id.btnBackMonday);
        btnBackMonday.setOnClickListener(v -> finish());

        // Initialize VideoViews
        videoCatCow = findViewById(R.id.videoCatCow);
        videoChildPose = findViewById(R.id.videoChildPose);
        videoBreathing = findViewById(R.id.videoBreathing);
        videoMeditation = findViewById(R.id.videoMeditation);
        videoWalking = findViewById(R.id.videoWalking);

        // Load local or remote video URIs
        setVideo(videoCatCow, "android.resource://" + getPackageName() + "/" + R.raw.cat_cow);
        setVideo(videoChildPose, "android.resource://" + getPackageName() + "/" + R.raw.child_pose);
        setVideo(videoBreathing, "android.resource://" + getPackageName() + "/" + R.raw.breathing);
        setVideo(videoMeditation, "android.resource://" + getPackageName() + "/" + R.raw.meditation);
        setVideo(videoWalking, "android.resource://" + getPackageName() + "/" + R.raw.walking);
    }

    private void setVideo(VideoView videoView, String uriPath) {
        Uri uri = Uri.parse(uriPath);
        videoView.setFocusable(false);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.setOnPreparedListener(mp -> {
            videoView.seekTo(1); // show preview frame
            videoView.clearFocus();
        });
    }
}
