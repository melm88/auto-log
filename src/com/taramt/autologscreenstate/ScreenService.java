package com.taramt.autologscreenstate;

import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.taramt.utils.Utils;

public class ScreenService extends Service {


	String alarm = "ALARM";
	Utils utils;
	static BroadcastReceiver mReceiver = new ScreenReceiver();
	static IntentFilter filter;
	static Intent intent;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC

		utils = new Utils(getApplicationContext());
		Log.i("oonService", "on Create called");

	}
	@Override
	public void onStart(Intent intent, int startId) {
		try {
			filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_USER_PRESENT);
			try {
				unregisterReceiver(mReceiver);
				Log.i("register oonService", "receiver unregistered");
			} catch (IllegalArgumentException e) {
				Utils.appendLog(e);
				
			}
			registerReceiver(mReceiver, filter);
			Log.i("register oonService", "receiver registered");
			//	Log.i("register oonService", "receiver not registered");
			stopAlarm();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}

	public void stopAlarm() {

		Intent intent = new Intent(getApplicationContext(), ScreenService.class);
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		am.cancel(pendingIntent);
		Log.i("oonServivce", "Alarm canceled");
	}


}
