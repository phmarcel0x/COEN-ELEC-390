package com.example.myapptest.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapptest.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

        // Initializaton of variables

        btn_back = findViewById(R.id.back_button);
        btn_reset = findViewById(R.id.reset_button);
        edit_email = findViewById(R.id.forgot_password_email);
        progressBar = findViewById(R.id.progress_bar);

        mAuth = FirebaseAuth.getInstance();

        // Reset Button Code --> If email is valid --> send reset password if not then error until user enters valid email
        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_email = edit_email.getText().toString().trim();
                if (!TextUtils.isEmpty(new_email)) {
                    reset_password();
                } else {
                    Toast.makeText(Forgot_Password_Activity.this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Back Button Code --> Intent to swtich from Forgot Password Activity back to Login Activity

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgot_Password_Activity.this, Login_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void reset_password() {
        progressBar.setVisibility(View.VISIBLE);
        btn_reset.setVisibility(View.INVISIBLE);

        mAuth.sendPasswordResetEmail(new_email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Forgot_Password_Activity.this, "Reset Password link has been sent to the registered Email", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Forgot_Password_Activity.this, Login_Activity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Forgot_Password_Activity.this, "Error!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        btn_reset.setVisibility(View.VISIBLE);
                    }
                });
    }
}