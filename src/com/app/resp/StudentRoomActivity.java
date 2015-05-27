package com.app.resp;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.net.ClientListenerFinalRunnable;
import com.app.net.QuestionSenderRunnable;
import com.app.pojo.Question;

public class StudentRoomActivity extends IpActivity {

	EditText inputResponse;
	Button sendButton;
	StudentRoomActivity thisactivity;
	protected BroadcastReceiver mWifiStateChangedReceiver;
	ClientListenerFinalRunnable questionListener;
	// todos los textview de la vista.
	TextView question, la, ca, lb, cb, lc, cc, ld, cd, le, ce, lf, cf, roomtv;
	// todos los botones
	Button ba, bb, bc, bd, be, bf;
	Question q;
	RespApp app;
	private Question lastQ;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_room);
		thisactivity = this;

	

		Intent i = getIntent();
		Bundle bundle = i.getExtras();
		room = null;
		app = (RespApp) getApplicationContext();

		if (i.hasExtra("room")) {
			room = bundle.getString("room");
		}
		Log.d("Student in the room", "Student have acces to room " + room);
		QuestionSenderRunnable qsr;
		try {
			qsr = new QuestionSenderRunnable(getIp(), app.TEACHER_PORT, "ok");
			(new Thread(qsr)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

		roomtv = (TextView) findViewById(R.id.room_textview);
		roomtv.setText("Sala Virtual: " + room);
		ca = (TextView) findViewById(R.id.textview_choice_a);
		cb = (TextView) findViewById(R.id.textview_choice_b);
		cc = (TextView) findViewById(R.id.textview_choice_c);
		cd = (TextView) findViewById(R.id.textview_choice_d);
		ce = (TextView) findViewById(R.id.textview_choice_e);
		cf = (TextView) findViewById(R.id.textview_choice_f);
		question = (TextView) findViewById(R.id.textview_question);
		question.setText(getString(R.string.must_wait));
		ba = (Button) findViewById(R.id.button_choice_a);
		bb = (Button) findViewById(R.id.button_choice_b);
		bc = (Button) findViewById(R.id.button_choice_c);
		bd = (Button) findViewById(R.id.button_choice_d);
		be = (Button) findViewById(R.id.button_choice_e);
		bf = (Button) findViewById(R.id.button_choice_f);
		// los hacemos invisibles hasta que aparezca una pregunta.
		setVisibleQuestion(true);
		setVisibleA(false);
		setVisibleB(false);
		setVisibleC(false);
		setVisibleD(false);
		setVisibleE(false);
		setVisibleF(false);
		this.startServer();
	}

	public void setQuestion() {

		if (q != null) {
			if (lastQ != null) {
				Log.d("LastQuestion", "last question =! de null");
				if (q.getText().compareTo(lastQ.getText()) == 0) {
					return;
				}

			}

			if ("Erase4hjfasd9415hj90hjq54290fhj2908fash"
					.compareTo(q.getText()) == 0) {
				lastQ = null;
				return;
			}
			question.setText(q.getText());

			String choices[] = q.getChoices();
			int pos = 0;
			for (int i = 0; i < 6; i++) {
				if (choices[i] == null) {
					pos = i;
					break;
				}

				if (i == 5) {
					pos = i + 1;
				}
			}

			if (pos == 0) {// no hay ninguna pregunta
				setVisibleQuestion(false);
				setVisibleA(false);
				setVisibleB(false);
				setVisibleC(false);
				setVisibleD(false);
				setVisibleE(false);
				setVisibleF(false);
			} else {
				if (pos == 1) {// hay 1 pregunta
					setVisibleQuestion(true);
					setVisibleA(true);
					setVisibleB(false);
					setVisibleC(false);
					setVisibleD(false);
					setVisibleE(false);
					setVisibleF(false);
					ca.setText(choices[0]);

				} else {
					if (pos == 2) {// hay 2 preguntas
						setVisibleQuestion(true);
						setVisibleA(true);
						setVisibleB(true);
						setVisibleC(false);
						setVisibleD(false);
						setVisibleE(false);
						setVisibleF(false);
						ca.setText(choices[0]);
						cb.setText(choices[1]);

					} else {
						if (pos == 3) {// hay 3 preguntas
							setVisibleQuestion(true);
							setVisibleA(true);
							setVisibleB(true);
							setVisibleC(true);
							setVisibleD(false);
							setVisibleE(false);
							setVisibleF(false);
							ca.setText(choices[0]);
							cb.setText(choices[1]);
							cc.setText(choices[2]);
						} else {
							if (pos == 4) {// hay 4 preguntas
								setVisibleQuestion(true);
								setVisibleA(true);
								setVisibleB(true);
								setVisibleC(true);
								setVisibleD(true);
								setVisibleE(false);
								setVisibleF(false);
								ca.setText(choices[0]);
								cb.setText(choices[1]);
								cc.setText(choices[2]);
								cd.setText(choices[3]);
							} else {
								if (pos == 5) {// hay 5 preguntas
									setVisibleQuestion(true);
									setVisibleA(true);
									setVisibleB(true);
									setVisibleC(true);
									setVisibleD(true);
									setVisibleE(true);
									setVisibleF(false);
									ca.setText(choices[0]);
									cb.setText(choices[1]);
									cc.setText(choices[2]);
									cd.setText(choices[3]);
									ce.setText(choices[4]);
								} else {
									if (pos == 6) {// hay 6 preguntas
										setVisibleQuestion(true);
										setVisibleA(true);
										setVisibleB(true);
										setVisibleC(true);
										setVisibleD(true);
										setVisibleE(true);
										setVisibleF(true);
										ca.setText(choices[0]);
										cb.setText(choices[1]);
										cc.setText(choices[2]);
										cd.setText(choices[3]);
										ce.setText(choices[4]);
										cf.setText(choices[5]);
									}
								}

							}
						}
					}
				}
			}
		}
	}

	private void choice(View v, String c) {
		Log.d("Student", "to send: " + c);
		QuestionSenderRunnable qsr;
		try {
			qsr = new QuestionSenderRunnable(getIp(), app.TEACHER_PORT, c);
			(new Thread(qsr)).start();
			setVisibleQuestion(true);
			question.setText("Haz presionado la alternativa " + c.toUpperCase()
					+ "!");
			setVisibleA(false);
			setVisibleB(false);
			setVisibleC(false);
			setVisibleD(false);
			setVisibleE(false);
			setVisibleF(false);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), R.string.incorrect_code,
					Toast.LENGTH_LONG).show();
		}
	}

	public void choiceA(View v) {
		choice(v, "a");
	}

	public void choiceB(View v) {
		choice(v, "b");
	}

	public void choiceC(View v) {
		choice(v, "c");
	}

	public void choiceD(View v) {
		choice(v, "d");
	}

	public void choiceE(View v) {
		choice(v, "e");
	}

	public void choiceF(View v) {
		choice(v, "f");
	}

	protected void onResume() {
		mWifiStateChangedReceiver = new WifiBroadcastReceiver(
				this);
		// Register the receiver for wifi state
		this.registerReceiver(mWifiStateChangedReceiver, new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION));
		Log.d("EVENT", "onResume");
		super.onResume();
		// Se levanta el server
		startServer();
	}

	@Override
	protected void onPause() {
		Log.d("EVENT", "onPause");
		try {
			this.unregisterReceiver(mWifiStateChangedReceiver);
		} catch (IllegalArgumentException e) {
			Log.e("ERROR", "unregisterReceiver", e);
		}
		super.onPause();
		// Es mas seguro bajar el servidor cuando se pausa la app
		stopServer();
	}

	private void setVisibleA(boolean visible) {
		if (visible == false) {
			ca.setVisibility(View.INVISIBLE);
			ba.setVisibility(View.INVISIBLE);
		} else {
			ca.setVisibility(View.VISIBLE);
			ba.setVisibility(View.VISIBLE);
		}
	}

	private void setVisibleB(boolean visible) {
		if (visible == false) {
			cb.setVisibility(View.INVISIBLE);
			bb.setVisibility(View.INVISIBLE);
		} else {
			cb.setVisibility(View.VISIBLE);
			bb.setVisibility(View.VISIBLE);
		}
	}

	private void setVisibleC(boolean visible) {
		if (visible == false) {
			cc.setVisibility(View.INVISIBLE);
			bc.setVisibility(View.INVISIBLE);
		} else {
			cc.setVisibility(View.VISIBLE);
			bc.setVisibility(View.VISIBLE);
		}
	}

	private void setVisibleD(boolean visible) {
		if (visible == false) {
			cd.setVisibility(View.INVISIBLE);
			bd.setVisibility(View.INVISIBLE);
		} else {
			cd.setVisibility(View.VISIBLE);
			bd.setVisibility(View.VISIBLE);
		}
	}

	private void setVisibleE(boolean visible) {
		if (visible == false) {
			ce.setVisibility(View.INVISIBLE);
			be.setVisibility(View.INVISIBLE);
		} else {
			ce.setVisibility(View.VISIBLE);
			be.setVisibility(View.VISIBLE);
		}
	}

	private void setVisibleF(boolean visible) {
		if (visible == false) {
			cf.setVisibility(View.INVISIBLE);
			bf.setVisibility(View.INVISIBLE);
		} else {
			cf.setVisibility(View.VISIBLE);
			bf.setVisibility(View.VISIBLE);
		}
	}

	private void setVisibleQuestion(boolean visible) {
		if (visible == false) {
			question.setVisibility(View.INVISIBLE);
		} else {
			question.setVisibility(View.VISIBLE);
		}
	}

	private void startServer() {
		Log.d("QuestionListener", "startServer()");
		if (questionListener != null && questionListener.isRunning()) {
			Log.d("Server", "Server already running");
		} else {
			RespApp app = (RespApp) getApplicationContext();
			questionListener = new ClientListenerFinalRunnable(
					app.STUDENT_PORT, thisactivity);
			new Thread(questionListener).start();
			Log.d("QuestionListener", "Server Started");
		}
	}

	private void stopServer() {
		Log.d("Server", "stopServer()");
		questionListener.stop();
	}

	public void fixQuestion(Question question) {
		Log.d("DEBUG", "Fixing question");
		lastQ = q;
		q = question;
	}

}
