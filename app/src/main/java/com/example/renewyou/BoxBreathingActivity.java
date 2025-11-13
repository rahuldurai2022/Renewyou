package com.example.renewyou;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class BoxBreathingActivity extends AppCompatActivity {

    private static final int INHALE_DURATION = 4000; // 4 seconds
    private static final int HOLD_AFTER_INHALE = 4000; // 4 seconds
    private static final int EXHALE_DURATION = 4000; // 4 seconds
    private static final int HOLD_AFTER_EXHALE = 4000; // 4 seconds

    private enum BreathingState {
        INHALE, HOLD_AFTER_INHALE, EXHALE, HOLD_AFTER_EXHALE
    }

    private LottieAnimationView animationView;
    private TextView tvPrompt, tvTimer;
    private Button btnStartStop;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private int currentCount = 4;
    private BreathingState currentState = BreathingState.INHALE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_breathing);

        // Initialize views
        animationView = findViewById(R.id.animationView);
        tvPrompt = findViewById(R.id.tvPrompt);
        tvTimer = findViewById(R.id.tvTimer);
        btnStartStop = findViewById(R.id.btnStartStop);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Set up back button
        btnBack.setOnClickListener(v -> finish());

        // Set up start/stop button
        btnStartStop.setOnClickListener(v -> {
            if (isRunning) {
                stopBreathingExercise();
            } else {
                startBreathingExercise();
            }
        });

        // Set up animation
        setupAnimation();
    }

    private void setupAnimation() {
        try {
            // Load the animation from raw resources
            animationView.setAnimation(R.raw.breathing_animation);
            animationView.setRepeatCount(ValueAnimator.INFINITE);
            animationView.setSpeed(0.5f);
            // Set initial scale if needed
            animationView.setScaleX(1f);
            animationView.setScaleY(1f);
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback to programmatic animation if the file fails to load
            setupFallbackAnimation();
        }
    }

    private void setupFallbackAnimation() {
        // Create a simple scale animation as fallback
        ValueAnimator scaleAnimator = ValueAnimator.ofFloat(0.5f, 1f);
        scaleAnimator.setDuration(4000);
        scaleAnimator.setRepeatMode(ValueAnimator.REVERSE);
        scaleAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleAnimator.addUpdateListener(animation -> {
            float scale = (float) animation.getAnimatedValue();
            animationView.setScaleX(scale);
            animationView.setScaleY(scale);
        });
        scaleAnimator.start();
    }

    private void startBreathingExercise() {
        isRunning = true;
        btnStartStop.setText("Stop");
        currentState = BreathingState.INHALE;
        startBreathingCycle();
    }

    private void stopBreathingExercise() {
        isRunning = false;
        btnStartStop.setText("Start");
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        animationView.pauseAnimation();
        tvPrompt.setText("Box Breathing");
        tvTimer.setText("4");
        currentCount = 4;
    }

    private void startBreathingCycle() {
        if (!isRunning) return;

        switch (currentState) {
            case INHALE:
                tvPrompt.setText("Breathe in...");
                startCountdown(INHALE_DURATION, () -> {
                    currentState = BreathingState.HOLD_AFTER_INHALE;
                    startBreathingCycle();
                });
                animationView.playAnimation();
                break;

            case HOLD_AFTER_INHALE:
                tvPrompt.setText("Hold...");
                startCountdown(HOLD_AFTER_INHALE, () -> {
                    currentState = BreathingState.EXHALE;
                    startBreathingCycle();
                });
                break;

            case EXHALE:
                tvPrompt.setText("Breathe out...");
                startCountdown(EXHALE_DURATION, () -> {
                    currentState = BreathingState.HOLD_AFTER_EXHALE;
                    startBreathingCycle();
                });
                animationView.reverseAnimationSpeed();
                animationView.playAnimation();
                break;

            case HOLD_AFTER_EXHALE:
                tvPrompt.setText("Hold...");
                startCountdown(HOLD_AFTER_EXHALE, () -> {
                    currentState = BreathingState.INHALE;
                    startBreathingCycle();
                });
                break;
        }
    }

    private void startCountdown(long duration, Runnable onFinish) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        currentCount = (int)(duration / 1000);
        tvTimer.setText(String.valueOf(currentCount));

        countDownTimer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                currentCount--;
                tvTimer.setText(String.valueOf(currentCount));
            }

            @Override
            public void onFinish() {
                onFinish.run();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isRunning) {
            stopBreathingExercise();
        }
    }
}