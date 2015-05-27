package com.app.net;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import android.os.Handler;
import android.util.Log;

import com.app.pojo.Status;

/**

 */
public class QuestionSenderRunnable implements Runnable {
	String toSend;
	String host;
	int port;
	Handler handler;
	Status status;
	static private final int timeout = 3000;
	Runnable run;

	public QuestionSenderRunnable(String h, int p, String s) {
		host = h;
		port = p;
		toSend = s;
	}

	public QuestionSenderRunnable(String h, int p, String s, Status stat,
			Handler handler, Runnable r) {
		host = h;
		port = p;
		toSend = s;
		status = stat;
		this.handler = handler;
		run = r;
	}

	public void run() {
		Socket clientSocket = null;
		DataInputStream is = null;
		PrintStream os = null;
		Log.d("Sender", "Sending '" + toSend + "' to: " + host + ":" + port);
		// Intento abrir conexion en un lugar indicado
		try {
			// SocketAddress sockaddr = new InetSocketAddress(host, port);
			clientSocket = new Socket(host, port);
			// clientSocket = new Socket();
			// clientSocket.connect(sockaddr, timeout);
			boolean autoFlush = true;
			String encoding = "UTF-8";
			//os = new PrintStream(clientSocket.getOutputStream());
			os = new PrintStream(clientSocket.getOutputStream(), autoFlush, encoding);
			is = new DataInputStream(clientSocket.getInputStream());
			//is = new DataInputStream(clientSocket.getInputStream(), autoFlush, encoding);
			
		} catch (ConnectException e) {
			if (status != null) {
				status.setStatus(6);
				handler.post(run);
			}
			
		} catch (UnknownHostException e) {
			Log.e("QuestionSender", "Don't know about host \"" + host + ":"
					+ port, e);
			Log.i("QuestionSender", "Interrupted");
			if (status != null) {
				status.setStatus(1);
				handler.post(run);
			}
		} catch (SocketTimeoutException e) {
			Log.e("QuestionSender", "SocketTimeout of " + timeout, e);
			Log.i("QuestionSender", "Interrupted");
			if (status != null) {
				status.setStatus(2);
				handler.post(run);
			}
		} catch (IOException e) {
			Log.e("QuestionSender",
					"Couldn't get I/O for the connection to host \"" + host
							+ ":" + port + "\"", e);
			Log.i("QuestionSender", "Interrupted");
			if (status != null) {
				status.setStatus(3);
				handler.post(run);
			}
		}

		// Si se pudo conectar, mando la pregunta
		if (clientSocket != null && os != null && is != null) {
			try {
				os.println(toSend);
				os.close();
				is.close();
				clientSocket.close();
				if (status != null) {
					status.setStatus(0);
					handler.post(run);
				}
			} catch (UnknownHostException e) {
				System.err.println("Trying to connect to unknown host: " + e);
				if (status != null) {
					status.setStatus(4);
					handler.post(run);
				}
			} catch (IOException e) {
				System.err.println("IOException:  " + e);
				if (status != null) {
					status.setStatus(5);
					handler.post(run);
				}
			}
		}
	}
}