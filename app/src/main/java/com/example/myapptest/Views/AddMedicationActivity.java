package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddMedicationActivity extends AppCompatActivity {

    private EditText editTextMedicationName;
    private Spinner spinnerTime;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("New Medication");

        // Get references to views
        editTextMedicationName = findViewById(R.id.editText_medication_name);
        spinnerTime = findViewById(R.id.spinner_time);

        // Set adapter to populate the spinner from the array resource
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.times_of_day, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(spinnerAdapter);

        // Request focus and show keyboard for the EditText
        editTextMedicationName.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextMedicationName, InputMethodManager.SHOW_IMPLICIT);

        // Set click listener for Save button
        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> saveMedication());

        // Set click listener for Cancel button
        Button cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(v -> cancel());
    }

    private void saveMedication() {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextMedicationName.getWindowToken(), 0);

        // Get user input values
        String medicationName = editTextMedicationName.getText().toString().trim();
        String selectedTime = spinnerTime.getSelectedItem().toString();

        // Validate input
        if (medicationName.isEmpty()) {
            Toast.makeText(this, "Please enter the medication name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ID for the medication
        String medicationId = databaseReference.child("Saved Medication").push().getKey();

        // Create Medication object
        Medication medication = new Medication(medicationId, medicationName, selectedTime);

        // Save medication to Firebase database under "Saved Medication" node
        if (medicationId != null) {
            DatabaseReference savedMedicationRef = FirebaseDatabase.getInstance().getReference().child("Saved Medication");
            savedMedicationRef.child(medicationId).setValue(medication)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AddMedicationActivity.this, "Medication saved successfully", Toast.LENGTH_SHORT).show();

                        // Start SavedMedicationActivity and pass medication details
                        Intent intent = new Intent(AddMedicationActivity.this, SavedMedicationActivity.class);
                        intent.putExtra("medicationName", medicationName);
                        intent.putExtra("selectedTime", selectedTime);
                        startActivity(intent);

                        // Finish the activity and return to SettingsActivity
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(AddMedicationActivity.this, "Failed to save medication", Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(this, "Failed to save medication", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancel() {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextMedicationName.getWindowToken(), 0);

        // Finish the activity and return to SettingsActivity
        finish();
    }
}
