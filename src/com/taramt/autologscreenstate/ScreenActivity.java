package com.taramt.autologscreenstate;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.taramt.autolog.R;
import com.taramt.autologdatausage.DataService;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

public class ScreenActivity extends Activity {

	DBAdapter db;
	TextView log, Average, Total;
	BroadcastReceiver mReceiver;
	String total, average;
	long totalDuration;
	SharedPreferences prefs;
	Utils utils;
	AlarmManager am;
	PendingIntent pendingIntent;
	String alarm = "ALARM";
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		registerReceiver();

		db = new DBAdapter(this);
		db.open();
		utils = new Utils(this);
		log = (TextView)findViewById(R.id.log);
		Average = (TextView)findViewById(R.id.Average);
		Total = (TextView)findViewById(R.id.Total_duration);
		ArrayList<String> sDetails = new ArrayList<String>();
		sDetails = db.getScreenStateDetails();
		db.close();
		log.setText(utils.getDetails(db, sDetails));
		totalDuration = prefs.getLong("t_locked", 0L)
				+ prefs.getLong("t_unlocked", 0L);
		showTotalDuration();
		showAverage();

	}

	public void registerReceiver() {
		Log.d("register", prefs.getBoolean("register", false)+"");
		if (prefs.getBoolean("register", true)) {

			IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_USER_PRESENT);
			mReceiver = new ScreenReceiver();
			try {
				registerReceiver(mReceiver, filter);
				Log.d("oonCreate", "receiver registered");
			} catch (IllegalArgumentException e) {

			}
			Editor edit = prefs.edit();
			edit.putBoolean("register", false);
			edit.commit();
		}
	}

	public void startAlarm() {
		Log.d(alarm, prefs.getBoolean(alarm, false)+"");
		if (!prefs.getBoolean(alarm, false)) {
			try {
				intent = new Intent(getApplicationContext(), ScreenService.class);
				pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
				am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
						1000*30, pendingIntent);
				Log.d("oon Alarm", "Alarm started");

				Editor edit = prefs.edit();
				// yes alarm started
				edit.putBoolean(alarm, true);
				edit.commit();
			} catch (Exception e) {
				Log.d("exceptionasdfdf",""+e);
			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "receiver unregistered", Toast.LENGTH_LONG).show();
		if (!prefs.getBoolean("register", true)) {
			try {
				unregisterReceiver(mReceiver);
				Log.d("oonDestroy", "receiver unregistered");
			} catch (IllegalArgumentException e) {

			}
			Editor edit = prefs.edit();
			edit.putBoolean("register", true);
			edit.commit();
		} 
		
		
		startAlarm();
	}




	public void Sort(View v) {

		Log.i("Clicked", "Clicked");

	}
	public void Top3(View v) {
		Log.i("Clicked", "Clicked");
	}


	public void showAverage() {
		db.open();
		average = "Average: \nActive:  " 
				+ utils.convert2Time(prefs.getLong("t_unlocked", 0L)/db.getrowcount()) 
				+ "\n";
		average = average + "Idle: "
				+ utils.convert2Time(prefs.getLong("t_locked", 0L)/db.getrowcount());
		Average.setText(average);
		db.close();
	}

	public void showTotalDuration() {
		total = "Total Duraion : " + utils.convert2Time(totalDuration) + "\n";
		total = total + "Active:  " 
				+ utils.convert2Time(prefs.getLong("t_unlocked", 0L)) + "\n";
		total = total + "Idle: "
				+ utils.convert2Time(prefs.getLong("t_locked", 0L));
		Total.setText(total);
	}

}
