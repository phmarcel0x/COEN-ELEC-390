package com.example.myapptest.Views;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicationNotesActivity extends AppCompatActivity {

    private LinearLayout medicationListLayout;
    private DatabaseReference databaseReference;
    private List<Medication> savedMedications;
    private HashMap<String, String> medicationNotesMap;
    private boolean notesChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_notes);

        medicationListLayout = findViewById(R.id.medication_list);
        notesChanged = false;

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Saved Medication");

        // Retrieve saved medications from the database
        savedMedications = new ArrayList<>();
        medicationNotesMap = new HashMap<>(); // Initialize medication notes map

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedMedications.clear();
                medicationNotesMap.clear(); // Clear medication notes map

                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    Medication medication = medicationSnapshot.getValue(Medication.class);
                    if (medication != null) {
                        savedMedications.add(medication);

                        // Retrieve existing custom notes from the medication object (if available)
                        String customNotes = medication.getCustomNotes();
                        if (customNotes != null) {
                            medicationNotesMap.put(medication.getId(), customNotes);
                        }
                    }
                }

                // Update the medication list with custom notes
                updateMedicationList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveMedicationNotes());
    }

    @Override
    public void onBackPressed() {
        if (notesChanged) {
            showSaveDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void updateMedicationList() {
        medicationListLayout.removeAllViews(); // Clear the existing views

        // Create a view for each saved medication
        for (int i = 0; i < savedMedications.size(); i++) {
            Medication medication = savedMedications.get(i);

            // Create a new LinearLayout to hold each medication item
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            itemLayout.setOrientation(LinearLayout.VERTICAL);

            // Create a TextView for the medication name
            TextView medicationTextView = new TextView(this);
            medicationTextView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            medicationTextView.setText(medication.getName());
            medicationTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            medicationTextView.setTypeface(null, Typeface.BOLD);

            // Create an EditText for the custom notes
            EditText notesEditText = new EditText(this);
            notesEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            notesEditText.setHint("Enter custom notes...");

            // Retrieve existing custom notes from the medication notes map (if available)
            String customNotes = medicationNotesMap.get(medication.getId());
            if (customNotes != null) {
                notesEditText.setText(customNotes);
            }

            // Set a tag on the EditText to identify it with the medication ID
            notesEditText.setTag(medication.getId());

            // Add a TextWatcher to the EditText to track changes and update the medication notes map
            notesEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No action needed
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Update the medication notes map with the custom notes
                    medicationNotesMap.put(medication.getId(), s.toString());
                    notesChanged = true;
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // No action needed
                }
            });

            // Add the TextView and EditText to the item layout
            itemLayout.addView(medicationTextView);
            itemLayout.addView(notesEditText);

            // Add the item layout to the medication list layout
            medicationListLayout.addView(itemLayout);
        }
    }

    private void saveMedicationNotes() {
        for (Medication medication : savedMedications) {
            String medicationId = medication.getId();
            String customNotes = medicationNotesMap.get(medicationId);

            // Update the medication object with the custom notes
            medication.setCustomNotes(customNotes);

            // Save the updated medication object to the Firebase database
            databaseReference.child(medicationId).setValue(medication);
        }

        Toast.makeText(this, "Medication notes saved", Toast.LENGTH_SHORT).show();
        notesChanged = false;
    }

    private void showSaveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Notes");
        builder.setMessage("Do you want to save changes made before exiting?");
        builder.setPositiveButton("Yes", (dialog, which) -> {
            saveMedicationNotes();
            finish();
        });
        builder.setNegativeButton("No", (dialog, which) -> finish());
        builder.show();
    }
}


