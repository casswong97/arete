package com.example.veronica.areteapp;

public class DailyExercise
{
    private String dailyExerciseQuestion;
    private String dailyExerciseAnswer;

    public DailyExercise() {
        // Default Constructor
    }

    public DailyExercise(String dailyExerciseQuestion, String dailyExerciseAnswer)
    {
        this.dailyExerciseQuestion = dailyExerciseQuestion;
        this.dailyExerciseAnswer = dailyExerciseAnswer;
    }

    public String getDailyExerciseQuestion()
    {
        return dailyExerciseQuestion;
    }

    public void setDailyExerciseQuestion(String dailyExerciseQuestion)
    {
        this.dailyExerciseQuestion = dailyExerciseQuestion;
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
