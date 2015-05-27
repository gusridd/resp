package com.app.resp;

import java.util.ArrayList;

import android.app.Application;
import android.util.Log;

import com.app.net.MultiThreadedServer;

public class RespApp extends Application {

	MultiThreadedServer teacherServer;
	ArrayList<String> ipList = new ArrayList<String>();
	public final int TEACHER_PORT = 8080;
	public final int STUDENT_PORT = 9000;
	public int[] responses;

	@Override
	public void onCreate() {
		ipList = new ArrayList<String>();
		responses = new int[6];
		teacherServer = new MultiThreadedServer(TEACHER_PORT, responses, ipList);
		(new Thread(teacherServer)).start();
		Log.d("TeacherServer","ServerStarted");
		super.onCreate();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public ArrayList<String> getIpList() {
		return this.ipList;
	}

	public MultiThreadedServer getTeacherServer() {
		return this.teacherServer;
	}
	public void setResponses(int[] responses){
	teacherServer.responses=responses;}

}
