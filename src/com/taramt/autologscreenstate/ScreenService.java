package com.taramt.autologscreenstate;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenService extends Service {

	SharedPreferences prefs;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC


		Log.d("oonService", "on Create called");

	}
	@Override
	public void onStart(Intent intent, int startId) {
		prefs=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		if (prefs.getBoolean("register", true)) {
			IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
			filter.addAction(Intent.ACTION_USER_PRESENT);
			BroadcastReceiver mReceiver = new ScreenReceiver();
			registerReceiver(mReceiver, filter);
			Log.d("oonService", "receiver registered");
			Editor edit = prefs.edit();
			edit.putBoolean("register", false);
			edit.commit();
		}
		Log.d("oonService", "receiver not registered");
	}

}
