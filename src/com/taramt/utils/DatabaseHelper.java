package com.taramt.utils;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	public DatabaseHelper(Context context) 
	{
		super(context,"AutoLog" , null, 1);
	}


	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table if not exists devicestate(activity text not null, timestamp text not null);");
		Log.d("devicestate","Table Created!!!");
		
		db.execSQL("create table if not exists lightsensor(value text not null, timestamp text not null);");
		Log.d("lightsensor","Table Created!!!");
		
		db.execSQL("create table if not exists wifianddata(network text not null, activity text not null, timestamp text not null);");
		Log.d("lightsensor","Table Created!!!");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

		try {
			
			db.execSQL("DROP TABLE IF EXISTS devicestate");
			db.execSQL("DROP TABLE IF EXISTS lightsensor");
			db.execSQL("DROP TABLE IF EXISTS wifianddata");
			
			onCreate(db);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		
		}
	}


}
