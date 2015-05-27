package com.app.resp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AboutActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_thisabout, menu);
        return true;
    }
    public void about(View v){
		Intent intent = new Intent(this, MainMenuActivity.class);
		
		this.startActivity(intent);
		return;}
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		
		case R.id.main_menu:
			about(null);
			return true;
		default: return super.onOptionsItemSelected(item);
		
		}
	}
}
