package com.taramt.autologscreenstate;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.taramt.utils.DBAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
	DBAdapter db;

	@Override
	public void onReceive(Context context, Intent intent) {
		
		db = new DBAdapter(context);
		db.open();
		SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

		String date =s.format(new Date());
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			
			db.inserScreenstate("Idle", date);
			
			Log.d("screen","screen off");

		} else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
			
			db.inserScreenstate("Active", date);
			
			Log.d("screen","user present");

		}
		db.close();
	}


	}


