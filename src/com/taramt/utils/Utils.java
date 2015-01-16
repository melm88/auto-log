package com.taramt.utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

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
	 * a method for getting the details from the database
	 */
	public String getDetails(DBAdapter db, ArrayList<String> details) {
		db.open();
		String detailss = "";
		try {		
			for (int i = 0; i<details.size(); i++) {
				detailss = details.get(i) + "\n\n" + detailss ;
			}
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return detailss;
	}


	public String convert2Time(long total) {
		
		String timeStr = "";
		try {
			long totalSec = total/1000;
			//new date object with time difference

			
			if(totalSec/3600 > 0) {
				timeStr = timeStr +  totalSec/3600+"h  : ";
			}
			if ((totalSec%3600)/60 > 0) {
				timeStr = timeStr + (totalSec%3600)/60 + "m  : ";
			}

			timeStr = timeStr + (totalSec%3600)%60 + "s";
		} catch(Exception e) {
			e.printStackTrace();
		}
		return timeStr;
	}

}
