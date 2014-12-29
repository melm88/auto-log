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
	static int ihour = 0;
	static int ahour = 0;
	static long itotal = 0L;
	static long atotal = 0L;
	Utils utils;
	public DBAdapter(Context context) {
		Log.d("AutoLog","Inside DBAdapter Constructor");
		DBHelper = new DatabaseHelper(context);
		utils = new Utils(context);
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
		getTotal(state, time);
		ContentValues cv = new ContentValues();
		cv.put("screenState", state);
		cv.put("timeStamp", time);
		cv.put("total", 0L);
		cv.put("cumulatetotal", 0L+"");	

		db.insert("phone_activity", null, cv);

	}

	public void enterPrefs(long total, String time, String state) {
		String lastTimeStamp = getLastrow().split(",")[1];
		lastTimeStamp = lastTimeStamp.split(" ")[0];
		Editor edit = prefs.edit();
		Log.d("LAST", time + " | " + state + " | " + total);
		if(state.equals("Active")) {
			if (lastTimeStamp.equals(time.split(" ")[0])) {
				edit.putLong("t_unlocked",total);
				Log.d("timeStamp T + LAstT same", time.split(" ")[0] + " | " + lastTimeStamp);
			} else {
				Log.d("timeStamp T + LAstT", time.split(" ")[0] + " | " + lastTimeStamp);
				edit.putLong("t_unlocked",0L);
			}
			
			edit.putString("s_unlocked", time);
		} else {
			if (lastTimeStamp.equals(time.split(" ")[0])) {
				edit.putLong("t_locked",total);
				Log.d("timeStamp T + LAstT same", time.split(" ")[0] + " | " + lastTimeStamp);
			} else {
				Log.d("timeStamp T + LAstT", time.split(" ")[0] + " | " + lastTimeStamp);
				edit.putLong("t_locked", 0L);
			}
			edit.putString("s_locked", time);
		}
		edit.commit();
	}
	public long updateTotal(String timeStamp, long total, String cumulateTotal) {

		open();
		ContentValues cv=new ContentValues();
		cv.put("cumulatetotal", cumulateTotal);
		cv.put("total", total);
		try {
			long n = db.update("phone_activity", 
					cv, "timeStamp=?", new String[] {timeStamp});
			Log.d("UPDATE", ""+n);
			return n;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in updating folders",e.toString());
		}
		close();
		return 0;
	}

	public ArrayList<String> getScreenStateDetails() {
		Cursor cursor = db.query("phone_activity", 
				null, null, null, null, null, null);
		ArrayList<String>screenStateDetailss = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String sDetailss = cursor.getString(cursor.getColumnIndex("sno"))
					+ "  " + cursor.getString(cursor.getColumnIndex("screenState")) 
					+ "  " + cursor.getString(cursor.getColumnIndex("timeStamp"))
					+ "  " + utils.convert2Time(cursor.getLong(cursor.getColumnIndex("total")));
			screenStateDetailss.add(sDetailss);
		}
		return screenStateDetailss;
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

	public long getTotal(String state, String time) {
		String rowDetails = getLastrow();
		String timeStamp = rowDetails.split(",")[1];
		String lastState = rowDetails.split(",")[0];
		long cumulateTotal = 0L;
		long total = 0L;

		total = getDiffTS(timeStamp, time);

		if (lastState.equals("Active")) {
			cumulateTotal =  prefs.getLong("t_unlocked", 0L);
			
			

			if (!timeStamp.equals("noLastTS") 
					&& ahour != Integer.parseInt(timeStamp.substring(11,13))) {
				ahour = Integer.parseInt(timeStamp.substring(11,13));
				insertSort(lastState, timeStamp, total, ahour+"");
				atotal = 0;
			} else {
				atotal = atotal + total;
				updateSort(lastState, timeStamp, atotal, ahour+"");
			}
			
		} else {
			cumulateTotal = prefs.getLong("t_locked", 0L);


			if (!timeStamp.equals("noLastTS") 
					&& ihour != Integer.parseInt(timeStamp.substring(11,13))) {
				ihour = Integer.parseInt(timeStamp.substring(11,13));
				insertSort("Idle", timeStamp, total, ihour+"");
				itotal = 0;
			} else {
				itotal = itotal + total;
				updateSort("Idle", timeStamp, itotal, ihour+"");
			}


		}
		if (!timeStamp.equals("noLastTS")) {
			// diff TS and time
			// add the diff to total
			//return total
			Log.d("LAST..", time + " | " + timeStamp + " | ");
			total = getDiffTS(timeStamp, time);
			cumulateTotal = cumulateTotal + total ;
		}
		enterPrefs(cumulateTotal, timeStamp, lastState);
		updateTotal(timeStamp, total, cumulateTotal+"");
		return cumulateTotal;
	}
	public String getLastrow() {
		Cursor cursor = db.query("phone_activity", 
				null, null, null, null, null, null);
		String sDetailss = "noLastTS";
		if (cursor.getCount() > 0) {
			cursor.moveToLast();
			sDetailss = cursor.getString(cursor.getColumnIndex("screenState"))
					+ "," + cursor.getString(cursor.getColumnIndex("timeStamp"));
		}
		return sDetailss;
	}




	public int getrowcount(String state) {

		Cursor cursor = db.query("phone_activity", 
				null, "screenState=?", new String[] {state}, null, null, "total");
		int count = cursor.getCount();
		if (count > 0) {
			return count;
		}
		return 1;
	}

	public ArrayList<String> getTop3(String state) {
		open();
		Cursor cursor = db.query("phone_activity", 
				null, "screenState=?", new String[] {state}, null, null, "total");
		ArrayList<String> top3 = new ArrayList<String>();
		cursor.moveToPosition(cursor.getCount() - 4);
		while(cursor.moveToNext()) {

			String sDetailss = cursor.getString(cursor.getColumnIndex("timeStamp"))
					+ "  " + utils.convert2Time(cursor.getLong(cursor.getColumnIndex("total")));
			top3.add(sDetailss);


		}
		close();
		return top3;
	}

	// SORT


	public void insertSort(String state, String time, long total, String hour) {

		ContentValues cv = new ContentValues();
		cv.put("screenState", state);
		cv.put("timeStamp", time);
		cv.put("total", total);
		cv.put("hour", hour);	
		Log.d("DBBInsert", state + " | " + time  + " | " + total  + " | " + hour);
		db.insert("sort", null, cv);
	}
	public long updateSort(String state, String timeStamp, long total, String hour) {

		open();
		ContentValues cv=new ContentValues();
		cv.put("timeStamp", timeStamp);
		cv.put("total", total);
		try {
			long n = db.update("sort", 
					cv, "screenState=? and hour=?", new String[] {state, hour});
			Log.d("UPDATESORT", ""+n);
			return n;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in updating folders",e.toString());
		}
		close();
		return 0;
	}
	public ArrayList<String> getSortDetails(String state) {
		open();
		Cursor cursor = db.query("sort", 
				null, "screenState=?", new String[] {state}, null, null, "total");
		ArrayList<String> screenStateDetailss = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String sDetailss = cursor.getString(cursor.getColumnIndex("timeStamp"))
					+ "  " + utils.convert2Time(cursor.getLong(cursor.getColumnIndex("total")))
					+ "  " + cursor.getString(cursor.getColumnIndex("hour"));
			screenStateDetailss.add(sDetailss);
		}
		close();
		return screenStateDetailss;
	}
}