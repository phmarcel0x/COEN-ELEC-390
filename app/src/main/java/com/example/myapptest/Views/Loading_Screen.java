package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;

public class Loading_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_screen);
        Handler handler = new Handler ();
        handler.postDelayed(() -> {
            startActivity(new Intent(Loading_Screen.this,Login_Activity.class));
            finish();
        },3000);
    }
}

