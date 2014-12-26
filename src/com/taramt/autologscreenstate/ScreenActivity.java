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
		utils = new Utils(this);

		db = new DBAdapter(this);
		db.open();
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

		if (ScreenActivity.this.getResources().getConfiguration().orientation == 1) {
			startAlarm();
		}

	}


	public void startAlarm() {
		try {

			intent = new Intent(getApplicationContext(), ScreenService.class);
			pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
					1000*30, pendingIntent);
			Log.i("oon Alarm", "Alarm started");
		} catch (Exception e) {
			Log.d("exceptionasdfdf",""+e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (ScreenActivity.this.getResources().getConfiguration().orientation == 1) {
			startAlarm();
		}
		Log.d("start", "onDestroy");
	}
	@Override
public void onPause() {
	super.onPause();
	Log.d("start", "onPause");
}
	public void Sort(View v) {


	}
	public void Top3(View v) {


		String Top3 = "Active:\n\n";

		ArrayList<String> top3 = new ArrayList<String>();
		top3 = db.getTop3("Active");
		Top3 = Top3 + utils.getDetails(db, top3) + "\n";
		top3 = db.getTop3("Idle");
		Top3 = Top3 + "Idle:\n\n" + utils.getDetails(db, top3) + "\n";
		log.setText(Top3);
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
