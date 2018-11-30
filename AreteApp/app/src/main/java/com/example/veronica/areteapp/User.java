package com.example.veronica.areteapp;

public class User {
    String userName;
    String calendarKey;

    public User() {
        // Default Constructor
    }

    public User(String username) {
        this.userName = username;
        this.calendarKey = username + "Calendar";
    }

    public String getUserName() {
        return userName;
    }

    public String getCalendarKey() {
        return calendarKey;
    }
}
