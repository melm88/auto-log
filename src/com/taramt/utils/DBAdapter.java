package com.taramt.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBAdapter {
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	SharedPreferences prefs;
	public DBAdapter(Context context) {
		Log.d("AutoLog","Inside DBAdapter Constructor");
		DBHelper = new DatabaseHelper(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}	


	public void open() {
		db = DBHelper.getReadableDatabase();
	}
	public void close() {
		DBHelper.close();
	}

	// To log the database of notification details
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
	
	// To get the notification details show in display
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
	
	// To log DataUsage of each app
	public long insertDataUsage(String tablename, String appName, 
			String send, String received, String total, String timeStamp)

	{
		Log.d("Dbb","DBA: " + appName 
				+ " | "+ send +" | "+received
				+ " | "+ total +" | "+timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("appName", appName);
		cv.put("send", send);
		cv.put("received", received);
		cv.put("total", total);
		cv.put("timeStamp", timeStamp);
		long n=db.insert(tablename, null, cv);
		Log.d("DataUsage", ""+n);
		return n;	
	}
	
	// to show the Datausage of each app in display
	public ArrayList<String> getDatausageofApps(){
		//String query="select email_id from contacts";
		Cursor cursor=db.query("DataUsage", null,null, null, null, null, null);
		ArrayList<String> datausageofApps = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("appName"))
					+ "  " + cursor.getString(cursor.getColumnIndex("send")) 
					+ "  " + cursor.getString(cursor.getColumnIndex("received"))
					+ "  " + cursor.getString(cursor.getColumnIndex("total"))
					+ "  " + cursor.getString(cursor.getColumnIndex("timeStamp"));
			datausageofApps.add(nDetails);
		}
		return datausageofApps;
	}
	
	
	// SCREEN STATE
	
	
	public void inserRecord(String state,String time){
		long total = 0L;
		ContentValues cv = new ContentValues();
		cv.put("screenState", state);
		cv.put("timeStamp", time);
		cv.put("total", total+"");	

		db.insert("phone_activity", null, cv);
		enterPrefs(total, time, state);
	}
	public void enterPrefs(long total, String time, String state) {
		Editor edit = prefs.edit();
		Log.d("LAST", time + " | " + state + " | " + total);
		if(state.equals("locked")) {
			edit.putLong("t_locked",total);
			edit.putString("s_locked", time);
		} else {
			edit.putLong("t_unlocked",total);
			edit.putString("s_unlocked", time);
		}
		edit.commit();
	}

	public ArrayList<String> getScreenStateDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("phone_activity", 
				null, null, null, null, null, null);
		ArrayList<String>screenStateDetailss = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String sDetailss = cursor.getString(cursor.getColumnIndex("sno"))
					+ "  " + cursor.getString(cursor.getColumnIndex("screenState")) 
					+ "  " + cursor.getString(cursor.getColumnIndex("timeStamp"));
			screenStateDetailss.add(sDetailss);
		}
		return screenStateDetailss;
	}

}