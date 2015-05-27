package com.app.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;
import android.util.Log;

import com.app.resp.StatisticsActivity;

/**

 */
public class WorkerRunnable extends MessengerThread implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;
	private ArrayList<String> ipList;
	public int[] responses;

	MultiThreadedServer server;
	private Activity activity;

	public WorkerRunnable(Socket clientSocket, ArrayList<String> ipList,
			String serverText, MultiThreadedServer server, int[] responses) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
		this.ipList = ipList;
		this.server = server;
		this.responses = responses;
		this.activity = server.getActivity();
	}

	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			String ip = clientSocket.getInetAddress().toString();
			// This removes the initial slash in ipv4
			ip = ip.substring(1).trim();

			synchronized (ipList) {
				// Adds the client IP to the global list only if is not
				// contained already
				if (!ipList.contains(ip))
					ipList.add(ip);
				// Shows all the IP's saves by this way
				Iterator<String> i = ipList.iterator();
				while (i.hasNext())
					System.out.println(i.next());
			}
			char c = (char) input.read();
			Log.i("response", "response: " + c);
			this.activity = server.getActivity();
			this.addReponse(c);
			// server.broadcastQuestion(same"HOLA\n");
			output.write("Ok".getBytes());
			output.close();
			input.close();
			System.out.println("Recibí conexion desde: " + ip);

		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		}
	}

	private void addReponse(char c) {
		switch (c) {
		case 'a':
		case 'A':
			this.responses[0]++;
			break;
		case 'b':
		case 'B':
			this.responses[1]++;
			break;
		case 'c':
		case 'C':
			this.responses[2]++;
			break;
		case 'd':
		case 'D':
			this.responses[3]++;
			break;
		case 'e':
		case 'E':
			this.responses[4]++;
			break;
		case 'f':
		case 'F':
			this.responses[5]++;
			break;

		default:
			break;
		}

		if (this.activity != null) {
			this.activity.runOnUiThread(new Runnable() {
				public void run() {
					// actualize la lista si está en cualquiera de las dos
					// actividades principales.
					try {
						StatisticsActivity act = (StatisticsActivity) activity;
						act.responses=responses;
						Log.i("WorkerRunnable",
								"showresponses");
						act.showResponses();
					} catch (Throwable t) {
						Log.i("WorkerRunnable",
								"StatisticsActivityCatchedException "
										+ t.getMessage());
					}
				}

			});
		}
		else Log.i("WorkerRunnable",
				"no me pasaste la activity pos weon");
	}

}