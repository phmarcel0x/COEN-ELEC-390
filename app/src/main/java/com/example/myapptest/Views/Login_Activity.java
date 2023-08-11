package com.example.myapptest.Views;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



import com.example.myapptest.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    // Declaration of the variables
    TextInputEditText edit_email, edit_password;
    Button btn_login;
    ProgressBar progressBar;
    TextView text_view, Forgot_Password;
    CheckBox show_hide_password;
    FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(getApplicationContext(), Homepage_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //  Get references from views (XML)
        mAuth = FirebaseAuth.getInstance();

        edit_email = findViewById(R.id.email);
        edit_password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progress_bar);
        text_view = findViewById(R.id.sign_up_now);
        Forgot_Password = findViewById(R.id.forgot_password);
        show_hide_password = findViewById(R.id.show_password);


        // On Sign Up Click --> Switch from login activity to sign up activity.
        text_view.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), Sign_Up_Activity.class);
            startActivity(intent);
            finish();
        });

        // On Login Click --> Check if data is saved in database else error
        // Code from Firebase Database website for Signing in a User
        btn_login.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            String email, password;

            email = String.valueOf(edit_email.getText());
            password = String.valueOf(edit_password.getText());

            // Check if user tries to login without entering any credentials Email and Password
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(Login_Activity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                Toast.makeText(Login_Activity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                return;
            }
            // Authentication through firebase database saved users
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(Login_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Homepage_Activity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Login_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    });


        });

        // Forgot Password Button --> On click intent to redirect towards Forgot Password Activity
        Forgot_Password.setOnClickListener(v -> {
            Intent intent = new Intent(Login_Activity.this, Forgot_Password_Activity.class);
            startActivity(intent);
            finish();
        });

        // Show / Hide password button that censors the password and only shows when the click is triggered.
        show_hide_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked == true){
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else {
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

}