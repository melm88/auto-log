package com.taramt.autologscreenstate;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.taramt.utils.DBAdapter;

public class ScreenReceiver extends BroadcastReceiver {
	DBAdapter db;

	@Override
	public void onReceive(Context context, Intent intent) {

		try {
			db = new DBAdapter(context);
			db.open();
			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

			String date =s.format(new Date());
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

				db.inserScreenstate("Idle", new Date().toString());

				Log.d("screen","screen off");

			} else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

				db.inserScreenstate("Active", new Date().toString());

				Log.d("screen","user present");

			}
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


}


