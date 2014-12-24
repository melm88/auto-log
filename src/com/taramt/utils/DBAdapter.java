package com.taramt.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	
	//To insert the screen state into the database
	public void inserScreenstate(String state,String time){
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
/*
 * getDiffTS is the method which will get the difference b/n 
 * current state time stamp and previous state time stamp
 *
 */
public long getDiffTS(String timeStamp, String time) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date time1 = null;
		Date timeStamp1 = null;
		long start = 0L, stop = 0L;
		try {
			time1 = sdf.parse(time);
			start = time1.getTime();
			
			timeStamp1 = sdf.parse(timeStamp);
			stop = timeStamp1.getTime();
			
			Log.d("LASTLONG", start + " | " +stop+ " | " + (start - stop)/1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (start - stop);
	}
	public ArrayList<String> getScreenStateDetails() {
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

	public long getTotal(String state, String time) {
		String timeStamp = getLastTS();
		long total = 0L;
		if (state.equals("locked")) {
			total =  prefs.getLong("t_locked", 0L);
		} else {
			total = prefs.getLong("t_unlocked", 0L);
		}
		if (!timeStamp.equals("noLastTS")) {
			// diff TS and time
			// add the diff to total
			//return total
			Log.d("LAST..", time + " | " + timeStamp + " | ");
			total = total + getDiffTS(timeStamp, time);
		}
		return total;
	}
	public String getLastTS() {
		Cursor cursor = db.query("phone_activity", 
				null, null, null, null, null, null);
		String sDetailss = "noLastTS";
		if (cursor.getCount() > 0) {
			cursor.moveToLast();
			sDetailss = cursor.getString(cursor.getColumnIndex("timeStamp"));
		}
		return sDetailss;
	}
}