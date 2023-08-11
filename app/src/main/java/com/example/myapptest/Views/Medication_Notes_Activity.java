package com.example.myapptest.Views;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.example.myapptest.Controllers.Medication_Data_Helper;
import com.example.myapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Medication_Notes_Activity extends AppCompatActivity {

    // Declaration of the variables
    private LinearLayout medicationListLayout;
    private DatabaseReference databaseReference;
    private List<Medication_Data_Helper> savedMedicationDataHelpers;
    private HashMap<String, String> medicationNotesMap;
    private boolean notesChanged;

    // Get the references from the views
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_notes);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> saveMedicationNotes());

        saveButton.setOnClickListener(v -> {
            saveMedicationNotes();
            navigateToHomepage_Activity();
        });

        // On click back button to redirect the user back to homepage if no notes have been added
        // If added then show what has been added then the user will go back to the homepage
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            if (notesChanged) {
                showSaveDialog();
            } else {
                navigateToHomepage_Activity();
            }
        });

        medicationListLayout = findViewById(R.id.medication_list);
        notesChanged = false;

        // Get the user's information --> User Specific saved medication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Saved Medication");

        // Retrieve saved medications from the database
        savedMedicationDataHelpers = new ArrayList<>();
        medicationNotesMap = new HashMap<>(); // Initialize medication notes map

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedMedicationDataHelpers.clear();
                medicationNotesMap.clear(); // Clear medication notes map

                // Create new medication and if not empty add it to the saved medication list.
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    Medication_Data_Helper medicationDataHelper = medicationSnapshot.getValue(Medication_Data_Helper.class);
                    if (medicationDataHelper != null) {
                        savedMedicationDataHelpers.add(medicationDataHelper);

                        // Retrieve existing custom notes from the medication object (if available)
                        String customNotes = medicationDataHelper.getCustomNotes();
                        if (customNotes != null) {
                            medicationNotesMap.put(medicationDataHelper.getId(), customNotes);
                        }
                    }
                }

                // Update the medication list with custom notes
                updateMedicationList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle Error Exception
            }
        });
    }

    // Function to redirect the user back the homepage activity once finished from the medication notes activity
    private void navigateToHomepage_Activity() {
        Intent intent = new Intent(Medication_Notes_Activity.this, Homepage_Activity.class);
        startActivity(intent);
        finish(); // Optional: finish the MedicationNotesActivity
    }

    // On click for the back button --> if notes have changed --> Save new notes and show the new output
    // If not changed --> go back to homepage activity
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
        for (int i = 0; i < savedMedicationDataHelpers.size(); i++) {
            Medication_Data_Helper medicationDataHelper = savedMedicationDataHelpers.get(i);

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
            medicationTextView.setText(medicationDataHelper.getName());
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
            String customNotes = medicationNotesMap.get(medicationDataHelper.getId());
            if (customNotes != null) {
                notesEditText.setText(customNotes);
            }

            // Set a tag on the EditText to identify it with the medication ID
            notesEditText.setTag(medicationDataHelper.getId());

            // Add a TextWatcher to the EditText to track changes and update the medication notes map
            notesEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // No action needed
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // Update the medication notes map with the custom notes
                    medicationNotesMap.put(medicationDataHelper.getId(), s.toString());
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

    // Save Medication Notes Function --> Retrieves ID from database and updates the custom notes related to that ID in the Firebase RTDB.
    private void saveMedicationNotes() {
        for (Medication_Data_Helper medicationDataHelper : savedMedicationDataHelpers) {
            String medicationId = medicationDataHelper.getId();
            String customNotes = medicationNotesMap.get(medicationId);

            // Update the medication object with the custom notes
            medicationDataHelper.setCustomNotes(customNotes);

            // Save the updated medication object to the Firebase database
            databaseReference.child(medicationId).setValue(medicationDataHelper);
        }

        Toast.makeText(this, "Medication notes saved", Toast.LENGTH_SHORT).show();
        notesChanged = false;
    }

    // Show Save Dialog Function to Save the chnages to the notes and show the changes to the user by creating a small popup message interacting
    // with the user.
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
