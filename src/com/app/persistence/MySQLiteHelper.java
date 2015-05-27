package com.app.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private final static String DATABASE_NAME = "resp_db";
	private final static int DATABASE_VERSION = 1;

	/* Tables */
	private final static String TABLE_ROOM = "room";
	private final static String TABLE_QUESTION = "question";
	private final static String TABLE_ALTERNATIVE = "alternative";
	private final static String TABLE_RESPONSE = "response";
	private final static String TABLE_ROOM_QUESTION = "room_question";

	/* Fields */
	private final static String room_id = "room_id";
	private final static String question_id = "question_id";
	private final static String alternative_id = "alternative_id";

	private final static String CREATE_TABLE_ROOM = "CREATE TABLE "
			+ TABLE_ROOM + " (" + room_id
			+ " INTEGER PRIMARY KEY AUTOINCREMENT)";
	private final static String CREATE_TABLE_QUESTION = "CREATE TABLE "
			+ TABLE_QUESTION + " (" + question_id
			+ " INTEGER PRIMARY KEY AUTOINCREMENT)";
	private final static String CREATE_TABLE_ALTERNATIVE = "CREATE TABLE "
			+ TABLE_ALTERNATIVE
			+ " (alternative_id integer PRIMARY KEY AUTOINCREMENT, question_id INTEGER REFERENCES question (question_id) ON DELETE CASCADE ON UPDATE CASCADE, text TEXT, is_correct INTEGER DEFAULT 0)";
	private final static String CREATE_TABLE_ANSWER = "CREATE TABLE answer";
	private final static String CREATE_ROOM_ANSWER = "CREATE TABLE room_question";

	private final static String DATABASE_CREATE = CREATE_TABLE_ROOM + ";"
			+ CREATE_TABLE_QUESTION + ";" + CREATE_TABLE_ALTERNATIVE + ";"
			+ TABLE_ROOM_QUESTION + ";" + CREATE_TABLE_ANSWER;
	
	private final static String INSERT_DEFAULT_QUESTION = "INSERT INTO "+TABLE_QUESTION;

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		database.execSQL(INSERT_DEFAULT_QUESTION);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ROOM);
		onCreate(db);
	}

}
