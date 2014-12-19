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
		db.execSQL("create table if not exists "
				+ "ChargerState(connection text not null, "
				+ "chargingstate text not null,"
				+ "chargingpoint text not null,"
				+ "battery text not null,"
				+ " timeStamp text not null);");
		Log.d("ChargerState", "Table Created ChargerState!!!");
		db.execSQL("create table if not exists "
				+ "AmbientTemperature(temperature text not null, "
				+ " timeStamp text not null);");
		Log.d("ChargerState", "Table Created AmbientTemperature!!!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		try {

			db.execSQL("DROP TABLE IF EXISTS ChargerState");
			onCreate(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}

	}
}
