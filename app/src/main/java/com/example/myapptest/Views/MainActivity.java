package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // Declaration of Variables
    FirebaseAuth auth;
    Button btn;
    TextView text_view;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        // Initialization of Variables
        auth = FirebaseAuth.getInstance();
        btn = findViewById(R.id.logout);
        text_view = findViewById(R.id.user);
        user = auth.getCurrentUser();

        // Check if the user exists
        if (user == null){
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        }
        else {
            text_view.setText(user.getEmail());
        }

        // On click to redirect the user back to the login page
        btn.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        });
    }
}