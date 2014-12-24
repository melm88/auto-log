package com.taramt.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public DatabaseHelper(Context context) {
		super(context, "AutoLog" , null, 1);
		Log.d("AutoLog", "Database created....");
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
				+ " total text not null);");
		System.out.println();
		Log.d("Notification", "Table Created!!!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		try {

			db.execSQL("DROP TABLE IF EXISTS NotificationDetails");
			db.execSQL("DROP TABLE IF EXISTS DataUsage");
			db.execSQL("DROP TABLE IF EXISTS phone_activity");
			onCreate(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}

	}
}
