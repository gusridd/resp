package com.app.resp;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ChoiceAdapter extends BaseAdapter {
	private Activity activity;
	private static LayoutInflater inflater = null;
	private ArrayList<HashMap<String, String>> choices;
	private HashMap<String, String> lastCreated;
	private String[] choiceLetters = {"A)", "B)", "C)", "D)", "E)", "F)"};

	public ChoiceAdapter(Activity a, ArrayList<HashMap<String, String>> c) {
		activity = a;
		choices = c;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount() {
		return choices.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.choice_item, null);

		// Recuperamos los componentes de una fila
		TextView letter = (TextView) vi.findViewById(R.id.text_choice_letter);
		EditText inputChoice = (EditText) vi.findViewById(R.id.input_choice);
		ImageButton delete = (ImageButton) vi.findViewById(R.id.button_delete_choice);
		
		// Activamos el listener especial del boton, que guarda una referencia al inputChoice
		

		HashMap<String, String> choice = choices.get(position);

		// Cargamos los valores correspondientes
		//letter.setText(choice.get("letter"));
		letter.setText(choiceLetters[position]);
		inputChoice.setText(choice.get("choice"));

		final int pos = position;
		final EditText inp = inputChoice; 
		inputChoice.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false && !((EditText)v).getText().toString().equals("ZXACBCAHBW")) {
					// Guardamos lo que se haya escrito
					HashMap<String, String> item = choices.get(pos);
					item.remove("choice");
					item.put("choice", inp.getText().toString());
					choices.set(pos, item);
				}
			}
		});
		
		// Chequeamos si fue el ultimo creado para darle el foco
		if (lastCreated == choice) {
			inputChoice.requestFocus();
		}
		
		if(this.getCount() <= 2){
			delete.setVisibility(View.INVISIBLE);
		}
		else{
			delete.setVisibility(View.VISIBLE);
		}
		
		return vi;
	}
	
	public void setLastCreated (HashMap<String, String> last) {
		lastCreated = last;
	}
}
