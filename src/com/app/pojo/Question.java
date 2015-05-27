package com.app.pojo;

public class Question {

	private int id;
	private String text;
	private String[] choices = new String[6];

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setChoice(int pos, String choice) {
		choices[pos - 1] = choice;
	}

	public String[] getChoices() {
		return choices;
	}
}
