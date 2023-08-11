package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends AppCompatActivity {

    // Declaration of variables
    Button btn_reset, btn_back;
    EditText edit_email;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    String new_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //  Get references from views (XML)

        btn_back = findViewById(R.id.back_button);
        btn_reset = findViewById(R.id.reset_button);
        edit_email = findViewById(R.id.forgot_password_email);
        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();

        // Reset Button Code --> If email is valid --> send reset password if not then error until user enters valid email
        btn_reset.setOnClickListener(v -> {
            new_email = edit_email.getText().toString().trim();
            if (!TextUtils.isEmpty(new_email)) {
                reset_password();
            } else {
                Toast.makeText(Forgot_Password_Activity.this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });

        // Back Button Code --> Intent to switch from Forgot Password Activity back to Login Activity

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(Forgot_Password_Activity.this, Login_Activity.class);
            startActivity(intent);
            finish();
        });
    }

    // Reset Password Function that checks it the email is saved in the database and sends a reset password link to the email provided.
    private void reset_password() {
        progressBar.setVisibility(View.VISIBLE);
        btn_reset.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(new_email)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(Forgot_Password_Activity.this, "Reset Password link has been sent to the registered Email", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Forgot_Password_Activity.this, Login_Activity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(Forgot_Password_Activity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    btn_reset.setVisibility(View.VISIBLE);
                });
    }
}