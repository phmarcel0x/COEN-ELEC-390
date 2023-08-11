package com.example.myapptest.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapptest.R;

public class Technical_Support_Activity extends AppCompatActivity {

    // Declaration of the variables
    TextView txt_support, txt_phone, txt_email;
    Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technical_support);

        // Get references from views (XML)
        txt_support = findViewById(R.id.text_support);
        txt_phone = findViewById(R.id.text_phone);
        txt_email = findViewById(R.id.text_email);

        btn_back = findViewById(R.id.back_button);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent (getApplicationContext(), Homepage_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}