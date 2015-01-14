package com.taramt.autologalarm;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;


@TargetApi(Build.VERSION_CODES.L) @SuppressLint("NewApi") public class Alarmactivity extends Activity {
	AlarmManager am;
	PendingIntent pendingIntent;
	Intent intent;
	SharedPreferences prefs;
	DBAdapter db;
	Utils utils;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarmactivity);
		prefs = getPreferences(MODE_PRIVATE);
		//		if (Alarmactivity.this.getResources().getConfiguration().orientation == 1) {
		//			startAlarm();
		//		}
		db = new DBAdapter(this);
		utils = new Utils(this);
		db.open();

		ArrayList<String> rows = db.getAlarmDetails();

		db.close();

		//displaying the log from database on list view 
		ListView listView = (ListView) findViewById(R.id.list);


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, rows);

		listView.setAdapter(adapter); 


	}

	public void startAlarm() {
		try {

			intent = new Intent(getApplicationContext(), AlarmService.class);
			pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 
					1000*60*5 , pendingIntent);
			Log.i("oon Alarm", "Alarm started");
		} catch (Exception e) {
			Log.d("exceptionasdfdf",""+e);
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alarmactivity, menu);
		return true;
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
