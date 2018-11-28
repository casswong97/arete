package com.example.veronica.areteapp;

public class ListItem
{
	String text = null;
	boolean selected = false;

	public ListItem(String text, boolean selected)
	{
		super();
		this.text = text;
		this.selected = selected;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}

}