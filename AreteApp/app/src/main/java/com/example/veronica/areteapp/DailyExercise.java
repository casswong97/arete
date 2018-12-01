package com.example.veronica.areteapp;

public class DailyExercise {
    String dailyExerciseAnswer;

    public DailyExercise() {
        // Default Constructor
    }

    public DailyExercise(String dailyExerciseAnswer) {
        this.dailyExerciseAnswer = dailyExerciseAnswer;
    }

    public String getDailyExerciseAnswer() {
        return dailyExerciseAnswer;
    }

    public void setDailyExerciseAnswer(String dailyExerciseAnswer) {
        this.dailyExerciseAnswer = dailyExerciseAnswer;
    }
}
