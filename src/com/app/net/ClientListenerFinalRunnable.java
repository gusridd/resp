package com.app.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.app.resp.StudentRoomActivity;

/**
* El listener para el cliente no es mas que un servidor que esta siempre
* esperando por conexiones entrantes.
*/
public class ClientListenerFinalRunnable implements Runnable{

    protected int          serverPort;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected StudentRoomActivity activity;
   

    public ClientListenerFinalRunnable(int port,StudentRoomActivity act){
        this.serverPort = port;
        activity=act;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
			// clientSocket es el socket en que se acepta la conexion entrante
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
				// Establecemos un timeout de 10 segs
				clientSocket.setSoTimeout(10000);
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            // leemos que es lo que viene por el socket
			
			new Thread(
	                new ClientWorkerRunnable(
	                    clientSocket, "Client Listener Server",this, this.activity)
	            ).start();

        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }
    
    public boolean isRunning() {
    	return !this.isStopped;
    }

    public synchronized void stop(){
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
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }

}
