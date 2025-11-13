package com.example.renewyou;

import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class ThursdayActivity extends AppCompatActivity {

    private ImageButton btnBackThursday;
    private VideoView videoTaiChiClouds, videoMountainPose, videoChildPose, videoDeepBreathing, videoWalking15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thursday);

        btnBackThursday = findViewById(R.id.btnBackThursday);
        if (btnBackThursday != null) btnBackThursday.setOnClickListener(v -> finish());

        videoTaiChiClouds = findViewById(R.id.videoTaiChiClouds);
        videoMountainPose = findViewById(R.id.videoMountainPose);
        videoChildPose = findViewById(R.id.videoChildPose);
        videoDeepBreathing = findViewById(R.id.videoDeepBreathing);
        videoWalking15 = findViewById(R.id.videoWalking15);

        setVideoByName(videoTaiChiClouds, "tai_chi_clouds");
        setVideoByName(videoMountainPose, "mountain_pose");
        setVideoByName(videoChildPose, "child_pose");
        setVideoByName(videoDeepBreathing, "deep_breathing");
        setVideoByName(videoWalking15, "walking");
    }

    private void setVideoByName(VideoView videoView, String rawName) {
        if (videoView == null) return;
        int resId = getResources().getIdentifier(rawName, "raw", getPackageName());
        if (resId == 0) {
            videoView.setVisibility(View.GONE);
            return;
        }
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + resId);
        videoView.setFocusable(false);
        videoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(this);
        videoView.setMediaController(mediaController);
        mediaController.setAnchorView(videoView);
        videoView.setOnPreparedListener(mp -> {
            videoView.seekTo(1);
            videoView.clearFocus();
        });
    }
}