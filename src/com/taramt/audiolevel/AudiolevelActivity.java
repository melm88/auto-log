package com.taramt.audiolevel;

import java.util.ArrayList;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
/*
 * MainActivity launches AudioLevel logger service. 
 */
public class AudiolevelActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audiolevel);

		Log.d("Activity", "onCreate");
		try {
			//displaying the log from database on text view
			DBAdapter db = new DBAdapter(this);
			db.open();
			ArrayList<String> rows = db.getaudiolog();
			db.close();
			ListView listView = (ListView) findViewById(R.id.list);


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, rows);

			listView.setAdapter(adapter); 
		} catch(Exception e) {
			e.printStackTrace();
		}

	}
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
}
