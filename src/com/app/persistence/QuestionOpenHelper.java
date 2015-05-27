package com.app.persistence;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.pojo.Question;

public class QuestionOpenHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 1;

	final String CREATE_TABLE_QUESTION = "CREATE TABLE Question ( id INTEGER PRIMARY KEY, question TEXT DEFAULT NULL, choice_1 TEXT DEFAULT NULL,"
			+ "	choice_2 TEXT DEFAULT NULL,"
			+ "	choice_3 TEXT DEFAULT NULL,"
			+ "	choice_4 TEXT DEFAULT NULL,"
			+ "	choice_5 TEXT DEFAULT NULL,"
			+ "	choice_6 TEXT DEFAULT NULL, n1 INTEGER DEFAULT 0, n2 INTEGER DEFAULT 0, n3 INTEGER DEFAULT 0, n4 INTEGER DEFAULT 0, n5 INTEGER DEFAULT 0, n6 INTEGER DEFAULT 0);";

	private SQLiteDatabase database;

	public QuestionOpenHelper(Context context) {
		super(context, "RESP_DB", null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_TABLE_QUESTION);
		//String[] answers = { "1", "2", "3" };
		// this.insertQuestion("Que numero es mas grande?", answers);
		// this.insertQuestion("Que numero es mas pequeï¿½o?", answers);
		database.execSQL("INSERT INTO Question (question, choice_1, choice_2) VALUES ('¿Sí o No?', 'Sí', 'No');");
		//database.execSQL("INSERT INTO Question (question, choice_1, choice_2) VALUES ('Tangananica o Tanganana?', 'Tangananica', 'Tanganana');");
		database.execSQL("INSERT INTO Question (question, choice_1, choice_2) VALUES ('¿Género?', 'Masculino', 'Femenino');");
		database.execSQL("INSERT INTO Question (question, choice_1, choice_2, choice_3, choice_4) VALUES ('¿Cuanto es la mitad de 2 más 2?', '2', '3', '1', '4');");
		database.execSQL("INSERT INTO Question (question, choice_1, choice_2, choice_3) VALUES ('¿Cuál fue la época de florecimiento de la cultura Maya?', 'Entre los siglos II y XIII', 'Entre los siglos III y XV','Entre los siglos IV y XVI');");
		database.execSQL("INSERT INTO Question (question, choice_1, choice_2,choice_3,choice_4,choice_5) VALUES ('¿Cómo te sientes hoy?', 'Excelente', 'Bien','Normal','Mal','Pésimo');");
	}

	
	public void onUpdate(String question, String[] choice,String id) {
		this.open();
		String update = "UPDATE Question SET question='"+question.replaceAll("'", "''")+"',";
		for (int i = 0; i < choice.length && choice[i] != null; i++) {
			update += "choice_" + (i + 1)+"='"+choice[i].replaceAll("'", "''")+
					"'";
			if (i != choice.length - 1 && choice[i+1] != null)
				update+= ",";
		}
		
		update+="WHERE id='"+id+"';";
		Log.d("DB", update);
		database.execSQL(update);

		this.close();
		
		
	}

	public void insertQuestion(String question, String[] choice) {
		Log.d("insertQuestion","choice.lenght " + choice.length);
		Log.d("insertQuestion","question '" + question+"'");
		
		//Si no hay 2 respuestas se setean como vacias para evitar errores
		if(choice[0] == null) choice[0] = "";
		if(choice[1] == null) choice[1] = "";
		
		this.open();
		String insert = "INSERT INTO Question (question,";
		for (int i = 0; i < choice.length && choice[i] != null; i++) {
			insert += "choice_" + (i + 1);
			if (i != choice.length - 1 && choice[i+1] != null)
				insert += ",";
		}
		insert += ") VALUES ('" + question.replaceAll("'", "''") + "', '";
		for (int i = 0; i < choice.length && choice[i] != null; i++) {
			insert += choice[i].replaceAll("'", "''");
			if (i != choice.length - 1 && choice[i+1] != null)
				insert += "','";
		}
		insert+="');";
		Log.d("DB", insert);

		// Si no hay respuestas lanza un android.database.sqlite.SQLiteException
		try{
			database.execSQL(insert);
		} finally{
			this.close();
		}

		
	}
	
	public void saveChoices(String id,int[] choices){
		
		this.open();
		String update = "UPDATE Question SET ";
		for (int i = 0; i < choices.length; i++) {
			update += "n" + (i + 1)+"='"+choices[i]+
					"'";
			if (i != choices.length - 1 )
				update+= ",";
		}
		
		update+="WHERE id='"+id+"';";
		Log.d("DB", update);
		database.execSQL(update);

		this.close();
		
	}
	public int[] getAnswers(String id){
		int[] answers=new int[6];
		this.open();
		String query = "SELECT * FROM Question WHERE id = '" + id + "'";
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			answers[0]=cursor.getInt(8);
			answers[1]=cursor.getInt(9);
			answers[2]=cursor.getInt(10);
			answers[3]=cursor.getInt(11);
			answers[4]=cursor.getInt(12);
			answers[5]=cursor.getInt(13);
		}
		this.close();
		cursor.close();
		return answers;
		
		
	}
	public ArrayList<int[]> getAllAnswers(){
		ArrayList<int[]> answers = new ArrayList<int[]>();
		
		this.open();
		String query = "SELECT * FROM Question";
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			int[] ans=new int[6];
			ans[0]=cursor.getInt(8);
			ans[1]=cursor.getInt(9);
			ans[2]=cursor.getInt(10);
			ans[3]=cursor.getInt(11);
			ans[4]=cursor.getInt(12);
			ans[5]=cursor.getInt(13);
			answers.add(ans);
			cursor.moveToNext();
		}
		this.close();
		cursor.close();
		
		return answers;
		
		
	}

	public ArrayList<Question> getQuestions() {
		ArrayList<Question> questions = new ArrayList<Question>();
		this.open();
		String select_query = "SELECT * FROM Question";
		Cursor cursor = database.rawQuery(select_query, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Question q = cursorToQuestion(cursor);
			questions.add(q);
			cursor.moveToNext();
		}
		this.close();
		cursor.close();
		return questions;
	}
	
	public Question getQuestion(int id) {
		Question question = new Question();
		this.open();
		String query = "SELECT * FROM Question WHERE id = '" + id + "'";
		Cursor cursor = database.rawQuery(query, null);
		cursor.moveToFirst();
		if (!cursor.isAfterLast()) {
			question = cursorToQuestion(cursor);
		}
		this.close();
		cursor.close();
		return question;
	}
	
	public void updateQuestion(String question, String[] choices, int id) {
		String query = "UPDATE Question SET question = '" + question.replaceAll("'", "''") + "'";
		
		for (int ix = 0; ix < choices.length; ++ix) {
			if (choices[ix] != null) {
				query += ", choice_" + (ix + 1) + "='" + choices[ix].replaceAll("'", "''") + "'";
			}
			else {
				query += ", choice_" + (ix + 1) + "=NULL";
			}
		}
		
		query += " WHERE id=" + id + ";";
		Log.d("DB", "Update question query: " + query);
		this.open();
		database.execSQL(query);
		this.close();
	}
	
	public void deleteQuestion(int id) {
		String query = "DELETE FROM Question WHERE id = '" + id + "';";
		Log.d("DB", "Delete question with id: " + id);
		this.open();
		database.execSQL(query);
		this.close();	
	}

	private Question cursorToQuestion(Cursor cursor) {
		Question q = new Question();
		q.setId(cursor.getInt(0));
		q.setText(cursor.getString(1));
		q.setChoice(1, cursor.getString(2));
		q.setChoice(2, cursor.getString(3));
		q.setChoice(3, cursor.getString(4));
		q.setChoice(4, cursor.getString(5));
		q.setChoice(5, cursor.getString(6));
		q.setChoice(6, cursor.getString(7));
		return q;
	}


	public void open() throws SQLException {
		database = this.getWritableDatabase();
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	public void deleteAnswers(){
		String query = "UPDATE Question SET n1='0',n2='0',n3='0',n4='0',n5='0',n6='0' WHERE 1;";
		this.open();
		database.execSQL(query);
		this.close();
	}

}
