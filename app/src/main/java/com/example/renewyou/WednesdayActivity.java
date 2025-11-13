package com.example.renewyou;

import android.os.Bundle;
import android.net.Uri;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class WednesdayActivity extends AppCompatActivity {

    private ImageButton btnBackWednesday;
    private VideoView videoBriskWalking, videoJumpRope, videoSeatedForwardBend, videoMeditation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wednesday);

        btnBackWednesday = findViewById(R.id.btnBackWednesday);
        if (btnBackWednesday != null) btnBackWednesday.setOnClickListener(v -> finish());

        videoBriskWalking = findViewById(R.id.videoBriskWalking);
        videoJumpRope = findViewById(R.id.videoJumpRope);
        videoSeatedForwardBend = findViewById(R.id.videoSeatedForwardBend);
        videoMeditation = findViewById(R.id.videoMeditation);

        setVideoByName(videoBriskWalking, "brisk_walking");
        setVideoByName(videoJumpRope, "jump_rope");
        setVideoByName(videoSeatedForwardBend, "seated_forward_bend");
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