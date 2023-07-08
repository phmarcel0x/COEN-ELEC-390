package com.example.myapptest.Views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.List;

public class SavedMedicationActivity extends AppCompatActivity {

    private LinearLayout medicationListLayout;
    private DatabaseReference databaseReference;
    private List<Medication> savedMedications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_medication);

        medicationListLayout = findViewById(R.id.medication_list);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Saved Medication");

        // Retrieve saved medications from the database
        savedMedications = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedMedications.clear();
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    Medication medication = medicationSnapshot.getValue(Medication.class);
                    if (medication != null) {
                        savedMedications.add(medication);
                    }
                }

                // Update the TextView with the saved medications
                updateMedicationList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Get the error message
                String errorMessage = databaseError.getMessage();

                // Display an error message to the user
                Toast.makeText(SavedMedicationActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

                // Log the error for debugging purposes
                Log.e("FirebaseError", errorMessage);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateMedicationList() {
        medicationListLayout.removeAllViews(); // Clear the existing views

        // Create a view for each saved medication
        for (int i = 0; i < savedMedications.size(); i++) {
            Medication medication = savedMedications.get(i);

            // Create a new RelativeLayout to hold each medication item
            RelativeLayout itemLayout = new RelativeLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(0, 0, 0, 16); // Add margin between items
            itemLayout.setLayoutParams(layoutParams);

            // Create a TextView for the medication info
            TextView medicationTextView = new TextView(this);
            RelativeLayout.LayoutParams textLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            medicationTextView.setLayoutParams(textLayoutParams);
            medicationTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            medicationTextView.setTypeface(null, Typeface.BOLD);
            medicationTextView.setText(medication.getName() + " - " + medication.getTime());

            // Create an ImageView for the delete button
            ImageView deleteImageView = new ImageView(this);
            RelativeLayout.LayoutParams deleteLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            deleteLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_END); // Align to the end (right-end)
            deleteLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL); // Center vertically
            deleteLayoutParams.setMargins(0, 0, 8, 0); // Add margin
            deleteImageView.setLayoutParams(deleteLayoutParams);
            deleteImageView.setImageResource(R.drawable.bin_24);
            deleteImageView.setContentDescription("Delete Medication");
            deleteImageView.setClickable(true);
            deleteImageView.setOnClickListener(v -> confirmDelete(medication));

            // Add the TextView and ImageView to the item layout
            itemLayout.addView(medicationTextView);
            itemLayout.addView(deleteImageView);

            // Add the item layout to the medication list layout
            medicationListLayout.addView(itemLayout);
        }
    }

    private void confirmDelete(Medication medication) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this medication?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Delete medication from Firebase database
                    if (medication != null) {
                        String medicationId = medication.getId();
                        databaseReference.child(medicationId).removeValue();
                        Toast.makeText(SavedMedicationActivity.this, "Medication deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
