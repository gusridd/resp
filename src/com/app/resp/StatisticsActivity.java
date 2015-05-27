package com.app.resp;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.net.MultiThreadedServer;
import com.app.net.QuestionSenderRunnable;
import com.app.persistence.QuestionOpenHelper;
import com.app.pojo.Question;

public class StatisticsActivity extends RespActivity {

	MultiThreadedServer server;
	protected BroadcastReceiver mWifiStateChangedReceiver;

	LinearLayout layout_a;
	LinearLayout layout_b;
	LinearLayout layout_c;
	LinearLayout layout_d;
	LinearLayout layout_e;
	LinearLayout layout_f;

	TextView questionTV;

	TextView choice_a;
	TextView choice_b;
	TextView choice_c;
	TextView choice_d;
	TextView choice_e;
	TextView choice_f;

	Button count_a;
	Button count_b;
	Button count_c;
	Button count_d;
	Button count_e;
	Button count_f;

	public int[] responses;
	String[] choices;
	String roomText;
	Question q;
	View_PieChart pieChart;
	private int BgColor = 0;
	private int Size = 202;

	TextView room;

	boolean roteRecien = false;
	Configuration oldConfig;
	/*
	 * public void onConfigurationChanged(Configuration newConfig) {
	 * super.onConfigurationChanged(newConfig);
	 * 
	 * // Checks the orientation of the screen if (newConfig.orientation ==
	 * Configuration.ORIENTATION_LANDSCAPE) { Toast.makeText(this, "landscape",
	 * Toast.LENGTH_SHORT).show(); } else if (newConfig.orientation ==
	 * Configuration.ORIENTATION_PORTRAIT){ Toast.makeText(this, "portrait",
	 * Toast.LENGTH_SHORT).show(); } }
	 */

