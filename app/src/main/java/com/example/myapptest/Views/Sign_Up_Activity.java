package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_Up_Activity extends AppCompatActivity {

    // Declaration of the variables
    TextInputEditText edit_email, edit_password;
    Button sign_up_btn;
    ProgressBar progressBar;
    TextView text_view;
    FirebaseAuth mAuth;

    // Once signed up --> Intent to switch to homepage from sign up page
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // If user is created successfully navigate to the homepage activity
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), Homepage_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //  Get references from views (XML)
        mAuth = FirebaseAuth.getInstance();

        edit_email = findViewById(R.id.email);
        edit_password = findViewById(R.id.password);
        sign_up_btn = findViewById(R.id.btn_sign_up);
        progressBar = findViewById(R.id.progress_bar);
        text_view = findViewById(R.id.login_now);

        // Login Now to navigate to the Login Activity
        text_view.setOnClickListener(v -> {
            Intent intent = new Intent (getApplicationContext(), Login_Activity.class);
            startActivity(intent);
            finish();
        });
        // Sign Up button to navigate to the Sign Up page
        sign_up_btn.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;

            email = String.valueOf(edit_email.getText());
            password = String.valueOf(edit_password.getText());

            // check if email is empty --> If yes error message to enter email
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Sign_Up_Activity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }
            // check if password is empty --> If yes error message to enter password
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Sign_Up_Activity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a new authentication for the user in the Firebase Database.
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);

                        // If successful --> Account created and navigate to the homepage activity
                        if (task.isSuccessful()) {
                            Toast.makeText(Sign_Up_Activity.this, "Account has been created.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Homepage_Activity.class);
                            startActivity(intent);
                            finish();
                        // if not successful --> error message
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Sign_Up_Activity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

    }
}