package com.taramt.utils;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	public DBAdapter(Context c) {
		context = c;	
		DBHelper = new DatabaseHelper(context);
	}	
	

	public long insertDeviceState(String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("activity", activity);
		cv.put("timestamp", timeStamp);
		long n=db.insert("devicestate", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	public long insertLightSensorValue(String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("value", activity);
		cv.put("timestamp", timeStamp);
		long n=db.insert("lightsensor", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	
	public void open() {
		db = DBHelper.getReadableDatabase();
	}
	public void close()
	{
		DBHelper.close();
	}
	
	public ArrayList<String> getdevicestatelog(){
		Cursor cursor=db.query("devicestate", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("activity"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}
	public ArrayList<String> getLightSensorlog(){
		Cursor cursor=db.query("lightsensor", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("value"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}
	
	}
