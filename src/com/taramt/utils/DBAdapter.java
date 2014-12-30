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
	
    //saves the boot,restart events
	public long insertDeviceState(String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("activity", activity);
		cv.put("timestamp", timeStamp);
		long n=db.insert("devicestate", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	//saves the light sensor value
	public long insertLightSensorValue(String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("value", activity);
		cv.put("timestamp", timeStamp);
		long n=db.insert("lightsensor", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	//saves wifi, mobile events
	public long insertWifiandData(String network, String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("network", network);
		cv.put("activity", activity);
		cv.put("timestamp", timeStamp);
		
		long n=db.insert("wifianddata", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	//Audio Level Value inserted
	public long insertAudioLevelValue(String value, String timeStamp) {
		Log.d("Dbb","DBA: " + value + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("value", value);
		cv.put("timestamp", timeStamp);
		long n=db.insert("audiolevel", null, cv);
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
	//Generated get device state log from database
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
	//Generated get Light Sensor log from database
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
	//Generated get wifi and data log from database
	public ArrayList<String> getwifianddatalog(){
		Cursor cursor=db.query("wifianddata", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String nDetails = 
					cursor.getString(cursor.getColumnIndex("network"))
					+ "\n" +cursor.getString(cursor.getColumnIndex("activity"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}
	//
	public ArrayList<String> getaudiolog(){
		Cursor cursor=db.query("audiolevel", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("value"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}
	}
