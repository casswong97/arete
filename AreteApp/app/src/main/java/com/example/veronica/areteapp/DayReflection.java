package com.example.veronica.areteapp;

public class DayReflection {
    int status;
    String dayReflection;

    public DayReflection() {
        // Default Constructor
    }

    public DayReflection(int qualityDay, String reflection) {
        this.status = qualityDay;
        this.dayReflection = reflection;
    }

    public int getStatus() {
        return status;
    }

    public String getDayReflection() {
        return dayReflection;
    }
}
