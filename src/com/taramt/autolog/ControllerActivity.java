package com.taramt.autolog;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.taramt.ambientlight.AmbientlightActivity;
import com.taramt.audiolevel.AudiolevelActivity;
import com.taramt.autologalarm.Alarmactivity;
import com.taramt.autologdatausage.DataUsage;
import com.taramt.autolognotification.NotificationActivity;
import com.taramt.autologscreenstate.ScreenActivity;
import com.taramt.boot.BootActivity;
import com.taramt.logmedia.MediaActivity;
import com.taramt.power.PowerActivity;
import com.taramt.temperature.TemperatureActivity;
import com.taramt.wifi.WifiActivity;

public class ControllerActivity extends Activity {
	ListView listView ;

	SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		listView = (ListView) findViewById(R.id.list);
		
		preferences=PreferenceManager.getDefaultSharedPreferences(this);
		
		String[] values = new String[] { 
				"Notification", 
				"MediaActivity",
				"Datausage",
				"PowerActivity",
				"ScreenActivity",
				"TemperatureActivity",
				"Alarm",
				"UserActivity", 
				"Callog", 
				"Location",
				"VisualizationofLocation",
				"WIFI and 3G data",
				"Ambient light",
				"Noise Level",
				"Boot and Reboot",
				"Fore ground apps"
		};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		listView.setAdapter(adapter); 

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent myIntent = null;
				switch(position){
				case 0: 
					myIntent = new Intent(getApplicationContext(), NotificationActivity.class);
					break;
				case 1: 
					myIntent = new Intent(getApplicationContext(), MediaActivity.class);
					break;
				case 2: 
					myIntent = new Intent(getApplicationContext(), DataUsage.class);
					break;
				case 3: 
					myIntent = new Intent(getApplicationContext(), PowerActivity.class);
					break;
				case 4: 
					myIntent = new Intent(getApplicationContext(), ScreenActivity.class);
					break;
				case 5: 
					myIntent = new Intent(getApplicationContext(), TemperatureActivity.class);
					break;
				case 6: 
					myIntent = new Intent(getApplicationContext(), Alarmactivity.class);
					break;
				case 7: 
					if(preferences.getBoolean("activity",false)){
						myIntent = new Intent(getApplicationContext(), ActivityRecognitionActivity.class);
						
					}else{
						myIntent = new Intent(getApplicationContext(), ActivityRecognitionActivity.class);
						
						AlarmManager am= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
						Intent intent=new Intent(getApplicationContext(),ActivityRecognitionService.class);
						PendingIntent pintent = PendingIntent
								.getService(getApplicationContext(), 0, intent, 0);
						am.setRepeating(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis(),
								5*60*1000, pintent);
						
						SharedPreferences.Editor editor=preferences.edit();
						editor.putBoolean("activity", true);
						editor.commit();
					}
					break;
				case 8: 
					myIntent = new Intent(getApplicationContext(), Calllog.class);
					break;
				case 9: 
					if(preferences.getBoolean("location",false)){
						myIntent = new Intent(getApplicationContext(), LocationActivity.class);
						
					}else{
						myIntent = new Intent(getApplicationContext(),LocationActivity .class);
						
						AlarmManager am= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
						Intent intent=new Intent(getApplicationContext(),LocationClass.class);
						PendingIntent pintent = PendingIntent
								.getService(getApplicationContext(), 0, intent, 0);
						am.setRepeating(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis(),
								5*60*1000, pintent);
						
						SharedPreferences.Editor editor=preferences.edit();
						editor.putBoolean("location", true);
						editor.commit();
					}
					break;
				case 10: 
					myIntent = new Intent(getApplicationContext(), VisualizationOfLocation.class);
					break;
				case 11: 
					myIntent = new Intent(getApplicationContext(), WifiActivity.class);
					break;
				case 12: 
					myIntent = new Intent(getApplicationContext(), AmbientlightActivity.class);
					break;
				case 13: 
					myIntent = new Intent(getApplicationContext(), AudiolevelActivity.class);
					break;
				case 14: 
					myIntent = new Intent(getApplicationContext(), BootActivity.class);
					break;
				case 15:
					myIntent = new Intent(getApplicationContext(), ForeGroundApp.class);

				} 
				startActivity(myIntent);
			}

		}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.controller, menu);
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