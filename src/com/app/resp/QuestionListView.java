package com.app.resp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.persistence.QuestionOpenHelper;

public class QuestionListView extends LinearLayout {
	private int realId;
	private TextView text;
	private Button button;
	private ImageButton edit;
	private ImageButton delete;
	public TeacherModeActivity tma;
	QuestionListView self;
	final QuestionAdapter adapter;
	int activate = -1;

	public QuestionListView(Context context, QuestionAdapter adapter) {
		super(context);
		inflate(context, R.layout.layout_question, this);
		self = this;
		this.adapter = adapter;
		text = (TextView) findViewById(R.id.textView_question);
		button = (Button) findViewById(R.id.button_activate_question);
		edit = (ImageButton) findViewById(R.id.imageButton_edit);
		delete = (ImageButton) findViewById(R.id.imageButton_delete);
		final Context ctx = context;
		tma = (TeacherModeActivity) ctx;

		edit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Armar el intent para llamar a la otra actividad
				Intent intent = new Intent(ctx, CreateQuestionActivity.class);
				intent.putExtra("id", realId);
				ctx.startActivity(intent);
			}
		});

		final QuestionAdapter qAdapter = this.adapter;
		// Codigo para borrar la pregunta
		delete.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Mostramos un Confirm Dialog
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);

				// Setting Dialog Title
				alertDialog.setTitle(ctx.getString(R.string.delete_alert_title));

				// Setting Dialog Message
				alertDialog.setMessage(ctx.getString(R.string.delete_alert_text));

				// Setting Icon to Dialog
				alertDialog.setIcon(ctx.getResources().getDrawable(android.R.drawable.ic_delete));

				// Setting Positive "Yes" Button
				alertDialog.setPositiveButton(ctx.getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Instanciamos el helper para borrar la
								// pregunta
								QuestionOpenHelper qoh = new QuestionOpenHelper(
										ctx);
								// La matamos y notificamos
								qoh.deleteQuestion(realId);
								qAdapter.notifyDataSetChanged();
							}
						});

				// Setting Negative "NO" Button
				alertDialog.setNegativeButton(ctx.getString(R.string.no),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// Write your code here to invoke NO event

								dialog.cancel();
							}
						});

				// Showing Alert Message
				alertDialog.show();
			}
		});

		this.setOnLongClickListener(new OnLongClickListener() {

			public boolean onLongClick(View v) {
				// Armar el intent para llamar a la otra actividad
				Intent intent = new Intent(ctx, CreateQuestionActivity.class);
				intent.putExtra("id", realId);
				ctx.startActivity(intent);
				return true;
			}
		});

	}

	public Button getToggleButton() {
		return this.button;
	}

	public int getActivate() {
		return activate;
	}

	public void setQuestionText(String q) {
		this.text.setText(q);
	}

	public String getQuestionText() {
		return this.text.getText().toString();
	}

	public void setRealId(int i) {
		this.realId = i;
	}

	public void setQuestion(String question, int id, int realId) {
		setQuestionText(question);
		setId(id);
		setRealId(realId);
	}
}
