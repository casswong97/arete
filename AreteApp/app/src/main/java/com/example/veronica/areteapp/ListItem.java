package com.example.veronica.areteapp;

public class ListItem
{
	String text = "";
	boolean selected = false;
	boolean visible = true;

	public ListItem() {}

	public ListItem(String text, boolean selected, boolean visible)
	{
		super();
		this.text = text;
		this.selected = selected;
		this.visible = visible;
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

	public boolean isVisible()
	{
		return visible;
	}

	public void setVisible(boolean visible)
	{
		this.visible = visible;
	}
}