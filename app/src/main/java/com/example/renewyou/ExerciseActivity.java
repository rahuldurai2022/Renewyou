package com.example.renewyou; // ⚠️ Change this to your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday, btnSunday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // 🔙 Back Button
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // 🗓️ Initialize all 7 buttons
        btnMonday = findViewById(R.id.btnMonday);
        btnTuesday = findViewById(R.id.btnTuesday);
        btnWednesday = findViewById(R.id.btnWednesday);
        btnThursday = findViewById(R.id.btnThursday);
        btnFriday = findViewById(R.id.btnFriday);
        btnSaturday = findViewById(R.id.btnSaturday);
        btnSunday = findViewById(R.id.btnSunday);

        // 🎯 Set listeners for each day button
        btnMonday.setOnClickListener(v -> openActivity(MondayActivity.class));
        btnTuesday.setOnClickListener(v -> openActivity(TuesdayActivity.class));
        btnWednesday.setOnClickListener(v -> openActivity(WednesdayActivity.class));
        btnThursday.setOnClickListener(v -> openActivity(ThursdayActivity.class));
        btnFriday.setOnClickListener(v -> openActivity(FridayActivity.class));
        btnSaturday.setOnClickListener(v -> openActivity(SaturdayActivity.class));
        btnSunday.setOnClickListener(v -> openActivity(SundayActivity.class));
    }

    // 🔗 Helper function to open activity
    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(ExerciseActivity.this, activityClass);
        startActivity(intent);
    }
}
