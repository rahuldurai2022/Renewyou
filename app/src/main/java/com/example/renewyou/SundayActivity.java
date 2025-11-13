package com.example.renewyou;

import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SundayActivity extends AppCompatActivity {

    private ImageButton btnBackSunday;
    private VideoView videoRelax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sunday);

        btnBackSunday = findViewById(R.id.btnBackSunday);
        if (btnBackSunday != null) btnBackSunday.setOnClickListener(v -> finish());

        videoRelax = findViewById(R.id.videoRelax);
        setVideoByName(videoRelax, "relax");
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