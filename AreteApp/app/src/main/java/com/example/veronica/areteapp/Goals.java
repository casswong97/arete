package com.example.veronica.areteapp;

public class Goals {
    String goalName;
    Boolean completion;
    String goalReflectionAnswer;

    public Goals () {
        // Default constructor
    }

    public Goals (String goalInput, Boolean completed, String reflection) {
        this.goalName = goalInput;
        this.completion = completed;
        this.goalReflectionAnswer = reflection;
    }

    public String getGoalName() {
        return goalName;
    }

    public String getGoalReflectionAnswer() {
        return goalReflectionAnswer;
    }

    public Boolean getCompletion() {
        return completion;
    }
}
