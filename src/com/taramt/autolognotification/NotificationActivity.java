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
public class NotificationActivity extends Activity {

	private String TAG = this.getClass().getSimpleName();
	private TextView txtView;
	private NotificationReceiver nReceiver;
	DBAdapter db;
	static ArrayList<String> nDetails = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
		txtView = (TextView) findViewById(R.id.textView);
		nReceiver = new NotificationReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.taramt.autolog.notification");
		registerReceiver(nReceiver,filter);
		db = new DBAdapter(this);
		// Showing the notifications
		showNotifications();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(nReceiver);
	}
	
	public void showNotifications() {
		db.open();
		nDetails = db.getNotificationDetails();
		String nDetailss = "";
		for (int i = 0; i<nDetails.size(); i++) {
			nDetailss = nDetails.get(i) + "\n\n" + nDetailss ;
		}
		txtView.setText(nDetailss);
		db.close();
	}
	

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

	class NotificationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.d(TAG, "Notification received");	
			// Showing the notifications
			showNotifications();
		}
	}



}
