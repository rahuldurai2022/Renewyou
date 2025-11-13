package com.example.renewyou;

import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class SaturdayActivity extends AppCompatActivity {

    private ImageButton btnBackSaturday;
    private VideoView videoJogOrCycle, videoSwimming, videoStretchHamstring, videoStretchShoulder, videoMeditation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saturday);

        btnBackSaturday = findViewById(R.id.btnBackSaturday);
        if (btnBackSaturday != null) btnBackSaturday.setOnClickListener(v -> finish());

        videoJogOrCycle = findViewById(R.id.videoJogOrCycle);
        videoSwimming = findViewById(R.id.videoSwimming);
        videoStretchHamstring = findViewById(R.id.videoStretchHamstring);
        videoStretchShoulder = findViewById(R.id.videoStretchShoulder);
        videoMeditation = findViewById(R.id.videoMeditation);

        setVideoByName(videoJogOrCycle, "jogging_or_cycling");
        setVideoByName(videoSwimming, "swimming");
        setVideoByName(videoStretchHamstring, "stretching_hamstring");
        setVideoByName(videoStretchShoulder, "stretching_shoulder");
        setVideoByName(videoMeditation, "meditation");
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