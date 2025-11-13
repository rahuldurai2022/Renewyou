package com.example.renewyou;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class BreathingActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnSimple, btnBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing);

        // Initialize views
        initializeViews();
        
        // Set up click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        btnSimple = findViewById(R.id.btnSimple);
        btnBox = findViewById(R.id.btnBox);
    }

    private void setupClickListeners() {
        // Back button returns to previous activity
        btnBack.setOnClickListener(v -> finish());

        // Simple Guided Breathing
        btnSimple.setOnClickListener(v -> 
            startActivity(new Intent(this, SimpleBreathingActivity.class))
        );

        // Box Breathing
        btnBox.setOnClickListener(v -> 
            startActivity(new Intent(this, BoxBreathingActivity.class))
        );
    }
}