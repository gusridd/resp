package com.app.resp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MainMenuActivity extends RespActivity {

	private AlertDialog.Builder alertDialog;
	private Button teacherButton;
	private Button studentButton;
	private TextView alertTextView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		teacherButton = (Button) findViewById(R.id.button_teacher_mode);
		studentButton = (Button) findViewById(R.id.button_student_mode);
		alertTextView = (TextView) findViewById(R.id.textView_alert);

		alertDialog = new AlertDialog.Builder(MainMenuActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.conex));

		// Setting Dialog Message
		alertDialog
				.setMessage(getString(R.string.conex_question));

		// Setting Icon to Dialog
		// alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton(getString(R.string.yes),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
						studentButton.setEnabled(true);
						teacherButton.setEnabled(true);
						alertTextView.setText("");
						dialog.cancel();
					}
				});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton(getString(R.string.no),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						studentButton.setEnabled(false);
						teacherButton.setEnabled(false);
						alertTextView.setText(getString(R.string.conex_warning));
						dialog.cancel();
					}
				});

		
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d("onResume", "wifi is not conected");
		if (!isWiFiConnected()) {
			// Showing Alert Message
			//alertDialog.show();
		}
		if (isWiFiConnected()) {
			studentButton.setEnabled(true);
			teacherButton.setEnabled(true);
			alertTextView.setText("");
		}
	}

	public void teacherMode(View view) {
		Intent intent = new Intent(this, TeacherModeActivity.class);
		startActivity(intent);
	}

	public void callActivity(View view) {
		Intent intent = new Intent(this, StudentFinalModeActivity.class);
		startActivity(intent);
	}
	public void tutorialActivity(View view) {
		Intent intent = new Intent(this, TutorialActivity.class);
		startActivity(intent);
	}

	public boolean isWiFiConnected() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		return netInfo.isConnectedOrConnecting();
	}
}
