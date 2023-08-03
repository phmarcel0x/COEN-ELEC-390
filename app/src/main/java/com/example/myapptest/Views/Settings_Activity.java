package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;

public class Settings_Activity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button backButton = findViewById(R.id.back_button);
        ImageButton technical_support_1 = findViewById(R.id.btn_technical_support_1);
        ImageButton technical_support_2 = findViewById(R.id.btn_technical_support_2);
        ImageButton legal_infomration = findViewById(R.id.btn_legal_information);
        ImageButton logout = findViewById(R.id.btn_logout);

        backButton.setOnClickListener(v -> {
            // Navigate back to the SettingsActivity
            Intent intent = new Intent(Settings_Activity.this, Homepage_Activity.class);
            startActivity(intent);
            finish(); // Optional: finish the AccessibilityActivity
        });

        technical_support_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Technical_Support_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        technical_support_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Technical_Support_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        legal_infomration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Legal_Information_Activity.class);
                startActivity(intent);
                finish();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext(), Logout_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}