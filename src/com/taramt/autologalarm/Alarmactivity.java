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
import android.widget.TextView;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;


@TargetApi(Build.VERSION_CODES.L) @SuppressLint("NewApi") public class Alarmactivity extends Activity {
	AlarmManager am;
	PendingIntent pendingIntent;
	Intent intent;
	SharedPreferences prefs;
	TextView alarmDetails;
	DBAdapter db;
	ArrayList<String> aDetails = new ArrayList<String>();
	Utils utils;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarmactivity);
		alarmDetails = (TextView)findViewById(R.id.alarmDetails);
		prefs = getPreferences(MODE_PRIVATE);
		if (Alarmactivity.this.getResources().getConfiguration().orientation == 1) {
			startAlarm();
		}
		db = new DBAdapter(this);
		utils = new Utils(this);
		db.open();

		aDetails = db.getAlarmDetails();
		db.close();
		alarmDetails.setText(utils.getDetails(db, aDetails));


	}

	public void startAlarm() {
		try {

			intent = new Intent(getApplicationContext(), AlarmService.class);
			pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 
					1000*30 , pendingIntent);
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
