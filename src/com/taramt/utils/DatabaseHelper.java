package com.taramt.utils;

import android.content.Context;
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
		//db.execSQL("create table if not exists email(mesgid INTEGER not null, subject text not null, email text not null, sender text not null, datetime text not null, receiver text not null, folder text not null, numofattachments integer not null, recipients text,  flags text not null, displayname text, attachmentName text, PRIMARY KEY(mesgid, email, folder));");
		Log.d("AutoLog","Table Created!!!");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}
}
