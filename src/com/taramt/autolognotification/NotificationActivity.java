package com.taramt.autolognotification;

import java.util.ArrayList;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;
public class NotificationActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private TextView txtView;
	private NotificationReceiver nReceiver;
	DBAdapter db;
	Utils utils;
	static ArrayList<String> nDetails = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		txtView = (TextView) findViewById(R.id.textView);
		/*
		 * defining nReceiver (NotificationReceiver) and 
		 * adding intentfileter action so that
		 * any values can be received here which will send from the 
		 * NLService through sendBroadcast method
		 */
//		nReceiver = new NotificationReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction("com.taramt.autolog.notification");
//		registerReceiver(nReceiver,filter);
		db = new DBAdapter(this);
		utils = new Utils(this);
		// Showing the notifications
		showNotifications();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	//	unregisterReceiver(nReceiver);
	}
	// getting notification details from database and displaying
	public void showNotifications() {
		db.open();
		nDetails = db.getNotificationDetails();
		txtView.setText(utils.getDetails(db, nDetails));
		db.close();
	}
	
/* this is for creating notification so that
 * we can create immediately to test the 
 * app instead of waiting for the actual 
 * notifications
 */
	public void CreateNotification(View v) {
	
		if(v.getId() == R.id.btnCreateNotify){
			NotificationManager nManager = (NotificationManager) 
					getSystemService(NOTIFICATION_SERVICE);
			NotificationCompat.Builder ncomp = 
					new NotificationCompat.Builder(this);
			ncomp.setContentTitle("My Notification");
			ncomp.setContentText("Notification Listener Service");
			ncomp.setTicker("Notification Listener Service Ticker");
			ncomp.setSmallIcon(R.drawable.ic_launcher);
			ncomp.setAutoCancel(true);
			nManager.notify((int)System.currentTimeMillis(),ncomp.build());
			
			showNotifications();
		}
	}

	public class NotificationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Notification received");	
			// Showing the notifications
			showNotifications();
		}
	}



}
