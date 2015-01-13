package com.taramt.autologscreenstate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

public class ScreenActivity extends Activity {


	private int pYear;
	private int pMonth;
	private int pDay;
	/** This integer will uniquely define the dialog
	 *  to be used for displaying date picker.*/
	static final int DATE_DIALOG_ID = 0;

	DBAdapter db;
	TextView log, Average, Total;
	String total, average;
	long totalDuration;
	SharedPreferences prefs;
	Utils utils;
	AlarmManager am;
	PendingIntent pendingIntent;
	String alarm = "ALARM";
	Intent intent;
	static int id = 0;
	String date;
	ArrayList<String> sDetails = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		utils = new Utils(this);

		db = new DBAdapter(this);
		db.open();
		log = (TextView)findViewById(R.id.log);
		Average = (TextView)findViewById(R.id.Average);
		Total = (TextView)findViewById(R.id.Total_duration);

		sDetails = db.getScreenStateDetails();
		db.close();
		log.setText(utils.getDetails(db, sDetails));
		showTotalDuration();
		showAverage();

//		db.open();
//		if (db.getrowcount("Active") == 0) {
//			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//			String date =s.format(new Date());
//			db.inserScreenstate("Active", date);
//		}
//		db.close();
//
//		if (ScreenActivity.this.getResources().getConfiguration().orientation == 1) {
//			startAlarm();
	//	}
	//	id = 0;




		/** Get the current date */
		final Calendar cal = Calendar.getInstance();
		pYear = cal.get(Calendar.YEAR);
		pMonth = cal.get(Calendar.MONTH);
		pDay = cal.get(Calendar.DAY_OF_MONTH);
	

	}


	public void startAlarm() {
		try {

			intent = new Intent(getApplicationContext(), ScreenService.class);
			pendingIntent = PendingIntent.getService(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
					1000*30, pendingIntent);
			Log.i("oon Alarm", "Alarm started");
		} catch (Exception e) {
			Log.d("exceptionasdfdf",""+e);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (ScreenActivity.this.getResources().getConfiguration().orientation == 1) {
			startAlarm();
		}
	}

	public void Sort(View v) {
		showDialog(DATE_DIALOG_ID);
		totalDuration = prefs.getLong("t_locked", 0L)
				+ prefs.getLong("t_unlocked", 0L);
		showTotalDuration();
		showAverage();
	}
	public void Sort() {
		String Sort = "Active:\n\n";
		Log.d("date_S", date+"");
		ArrayList<String> sort = new ArrayList<String>();
		sort = db.getSortDetails("Active", date);
		Sort = Sort + utils.getDetails(db, sort) + "\n";
		sort = db.getSortDetails("Idle", date);
		Sort = Sort + "Idle:\n\n" + utils.getDetails(db, sort) + "\n";
		log.setText(Sort);
	}
	public void Top3(View v) {

		if (id == 0) {
			String Top3 = "Active:\n\n";

			ArrayList<String> top3 = new ArrayList<String>();
			top3 = db.getTop3("Active");
			Top3 = Top3 + utils.getDetails(db, top3) + "\n";
			top3 = db.getTop3("Idle");
			Top3 = Top3 + "Idle:\n\n" + utils.getDetails(db, top3) + "\n";
			log.setText(Top3);
			id = 1;
		} else {
			db.open();
			sDetails = db.getScreenStateDetails();
			db.close();
			log.setText(utils.getDetails(db, sDetails));
			id = 0;
		}

	}


	public void showAverage() {
		db.open();
		int a_rowcount = db.getrowcount("Active");
		int i_rowcount = db.getrowcount("Idle");
		if (a_rowcount == 0) {
			a_rowcount = 1;
		}
		if (i_rowcount == 0) {
			i_rowcount = 1;
		}
		average = "Average: \nActive:  " 
				+ utils.convert2Time(prefs.getLong("t_unlocked", 0L)/a_rowcount) 
				+ "\n";
		average = average + "Idle: "
				+ utils.convert2Time(prefs.getLong("t_locked", 0L)/i_rowcount);
		Average.setText(average);
		db.close();
	}

	public void showTotalDuration() {
		total = "Total Duraion : " + utils.convert2Time(totalDuration) + "\n";
		total = total + "Active:  " 
				+ utils.convert2Time(prefs.getLong("t_unlocked", 0L)) + "\n";
		total = total + "Idle: "
				+ utils.convert2Time(prefs.getLong("t_locked", 0L));
		Total.setText(total);
		Log.d("Totalduration", prefs.getLong("t_locked", 0L) + " | " + prefs.getLong("t_locked", 0L));
	}


	/** Callback received when the user "picks" a date in the dialog */
	private DatePickerDialog.OnDateSetListener pDateSetListener =
			new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, 
				int monthOfYear, int dayOfMonth) {
			pYear = year;
			pMonth = monthOfYear;
			pDay = dayOfMonth;
			updateDisplay();
			Sort();
		}
	};
	/** Updates the date in the TextView */
	private void updateDisplay() {
		// Month is 0 based so add 1
		pMonth = pMonth + 1;
		String pMonth1 = pMonth+"";
		String pDay1 = pDay+"";
		if (pMonth < 10) {
			pMonth1 = "0"+pMonth;
		}
		if (pDay < 10) {
			pDay1 = "0"+pDay;
		}
		date = pDay1 + "-" + pMonth1 + "-" + pYear;
		Log.d("date_u", date+"");
	}

	/** Create a new dialog for date picker */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			Log.d("check", pDay + "-" + pMonth + "-" + pYear);
			return new DatePickerDialog(this, 
					pDateSetListener,
					pYear, pMonth, pDay);
		}
		return null;
	}
}
