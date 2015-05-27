package com.app.resp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.net.IpFormatter;
import com.app.persistence.QuestionOpenHelper;
import com.app.pojo.Question;

public class TeacherModeActivity extends RespActivity {
	int[] responses = new int[6];
	TextView tv;
	TextView text_room;
	String room;
	ListView lv;
	QuestionAdapter questionAdapter;
	ArrayList<QuestionListView> question_view_list = new ArrayList<QuestionListView>();

	int votes[] = new int[4];
	String ip = "Esto no es magico";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d("EVENT", "onCreate");
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teacher_mode);
		text_room = (TextView) findViewById(R.id.text_room);
		questionAdapter = new QuestionAdapter(this);
		Log.d("DEBUG", "getting Intent");
		Log.d("CAST", findViewById(R.id.listView_questionsa).getClass()
				.toString());
		lv = (ListView) findViewById(R.id.listView_questionsa);

		int l = questionAdapter.getCount();
		for (int i = 0; i < l; i++) {
			question_view_list.add((QuestionListView) questionAdapter.getView(
					i, null, this.lv));
		}
		lv.setAdapter(questionAdapter);
	}

	@Override
	protected void onResume() {
		Log.d("EVENT", "onResume");
		super.onResume();
		this.showIp();
		questionAdapter.setRoom(room);
		questionAdapter.notifyDataSetChanged();
	}

	public QuestionAdapter getQuestionAdapter() {
		return questionAdapter;
	}

	private void showIp() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		IpFormatter formatter = new IpFormatter();
		room = formatter.ipEncode(dhcp.netmask, dhcp.ipAddress);
		text_room.setText(room);
	}

	static public String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}
		return "NO IP";
	}

	public void launchCreateQuestionActivity(View v) {
		Intent intent = new Intent(this, CreateQuestionActivity.class);
		startActivity(intent);
	}

	public ArrayList<QuestionListView> getQuestion_view_list() {
		return question_view_list;
	}

	public void export() {
		Log.d("EVENT", "exportando TODO");
		Intent intent = new Intent(android.content.Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(android.content.Intent.EXTRA_SUBJECT,
				getString(R.string.subject));
		ArrayList<Question> questions = (new QuestionOpenHelper(this))
				.getQuestions();
		ArrayList<int[]> ans = (new QuestionOpenHelper(this)).getAllAnswers();
		String exporting = "";
		for (int j = 0; j < questions.size(); j++) {
			Question q = questions.get(j);
			int[] responses = ans.get(j);
			String[] choices = q.getChoices();

			int size = 0;
			int sum = 0;
			for (int i = 0; i < 6; i++) {

				if (choices[i] == null) {
					size = i;
					break;
				}
				sum = sum + responses[i];
				if (i == 5) {
					size = i + 1;
				}
			}

			String aux = "";
			for (int i = 0; i < size; i++) {
				aux = aux + choices[i] + " : " + responses[i] + '\n';
			}
			if (sum != 0) {
				exporting = exporting + q.getText() + '\n' + aux + '\n'
						+ "-------" + '\n' + '\n';
			}
		}
		if (exporting.equals("")) {
			Toast.makeText(getBaseContext(), R.string.export_error, Toast.LENGTH_SHORT).show();
		} else {
			intent.putExtra(android.content.Intent.EXTRA_TEXT, exporting);
			startActivity(Intent
					.createChooser(intent, getText(R.string.export)));
		}
		return;
	}

	public void deleteAll() {
		(new QuestionOpenHelper(this)).deleteAnswers();
		Toast.makeText(getBaseContext(), R.string.delete_answers, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.export:
			export();
			return true;
		case R.id.deleteall:
			deleteAll();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_teachermode, menu);
		return true;
	}

}
