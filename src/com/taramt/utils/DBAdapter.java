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
			String nDetails, String timeStamp) {
		Log.d("Notification",
				"DBA: " + packageName 
				+ " | " + nDetails + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("appName", packageName);
		cv.put("notificationDetails", nDetails);
		cv.put("timeStamp", timeStamp);
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

	public ArrayList<String> getNotificationDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("NotificationDetails", 
				null, null, null, null, null, null);
		ArrayList<String> NotificationDetails = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("appName"))
					+ "  " + cursor.getString(cursor.getColumnIndex("notificationDetails")) 
					+ "  " + cursor.getString(cursor.getColumnIndex("timeStamp"));
			NotificationDetails.add(nDetails);
		}
		return NotificationDetails;
	}

}