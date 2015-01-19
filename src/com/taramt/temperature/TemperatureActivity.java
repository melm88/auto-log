package com.taramt.temperature;

import java.util.ArrayList;
import java.util.Date;

import com.taramt.autolog.MainActivity;
import com.taramt.autolog.R;
import com.taramt.autolog.R.layout;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TemperatureActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperature);
		try {
			//TextView in TemperatureActivity
			//enableTemperatureSensing();
			displayTemperatureSensingData();
			//Intent it = new Intent(this, SensorActivity.class);
			//startActivity(it);
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	//Method to initiate the service for capturing AmbientTemperature
	public void enableTemperatureSensing() {
		Intent iServe = new Intent(TemperatureActivity.this,
				TemperatureSensor.class);
		TemperatureActivity.this.startService(iServe);
		displayTemperatureSensingData();
	}

	//Method to stop the service for capturing AmbientTemperature
	public void stopTemperatureSensing() {
		Intent iServe = new Intent(TemperatureActivity.this,
				TemperatureSensor.class);
		TemperatureActivity.this.stopService(iServe);
	}

	//Method to display the results from Temperature table onto MainActivity
	public void displayTemperatureSensingData() {
		try {
			DBAdapter dba = new DBAdapter(this);
			dba.open();
			ArrayList<String> temperatureData = dba.getTemperatureDetails();
			//Display results only if there are data in DB.
			//Otherwise result should show "No Temperature Change"
			dba.close();
			//displaying the log from database on list view 
			if(temperatureData != null) {
				ListView listView = (ListView) findViewById(R.id.list);


				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, android.R.id.text1, temperatureData);

				listView.setAdapter(adapter); 

			}
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}

	}
}
