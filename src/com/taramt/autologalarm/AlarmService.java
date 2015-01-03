package com.taramt.autologalarm;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.taramt.utils.DBAdapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

@SuppressLint("NewApi") public class AlarmService extends Service {
	SharedPreferences prefs;
	static String Tag = "NEXT_ALARM";
	DBAdapter db;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
	
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		db = new DBAdapter(getApplicationContext());
		prefs = getSharedPreferences("ALARM", MODE_PRIVATE);
		
		String nextAlarm = Settings.System.getString(getApplicationContext().
				getContentResolver(),Settings.System.NEXT_ALARM_FORMATTED);
		/* getting nextalarmclock information 
		 * through getNextAlarmClock
		 * works only for api level 21 and above
		  */
		try {
		    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		    nextAlarm = am.getNextAlarmClock().toString();
		    Log.d(Tag + "_AI", nextAlarm);
		    } catch (NoSuchMethodError e) {
		        
		    }
		
		 /*if alarm is not set then nextAlarm = "" 
		  * so not iserting this data into the database
		  */
		if (!nextAlarm.equals(prefs.getString("nextAlarm", "")) 
				&& !nextAlarm.equals("")
				&& !nextAlarm.equals(" ")) {
			db.open();
			db.insertAlarmDetails("alarmset", nextAlarm);
			Log.d(Tag, "next alarm is:  " + nextAlarm);
			Editor editor = prefs.edit();
			editor.putString("nextAlarm", nextAlarm);
			editor.commit();
			db.close();
		}
		
	}
	
	
	
}

