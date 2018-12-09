package com.example.veronica.areteapp;

import java.util.ArrayList;
import java.util.Arrays;

public class Questions
{
	private ArrayList<String> questionList;

	public Questions()
	{
		this.questionList = new ArrayList<String>(Arrays.asList(
				"What do you care about deeply, and why?",
				"List what your top three strengths are.",
				"Reflect a societal problem for which you would like to contribute a solution.",
				"When you have had great day, what was it you were doing and what are you not doing?"));
	}

	public String getNextQuestion(int index)
	{
		index+=1;
		return questionList.get(index % questionList.size());
	}
}
