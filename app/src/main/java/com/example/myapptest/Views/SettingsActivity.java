package com.example.myapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;


public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button addMedicationButton = findViewById(R.id.btn_add_new_medication);
        Button savedMedicationsButton = findViewById(R.id.btn_saved_medication);
        Button medicationCommentsButton = findViewById(R.id.btn_medication_notes);
        Button accessibilityButton = findViewById(R.id.btn_accessibility);
        Button backButton = findViewById(R.id.back_button);

        addMedicationButton.setOnClickListener(this);
        savedMedicationsButton.setOnClickListener(this);
        medicationCommentsButton.setOnClickListener(this);
        accessibilityButton.setOnClickListener(this);
        backButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_add_new_medication) {// Handle "Add New Medication" button click
            Intent addMedicationIntent = new Intent(this, AddMedicationActivity.class);
            startActivity(addMedicationIntent);
        } else if (id == R.id.btn_saved_medication) {// Handle "List of Saved Medications" button click
            Intent savedMedicationsIntent = new Intent(this, SavedMedicationActivity.class);
            startActivity(savedMedicationsIntent);
        } else if (id == R.id.btn_medication_notes) {// Handle "Comments About Medications" button click
            Intent medicationCommentsIntent = new Intent(this, MedicationNotesActivity.class);
            startActivity(medicationCommentsIntent);
        } else if (id == R.id.btn_accessibility) {// Handle "Accessibility" button click
            Intent accessibilityIntent = new Intent(this, AccessibilityActivity.class);
            startActivity(accessibilityIntent);
        } else if (id == R.id.back_button) {// Handle "Back" button click
            Intent accessibilityIntent = new Intent(this, Homepage_Activity.class);
            startActivity(accessibilityIntent);
        }
    }
}
