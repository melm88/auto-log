package com.taramt.autologscreenstate;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taramt.autolog.R;
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
	static int id = 0;
	ArrayList<String> sDetails = new ArrayList<String>();
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
	
		sDetails = db.getScreenStateDetails();
		db.close();
		// displaying general database values of Screen activity
		log.setText(utils.getDetails(db, sDetails));
		
		/* calling the method to show the total duration 
		 * of each screen activity Active and Idle
		*/
		
		showTotalDuration();
		
		/* calling the method to show the average 
		 * of each screen activity Active and Idle
		*/
		showAverage();
		
		/*
		 * startAlarm calling only when it is portrait
		 * because if orientation changes oncreate will
		 * be called and alram will be started and stopped
		 * multiple times.
		 */
		if (ScreenActivity.this.getResources().getConfiguration().orientation == 1) {
			startAlarm();
		}
		id = 0;
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
	
	/*
	 * Method for showing the sorted Active and
	 * Idle cycles in hourly manner from highest 
	 * to lowest
	 */
	public void Sort(View v) {
		
		showTotalDuration();
		showAverage();

		String Sort = "Active:\n\n";

		ArrayList<String> sort = new ArrayList<String>();
		sort = db.getSortDetails("Active");
		Sort = Sort + utils.getDetails(db, sort) + "\n";
		sort = db.getSortDetails("Idle");
		Sort = Sort + "Idle:\n\n" + utils.getDetails(db, sort) + "\n";
		log.setText(Sort);

	}
	
	/*
	 *  Method to show the top3 values of
	 *  Active Idle cycles 
	 *  By using "id" also showing general database 
	 *  version active idle cycles 
	 */
	public void Top3(View v) {

		if (id == 0) {
			// showing top3
			String Top3 = "Active:\n\n";

			ArrayList<String> top3 = new ArrayList<String>();
			top3 = db.getTop3("Active");
			Top3 = Top3 + utils.getDetails(db, top3) + "\n";
			top3 = db.getTop3("Idle");
			Top3 = Top3 + "Idle:\n\n" + utils.getDetails(db, top3) + "\n";
			log.setText(Top3);
			id = 1;
		} else {
			// showing general database by reading the table 
			db.open();
			sDetails = db.getScreenStateDetails();
			db.close();
			log.setText(utils.getDetails(db, sDetails));
			id = 0;
		}
		
	}

	/*
	 * Method for showing the average of each cycle
	 * db.getRowcount(stateName) is to get no. of records
	 * saved in database of that particular state and
	 * converting to hh:mm:ss 
	 */
	public void showAverage() {
		db.open();
		average = "Average: \nActive:  " 
				+ utils.convert2Time(prefs.getLong("t_unlocked", 0L)/db.getrowcount("Active")) 
				+ "\n";
		average = average + "Idle: "
				+ utils.convert2Time(prefs.getLong("t_locked", 0L)/db.getrowcount("Idle"));
		Average.setText(average);
		db.close();
	}
	

	/*
	 * Method for showing the total duration 
	 * of each cycle
	 * converting duration to hh:mm:ss 
	 */

	public void showTotalDuration() {
		totalDuration = prefs.getLong("t_locked", 0L)
				+ prefs.getLong("t_unlocked", 0L);
		
		total = "Total Duraion : " + utils.convert2Time(totalDuration) + "\n";
		total = total + "Active:  " 
				+ utils.convert2Time(prefs.getLong("t_unlocked", 0L)) + "\n";
		total = total + "Idle: "
				+ utils.convert2Time(prefs.getLong("t_locked", 0L));
		Total.setText(total);
		Log.d("Totalduration", prefs.getLong("t_locked", 0L) + " | " + prefs.getLong("t_locked", 0L));
	}



}
