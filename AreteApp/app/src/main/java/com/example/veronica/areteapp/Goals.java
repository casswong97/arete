package com.example.veronica.areteapp;

public class Goals {
    String goalName = "";
    boolean completion = false;
    String goalReflectionAnswer;
    boolean visible = true;

    public Goals () {
        // Default constructor
    }

    public Goals (String goalName, boolean completed, String reflection, boolean visible) {
        this.goalName = goalName;
        this.completion = completed;
        this.goalReflectionAnswer = reflection;
        this.visible = visible;
    }

    public String getGoalName() {
        return goalName;
    }
    public void setGoalName(String goalName) {this.goalName = goalName;}

    public String getGoalReflectionAnswer() {
        return goalReflectionAnswer;
    }
    public void setGoalReflectionAnswer (String goalName) {this.goalName = goalName;}

    public boolean getCompletion() {
        return completion;
    }

    public boolean getVisible()
	{
		return this.visible;
	}

    public void setCompletion(boolean completion)
    {
        this.completion = completion;
    }

    {
        this.goalReflectionAnswer = goalReflectionAnswer;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

}
