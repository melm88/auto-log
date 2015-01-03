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
		db = new DBAdapter(getApplicationContext());
		Log.d(Tag, "Service onCreate called");
		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(Tag, "Service onStart called");
		
		
	}
	
	
	
}

