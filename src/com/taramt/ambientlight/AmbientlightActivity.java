package com.taramt.ambientlight;

import java.util.ArrayList;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;

import android.support.v7.app.ActionBarActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AmbientlightActivity extends ActionBarActivity {
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ambientlight);
		//First time
		SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(this);
		if (savedValues.getBoolean(getString(R.string.Start), true)) {
			SharedPreferences.Editor editor = savedValues.edit();
			editor.putBoolean(getString(R.string.Start), false);
			editor.commit();
			Intent intent = new Intent(this, Ambientlightservice.class);
			PendingIntent pintent = PendingIntent
					.getService(this, 0, intent, 0);
			AlarmManager alarm = (AlarmManager) 
					getSystemService(Context.ALARM_SERVICE);
			alarm.cancel(pintent);
			alarm.setRepeating(AlarmManager.RTC_WAKEUP, 
					System.currentTimeMillis()+5*60*1000,
					5*60*1000, pintent); 


		
		} else {
			Log.d("flag", "timer is already set");
		}
				TextView tv = (TextView)findViewById(R.id.ambientlightlog);
		  DBAdapter db = new DBAdapter(this);
		   db.open();
		   ArrayList<String> row = db.getLightSensorlog();
		   String content = "";
		   for (int i = 0; i < row.size(); i++) {
			   content = content +  row.get(i)+ "\n\n";
		}
		   db.close();
		   tv.setText(content);
	
              }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ambientlight, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
