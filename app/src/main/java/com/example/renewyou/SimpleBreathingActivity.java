package com.example.renewyou;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.FrameLayout;
import androidx.appcompat.app.AppCompatActivity;

public class SimpleBreathingActivity extends AppCompatActivity {

    ImageButton btnBack;
    TextView tvCountdown, tvPrompt;
    FrameLayout circleLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_breathing);

        btnBack = findViewById(R.id.btnBack);
        tvCountdown = findViewById(R.id.tvCountdown);
        tvPrompt = findViewById(R.id.tvPrompt);
        circleLayout = findViewById(R.id.circleLayout);

        btnBack.setOnClickListener(v -> finish());

        startBreathingCycle();
    }

    private void startBreathingCycle() {
        // 4s inhale -> 4s hold -> 6s exhale
        new CountDownTimer(14000, 1000) {
            int step = 0;

            public void onTick(long millisUntilFinished) {
                step++;

                if (step <= 4) {
                    tvPrompt.setText("Inhale...");
                    tvCountdown.setText(String.valueOf(5 - step));
                    animateCircle(1.2f);
                } else if (step <= 8) {
                    tvPrompt.setText("Hold...");
                    tvCountdown.setText(String.valueOf(9 - step));
                    animateCircle(1.0f);
                } else {
                    tvPrompt.setText("Exhale...");
                    tvCountdown.setText(String.valueOf(15 - step));
                    animateCircle(0.8f);
                }
            }

            public void onFinish() {
                tvCountdown.setText("Done");
                tvPrompt.setText("Well done! You’ve completed a cycle.");
            }
        }.start();
    }

    private void animateCircle(float scale) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(circleLayout, "scaleX", scale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(circleLayout, "scaleY", scale);
        scaleX.setDuration(800);
        scaleY.setDuration(800);
        scaleX.start();
        scaleY.start();
    }
}