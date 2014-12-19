package com.taramt.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.widget.TextView;

public class Utils {
	Context context;
	
	public Utils(Context context) {
		this.context = context;
	}
	
	/*
	 *  Retrieving the appName using packageName
	 */
	public String getAppName(String packageName) {
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = packageManager.getApplicationInfo(packageName, 0);
		} catch (final NameNotFoundException e) {}
		final String appName = (String)((applicationInfo != null)
				? packageManager.getApplicationLabel(applicationInfo) : "???");
		return appName;
	}
	
	/*
	 * a method for showing the details from the database
	 */
	public void showDetails(DBAdapter db, ArrayList<String> details, TextView tv) {
		db.open();
		
		details = db.getNotificationDetails();
		String nDetailss = "";
		for (int i = 0; i<details.size(); i++) {
			nDetailss = details.get(i) + "\n\n" + nDetailss ;
		}
		tv.setText(nDetailss);
		db.close();
	}

}
