package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;

public class AccessibilityActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessibility);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Navigate back to the SettingsActivity
            Intent intent = new Intent(AccessibilityActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish(); // Optional: finish the AccessibilityActivity
        });
    }
}