	private int id_final;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics_canvas);
		// Register the receiver for wifi state
		
		
		
		pieChart = new View_PieChart(this);
		int OverlayId = R.drawable.circle202;
		pieChart.setLayoutParams(new LayoutParams(Size, Size));
		pieChart.setGeometry(Size, Size, 5, 5, 5, 5, OverlayId);
		pieChart.setSkinParams(BgColor);

		LinearLayout canvas_layout = (LinearLayout) findViewById(R.id.canvas_container);
		canvas_layout.addView(pieChart, 0);
		canvas_layout.setGravity(Gravity.CENTER_HORIZONTAL);

		ArrayList<Question> questions = (new QuestionOpenHelper(this))
				.getQuestions();
		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		int id = -1;
		if (i.hasExtra("id")) {
			id = bundle.getInt("id");
		}
		q=(new QuestionOpenHelper(this)).getQuestion(id);


		String questionText;

		if (i.hasExtra("question")) {
			questionText = bundle.getString("question");
			questionTV = (TextView) findViewById(R.id.current_question);
			questionTV.setText(questionText);
		}

		Log.d("DEBUG", "question id: " + id+" Pregunta: "+q.getText());
		
		choices = q.getChoices();

		layout_a = (LinearLayout) findViewById(R.id.answer_a);
		layout_b = (LinearLayout) findViewById(R.id.answer_b);
		layout_c = (LinearLayout) findViewById(R.id.answer_c);
		layout_d = (LinearLayout) findViewById(R.id.answer_d);
		layout_e = (LinearLayout) findViewById(R.id.answer_e);
		layout_f = (LinearLayout) findViewById(R.id.answer_f);

		choice_a = (TextView) findViewById(R.id.current_a);
		choice_b = (TextView) findViewById(R.id.current_b);
		choice_c = (TextView) findViewById(R.id.current_c);
		choice_d = (TextView) findViewById(R.id.current_d);
		choice_e = (TextView) findViewById(R.id.current_e);
		choice_f = (TextView) findViewById(R.id.current_f);

		count_a = (Button) findViewById(R.id.count_a);
		count_b = (Button) findViewById(R.id.count_b);
		count_c = (Button) findViewById(R.id.count_c);
		count_d = (Button) findViewById(R.id.count_d);
		count_e = (Button) findViewById(R.id.count_e);
		count_f = (Button) findViewById(R.id.count_f);

		if (i.hasExtra("room")) {
			roomText = bundle.getString("room");
			room = (TextView) findViewById(R.id.room_textview);
			room.setText(getString(R.string.room) + roomText);
		}

		choice_a.setText(q.getChoices()[0]);
		choice_b.setText(q.getChoices()[1]);
		choice_c.setText(q.getChoices()[2]);
		choice_d.setText(q.getChoices()[3]);
		choice_e.setText(q.getChoices()[4]);
		choice_f.setText(q.getChoices()[5]);

		RespApp app = (RespApp) getApplicationContext();
		app.getTeacherServer().setActivity(this);

		int[] answers=(new QuestionOpenHelper(this)).getAnswers(""+id);
		id_final=id;
		app.responses=answers;
		app.setResponses(answers);
		responses=app.responses;

		this.showResponses();
		this.sendQuestion(q);
	}

	private ArrayList<PieItem> cacheEmpty = new ArrayList<PieItem>();

	private void configGraph() {
		ArrayList<PieItem> list = generateDataFromResponses(responses);
		int MaxCount = 0;
		for (int i = 0; i < list.size(); i++) {
			MaxCount += responses[i];
		}
		if (MaxCount != 0) {
			pieChart.setData(list, MaxCount);
		} else {
			pieChart.setData(cacheEmpty, MaxCount);
		}
		pieChart.invalidate();
	}

	private ArrayList<PieItem> generateDataFromResponses(int[] responses) {
		ArrayList<PieItem> list = new ArrayList<PieItem>();
		for (int i = 0; i < responses.length; i++) {
			PieItem item = new PieItem();
			item.setCount(responses[i]);
			item.setLabel(getLabel(i));
			item.setColor(getColor(i));
			list.add(item);
		}
		return list;
	}

	private String getLabel(int i) {
		switch (i) {
		case 0:
			return "a";
		case 1:
			return "b";
		case 2:
			return "c";
		case 3:
			return "d";
		case 4:
			return "e";
		case 5:
			return "f";
		default:
			return "";
		}
	}

	private int getColor(int i) {
		switch (i) {
		case 0:
			return 0xFF2F6699;
		case 1:
			return 0xFF99CC00;
		case 2:
			return 0xFFFF4444;
		case 3:
			return 0xFF33B5E5;
		case 4:
			return 0xFFAA66CC;
		case 5:
			return 0xFFFFBB33;
		default:
			return 0;
		}
	}

	public void reSend(View v) {
		Log.d("EVENT", "sending Questions...");
		sendQuestion(q);

	}

	public void showResponses() {
		if (choices == null)
			return;
		int size = -1;
		for (int i = 0; i < 6; i++) {
			if (choices[i] == null) {
				Log.d("DEBUG", "# choices: " + i);
				size = i;
				break;
			}
		}
		// This call configures the data for making the graph visible
		configGraph();

		count_a.setText(Integer.toString(responses[0]));
		count_b.setText(Integer.toString(responses[1]));
		count_c.setText(Integer.toString(responses[2]));
		count_d.setText(Integer.toString(responses[3]));
		count_e.setText(Integer.toString(responses[4]));
		count_f.setText(Integer.toString(responses[5]));

		switch (size) {
		case 2:
			if (layout_c.getParent() != null && layout_d.getParent() != null
					&& layout_e.getParent() != null
					&& layout_f.getParent() != null) {
				System.out.println("layout_c: " + layout_c);
				System.out.println("parent" + layout_c.getParent());
				((ViewManager) layout_c.getParent()).removeView(layout_c);
				((ViewManager) layout_d.getParent()).removeView(layout_d);
				((ViewManager) layout_e.getParent()).removeView(layout_e);
				((ViewManager) layout_f.getParent()).removeView(layout_f);
			}
			break;
		case 3:
			if (layout_d.getParent() != null && layout_e.getParent() != null
					&& layout_f.getParent() != null) {
				((ViewManager) layout_d.getParent()).removeView(layout_d);
				((ViewManager) layout_e.getParent()).removeView(layout_e);
				((ViewManager) layout_f.getParent()).removeView(layout_f);
			}
			break;
		case 4:
			if (layout_e.getParent() != null && layout_f.getParent() != null) {
				((ViewManager) layout_e.getParent()).removeView(layout_e);
				((ViewManager) layout_f.getParent()).removeView(layout_f);
			}
			break;
		case 5:
			if (layout_f.getParent() != null) {
				((ViewManager) layout_f.getParent()).removeView(layout_f);
			}
			break;
		default:
			break;
		}
	}

	private void sendQuestion(Question q) {
		QuestionParser qp = new QuestionParser();
		String question = qp.getStringFromQuestion(q);
		Log.d("CAST", getApplicationContext().getClass().toString());
		RespApp app = (RespApp) getApplicationContext();
		ArrayList<String> ipList = app.getIpList();
		int count = 0;
		if (ipList != null) {
			Iterator<String> i = ipList.iterator();
			while (i.hasNext()) {
				QuestionSenderRunnable qsr = new QuestionSenderRunnable(i
						.next().toString(), app.STUDENT_PORT, question);
				(new Thread(qsr)).start();
				count++;
			}
		}
		Toast.makeText(
				getApplicationContext(),
				getString(R.string.question_send_to) + " " + count + " "
						+ getString(R.string.students), Toast.LENGTH_SHORT)
				.show();
		return;
	}

	public void eraseData(View v) {
		for (int i = 0; i < responses.length; i++) {
			responses[i] = 0;
		}
		(new QuestionOpenHelper(this)).saveChoices("" + id_final, responses);
		QuestionParser qp = new QuestionParser();
		Question eraseQuestion = new Question();
		eraseQuestion.setText("Erase4hjfasd9415hj90hjq54290fhj2908fash");
		eraseQuestion.setChoice(1, "alt1");
		eraseQuestion.setChoice(2, "alt2");
		String seraseQuestion = qp.getStringFromQuestion(eraseQuestion);
		Log.d("CAST", getApplicationContext().getClass().toString());
		RespApp app = (RespApp) getApplicationContext();
		ArrayList<String> ipList = app.getIpList();
		int count = 0;
		if (ipList != null) {
			Iterator<String> i = ipList.iterator();
			while (i.hasNext()) {
				QuestionSenderRunnable qsr = new QuestionSenderRunnable(i
						.next().toString(), app.STUDENT_PORT, seraseQuestion);
				(new Thread(qsr)).start();
				count++;
			}
		}
		Toast.makeText(getApplicationContext(),
				"Reset enviado" + count + " alumnos", Toast.LENGTH_SHORT)
				.show();

		showResponses();
	}

	protected void onPause() {
		Log.d("EVENT", "onPause: Guardando alternativas");
		// Alternatives are saved for future use
		(new QuestionOpenHelper(this)).saveChoices("" + id_final, responses);
		Log.d("EVENT", "onPause");
		try {
			this.unregisterReceiver(mWifiStateChangedReceiver);
		} catch (IllegalArgumentException e) {
			Log.e("ERROR", "unregisterReceiver", e);
		}
		super.onPause();
	}

	public void export() {
		Log.d("EVENT", "exportando");
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getString(R.string.subject));
		String[] choices = q.getChoices();
		int size = 0;
		for (int i = 0; i < 6; i++) {
			if (choices[i] == null) {
				size = i;
				break;
			}
			
			if(i==5){
				size=i+1;
			}
		}
		String aux = "";
		for (int i = 0; i < size; i++) {
			aux = aux + choices[i] + " : " + responses[i] + '\n';
		}

		String exporting = q.getText() + '\n' + aux;
		intent.putExtra(android.content.Intent.EXTRA_TEXT, exporting);
		startActivity(Intent.createChooser(intent, getText(R.string.export)));
		return;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.export:
			export();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_statistics, menu);
		return true;
	}
	protected void onResume() {
		mWifiStateChangedReceiver = new WifiBroadcastReceiver(this);
		this.registerReceiver(mWifiStateChangedReceiver, new IntentFilter(
		WifiManager.WIFI_STATE_CHANGED_ACTION));
		Log.d("EVENT", "onResume");
		super.onResume();
		
	}

}
