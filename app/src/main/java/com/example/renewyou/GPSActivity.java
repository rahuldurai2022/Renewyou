package com.example.renewyou;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

public class GPSActivity extends AppCompatActivity {

    private static final int REQ_LOCATION = 1001;

    private FusedLocationProviderClient fusedClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;

    private Location startLocation = null;
    private Location lastLocation = null;
    private boolean tracking = false;

    private TextView tvDistance, tvFromTo;
    private Button btnStart, btnStop;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsactivity);

        tvDistance = findViewById(R.id.tvDistance);
        tvFromTo = findViewById(R.id.tvFromTo);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnBack = findViewById(R.id.btnBackGps);

        btnBack.setOnClickListener(v -> finish());

        fusedClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = new LocationRequest.Builder(2000)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setMinUpdateIntervalMillis(1000)
                .build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null) return;
                Location loc = locationResult.getLastLocation();
                if (loc == null) return;
                lastLocation = loc;
                if (startLocation != null) {
                    float meters = startLocation.distanceTo(loc);
                    updateDistanceUI(meters);
                }
            }
        };

        btnStart.setOnClickListener(v -> startTracking());
        btnStop.setOnClickListener(v -> stopTracking());

        ensurePermission();
    }

    private void startTracking() {
        if (!hasPermission()) {
            ensurePermission();
            return;
        }
        tracking = true;
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);

        // get current as start, then begin updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedClient.getLastLocation().addOnSuccessListener(loc -> {
                if (loc != null) {
                    startLocation = loc;
                    tvFromTo.setText("Start: " + formatLatLng(loc) + "  End: —");
                }
                requestUpdates();
            });
        }
    }

    private void stopTracking() {
        tracking = false;
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        fusedClient.removeLocationUpdates(locationCallback);
        if (startLocation != null && lastLocation != null) {
            float meters = startLocation.distanceTo(lastLocation);
            updateDistanceUI(meters);
            tvFromTo.setText("Start: " + formatLatLng(startLocation) + "  End: " + formatLatLng(lastLocation));
        }
    }

    private void requestUpdates() {
        if (!hasPermission()) return;
        fusedClient.requestLocationUpdates(locationRequest, locationCallback, getMainLooper());
    }

    private void updateDistanceUI(float meters) {
        double km = meters / 1000.0;
        tvDistance.setText(String.format("Distance: %.2f km", km));
    }

    private boolean hasPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void ensurePermission() {
        if (!hasPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQ_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_LOCATION) {
            if (hasPermission() && tracking) {
                requestUpdates();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tracking) {
            fusedClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tracking && hasPermission()) {
            requestUpdates();
        }
    }

    private String formatLatLng(Location l) {
        return String.format("%.5f, %.5f", l.getLatitude(), l.getLongitude());
    }
}