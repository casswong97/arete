package com.example.veronica.areteapp;

public class DayReflection {

    float rating;
    String dayReflection;

    public DayReflection() {
        // Default Constructor
    }

    public DayReflection(float qualityDay, String reflection) {
        this.rating = qualityDay;
        this.dayReflection = reflection;

    }

    public float getRating() {
        return rating;
    }

    public String getDayReflection() {
        return dayReflection;
    }
}
