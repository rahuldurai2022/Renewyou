package com.example.renewyou;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.renewyou.adapter.FeedbackAdapter;
import com.example.renewyou.model.Feedback;
import com.example.renewyou.network.ApiClient;
import com.example.renewyou.network.FeedbackApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private EditText etName, etEmail, etFeedback;
    private Spinner spinnerRating;
    private Button btnSubmit, btnUpdate, btnBack;
    private RecyclerView rvFeedback;
    private FeedbackAdapter adapter;
    private String editingId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etFeedback = findViewById(R.id.etFeedback);
        spinnerRating = findViewById(R.id.spinnerRating);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnBack = findViewById(R.id.btnBack);
        rvFeedback = findViewById(R.id.rvFeedback);

        // Setup Rating Spinner values
        String[] ratings = {"1 - Poor", "2 - Fair", "3 - Good", "4 - Very Good", "5 - Excellent"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, ratings);
        spinnerRating.setAdapter(adapter);

        // RecyclerView
        this.adapter = new FeedbackAdapter(new FeedbackAdapter.Listener() {
            @Override public void onEdit(Feedback item, int position) {
                startEditing(item);
            }
            @Override public void onDelete(Feedback item, int position) {
                deleteFeedback(item.get_id());
            }
        });
        rvFeedback.setLayoutManager(new LinearLayoutManager(this));
        rvFeedback.setAdapter(this.adapter);

        // Submit button action
        btnSubmit.setOnClickListener(v -> {
            submitFeedback();
        });

        btnUpdate.setOnClickListener(v -> {
            if (editingId != null) {
                updateFeedback(editingId);
            }
        });

        // Back button action
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Initial load
        loadFeedback();
    }

    private int parseSpinnerRating() {
        String sel = spinnerRating.getSelectedItem().toString();
        if (sel.length() > 0) {
            char c = sel.charAt(0);
            if (Character.isDigit(c)) return Character.getNumericValue(c);
        }
        return 5;
    }

    private Feedback collectForm() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String text = etFeedback.getText().toString().trim();
        int rating = parseSpinnerRating();

        if (name.isEmpty() || email.isEmpty() || text.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            return null;
        }
        Feedback f = new Feedback();
        f.setName(name);
        f.setEmail(email);
        f.setFeedback(text);
        f.setRating(rating);
        return f;
    }

    private void clearForm() {
        etName.setText("");
        etEmail.setText("");
        etFeedback.setText("");
        spinnerRating.setSelection(0);
        editingId = null;
        btnUpdate.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private FeedbackApi api() {
        return ApiClient.get().create(FeedbackApi.class);
    }

    private void loadFeedback() {
        api().listFeedback().enqueue(new Callback<List<Feedback>>() {
            @Override public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setItems(response.body());
                } else {
                    Toast.makeText(FeedbackActivity.this, "Failed to load", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Toast.makeText(FeedbackActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void submitFeedback() {
        Feedback body = collectForm();
        if (body == null) return;
        btnSubmit.setEnabled(false);
        api().createFeedback(body).enqueue(new Callback<Feedback>() {
            @Override public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                btnSubmit.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Submitted", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadFeedback();
                } else {
                    String msg = extractError(response);
                    Toast.makeText(FeedbackActivity.this, "Submit failed: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Feedback> call, Throwable t) {
                btnSubmit.setEnabled(true);
                Toast.makeText(FeedbackActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startEditing(Feedback item) {
        editingId = item.get_id();
        etName.setText(item.getName());
        etEmail.setText(item.getEmail());
        etFeedback.setText(item.getFeedback());
        int idx = Math.max(0, Math.min(4, item.getRating() - 1));
        spinnerRating.setSelection(idx);
        btnSubmit.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);
    }

    private void updateFeedback(String id) {
        Feedback body = collectForm();
        if (body == null) return;
        btnUpdate.setEnabled(false);
        api().updateFeedback(id, body).enqueue(new Callback<Feedback>() {
            @Override public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                btnUpdate.setEnabled(true);
                if (response.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    clearForm();
                    loadFeedback();
                } else {
                    String msg = extractError(response);
                    Toast.makeText(FeedbackActivity.this, "Update failed: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Feedback> call, Throwable t) {
                btnUpdate.setEnabled(true);
                Toast.makeText(FeedbackActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFeedback(String id) {
        api().deleteFeedback(id).enqueue(new Callback<Void>() {
            @Override public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    loadFeedback();
                } else {
                    String msg = extractError(response);
                    Toast.makeText(FeedbackActivity.this, "Delete failed: " + msg, Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(FeedbackActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String extractError(Response<?> response) {
        try {
            if (response.errorBody() != null) {
                return response.errorBody().string();
            }
        } catch (Exception ignored) {}
        return String.valueOf(response.code());
    }
}
