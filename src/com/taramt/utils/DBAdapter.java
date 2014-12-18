package com.taramt.utils;

import android.content.Context;
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

}