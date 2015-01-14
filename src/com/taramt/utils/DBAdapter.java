package com.taramt.utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

public class DBAdapter {
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	SharedPreferences prefs;
	static int ihour = 0;
	static int ahour = 0;
	static long itotal = 0L;
	static long atotal = 0L;
	Utils utils;
	static int id = 0;
	public DBAdapter(Context context) {
		Log.d("AutoLog","Inside DBAdapter Constructor");
		DBHelper = new DatabaseHelper(context);
		utils = new Utils(context);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}	


	public void open() {
		db = DBHelper.getReadableDatabase();
	}
	public void close() {
		DBHelper.close();
	}

	// To log the database of notification details
	public long insertNotificationDetails(String packageName, 
			String nDetails, String timeStamp) {
		Log.d("Notification",
				"DBA: " + packageName 
				+ " | " + nDetails + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("appName", packageName);
		cv.put("notificationDetails", nDetails);
		cv.put("timestamp", timeStamp);
		long n=db.insert("NotificationDetails", null, cv);
		Log.d("NotificationDetails", "" + n);
		return n;	
	}

	// To get the notification details show in display
	public ArrayList<String> getNotificationDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("NotificationDetails", 
				null, null, null, null, null, null);
		ArrayList<String> NotificationDetails = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("appName"))
					+ "  " + cursor.getString(cursor.getColumnIndex("notificationDetails")) 
					+ "  " + cursor.getString(cursor.getColumnIndex("timestamp"));
			NotificationDetails.add(nDetails);
		}
		return NotificationDetails;
	}

	// To log DataUsage of each app
	public long insertDataUsage(String tablename, String appName, 
			String send, String received, String total, String timeStamp)

	{
		Log.d("Dbb","DBA: " + appName 
				+ " | "+ send +" | "+received
				+ " | "+ total +" | "+timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("appName", appName);
		cv.put("send", send);
		cv.put("received", received);
		cv.put("total", total);
		cv.put("timestamp", timeStamp);
		long n=db.insert(tablename, null, cv);
		Log.d("DataUsage", ""+n);
		return n;	
	}

	// to show the Datausage of each app in display
	public ArrayList<String> getDatausageofApps(){
		//String query="select email_id from contacts";
		Cursor cursor=db.query("DataUsage", null,null, null, null, null, null);
		ArrayList<String> datausageofApps = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("appName"))
					+ "  " + cursor.getString(cursor.getColumnIndex("send")) 
					+ "  " + cursor.getString(cursor.getColumnIndex("received"))
					+ "  " + cursor.getString(cursor.getColumnIndex("total"))
					+ "  " + cursor.getString(cursor.getColumnIndex("timestamp"));
			datausageofApps.add(nDetails);
		}
		return datausageofApps;
	}


	// SCREEN STATE

	//To insert the screen state into the database
	public void inserScreenstate(String state,String time){
		if (!getLastrow().equals("noLastDetails") || id != 0) {
			getTotal(state, time);
			Log.d("De", id+"");
		} else {
			id++;
		}

		ContentValues cv = new ContentValues();
		cv.put("screenState", state);
		cv.put("timestamp", time);
		cv.put("total", 0L);
		cv.put("cumulatetotal", 0L+"");	

		db.insert("phone_activity", null, cv);

	}

	public void enterPrefs(long cumulateTotal, String time, String state) {

		String lastTimeStamp = getLastrow().split(",")[1];
		lastTimeStamp = lastTimeStamp.split(" ")[0];
		Editor edit = prefs.edit();
		Log.d("LAST", time + " | " + state + " | " + cumulateTotal);
		if(state.equals("Active")) {
			if (lastTimeStamp.equals(time.split(" ")[0])) {
				edit.putLong("t_unlocked",cumulateTotal);
				Log.d("cTunlocked",prefs.getLong("t_unlocked", 0L) + "");
				Log.d("timeStamp T + LAstT same", time.split(" ")[0] + " | " + lastTimeStamp + " | " + cumulateTotal);
			} else {
				Log.d("timeStamp T + LAstT", time.split(" ")[0] + " | " + lastTimeStamp);
				edit.putLong("t_unlocked",0L);

			}

			edit.putString("s_unlocked", time);
		} else {
			if (lastTimeStamp.equals(time.split(" ")[0])) {
				edit.putLong("t_locked",cumulateTotal);
				Log.d("cTlocked",prefs.getLong("t_locked", 0L) + "");
				Log.d("timeStamp T + LAstT same", time.split(" ")[0] + " | " + lastTimeStamp + " | " + cumulateTotal);
			} else {
				Log.d("timeStamp T + LAstT", time.split(" ")[0] + " | " + lastTimeStamp);
				edit.putLong("t_locked", 0L);
			}
			edit.putString("s_locked", time);
		}
		edit.commit();
	}

	public long updateTotal(String timeStamp, long total, String cumulateTotal) {

		open();
		ContentValues cv=new ContentValues();
		cv.put("cumulatetotal", cumulateTotal);
		cv.put("total", total);
		try {
			long n = db.update("phone_activity", 
					cv, "timestamp=?", new String[] {timeStamp});
			Log.d("UPDATE", ""+n);
			return n;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in updating folders",e.toString());
		}
		close();
		return 0;
	}
	//Audio Level Value inserted
	public long insertAudioLevelValue(String value, String timeStamp) {
		Log.d("Dbb","DBA: " + value + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("value", value);
		cv.put("timestamp", timeStamp);
		long n=db.insert("audiolevel", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	public ArrayList<String> getScreenStateDetails() {
		Cursor cursor = db.query("phone_activity", 
				null, null, null, null, null, null);
		ArrayList<String>screenStateDetailss = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String sDetailss = cursor.getString(cursor.getColumnIndex("sno"))
					+ "  " + cursor.getString(cursor.getColumnIndex("screenState")) 
					+ "  " + cursor.getString(cursor.getColumnIndex("timestamp"))
					+ "  " + utils.convert2Time(cursor.getLong(cursor.getColumnIndex("total")));
			screenStateDetailss.add(sDetailss);
		}
		return screenStateDetailss;
	}
	/*
	 * getDiffTS is the method which will get the difference b/n 
	 * current state time stamp and previous state time stamp
	 *
	 */
	public long getDiffTS(String timeStamp, String time) {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		Date time1 = null;
		Date timeStamp1 = null;
		long start = 0L, stop = 0L;
		try {
			time1 = sdf.parse(time);
			start = time1.getTime();

			timeStamp1 = sdf.parse(timeStamp);
			stop = timeStamp1.getTime();

			Log.d("LASTLONG", start + " | " +stop+ " | " + (start - stop)/1000);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (start - stop);
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


	public ArrayList<String> getLocationDetailsArrayList(){
		ArrayList<String> rows= new ArrayList<String>();
 		Log.d("DBAdapter","getlocation");
		open();
		String query="select * from LOCATION";
		Cursor cursor;
		try {
			cursor = db.rawQuery(query, null);
			int size=cursor.getCount();
			Log.d("no of location logs",size+"");
			while(cursor.moveToNext()){
				String nDetails = cursor.getString(1)
						+ "\n" + cursor.getString(2)
						+ "\n" + cursor.getString(3)
				+ "\n" + cursor.getString(4)
				+ "\n" + cursor.getString(5)
				+ "\n" + cursor.getString(6);
				
				rows.add(nDetails);
			}
			db.close();
			return rows;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in getlocation",e.toString());
			return null;
		}
		
	}

	public void insertActivity(String timestamp,String activity,String confidence){

		Log.d("DBAdapter","insertActivity");
		open();

		//String query="insert into LOCATION VALUES ("+timestamp+","+lat+","+lon+","+accuracy+","+address+","+type+")";
		ContentValues locationValues=new ContentValues();
		locationValues.put("timestamp", timestamp);
		locationValues.put("activity", activity);
		locationValues.put("confidence", confidence);

		db.insert("ACTIVITIES", null, locationValues);
		//db.execSQL(query);
		db.close();
	}

	public ArrayList<String> getActivities(){
		ArrayList<String> rows = new ArrayList<String>();

		Log.d("DBAdapter","getactivities");
		open();
		String query="select * from ACTIVITIES";
		Cursor cursor;
		try {
			cursor = db.rawQuery(query, null);
			int size=cursor.getCount();
			Log.d("no of activities logs",size+"");
			while(cursor.moveToNext()){

				String nDetails = cursor.getString(1)
						+ "\n" + cursor.getString(2)
						+ "\n" + cursor.getString(3);
				
				rows.add(nDetails);	

			}
			db.close();
			return rows;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in getactivity",e.toString());
			return null;
		}
	}

	public long getTotal(String state, String time) {
		Editor editor = prefs.edit();
		String rowDetails = getLastrow();
		String timeStamp = rowDetails.split(",")[1];
		String lastState = rowDetails.split(",")[0];
		long cumulateTotal = 0L;
		long total = getDiffTS(timeStamp, time);

		if (lastState.equals("Active")) {

			cumulateTotal =  total + prefs.getLong("t_unlocked", 0L);

			Log.d("cumulateTotal", cumulateTotal + "");

			if (ahour != Integer.parseInt(timeStamp.substring(11,13))) {
				ahour = Integer.parseInt(timeStamp.substring(11,13));
				insertSort(lastState, timeStamp, total, ahour+"");
				editor.putString("aTS", timeStamp);
				atotal = total;
				Log.d("DebActive", lastState + " | " + timeStamp 
						+ " | " + utils.convert2Time(cumulateTotal) 
						+ " | " + utils.convert2Time(atotal));
			} else {
				atotal = atotal + total;
				updateSort(lastState, prefs.getString("aTS", timeStamp), atotal, ahour+"");
				Log.d("DebActive", lastState + " | " + timeStamp 
						+ " | " + utils.convert2Time(cumulateTotal)
						+ " | " + utils.convert2Time(atotal));
			}

		} else {
			cumulateTotal = total + prefs.getLong("t_locked", 0L);


			if (ihour != Integer.parseInt(timeStamp.substring(11,13))) {
				ihour = Integer.parseInt(timeStamp.substring(11,13));
				insertSort(lastState, timeStamp, total, ihour+"");
				editor.putString("iTS", timeStamp);
				itotal = total;
				Log.d("DebIdle", lastState + " | " + timeStamp 
						+ " | " + utils.convert2Time(cumulateTotal) 
						+ " | " + utils.convert2Time(itotal));
			} else {
				itotal = itotal + total;
				updateSort(lastState, prefs.getString("iTS", timeStamp), itotal, ihour+"");
				Log.d("DebIdle", lastState + " | " + timeStamp 
						+ " | " + utils.convert2Time(cumulateTotal) 
						+ " | " + utils.convert2Time(itotal));
			}

		}
		editor.commit();

		// diff TS and time
		// add the diff to total
		//return total
		enterPrefs(cumulateTotal, timeStamp, lastState);
		Log.d("cumulateTotalAfter", cumulateTotal + "");
		updateTotal(timeStamp, total, cumulateTotal+"");
		return cumulateTotal;
	}
	public String getLastrow() {
		Cursor cursor = db.query("phone_activity", 
				null, null, null, null, null, null);
		String sDetailss = "noLastDetails";
		if (cursor.getCount() > 0) {
			cursor.moveToLast();
			sDetailss = cursor.getString(cursor.getColumnIndex("screenState"))
					+ "," + cursor.getString(cursor.getColumnIndex("timestamp"));
		}
		return sDetailss;
	}

	public int getrowcount(String state) {

		Cursor cursor = db.query("phone_activity", 
				null, "screenState=?", new String[] {state}, null, null, "total");
		return cursor.getCount();
	}

	public ArrayList<String> getTop3(String state) {
		open();
		Cursor cursor = db.query("phone_activity", 
				null, "screenState=?", new String[] {state}, null, null, "total");
		ArrayList<String> top3 = new ArrayList<String>();
		cursor.moveToPosition(cursor.getCount() - 4);
		while(cursor.moveToNext()) {

			String sDetailss = cursor.getString(cursor.getColumnIndex("timestamp"))
					+ "  " + utils.convert2Time(cursor.getLong(cursor.getColumnIndex("total")));
			top3.add(sDetailss);


		}
		close();
		return top3;
	}


	// SORT


	public void insertSort(String state, String time, long total, String hour) {

		ContentValues cv = new ContentValues();
		cv.put("screenState", state);
		cv.put("timestamp", time);
		cv.put("total", total);
		cv.put("hour", hour);	
		Log.d("DBBInsert", state + " | " + time  + " | " + total  + " | " + hour);
		db.insert("sort", null, cv);
	}
	public long updateSort(String state, String timeStamp, long total, String hour) {

		open();
		ContentValues cv=new ContentValues();
		cv.put("total", total);
		try {
			long n = db.update("sort", 
					cv, "screenState=? and hour=? and timestamp=?",
					new String[] {state, hour, timeStamp});
			Log.d("UPDATESORT", ""+n);
			return n;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d("exception in updating folders",e.toString());
		}
		close();
		return 0;
	}
	public ArrayList<String> getSortDetails(String state, String date) {
		open();
		Cursor cursor = db.query("sort", 
				null, "screenState=? and timestamp LIKE '"+date+"%'", new String[] {state}, null, null, "total");
		ArrayList<String> screenStateDetailss = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String sDetailss = cursor.getString(cursor.getColumnIndex("timestamp"))
					+ "  " + utils.convert2Time(cursor.getLong(cursor.getColumnIndex("total")))
					+ "  " + cursor.getString(cursor.getColumnIndex("hour"));
			screenStateDetailss.add(sDetailss);
		}
		close();
		return screenStateDetailss;
	}



	// ALARMDETAILS
	// To log the database of Alarm details
	public long insertAlarmDetails(String alarm, 
			String timeStamp) {
		try {
			Log.d("AALARM",
					"DBA: " + alarm 
					+  timeStamp);
			ContentValues cv = new ContentValues();
			cv.put("alarm", alarm);
			cv.put("timestamp", timeStamp);
			long n = db.insertOrThrow("AlarmDetails", null, cv);
			Log.d("AlarmInserted", "" + n);
			return n;	
		} catch (android.database.sqlite.SQLiteConstraintException s) {

		} 
		return 0;
	}

	// To get the Alarm details show in display
	public ArrayList<String> getAlarmDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("AlarmDetails", 
				null, null, null, null, null, null);
		ArrayList<String> AlarmDetails = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String aDetails = cursor.getString(cursor.getColumnIndex("alarm"))
					+ "  " + cursor.getString(cursor.getColumnIndex("timestamp"));
			AlarmDetails.add(aDetails);
		}
		return AlarmDetails;
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
		cv.put("timestamp", time);
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

		if(cursor != null) {
			while(cursor.moveToNext()) {
				String nDetails = cursor.getString(cursor.getColumnIndex("connection"))
						+ "  " + cursor.getString(cursor.getColumnIndex("chargingstate"))
						+ "  " + cursor.getString(cursor.getColumnIndex("chargingpoint"))
						+ "  " + cursor.getString(cursor.getColumnIndex("battery"))
						+ "  " + cursor.getString(cursor.getColumnIndex("timestamp"));
				PowerDetails.add(nDetails);
			}
		}		

		return PowerDetails;
	}

	//Insert Temperature data into table
	public long insertTemperatureDetails(String temperature, String time) {
		Log.d("Temperature",
				"DBA: " + temperature + " | " + time);
		ContentValues cv = new ContentValues();
		cv.put("temperature", temperature);
		cv.put("timestamp", time);
		long n=db.insert("AmbientTemperature", null, cv);
		Log.d("Temperature", "" + n);
		return n;	
	}

	//Retrieve TemperatureDetails
	public ArrayList<String> getTemperatureDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("AmbientTemperature", 
				null, null, null, null, null, "timestamp DESC");
		ArrayList<String> TemperatureDetails = new ArrayList<String>();
		if(cursor != null) {
			int count = 1;
			while(cursor.moveToNext()) {
				String nDetails = count+") " + cursor.getString(cursor.getColumnIndex("temperature"))
						+ "  " + cursor.getString(cursor.getColumnIndex("timestamp")).split("GMT")[0];
				TemperatureDetails.add(nDetails);
				count++;
			}
		}

		return TemperatureDetails;
	}

	//Insert Image/Video info into table
	public long insertMediaDetails(String path, String mediatype, String time) {
		Log.d("MediaEvent",
				"DBA: " + path + " | " + mediatype + " | " + time);
		long n=0;
		try {
			ContentValues cv = new ContentValues();
			cv.put("filepath", path);
			cv.put("filetype", mediatype);
			cv.put("timestamp", time);
			n=db.insert("MediaEvent", null, cv);
			Log.d("MediaEvent", "" + n);
		} catch(Exception e) {
			Log.d("MediaEvent",e.toString());
			//e.printStackTrace();
		}

		return n;	
	}
	//Retrieve MediaDetails
	public ArrayList<String> getMediaDetails() {
		//String query="select email_id from contacts";
		Cursor cursor = db.query("MediaEvent", 
				null, null, null, null, null, null);
		ArrayList<String> MediaDetails = new ArrayList<String>();
		if(cursor != null) {
			int count = 1;
			while(cursor.moveToNext()) {
				String nDetails = count +") "+ cursor.getString(cursor.getColumnIndex("filepath"))
						+ " | " + cursor.getString(cursor.getColumnIndex("filetype"))
						+ " | " + cursor.getString(cursor.getColumnIndex("timestamp")).split("GMT")[0];
				MediaDetails.add(nDetails);
				count++;
			}
		}

		return MediaDetails;
	}
	//saves the boot,restart events
	public long insertDeviceState(String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("activity", activity);
		cv.put("timestamp", timeStamp);
		long n=db.insert("devicestate", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	//saves the light sensor value
	public long insertLightSensorValue(String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("value", activity);
		cv.put("timestamp", timeStamp);
		long n=db.insert("lightsensor", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	//saves wifi, mobile events
	public long insertWifiandData(String network, String activity, String timeStamp) {
		Log.d("Dbb","DBA: " + activity + " | " + timeStamp);
		ContentValues cv = new ContentValues();
		cv.put("network", network);
		cv.put("activity", activity);
		cv.put("timestamp", timeStamp);

		long n=db.insert("wifianddata", null, cv);
		Log.d("tablename", "inserted a row "+n);
		return n;	
	}
	//Generated get device state log from database
	public ArrayList<String> getdevicestatelog(){
		Cursor cursor=db.query("devicestate", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("activity"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}
	//Generated get Light Sensor log from database
	public ArrayList<String> getLightSensorlog(){
		Cursor cursor=db.query("lightsensor", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while(cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("value"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}
	//Generated get wifi and data log from database
	public ArrayList<String> getwifianddatalog(){
		Cursor cursor=db.query("wifianddata", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String nDetails = 
					cursor.getString(cursor.getColumnIndex("network"))
					+ "\n" +cursor.getString(cursor.getColumnIndex("activity"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}
	//Generated get Audio level log from database
	public ArrayList<String> getaudiolog(){
		Cursor cursor=db.query("audiolevel", null,null, null, null, null, null);
		ArrayList<String> entry = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String nDetails = cursor.getString(cursor.getColumnIndex("value"))
					+ "\n" + cursor.getString(cursor.getColumnIndex("timestamp"));
			entry.add(nDetails);
		}
		return entry;
	}

	/*
	 * insertForeGroundApp method for inserting foregroundApp
	 */
	public void insertForeGroundApp(String timeStamp,String AppName){
		Log.d("DBAdapter","insertForeGroundApp");
		open();

		ContentValues app=new ContentValues();
		app.put("timestamp", timeStamp);
		app.put("app", AppName);

		db.insert("FORE_GROUND_APP", null, app);

		db.close();
	}
	/*
	 * getForeGroundApp method for getting foregroundApp
	 */
			public ArrayList<String> getForeGroundApp(){
				ArrayList<String> entry = new ArrayList<String>();
				Log.d("DBAdapter","getforegroundapp");
				open();
				String query="select * from FORE_GROUND_APP";
				Cursor cursor;
				try {
					cursor = db.rawQuery(query, null);
					int size=cursor.getCount();
					Log.d("no of app logs",size+"");
					while(cursor.moveToNext()){
						String nDetails = cursor.getString(1)
								+ "\n" + cursor.getString(2);
						entry.add(nDetails);
									}
					db.close();
					return entry;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.d("exception in getforegroundapp",e.toString());
					return null;
				}

	}


	// SYNC state cursors/arraylist retrieval methods
	//SYNC DB VALUE-GENERATOR
	public HashMap<String, ArrayList<String>> getSYNCData() {

		String query1 = "";
		Cursor c = null;

		ArrayList<String> arrTblNames = new ArrayList<String>();
		HashMap<String, ArrayList<String>> tables_for_sync = new HashMap<String, ArrayList<String>>();
		ArrayList<String> table_data;
		try {
			c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
			//If there are tables in database then iterate through them and add it to
			// the ArrayList<String> arrTblNames
			if (c.moveToFirst()&&c!=null) {
				while ( !c.isAfterLast() ) {
					arrTblNames.add( c.getString( c.getColumnIndex("name")) );
					c.moveToNext();
				}

			}
			c.close();

			//Log.d("toServer",arrTblNames.toString());

			//Iterate through the ArrayList of tablename (arrTblNames)
			for(String tablname : arrTblNames) {
				//Proceed only if table names are not the default-android tables
				if(!tablname.equalsIgnoreCase("android_metadata") && !tablname.equalsIgnoreCase("sqlite_sequence")) {
					c = db.rawQuery("SELECT * FROM "+tablname+" WHERE sync=0", null);
					//If cursor is not null then proceed
					if(c!=null) {
						table_data = new ArrayList<String>();
						Log.d("toServerDBSYNC",""+tablname+" : "+c.getCount()+"|"+c.getColumnCount());
						int column_count = c.getColumnCount();
						//Iterate through the rows in the particular table
						while(c.moveToNext()){
							String temp = "";
							//Iterate and fetch data in each column of the table
							//Insert data to a temp_string and add to arraylist table_data
							for(int k=0; k<column_count-1; k++) {
								Cursor typeCursor = 
										db.rawQuery("select typeof (" + c.getColumnName(k) + ") from " + tablname, null);
								typeCursor.moveToFirst();
								//Log.d("toServerTYPE", tablname+"|"+k+":"+typeCursor.getString(0));
								if(typeCursor.getString(0).equals("text"))
									temp += c.getString(k)+"|";
								else
									temp += c.getInt(k)+"|";
								typeCursor.close();							
							}
							temp = temp.substring(0, temp.lastIndexOf("|"));
							table_data.add(temp);
							//Log.d("toServerTYPE", temp);
						}
						//Insert the obtained data into a hashmap where key is tablename and value is data
						tables_for_sync.put(tablname, table_data);
						Log.d("toServerHASHV","tablname :"+tables_for_sync.get(tablname).toString());
						//Log.d("toServerHASH",tables_for_sync.keySet().toString());
					}
					c.close();

				}			
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if(c!=null)
				c.close();
		}

		return tables_for_sync;
	}
	
	//Update SYNC column values for the given row of the given table 
	public void updateSYNCTable(String tablename, String timestamp) {
		//String query = "UPDATE "+tablename+" SET sync="+1+" WHERE timestamp='"+timestamp+"'";
		try {
			//Cursor c = db.rawQuery(query, null);
			ContentValues args = new ContentValues();
			args.put("sync", 1);
			boolean upd_state = db.update(tablename, args, "timestamp" + "='" + timestamp+"'", null) > 0;
			Log.d("toServerSYNCUPD", "Upd "+tablename+" | "+timestamp);
		} catch(Exception e) {
			e.printStackTrace();
		}

	}




}