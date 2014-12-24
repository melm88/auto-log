package com.taramt.utils;

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
	public void insertLocationDetails(String timeStamp,String lat , String lon, String accuracy, String address, String type){
		Log.d("DBAdapter","insertLocation");
		open();
				
		//String query="insert into LOCATION VALUES ("+timeStamp+","+lat+","+lon+","+accuracy+","+address+","+type+")";
		ContentValues locationValues=new ContentValues();
		locationValues.put("timestamp", timeStamp);
		locationValues.put("lat", lat);
		locationValues.put("lon", lon);
		locationValues.put("accuracy", accuracy);
		locationValues.put("address", address);
		locationValues.put("ltype", type);
		db.insert("LOCATION", null, locationValues);
		//db.execSQL(query);
		db.close();
	}
	public String[][] getLocationDetails(){
		Log.d("DBAdapter","getlocation");
		open();
		String query="select * from LOCATION";
		Cursor cursor;
		try {
			cursor = db.rawQuery(query, null);
			int size=cursor.getCount();
			Log.d("no of location logs",size+"");
			String[][] data=new String[size][6];
			int i=0;
			while(cursor.moveToNext()){
				data[i][0]=cursor.getString(1);
				data[i][1]=cursor.getString(2);
				data[i][2]=cursor.getString(3);
				data[i][3]=cursor.getString(4);
				data[i][4]=cursor.getString(5);
				data[i][5]=cursor.getString(6);
				i++;
			}
			db.close();
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in getlocation",e.toString());
			return null;
		}
		
	}
	
	public void insertActivity(String timestamp,String activity,String confidence){
		
		Log.d("DBAdapter","insertActivity");
		open();
				
		//String query="insert into LOCATION VALUES ("+timeStamp+","+lat+","+lon+","+accuracy+","+address+","+type+")";
		ContentValues locationValues=new ContentValues();
		locationValues.put("timestamp", timestamp);
		locationValues.put("activity", activity);
		locationValues.put("confidence", confidence);
		
		db.insert("ACTIVITIES", null, locationValues);
		//db.execSQL(query);
		db.close();
	}
	
	public String[][] getActivities(){
		Log.d("DBAdapter","getactivities");
		open();
		String query="select * from ACTIVITIES";
		Cursor cursor;
		try {
			cursor = db.rawQuery(query, null);
			int size=cursor.getCount();
			Log.d("no of activities logs",size+"");
			String[][] data=new String[size][3];
			int i=0;
			while(cursor.moveToNext()){
				data[i][0]=cursor.getString(1);
				data[i][1]=cursor.getString(2);
				data[i][2]=cursor.getString(3);
				
				i++;
			}
			db.close();
			return data;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in getactivity",e.toString());
			return null;
		}
	}

}