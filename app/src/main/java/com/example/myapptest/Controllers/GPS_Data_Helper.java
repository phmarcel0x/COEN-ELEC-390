package com.example.myapptest.Controllers;

public class GPS_Data_Helper {
    float latitude;
    float longitude;

    public GPS_Data_Helper(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}
