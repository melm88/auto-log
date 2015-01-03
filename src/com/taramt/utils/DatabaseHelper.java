package com.taramt.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, "AutoLog" , null, 2);
	//	Log.d("AutoLog", "Database created....");
	}

	//Create tables
	public void onCreate(SQLiteDatabase db) {
		
		// Table for NotificationDetails logging
		db.execSQL("create table if not exists "
				+ "NotificationDetails(appName text not null, "
				+ "notificationDetails text not null,"
				+ " timeStamp text not null);");
		
		// Table for DataUsageLogging
		db.execSQL("create table if not exists DataUsage(appName text not null, "
				+ "send text not null, received text not null, "
				+ "total text not null, timeStamp text not null);");
		
		// Table for ScreenstateLogging
		db.execSQL("create table if not exists "
				+ "phone_activity(sno INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "screenState text not null,"
				+ "timeStamp text not null,"
				+ "total long not null,"
				+ "cumulatetotal text not null);");
		// Table for Screenstate sorting 
		
		db.execSQL("create table if not exists "
				+ "sort(sno INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "hour text not null,"
				+ "screenState text not null,"
				+ "timeStamp text not null,"
				+ "total long not null);");
		
		
		// Table for AlarmDetails logging
		db.execSQL("create table if not exists "
				+ "AlarmDetails(alarm text not null, "
				+ " timeStamp text not null,"
				+ " PRIMARY KEY ( timeStamp ));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		try {

			db.execSQL("DROP TABLE IF EXISTS NotificationDetails");
			db.execSQL("DROP TABLE IF EXISTS DataUsage");
			db.execSQL("DROP TABLE IF EXISTS phone_activity");
			db.execSQL("DROP TABLE IF EXISTS AlarmDetails");
			onCreate(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}

	}
}
