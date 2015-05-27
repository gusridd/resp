package com.app.resp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class TutorialActivity extends RespActivity {
	//numero de página
	int number;
	ImageView image;
	TextView texto;
	TextView titulo;
	ScrollView scroll;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        number=1;
        image=(ImageView)findViewById(R.id.tutorial_imagen);
        texto=(TextView)findViewById(R.id.tutorial_texto);
        titulo=(TextView)findViewById(R.id.tutorial_titulo);
        scroll=(ScrollView)findViewById(R.id.tutorial_scrollview);
        
    }

    public void adelante(View v){
   
        
    	switch(number){
    	case 1:
    	image.setImageResource(R.drawable.tutorial2);
    	texto.setText(R.string.tutorial2);
    	titulo.setText("TUTORIAL 2/11");
    	break;
    	case 2:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial3);
        	texto.setText(R.string.tutorial3);
        	titulo.setText("TUTORIAL 3/11");
        	break;
    	case 3:
        	image.setImageResource(R.drawable.tutorial4);
        	texto.setText(R.string.tutorial4);
        	titulo.setText("TUTORIAL 4/11");
        	break;
    	case 4:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial5);
        	texto.setText(R.string.tutorial5);
        	titulo.setText("TUTORIAL 5/11");
        	break;
    	case 5:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial6);
        	texto.setText(R.string.tutorial6);
        	titulo.setText("TUTORIAL 6/11");
        	break;
    	case 6:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial7);
        	texto.setText(R.string.tutorial7);
        	titulo.setText("TUTORIAL 7/11");
        	break;
    	case 7:
        	image.setImageResource(R.drawable.tutorial8);
        	texto.setText(R.string.tutorial8);
        	titulo.setText("TUTORIAL 8/11");
        	break;
    	case 8:
        	image.setImageResource(R.drawable.tutorial9);
        	texto.setText(R.string.tutorial9);
        	titulo.setText("TUTORIAL 9/11");
        	break;
    	case 9:
        	image.setImageResource(R.drawable.tutorial10);
        	texto.setText(R.string.tutorial10);
        	titulo.setText("TUTORIAL 10/11");
        	break;
    	case 10:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial11);
        	texto.setText(R.string.tutorial11);
        	titulo.setText("TUTORIAL 11/11");
        	break;
    	case 11:
    		Toast.makeText(this, "Haz completado el tutorial", Toast.LENGTH_SHORT).show()
			;
    		this.finish();
    	}
    	scroll.pageScroll(View.FOCUS_UP);
    	number++;
    }
    public void atras(View v){
    	switch(number){
    	case 1:
    		this.finish();
    	break;
    	case 2:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial1);
        	texto.setText(R.string.tutorial1);
        	titulo.setText("TUTORIAL 1/11");
        	break;
    	case 3:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial2);
        	texto.setText(R.string.tutorial2);
        	titulo.setText("TUTORIAL 2/11");
        	break;
    	case 4:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial3);
        	texto.setText(R.string.tutorial3);
        	titulo.setText("TUTORIAL 3/11");
        	break;
    	case 5:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial4);
        	texto.setText(R.string.tutorial4);
        	titulo.setText("TUTORIAL 4/11");
        	break;
    	case 6:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial5);
        	texto.setText(R.string.tutorial5);
        	titulo.setText("TUTORIAL 5/11");
        	break;
    	case 7:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial6);
        	texto.setText(R.string.tutorial6);
        	titulo.setText("TUTORIAL 6/11");
        	break;
    	case 8:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial7);
        	texto.setText(R.string.tutorial7);
        	titulo.setText("TUTORIAL 7/11");
        	break;
    	case 9:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial8);
        	texto.setText(R.string.tutorial8);
        	titulo.setText("TUTORIAL 8/11");
        	break;
    	case 10:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial9);
        	texto.setText(R.string.tutorial9);
        	titulo.setText("TUTORIAL 9/11");
        	break;
    	case 11:
    		scroll.pageScroll(View.FOCUS_UP);
        	image.setImageResource(R.drawable.tutorial10);
        	texto.setText(R.string.tutorial10);
        	titulo.setText("TUTORIAL 10/11");
        	break;
    	}
    	scroll.pageScroll(View.FOCUS_UP);
    	
    	number--;
    }
}
