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
	 * a method for getting the details from the database
	 */
	public String getDetails(DBAdapter db, ArrayList<String> details) {
		db.open();
		String detailss = "";
		for (int i = 0; i<details.size(); i++) {
			detailss = details.get(i) + "\n\n" + detailss ;
		}
		db.close();
		return detailss;
	}
	


}
