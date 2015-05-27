package com.app.resp;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.persistence.QuestionOpenHelper;
import com.app.pojo.Question;

public class QuestionAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<Question> questions;
	ArrayList<String> ipList;
	String room;
	Question q;

	public QuestionAdapter(Context context) {
		super();
		this.context = context;
		this.questions = (new QuestionOpenHelper(this.context)).getQuestions();
	}
	
	@Override
	public void notifyDataSetChanged() {
		this.questions = (new QuestionOpenHelper(this.context)).getQuestions();
		super.notifyDataSetChanged();
	}
	
	public int getCount() {
		return questions.size();
	}

	public Object getItem(int k) {
		return questions.get(k);
	}

	public long getItemId(int arg0) {
		return 0;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		QuestionListView qlv;
		if (convertView == null) {
			qlv = new QuestionListView(parent.getContext(),this);
		} else {
			qlv = (QuestionListView) convertView;
		}
		
		q = questions.get(position);
		final String questionText = q.getText();
		final int id=q.getId();
		
		//Setear la acci�n del toggleButton
		final QuestionAdapter self = this;
		final int final_position = position;
		
		qlv.getToggleButton().setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(self.context, StatisticsActivity.class);
				intent.putExtra("id", id);
				intent.putExtra("question", questionText);
				intent.putExtra("room", room);
				self.context.startActivity(intent);
				
			}
		});
		
		// Cargar la pregunta
		qlv.setQuestion(q.getText(), position + 1, q.getId());
		
		return qlv;
	}
	
}
