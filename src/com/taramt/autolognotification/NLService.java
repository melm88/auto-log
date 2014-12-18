package com.taramt.autolognotification;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taramt.utils.DBAdapter;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcel;
import android.os.Parcelable;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

public class NLService extends NotificationListenerService {
	
	List<String> notificationDetails = new ArrayList<String>();
	private String TAG = this.getClass().getSimpleName();
	DBAdapter db;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "in on Create");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@Override
	public void onNotificationPosted(StatusBarNotification sbn) {
		
		/*
		 * While mobile get notification this method will be invoked
		 * Send the broadcast to update the details which are shown in the activity
		 */
		Log.i(TAG, "**********  onNotificationPosted *********");
		Log.i(TAG, "ID :" + sbn.getId() + "\t" 
				+ sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
		
		sendBrodcast();
	}
	
	public void sendBrodcast() {
		Intent i = new  Intent("com.taramt.autolog.notification");
        i.putExtra("notification_event","onNotificationPosted :");
        sendBroadcast(i);
	}




	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i(TAG,"********** onNOtificationRemoved   **********");
		Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
	}
}
