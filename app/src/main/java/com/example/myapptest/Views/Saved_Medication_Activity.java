package com.example.myapptest.Views;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import java.util.List;

public class Saved_Medication_Activity extends AppCompatActivity {

    // Declaration of Variables
    private LinearLayout medicationListLayout;
    private DatabaseReference databaseReference;
    private List<Medication_Data_Helper> savedMedicationDataHelpers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_medication);

        //  Get references from views (XML)
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Navigate back to the Homepage_Activity
            Intent intent = new Intent(Saved_Medication_Activity.this, Homepage_Activity.class);
            startActivity(intent);
            finish(); // Optional: finish the SavedMedicationActivity
        });

        medicationListLayout = findViewById(R.id.medication_list);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();
        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Saved Medication");

        // Retrieve saved medications from the database
        savedMedicationDataHelpers = new ArrayList<>();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                savedMedicationDataHelpers.clear();
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    Medication_Data_Helper medicationDataHelper = medicationSnapshot.getValue(Medication_Data_Helper.class);
                    if (medicationDataHelper != null) {
                        savedMedicationDataHelpers.add(medicationDataHelper);
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
                Toast.makeText(Saved_Medication_Activity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();

                // Log the error for debugging purposes
                Log.e("FirebaseError", errorMessage);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateMedicationList() {
        medicationListLayout.removeAllViews(); // Clear the existing views

        // Create a view for each saved medication
        for (int i = 0; i < savedMedicationDataHelpers.size(); i++) {
            Medication_Data_Helper medicationDataHelper = savedMedicationDataHelpers.get(i);

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
            medicationTextView.setText(medicationDataHelper.getName() + " - " + medicationDataHelper.getTime() + "\n" + medicationDataHelper.getDate());

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
            deleteImageView.setOnClickListener(v -> confirmDelete(medicationDataHelper));

            // Add the TextView and ImageView to the item layout
            itemLayout.addView(medicationTextView);
            itemLayout.addView(deleteImageView);

            // Add the item layout to the medication list layout
            medicationListLayout.addView(itemLayout);
        }
    }

    // Confirm Delete function creating a small popup to ask the user if he / she are sure of deleting an already saved medication from the list.
    private void confirmDelete(Medication_Data_Helper medicationDataHelper) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this medication?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    ask(medicationDataHelper);
                    // Delete medication from Firebase database
                    if (medicationDataHelper != null) {
                        String medicationId = medicationDataHelper.getId();
                        databaseReference.child(medicationId).removeValue();
                        Toast.makeText(Saved_Medication_Activity.this, "Medication deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    // Ask function to ask the user if he / she would like to update the medication list after deletion
    // It consists of editing the list by asking a popup to update.
    // If yes navigate to add new medication
    // If not stay in saved medication activity
    public void ask(Medication_Data_Helper medicationDataHelper){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("Update Medication List")
                .setMessage("Would you like to add a new medication to the list?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent (getApplicationContext(), Add_Medication_Activity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}

