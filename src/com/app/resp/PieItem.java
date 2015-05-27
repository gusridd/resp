package com.app.resp;

public class PieItem {
	private int Count;
	private String Label;
	private float Percent;
	private int Color;
	
	public void setCount(int count) {
		this.Count = count;		
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public float getPercent() {
		return Percent;
	}

	public void setPercent(float percent) {
		Percent = percent;
	}

	public int getColor() {
		return Color;
	}

	public void setColor(int color) {
		Color = color;
	}

	public int getCount() {
		return Count;
	}
}

