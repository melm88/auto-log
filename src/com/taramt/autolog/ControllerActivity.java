package com.taramt.autolog;

import com.taramt.autologdatausage.DataUsage;
import com.taramt.autolognotification.NotificationActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ControllerActivity extends Activity {
	Button Notification, Datausage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		Notification = (Button) findViewById(R.id.NotificationDetails);
		Datausage = (Button) findViewById(R.id.Datausage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.controller, menu);
		return true;
	}

	public void openActivity(View v) {
		if(v == Notification) {
			
			Intent myIntent=new Intent(this,NotificationActivity.class);
			startActivity(myIntent);
			finish();
			
		} else if (v == Datausage) {
			
			Intent myIntent=new Intent(this,DataUsage.class);
			startActivity(myIntent);
			finish();
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
