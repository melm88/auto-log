package com.taramt.utils;

import java.sql.SQLDataException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.preference.PreferenceManager;
import android.util.Log;

public class DBAdapter {
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	static String TAG = "AALARM";
	public DBAdapter(Context context) {
		Log.d("AutoLog","Inside DBAdapter Constructor");
		DBHelper = new DatabaseHelper(context);
		
	}	


	public void open() {
		db = DBHelper.getReadableDatabase();
	}
	public void close() {
		DBHelper.close();
	}

	// To log the database of Alarm details
	public long insertAlarmDetails(String alarm, 
			String timeStamp) {
		try {
			Log.d(TAG,
					"DBA: " + alarm 
					+  timeStamp);
			ContentValues cv = new ContentValues();
			cv.put("alarm", alarm);
			cv.put("timeStamp", timeStamp);
			long n = db.insertOrThrow("AlarmDetails", null, cv);
			Log.d("AlarmInserted", "" + n);
			return n;	
		} catch (android.database.sqlite.SQLiteConstraintException s) {

		} 
		return 0;
	}

	// To get the Alarm details show in display
	public ArrayList<String> getAlarmDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("AlarmDetails", 
				null, null, null, null, null, null);
		ArrayList<String> AlarmDetails = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String aDetails = cursor.getString(cursor.getColumnIndex("alarm"))
					+ "  " + cursor.getString(cursor.getColumnIndex("timeStamp"));
			AlarmDetails.add(aDetails);
		}
		return AlarmDetails;
	}

}