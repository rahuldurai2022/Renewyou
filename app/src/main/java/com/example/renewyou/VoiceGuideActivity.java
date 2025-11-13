package com.example.renewyou;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class VoiceGuideActivity extends AppCompatActivity {

    private LinearLayout chatLayout;
    private EditText etMessage;
    private ScrollView chatScroll;
    private TextToSpeech tts;
    private static final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_guide);

        // ✅ Enable ActionBar Back Button
        // Back button functionality
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            // Navigate back to MainActivity
            Intent intent = new Intent(VoiceGuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Close current activity
        });

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Voice Guide");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        chatLayout = findViewById(R.id.chatLayout);
        etMessage = findViewById(R.id.etMessage);
        Button btnSend = findViewById(R.id.btnSend);
        ImageButton btnMic = findViewById(R.id.btnMic);
        chatScroll = findViewById(R.id.chatScroll);

        // Initialize Text-to-Speech
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                tts.setLanguage(Locale.US);
            }
        });

        // Send Button (Text input)
        btnSend.setOnClickListener(v -> {
            String message = etMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                addMessage("You: " + message, Gravity.END, 0xFFBBDEFB);
                etMessage.setText("");
                botReply(message, true);
            }
        });

        // Mic Button (Speech-to-Text)
        btnMic.setOnClickListener(v -> startVoiceInput());
    }

    private void addMessage(String text, int gravity, int bgColor) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(16);
        tv.setPadding(20, 10, 20, 10);
        tv.setBackgroundColor(bgColor);

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10, 10, 10, 10);
        params.gravity = gravity;

        tv.setLayoutParams(params);
        chatLayout.addView(tv);

        chatScroll.post(() -> chatScroll.fullScroll(View.FOCUS_DOWN));
    }

    private void botReply(String userMsg, boolean speak) {
        String reply;

        if (userMsg.toLowerCase().contains("hello")) {
            reply = "Hi! Welcome to ReNewYou. How are you feeling today?";
        } else if (userMsg.toLowerCase().contains("breathing")) {
            reply = "Try deep breathing: Inhale 4 seconds, hold 4, exhale 6.";
        } else if (userMsg.toLowerCase().contains("exercise")) {
            reply = "Posture tip: Sit straight, shoulders relaxed, chin slightly down.";
        } else if (userMsg.toLowerCase().contains("stress")) {
            reply = "Close your eyes, breathe slowly, and focus on relaxation.";
        } else if (userMsg.toLowerCase().contains("bye")) {
            reply = "Goodbye! Stay strong and keep improving.";
        } else {
            reply = "I’m still learning. Try asking about breathing, exercise, or stress relief.";
        }

        addMessage("Bot: " + reply, Gravity.START, 0xFFFFFFFF);

        if (speak && tts != null) {
            tts.speak(reply, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    // Launch Google STT
    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Handle STT result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                addMessage("You (voice): " + spokenText, Gravity.END, 0xFFBBDEFB);
                botReply(spokenText, true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    // ✅ Handle Back Button Click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Go back to MainActivity
            Intent intent = new Intent(VoiceGuideActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}