package com.taramt.autologdatausage;

import java.util.ArrayList;

import com.taramt.autolog.R;
import com.taramt.autolog.R.id;
import com.taramt.autolog.R.layout;
import com.taramt.autolog.R.menu;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
public class DataUsage extends Activity {
	SharedPreferences shared;
	TextView dataUsage;
	DBAdapter db;
	Utils utils;
	static ArrayList<String> dataUsageApps = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_data_usage);
		dataUsage  = (TextView) findViewById(R.id.dataUsage);
		db = new DBAdapter(this);
		utils = new Utils(this);
		// Showing datausage from database
		showdataUsage();
		// starting the service to run every 5 minutes
		startAlarm();
		Log.d("DATAUSAGE", "in on create");
	}
	// method for starting alarm for service
	public void startAlarm() {

		try {
			Intent intentt = new Intent(getApplicationContext(), DataService.class);
			PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),0,intentt,PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
					1000*60*5, pendingIntent);
		} catch (Exception e) {
			Log.d("exceptionasdfdf",""+e);
		}

	}
	// Method to stop the service 
	public void stopService() {
		Intent intent = new Intent(this, DataService.class);
		stopService(intent);
		try {

			PendingIntent pendingIntent = PendingIntent.getService(this, 
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			AlarmManager am =(AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.cancel(pendingIntent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Showing the data usage of each app from the phone database
	 */
	public void showdataUsage() {
		Log.d("DATAUSAGE", "in showdataUsage");
		db.open();
		dataUsageApps = db.getDatausageofApps();
		dataUsage.setText(utils.getDetails(db, dataUsageApps));
		db.close();
	}

}