package com.taramt.autolognotification;

import java.util.ArrayList;
import android.support.v4.app.NotificationCompat;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;
public class NotificationActivity extends Activity {

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
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(nReceiver);
	}

	class NotificationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			
		}
	}



}
