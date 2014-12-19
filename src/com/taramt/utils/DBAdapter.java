package com.taramt.utils;

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
	
	//Constructor
	public DBAdapter(Context c)
	{
		Log.d("AutoLog","Inside DBAdapter Constructor");
		context=c;	
		DBHelper=new DatabaseHelper(context);
	}
	
	//Open database connection
	public void open()
	{
		db = DBHelper.getWritableDatabase();

	}

	//close database connection
	public void close()
	{
		DBHelper.close();
	}
	
	//Insert data into ChargerState table (Power_Connected / Power_Disconnected)
	public long insertPowerDetails(String connection, 
			String chargingstate, String chargingport, String batterylevel, String time) {
		Log.d("ChargerState",
				"DBA: " + connection 
				+ " | " + chargingstate + " | "+ chargingport +" | " + batterylevel 
				+ " | " + time);
		ContentValues cv = new ContentValues();
		cv.put("connection", connection);
		cv.put("chargingstate", chargingstate);
		cv.put("chargingpoint", chargingport);
		cv.put("battery", batterylevel);
		cv.put("timeStamp", time);
		long n=db.insert("ChargerState", null, cv);
		Log.d("ChargerState", "" + n);
		return n;	
	}
	
	//Retrieve ChargerDetails
	public ArrayList<String> getPowerDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("ChargerState", 
				null, null, null, null, null, null);
		ArrayList<String> PowerDetails = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("connection"))
					+ "  " + cursor.getString(cursor.getColumnIndex("chargingstate"))
					+ "  " + cursor.getString(cursor.getColumnIndex("chargingpoint"))
					+ "  " + cursor.getString(cursor.getColumnIndex("battery"))
					+ "  " + cursor.getString(cursor.getColumnIndex("timeStamp"));
			PowerDetails.add(nDetails);
		}
		return PowerDetails;
	}
	
	//Insert Temperature data into table
	public long insertTemperatureDetails(String temperature, String time) {
		Log.d("Temperature",
				"DBA: " + temperature + " | " + time);
		ContentValues cv = new ContentValues();
		cv.put("temperature", temperature);
		cv.put("timeStamp", time);
		long n=db.insert("AmbientTemperature", null, cv);
		Log.d("Temperature", "" + n);
		return n;	
	}

}