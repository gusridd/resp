package com.app.resp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.app.net.MultiThreadedServer;
import com.app.persistence.QuestionOpenHelper;
import com.app.pojo.Question;

public class CreateQuestionActivity extends ListActivity {
	private ChoiceAdapter adapter;

	ArrayList<HashMap<String, String>> choicesList;
	MultiThreadedServer server;
	int[] responses = new int[6];

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_question);

		ListView lv = getListView();

		// Cargamos el header de la lista (pregunta)
		LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
		View headerView = inflater.inflate(R.layout.create_question_header, lv,
				false);
		lv.addHeaderView(headerView);

		// Y Ahora el footer (boton agregar alternativa)
		View footerView = inflater.inflate(R.layout.create_question_footer, lv,
				false);
		lv.addFooterView(footerView);

		// Vemos si habia que precargar una pregunta

		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		int id = -1;
		if (i.hasExtra("id")) {
			id = bundle.getInt("id");
		}

		final int fId = id;
		// Boton de agregar alternativa
		Button addChoice = (Button) findViewById(R.id.button_add_choice);
		addChoice.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				HashMap<String, String> item = new HashMap<String, String>();
				item.put("choice", "");
				choicesList.add(item);
				adapter.setLastCreated(item);
				adapter.notifyDataSetChanged();
				if (choicesList.size() == 6) {
					// Desactivamos el boton
					((Button) v).setEnabled(false);
					((Button) v).setText(getString(R.string.max_choices));
				}
			}
		});

		// Boton de guardar la pregunta
		Button saveQuestion = (Button) findViewById(R.id.button_save_question);
		saveQuestion.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Save the question to the database
				QuestionOpenHelper qoh = new QuestionOpenHelper(
						getBaseContext());
				String question = ((EditText) findViewById(R.id.input_question))
						.getText().toString();
				String[] choice = new String[6];
				Iterator<HashMap<String, String>> iter = choicesList.iterator();
				int ix = 0;
				while (iter.hasNext()) {
					choice[ix] = iter.next().get("choice");
					ix++;
				}
				// Vemos si la pregunta ya existia o es nueva
				// Ya existia solo si tenia id
				if (fId != -1) {
					qoh.updateQuestion(question, choice, fId);
				} else {
					qoh.insertQuestion(question, choice);
				}
				finish();
			}
		});

		choicesList = new ArrayList<HashMap<String, String>>();

		// Si la pregunta es nueva se agregan 2 alternativas por defecto
		if (id == -1) {
			HashMap<String, String> a = new HashMap<String, String>();
			HashMap<String, String> b = new HashMap<String, String>();

			a.put("A)", "Respuesta por defecto a");
			b.put("B)", "Respuesta por defecto b");

			choicesList.add(a);
			choicesList.add(b);
		}

		if (id != -1) {
			Log.d("DEBUG", "Hay que precargar datos para la id " + id);
			// Recuperamos la pregunta de la BD
			QuestionOpenHelper qoh = new QuestionOpenHelper(getBaseContext());
			Question q = qoh.getQuestion(id);

			if (q != null) {
				// Ponemos la pregunta
				((EditText) findViewById(R.id.input_question)).setText(q
						.getText());

				// Recuperamos las alternativas
				String[] choices = q.getChoices();
				String singleChoice;
				int ix = 0;
				// Las vamos poniendo en el ArrayList
				while (ix < 6 && (singleChoice = choices[ix++]) != null) {
					Log.d("DEBUG", "Precargando alternativa: " + singleChoice);
					HashMap<String, String> item = new HashMap<String, String>();
					item.put("choice", singleChoice);
					choicesList.add(item);
				}
			} else {
				Log.d("DEBUG",
						"Se consulto por una pregunta con id que no existe");
			}
		}

		Button addChoiceButton = (Button) findViewById(R.id.button_add_choice);
		if (choicesList.size() == 6) {
			// Desactivamos el boton
			addChoiceButton.setEnabled(false);
			addChoiceButton.setText(getString(R.string.max_choices));
		}
		
		adapter = new ChoiceAdapter(this, choicesList);
		setListAdapter(adapter);
	}

	public void deleteChoice(View v) {
		ListView lv = getListView();
		int position = lv.getPositionForView(v) - 1; // Se le resta 1 por el
														// header
		ViewGroup linearLayout = (ViewGroup) lv.getChildAt(position + 1);
		EditText inputChoice = (EditText) linearLayout.getChildAt(1); // 1 es la
																		// posicion
																		// del
																		// EditText
		inputChoice.setText("ZXACBCAHBW");
		inputChoice.requestFocus();
		choicesList.remove(position);
		adapter.notifyDataSetChanged();

		// Siempre hay que activar el boton cuando se borra una alternativa
		((Button) findViewById(R.id.button_add_choice)).setEnabled(true);
		((Button) findViewById(R.id.button_add_choice))
				.setText(getString(R.string.add_choice));
	}

	protected void onResume() {
		Log.d("EVENT", "onResume");
		super.onResume();
		adapter.notifyDataSetChanged();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_about, menu);
		return true;
	}

	public void about() {
		Intent intent = new Intent(this, AboutActivity.class);
		this.startActivity(intent);
		return;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.about:
			about();
			return true;
		default:
			return super.onOptionsItemSelected(item);

		}
	}

}
