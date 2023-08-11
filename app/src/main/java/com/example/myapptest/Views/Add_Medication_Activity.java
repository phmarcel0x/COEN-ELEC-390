package com.example.myapptest.Views;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.Controllers.Medication_Data_Helper;
import com.example.myapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Add_Medication_Activity extends AppCompatActivity {

    // Declaration of the Variables
    private EditText editTextMedicationName;
    private Button spinnerTime, date_view;

    private DatabaseReference databaseReference;
    String timeTonotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medication);

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("New Medication");

        //  Get references from views (XML)
        editTextMedicationName = findViewById(R.id.editText_medication_name);
        spinnerTime = findViewById(R.id.spinner_time);
        date_view = findViewById(R.id.calendar_date);
        spinnerTime.setOnClickListener(v -> selectTime());
        date_view.setOnClickListener(v -> selectDate());

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

        // Set click listener for Cancel button
        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            // Navigate back to the Homepage_Activity
            Intent intent = new Intent(Add_Medication_Activity.this, Homepage_Activity.class);
            startActivity(intent);
            finish(); // Optional: finish the AccessibilityActivity
        });

    }
    // Time Picker --> Create Button once clicked clock pop up
    private void selectTime() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify = i + ":" + i1;
                spinnerTime.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();

    }

    // Date Picker --> Create Button for Date once clicked calendar pop up
    private void selectDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date_view.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    // Function to Format the Time in hours and minutes to be saved in database
    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }
        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }
        return time;
    }

    // Save Medication Function that saved Name, Date and Time in the Firebase RTDB under the user specific nodes.

    private void saveMedication() {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextMedicationName.getWindowToken(), 0);

        // Get user input values
        String medicationName = editTextMedicationName.getText().toString().trim();
        String selectedTime = spinnerTime.getText().toString();
        String selectedDate = date_view.getText().toString();

        // Validate input
        if (selectedTime.equals("Medication Time")&&selectedDate.equals("Start Date")&&medicationName.isEmpty()) {
            Toast.makeText(this, "Please select a Medication Name, Time and Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (selectedTime.equals("Medication Time")&&selectedDate.equals("Start Date")) {
            Toast.makeText(this, "Please select a Time and Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (selectedTime.equals("Medication Time")&&medicationName.isEmpty()) {
            Toast.makeText(this, "Please select a Medication Name and Time", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (selectedDate.equals("Start Date")&&medicationName.isEmpty()) {
            Toast.makeText(this, "Please select a Medication Name and Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validating if the time is Selected

        if (selectedTime.equals("Medication Time")) {
            Toast.makeText(this, "Please select a Time", Toast.LENGTH_SHORT).show();
            return;
        }
        // Validating if the Date is Selected
        else if (selectedDate.equals("Start Date")) {
            Toast.makeText(this, "Please select a Start Date", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (medicationName.isEmpty()) {
            Toast.makeText(this, "Please enter the Medication Name", Toast.LENGTH_SHORT).show();
            return;
        }


        // Get the currently authenticated user
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Make sure a user is authenticated before trying to get their UID
        if (user != null) {
            String uid = user.getUid();

            // Generate a unique ID for the medication
            String medicationId = databaseReference.child("Users").child(uid).child("Saved Medication").push().getKey();

            // Create Medication object
            Medication_Data_Helper medicationDataHelper = new Medication_Data_Helper(medicationId, medicationName, selectedTime, selectedDate);

            // Save medication to Firebase database under the user's "Saved Medication" node
            if (medicationId != null) {
                DatabaseReference savedMedicationRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Saved Medication");
                savedMedicationRef.child(medicationId).setValue(medicationDataHelper)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(Add_Medication_Activity.this, "Medication saved successfully", Toast.LENGTH_SHORT).show();

                            // Start SavedMedicationActivity and pass medication details
                            Intent intent = new Intent(Add_Medication_Activity.this, Saved_Medication_Activity.class);
                            intent.putExtra("medicationName", medicationName);
                            intent.putExtra("selectedTime", selectedTime);
                            intent.putExtra("selectedDate", selectedDate);
                            startActivity(intent);

                            // Finish the activity and return to Homepage_Activity
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(Add_Medication_Activity.this, "Failed to save medication", Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Failed to save medication", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where no user is authenticated
            Toast.makeText(Add_Medication_Activity.this, "No user is authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    // On cancel function when the user clicks the cancel button while saving a new medication

    private void cancel() {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextMedicationName.getWindowToken(), 0);

        // Finish the activity and return to Homepage_Activity
        finish();
    }


}
