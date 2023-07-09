package com.example.myapptest.Controllers;

import java.io.Serializable;

public class Medication implements Serializable {
    private String id;
    private String name;
    private String time;
    private String customNotes;

    public Medication() {
        // Required empty constructor for Firebase
    }

    public Medication(String medicationId, String medicationName, String selectedTime) {
        this.id = medicationId;
        this.name = medicationName;
        this.time = selectedTime;
    }

    public Medication(String id, String name, String time, String customNotes) {
        this.id = id;
        this.name = name;
        this.time = time;
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

    public String getCustomNotes() {
        return customNotes;
    }

    public void setCustomNotes(String customNotes) {
        this.customNotes = customNotes;
    }
}
