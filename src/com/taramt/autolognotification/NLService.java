package com.taramt.autolognotification;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

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

/*
 *  Only applicable for APILevel above 17
 *  After installing this app goto
 *  Setting > Security > NotificationAccess
 *  Check for Autolog
 *   
 */

public class NLService extends NotificationListenerService {

	List<String> notificationDetails = new ArrayList<String>();
	private String TAG = this.getClass().getSimpleName();
	DBAdapter db;
	Utils utils;
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
		insertDB(sbn);
	}

	public void sendBrodcast() {
		Intent i = new  Intent("com.taramt.autolog.notification");
		i.putExtra("notification_event","onNotificationPosted :");
		sendBroadcast(i);
	}

	/*
	 *  Retrieving the appName using packageName
	 */
	public String getAppName(String packageName) {
		PackageManager packageManager = getApplicationContext().getPackageManager();
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = packageManager.getApplicationInfo(packageName, 0);
		} catch (final NameNotFoundException e) {}
		final String appName = (String)((applicationInfo != null)
				? packageManager.getApplicationLabel(applicationInfo) : "???");
		return appName;
	}

	/*
	 * Inserting the Notificationdetails with TS 
	 * Uses getNotificationDetails method
	 */
	public void insertDB(StatusBarNotification sbn) {
		utils = new Utils(getApplicationContext());
		// getting appname 
		String appName = utils.getAppName(sbn.getPackageName());
		// timeStamp
		Date date = new Date(sbn.getPostTime());
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm");
		String timeStamp = formatter.format(date);

		Notification notification = sbn.getNotification();
		// getting notificationDetails
		notificationDetails = getNotificationDetails(notification);
		String nDetails = "";
		if (nDetails!=null) nDetails = notificationDetails.toString();
		db = new DBAdapter(getBaseContext());
		db.open();
		db.insertNotificationDetails(appName , nDetails, date.toString());
		db.close();
	}

	public static List<String> getNotificationDetails(Notification notification) {
		// extracting the information from the view
		RemoteViews views = notification.bigContentView;
		if (views == null) views = notification.contentView;
		if (views == null) return null;

		List<String> text = new ArrayList<String>();
		try {
			Field field = views.getClass().getDeclaredField("mActions");
			field.setAccessible(true);

			@SuppressWarnings("unchecked")
			ArrayList<Parcelable> actions = (ArrayList<Parcelable>) field.get(views);

			// Find the setText() actions
			for (Parcelable p : actions) {
				Parcel parcel = Parcel.obtain();
				p.writeToParcel(parcel, 0);
				parcel.setDataPosition(0);

				int tag = parcel.readInt();
				if (tag != 2) continue;
				parcel.readInt();

				String methodName = parcel.readString();
				if (methodName == null) {
					continue;
				} else if (methodName.equals("setText")) {
					parcel.readInt();
					// Store the actual string
					String t = TextUtils.CHAR_SEQUENCE_CREATOR.
							createFromParcel(parcel).toString().trim();
					text.add(t);
				}
				parcel.recycle();
			}
		} catch (Exception e) {
			Log.d("NotificationActivity", e.toString());
		}

		return text;
	}

	@Override
	public void onNotificationRemoved(StatusBarNotification sbn) {
		Log.i(TAG,"********** onNOtificationRemoved   **********");
		Log.i(TAG,"ID :" + sbn.getId() + "\t" 
				+ sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
	}
}
