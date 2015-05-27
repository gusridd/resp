package com.app.resp;

import com.app.pojo.Question;

public class QuestionParser {

	public static final String SEPARATOR = "<_BENJAMIN_>";
	public static final String INIT = "<_INIT_>";
	public static final String END = "<_END_>";

	public Question getQuestionFromString(String str) {
		Question q = new Question();
		if(str==null){
			return null;
		}
		if(str.indexOf(INIT)==-1){
			return null;
		}
		int init = str.indexOf(INIT) + INIT.length();
		int end = str.indexOf(END);
		int from = 0;
		int total_separators = 0;
		int pos;
		while ((pos = str.indexOf(SEPARATOR, from)) != -1) {
			total_separators++;
			from = pos+1;
		}
		// Se obtiene la pregunta
		String text = str.substring(init,str.indexOf(SEPARATOR));
		q.setText(text);
		System.out.println(total_separators);
		from = 0;
		for(int i = 0; i<total_separators; i++){
			int j = str.indexOf(SEPARATOR, from) + SEPARATOR.length();
			int f = str.indexOf(SEPARATOR,j);
			if(f == -1){
				f = end;
			}
			String choice = str.substring(j, f);
			q.setChoice(i+1, choice);
			from=f;
		}

		return q;
	}

	public String getStringFromQuestion(Question q) {
		StringBuffer buffer = new StringBuffer();
		String choices[] = q.getChoices();

		buffer.append(INIT);
		buffer.append(q.getText());
		for (int i = 0; i < choices.length; i++) {
			if (choices[i] == null)
				break;
			buffer.append(SEPARATOR);
			buffer.append(choices[i]);
		}
		buffer.append(END);

		return buffer.toString();
	}
}
