package com.app.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import android.app.Activity;

public class MultiThreadedServer implements Runnable {

	protected int serverPort;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	public int[] responses;

	private ArrayList<String> ipList;
	private Activity activity;

	public MultiThreadedServer(int port, int[] responses, ArrayList<String> ipList) {

		this.serverPort = port;
		this.responses = responses;
		this.ipList = ipList;
	}

	public ArrayList<String> getIps() {
		return ipList;
	}

	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection",
						e);
			}
			new Thread(new WorkerRunnable(clientSocket, ipList,
					"Multithreaded Server", this, responses)).start();
		}
		System.out.println("Server Stopped.");
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized boolean isRunning() {
		return !this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port " + serverPort, e);
		}
	}

	public void broadcastQuestion(String question) {
		synchronized (ipList) {
			Iterator<String> it = ipList.iterator();
			while (it.hasNext()) {
				new Thread(new QuestionSenderRunnable(it.next(), serverPort,
						question)).start();
			}
		}
	}
	public void setActivity(Activity a){
		this.activity = a;
	}

	public Activity getActivity() {
		return this.activity;
	}
	
}