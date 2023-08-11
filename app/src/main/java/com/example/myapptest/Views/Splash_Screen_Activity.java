package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;

public class Splash_Screen_Activity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 4000; // Splash screen delay in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Delayed execution for the splash screen
        new Handler().postDelayed(() -> {
            // Start the next activity after the splash screen delay
            Intent intent = new Intent(Splash_Screen_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }
}
