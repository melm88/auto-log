package com.taramt.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter {

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	public DBAdapter(Context c) {
		Log.d("AutoLog","Inside DBAdapter Constructor");
		context = c;	
		DBHelper = new DatabaseHelper(context);
	}	


	public long insertNotificationDetails(String packageName, 
			String date, String time) {
		Log.d("Notification",
				"DBA: " + packageName 
				+ " | " + date + " | " + time);
		ContentValues cv = new ContentValues();
		cv.put("appName", packageName);
		cv.put("notificationDetails", date);
		cv.put("timeStamp", time);
		long n=db.insert("NotificationDetails", null, cv);
		Log.d("NotificationDetails", "" + n);
		return n;	
	}

	public void open() {
		db = DBHelper.getReadableDatabase();
	}
	public void close() {
		DBHelper.close();
	}

	
}