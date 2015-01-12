package com.taramt.autologalarm;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.taramt.utils.DBAdapter;

@SuppressLint("NewApi") public class AlarmService extends Service {
	SharedPreferences prefs;
	static String Tag = "NEXT_ALARM";
	DBAdapter db;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {
		db = new DBAdapter(getApplicationContext());
		Log.d(Tag, "Service onCreate called");
		
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(Tag, "Service onStart called");
		prefs = getSharedPreferences("ALARM", MODE_PRIVATE);
		
		String nextAlarm = "";
		nextAlarm = getNextAlarm(getApplicationContext());
	
		/* getting nextalarmclock information 
		 * through getNextAlarmClock
		 * works only for api level 21 and above
		  */
		try {
		    AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		    nextAlarm = am.getNextAlarmClock().toString();
		    Log.d(Tag + "_AI", nextAlarm);
		    } catch (NoSuchMethodError e) {
		        
		    }
		
	//	Log.d(Tag + "Repeat", "next alarm is:  " + nextAlarm);
		if (!nextAlarm.equals(prefs.getString("nextAlarm", "")) 
				&& !nextAlarm.equals("")
				&& !nextAlarm.equals(" ")) {
			db.open();
			db.insertAlarmDetails("alarmset", nextAlarm);
			Log.d(Tag, "next alarm is:  " + nextAlarm);
			Editor editor = prefs.edit();
			editor.putString("nextAlarm", nextAlarm);
			editor.commit();
			db.close();
		}
		
		
		
	}
	
	
	public static String getNextAlarm(Context context) {
		// collecting short names of days    
		DateFormatSymbols symbols = new DateFormatSymbols();
		// and fill with those names map...
		Map<String, Integer> map = new HashMap<String, Integer>();
		String[] dayNames = symbols.getShortWeekdays();
		
		
		map.put(dayNames[Calendar.MONDAY], Calendar.TUESDAY);
		map.put(dayNames[Calendar.TUESDAY], Calendar.WEDNESDAY);
		map.put(dayNames[Calendar.WEDNESDAY], Calendar.THURSDAY);
		map.put(dayNames[Calendar.THURSDAY], Calendar.FRIDAY);
		map.put(dayNames[Calendar.FRIDAY], Calendar.SATURDAY);
		map.put(dayNames[Calendar.SATURDAY], Calendar.SUNDAY);
		map.put(dayNames[Calendar.SUNDAY], Calendar.MONDAY);
	
		String nextAlarm = Settings.System.getString(context.getContentResolver(),
				Settings.System.NEXT_ALARM_FORMATTED);
		
		Log.d(Tag + "original", nextAlarm);
		
		// In case if alarm isn't set.....
		if ((nextAlarm==null) || ("".equals(nextAlarm))) return null;
		// day
		String nextAlarmDay = nextAlarm.split(" ")[0];
		// getting number....
		int alarmDay = map.get(nextAlarmDay);

		Date now = new Date();      
		String dayOfWeek = new SimpleDateFormat("EE", Locale.getDefault()).format(now);     
		int today = map.get(dayOfWeek);

		// calculating no of days we have to next alarm :-)
		int daysToAlarm = alarmDay-today;
		// sometimes it will  be negtive number so add 7.
		if (daysToAlarm<0) {
			daysToAlarm += 7;
		}



		//  building date, and parse it.....
		try {
			Calendar cal2 = Calendar.getInstance();
			String str = cal2.get(Calendar.YEAR)+"-"+(cal2.get(Calendar.MONTH)+1)+"-"+(cal2.get(Calendar.DAY_OF_MONTH));

			SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-d hh:mm");

			cal2.setTime(df.parse(str+nextAlarm.substring(nextAlarm.indexOf(" "))));
			cal2.add(Calendar.DAY_OF_YEAR, daysToAlarm);
			
			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
			
			return s.format(cal2.getTime()) + " "+ nextAlarm.split(" ")[1];
		} catch (Exception e) {

		}
		// in case if cannot calculate...
		return null;
	}

}

