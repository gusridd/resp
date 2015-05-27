package com.app.resp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.net.QuestionSenderRunnable;
import com.app.pojo.Status;

public class StudentFinalModeActivity extends IpActivity {

	ProgressBar pb;
	Handler mHandler;
	Status mStatus = new Status();
	StudentFinalModeActivity that;
	EditText text;
	Button enter;
	
	private Runnable decide = new Runnable() {
		
		public void run() {
			//Caso exitoso
			if(mStatus.getStatus() == 0){
				Intent intent = new Intent(that, StudentRoomActivity.class);
				// agregamos la sala al intent
				intent.putExtra("room", room);
				pb.setVisibility(View.INVISIBLE);
				startActivity(intent);
			} else {
				Toast.makeText(that, R.string.connection_error, Toast.LENGTH_LONG).show();
				pb.setVisibility(View.INVISIBLE);
			}
			text.setEnabled(true);
			enter.setEnabled(true);
		}
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		that = this;
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_final_mode);
		this.pb = (ProgressBar) findViewById(R.id.progressBarStudent);
		enter = (Button) findViewById(R.id.roombutton);
		this.mHandler = new Handler();
	}

	public void studentRoom(View view) {
		QuestionSenderRunnable qsr;
		text = (EditText) findViewById(R.id.room_edittext);
		room = text.getText().toString();
		try {
			qsr = new QuestionSenderRunnable(getIp(),
					((RespApp) getApplicationContext()).TEACHER_PORT, room,
					mStatus, mHandler, decide);
			Thread t = new Thread(qsr);
			Log.d("Dialog", "show");
			pb.setVisibility(View.VISIBLE);
			text.setEnabled(false);
			enter.setEnabled(false);
			t.start();
			

		} catch (Exception e) {
			Log.d("Room Fail", "Falla la sala " + room);
			Toast.makeText(getApplicationContext(), R.string.incorrect_code,
					Toast.LENGTH_LONG).show();
		}
	}

}
