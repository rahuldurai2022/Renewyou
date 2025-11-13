package com.example.renewyou;

import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class FridayActivity extends AppCompatActivity {

    private ImageButton btnBackFriday;
    private VideoView videoSquats, videoPushups, videoBandRows, videoPlank, videoSavasana;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friday);

        btnBackFriday = findViewById(R.id.btnBackFriday);
        if (btnBackFriday != null) btnBackFriday.setOnClickListener(v -> finish());

        videoSquats = findViewById(R.id.videoSquats);
        videoPushups = findViewById(R.id.videoPushups);
        videoBandRows = findViewById(R.id.videoBandRows);
        videoPlank = findViewById(R.id.videoPlank);
        videoSavasana = findViewById(R.id.videoSavasana);

        setVideoByName(videoSquats, "bodyweight_squats");
        setVideoByName(videoPushups, "pushups");
        setVideoByName(videoBandRows, "resistance_band_rows");
        setVideoByName(videoPlank, "plank");
        setVideoByName(videoSavasana, "savasana");
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