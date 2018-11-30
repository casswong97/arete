package com.example.veronica.areteapp;

public class Date {
    Date dateEntry;
    String exerciseAnswer;
    String goalsKey;
    String reflectionKey;

    public Date() {
        // Default Constructor
    }

    public Date(Date dateEntry, String exerciseResponse, String goalsKey, String reflectionKey) {
        this.dateEntry = dateEntry;
        this.exerciseAnswer = exerciseResponse;
        this.goalsKey = goalsKey;
        this.reflectionKey = reflectionKey;
    }

    public Date getDateEntry() {
        return dateEntry;
    }

    public String getExerciseAnswer() {
        return exerciseAnswer;
    }

    public String getGoalsKey() {
        return goalsKey;
    }

    public String getReflectionKey() {
        return reflectionKey;
    }
}
