package com.example.renewyou;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import com.google.android.material.card.MaterialCardView;

import androidx.appcompat.app.AppCompatActivity;

public class DailyPlansActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_plans);

        ImageButton back = findViewById(R.id.btnBackDailyPlans);
        if (back != null) back.setOnClickListener(v -> finish());

        MaterialCardView btnMon = findViewById(R.id.btnMonday);
        MaterialCardView btnTue = findViewById(R.id.btnTuesday);
        MaterialCardView btnWed = findViewById(R.id.btnWednesday);
        MaterialCardView btnThu = findViewById(R.id.btnThursday);
        MaterialCardView btnFri = findViewById(R.id.btnFriday);
        MaterialCardView btnSat = findViewById(R.id.btnSaturday);
        MaterialCardView btnSun = findViewById(R.id.btnSunday);

        if (btnMon != null) btnMon.setOnClickListener(v -> startActivity(new Intent(this, MondayActivity.class)));
        if (btnTue != null) btnTue.setOnClickListener(v -> startActivity(new Intent(this, TuesdayActivity.class)));
        if (btnWed != null) btnWed.setOnClickListener(v -> startActivity(new Intent(this, WednesdayActivity.class)));
        if (btnThu != null) btnThu.setOnClickListener(v -> startActivity(new Intent(this, ThursdayActivity.class)));
        if (btnFri != null) btnFri.setOnClickListener(v -> startActivity(new Intent(this, FridayActivity.class)));
        if (btnSat != null) btnSat.setOnClickListener(v -> startActivity(new Intent(this, SaturdayActivity.class)));
        if (btnSun != null) btnSun.setOnClickListener(v -> startActivity(new Intent(this, SundayActivity.class)));
    }
}
