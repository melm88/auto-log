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
				data[i][0]=cursor.getString(cursor.getColumnIndex("timeStamp"));
				data[i][1]=cursor.getString(cursor.getColumnIndex("lat"));
				data[i][2]=cursor.getString(cursor.getColumnIndex("lon"));
				data[i][3]=cursor.getString(cursor.getColumnIndex("accuracy"));
				data[i][4]=cursor.getString(cursor.getColumnIndex("address"));
				data[i][5]=cursor.getString(cursor.getColumnIndex("ltype"));
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

}