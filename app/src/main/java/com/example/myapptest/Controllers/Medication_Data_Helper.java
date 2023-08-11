package com.example.myapptest.Controllers;

import java.io.Serializable;

public class Medication_Data_Helper implements Serializable {

    // Declaration of the variables
    private String id;
    private String name;
    private String time;
    private String date;
    private String customNotes;

    // Empty Constructor
    public Medication_Data_Helper() {
        // Required empty constructor for Firebase
    }

    // Constructor with arguments for the Firebase Database
    public Medication_Data_Helper(String medicationId, String medicationName, String selectedTime, String selectedDate) {
        this.id = medicationId;
        this.name = medicationName;
        this.time = selectedTime;
        this.date = selectedDate;
    }

    public Medication_Data_Helper(String id, String name, String time, String date, String customNotes) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;
        this.customNotes = customNotes;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }
    public String getDate(){
        return date;
    }

    public String getCustomNotes() {
        return customNotes;
    }

    public void setCustomNotes(String customNotes) {
        this.customNotes = customNotes;
    }
}
