package com.example.myapptest.Views;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapptest.Controllers.Medication;
import com.example.myapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddMedicationActivity extends AppCompatActivity {

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

        // Get references to views
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
            Intent intent = new Intent(AddMedicationActivity.this, Homepage_Activity.class);
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


    private void saveMedication() {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextMedicationName.getWindowToken(), 0);

        // Get user input values
        String medicationName = editTextMedicationName.getText().toString().trim();
        String selectedTime = spinnerTime.getText().toString();
        String selectedDate = date_view.getText().toString();

        // Validate input
        if (medicationName.isEmpty()) {
            Toast.makeText(this, "Please enter the medication name", Toast.LENGTH_SHORT).show();
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
            Medication medication = new Medication(medicationId, medicationName, selectedTime, selectedDate);

            // Save medication to Firebase database under the user's "Saved Medication" node
            if (medicationId != null) {
                DatabaseReference savedMedicationRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Saved Medication");
                savedMedicationRef.child(medicationId).setValue(medication)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddMedicationActivity.this, "Medication saved successfully", Toast.LENGTH_SHORT).show();

                            // Start SavedMedicationActivity and pass medication details
                            Intent intent = new Intent(AddMedicationActivity.this, SavedMedicationActivity.class);
                            intent.putExtra("medicationName", medicationName);
                            intent.putExtra("selectedTime", selectedTime);
                            intent.putExtra("selectedDate", selectedDate);
                            startActivity(intent);

                            // Finish the activity and return to Homepage_Activity
                            finish();
                        })
                        .addOnFailureListener(e -> Toast.makeText(AddMedicationActivity.this, "Failed to save medication", Toast.LENGTH_SHORT).show());
                scheduleNotification(selectedTime);
            } else {
                Toast.makeText(this, "Failed to save medication", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Handle the case where no user is authenticated
            Toast.makeText(AddMedicationActivity.this, "No user is authenticated.", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancel() {
        // Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editTextMedicationName.getWindowToken(), 0);

        // Finish the activity and return to Homepage_Activity
        finish();
    }

    private void scheduleNotification(String selectedTime){
        Intent notificationIntent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, notificationIntent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, getFutureTime(selectedTime), pendingIntent);
    }
    private long getFutureTime(String selectedTime){
        String[] parts = selectedTime.split(":");
        int hour = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);

        Calendar calendar = Calendar.getInstance();
        Calendar now = (Calendar) calendar.clone();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        if(calendar.before(now)){
            calendar.add(Calendar.DATE, 1);
        }
     return calendar.getTimeInMillis();
    }
}
