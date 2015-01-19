package com.taramt.utils;

import java.util.Date;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	
	public DatabaseHelper(Context context) 
	{
		super(context,"AutoLog" , null, 1);
		Log.d("AutoLog","Database created....");

	}

		
	//Create tables

	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table if not exists devicestate(activity text not null, timestamp text not null, sync integer default 0);");
		Log.d("devicestate","Table Created!!!");
		
		db.execSQL("create table if not exists lightsensor(value text not null, timestamp text not null, sync integer default 0);");
		Log.d("lightsensor","Table Created!!!");
		
		db.execSQL("create table if not exists wifianddata(network text not null, activity text not null, timestamp text not null, sync integer default 0);");
		Log.d("lightsensor","Table Created!!!");

		//db.execSQL("create table if not exists email(mesgid INTEGER not null, subject text not null, email text not null, sender text not null, datetime text not null, receiver text not null, folder text not null, numofattachments integer not null, recipients text,  flags text not null, displayname text, attachmentName text, PRIMARY KEY(mesgid, email, folder));");
		db.execSQL("create table if not exists LOCATION(lid INTEGER not null, timestamp text not null, lat text not null, lon text not null, accuracy text not null, address text not null, ltype text not null, sync integer default 0, PRIMARY KEY(lid));");
		db.execSQL("create table if not exists ACTIVITIES(aid INTEGER not null,timestamp text not null, activity text not null, confidence text not null, sync integer default 0, PRIMARY KEY(aid));");
		db.execSQL("create table if not exists FORE_GROUND_APP(fid INTEGER not null, timestamp text not null,app text not null, sync integer default 0, PRIMARY KEY(fid));");
		Log.d("AutoLog","Table Created!!!");

		// Table for NotificationDetails logging
		db.execSQL("create table if not exists "
				+ "NotificationDetails(appName text not null, "
				+ "notificationDetails text not null,"
				+ " timestamp text not null, sync integer default 0);");
		
		// Table for DataUsageLogging
		db.execSQL("create table if not exists DataUsage(appName text not null, "
				+ "send text not null, received text not null, "
				+ "total text not null, timestamp text not null, sync integer default 0);");
		
		// Table for ScreenstateLogging
		db.execSQL("create table if not exists "
				+ "phone_activity(sno INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "screenState text not null,"
				+ "timestamp text not null,"
				+ "total long not null,"
				+ "cumulatetotal text not null, sync integer default 0);");
		// Table for Screenstate sorting 
		
		db.execSQL("create table if not exists "
				+ "sort(sno INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "hour text not null,"
				+ "screenState text not null,"
				+ "timestamp text not null,"
				+ "total long not null, sync integer default 0);");
		
		
		// Table for AlarmDetails logging
		db.execSQL("create table if not exists "
				+ "AlarmDetails(alarm text not null, "
				+ " timestamp text not null, sync integer default 0,"
				+ " PRIMARY KEY ( timeStamp ));");
		
		// Table for ChargerState
		db.execSQL("create table if not exists "
				+ "ChargerState(connection text not null, "
				+ "chargingstate text not null,"
				+ "chargingpoint text not null,"
				+ "battery text not null,"
				+ "timestamp text not null, sync integer default 0);");
		Log.d("AutoLogDB", "Table Created ChargerState!!!");
		
		// Table for AmbientTemperature
		db.execSQL("create table if not exists "
				+ "AmbientTemperature(temperature text not null,"
				+ "timestamp text not null, sync integer default 0);");
		Log.d("AutoLogDB", "Table Created AmbientTemperature!!!");
		
		// Table for MediaEvent
		db.execSQL("create table if not exists "
				+ "MediaEvent(filepath text not null,"
				+ "filetype text not null,"
				+ "timestamp text not null, sync integer default 0, primary key(filepath));");
		Log.d("AutoLogDB", "Table Created MediaEvent!!!");

		db.execSQL("create table if not exists audiolevel(value text not null, timestamp text not null,"
				+ " sync integer default 0);");
		Log.d("audiolevel","Table Created!!!");
	

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		try {

			db.execSQL("DROP TABLE IF EXISTS NotificationDetails");
			db.execSQL("DROP TABLE IF EXISTS DataUsage");
			db.execSQL("DROP TABLE IF EXISTS phone_activity");
			db.execSQL("DROP TABLE IF EXISTS AlarmDetails");
			db.execSQL("DROP TABLE IF EXISTS ChargerState");
			db.execSQL("DROP TABLE IF EXISTS AmbientTemperature");
			db.execSQL("DROP TABLE IF EXISTS MediaEvent");
			
			db.execSQL("DROP TABLE IF EXISTS devicestate");
			db.execSQL("DROP TABLE IF EXISTS lightsensor");
			db.execSQL("DROP TABLE IF EXISTS wifianddata");
			db.execSQL("DROP TABLE IF EXISTS audiolevel");
			db.execSQL("DROP TABLE IF EXISTS sort");
			db.execSQL("DROP TABLE IF EXISTS LOCATION");
			db.execSQL("DROP TABLE IF EXISTS ACTIVITIES");
			db.execSQL("DROP TABLE IF EXISTS FORE_GROUND_APP");


			onCreate(db);
		} catch (SQLException e) {
			Utils.appendLog(e);
			

		}
	}
}
