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
    public void setGoalName(String goalName) {this.goalName = goalName;}

    public String getGoalReflectionAnswer() {
        return goalReflectionAnswer;
    }
    public void setGoalReflectionAnswer (String goalName) {this.goalName = goalName;}

    public Boolean getCompletion() {return completion; }
    public void setCompletion(String goalName) {this.goalName = goalName;}
}
