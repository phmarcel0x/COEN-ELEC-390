package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Logout_Activity extends AppCompatActivity {

    // Declaration of Variables
    FirebaseAuth auth;
    Button btn, logout_btn, back_button;
    TextView text_view;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        //  Get references from views (XML)
        auth = FirebaseAuth.getInstance();
        btn = findViewById(R.id.logout);
        back_button = findViewById(R.id.back_button);
        text_view = findViewById(R.id.user);
        user = auth.getCurrentUser();

        // Check if user exists
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        }
        else {
            text_view.setText(user.getEmail());
        }

        // On click logout redirect back to login activity if ever the user wants to log on to a different account
        btn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        });

        // Back Button on click to bring the user back to the Homepage Activity
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Homepage_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}