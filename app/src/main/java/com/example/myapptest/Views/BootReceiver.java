package com.example.myapptest.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.AlarmManager;
import android.app.PendingIntent;

import androidx.annotation.NonNull;

import com.example.myapptest.Controllers.Medication;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Restart your alarms here
            rescheduleAlarms(context);
        }
    }

    private void rescheduleAlarms(final Context context) {
        DatabaseReference savedMedicationRef = FirebaseDatabase.getInstance().getReference().child("Saved Medication");
        savedMedicationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot medicationSnapshot : dataSnapshot.getChildren()) {
                    Medication medication = medicationSnapshot.getValue(Medication.class);
                    if (medication != null) {
                        scheduleNotification(context, medication.getTime(), Integer.parseInt(medication.getId()));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Error handling
            }
        });
    }

    private void scheduleNotification(Context context, String time, int id) {
        Intent notificationIntent = new Intent(context, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, id, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, getFutureTime(time), pendingIntent);
    }

    private long getFutureTime(String time) {
        String[] parts = time.split(":");  // Use 'time' instead of 'selectedTime'
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