package com.app.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MessengerThread extends Thread {

	/*protected DataInputStream is;
	protected PrintStream os;*/
	
	InputStream input;
	OutputStream output;

	private static final int HEADER_SIZE = 4;
	private static final int BUFFER_SIZE = 1024;

	protected Socket socket;

	public String getMessage() throws IOException {
		StringBuffer header_buffer = new StringBuffer(HEADER_SIZE);
		StringBuffer buffer = new StringBuffer(BUFFER_SIZE);
		int message_size;

		// We try to read the header
		for (int i = 0; i < HEADER_SIZE; i++) {
			header_buffer.append(input.read());
		}

		// We now know the message complete size
		message_size = Integer.parseInt(header_buffer.toString());

		for (int i = 0; i < message_size; i++) {
			buffer.append(input.read());
		}
		return buffer.toString();
	}

	public void sendMessage(String str) throws IOException {
		String header = String.format("%0" + HEADER_SIZE + "d", str.length());
		String msg = header + str;
		byte[] buffer = msg.getBytes();
		output.write(buffer);
	}
}
