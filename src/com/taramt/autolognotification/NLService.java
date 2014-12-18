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
		Log.i(TAG, "**********  onNotificationPosted *********");
		Log.i(TAG, "ID :" + sbn.getId() + "\t" 
				+ sbn.getNotification().tickerText + "\t" + sbn.getPackageName());

	}
	
	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i(TAG,"********** onNOtificationRemoved   **********");
		Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
	}
}
