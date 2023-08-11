package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;

public class Legal_Information_Activity extends AppCompatActivity {

    // Declaration of variables
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal_information);

        //  Get references from views (XML)
        btn_back = findViewById(R.id.back_button);

        // On click to change between activities
        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Homepage_Activity.class);
            startActivity(intent);
            finish();
        });
    }
}