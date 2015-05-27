package com.app.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import android.util.Log;

import com.app.pojo.Question;
import com.app.resp.QuestionParser;
import com.app.resp.StudentRoomActivity;

public class ClientWorkerRunnable extends MessengerThread implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;
	protected String question_input;
	
	

	ClientListenerFinalRunnable server;
	private StudentRoomActivity activity;

	public ClientWorkerRunnable(Socket clientSocket, 
			String serverText, ClientListenerFinalRunnable server,
			StudentRoomActivity act) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
		this.server = server;
		
		this.activity = act;
	}

	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			String ip = clientSocket.getInetAddress().toString();
			// This removes the initial slash in ipv4
			ip = ip.substring(1).trim();
			
			System.out.println("Estoy conectandome desde: " + ip+ " input: "+input.toString());
			try {
				//DataInputStream is = new DataInputStream(clientSocket.getInputStream());
				BufferedReader is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String responseLine;
				if ((responseLine = is.readLine()) != null) {
					System.out.println("Recibi:" + responseLine+ " Room");
					question_input=responseLine;
				}
				
				// Cerramos todo
				//clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String question =  question_input;
			addQuestion(question);
			// server.broadcastQuestion("HOLA\n");
			output.write("Ok".getBytes());
			output.close();
			input.close();
			System.out.println("Recib� conexion desde: " + ip);

		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		}
	}
	public void addQuestion(String Question){
		QuestionParser qp=new QuestionParser();
		String question_utf8 = Question;
		try {
			question_utf8 = new String(Question.getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			question_utf8 = Question;
			e.printStackTrace();
		}
		Question q=qp.getQuestionFromString(question_utf8);
		if(q!=null){
		Log.d("DEBUG", "alternativa1 "+q.getChoices()[0]+" Alternativa 2 "+q.getChoices()[1]);}
		Log.d("DEBUG", "");
		activity.fixQuestion(q);
		
		this.activity.runOnUiThread(new Runnable() {
			public void run() {
				
				// stuff that updates ui
				activity.setQuestion();
			}
		});
	
		
	}
	

}
