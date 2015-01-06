package com.taramt.autolog;

import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.TextView;

/**
 * 
 * @author ASHOK
 *
 * This class is used to get the call log from the android phone .
 * 
 */
public class Calllog extends Activity {

	TextView log;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calllog);
		
		log = (TextView) findViewById(R.id.call_log);
	
		// get the call log
		getCallDetails(); 
	
	}
	
/*
 * getCallDetails method is used to get call details from the phone database.
 */
	@SuppressWarnings("deprecation")
	private void getCallDetails() { 
		
		StringBuffer sb = new StringBuffer(); 
		
		Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, 
				null, null, null, null); 
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER); 
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE); 
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		
		sb.append("Call Log :");
		
		// loop for traversing whole log
		while (managedCursor.moveToNext()) { 
			String phNumber = managedCursor.getString(number); 
			String callType = managedCursor.getString(type);
			String callDate = managedCursor.getString(date); 
			Date callDayTime = new Date(Long.valueOf(callDate)); 
			String callDuration = managedCursor.getString(duration);
			String dir = null; int dircode = Integer.parseInt(callType); 
			switch (dircode) { 
				case CallLog.Calls.OUTGOING_TYPE: dir = "OUTGOING";
					break; 
				case CallLog.Calls.INCOMING_TYPE: dir = "INCOMING"; 
					break; 
				case CallLog.Calls.MISSED_TYPE: dir = "MISSED"; 
					break; 
					} 
			sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- " 
					+ dir + " \nCall Date:--- " + callDayTime
					+ " \nCall duration in sec :--- " + callDuration); 
			sb.append("\n----------------------------------"); } 
		
		// set the log to textview.
		log.setText(sb); 
		} 
	
}
