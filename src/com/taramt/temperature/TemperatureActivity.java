package com.taramt.temperature;

import java.util.ArrayList;

import com.taramt.autolog.MainActivity;
import com.taramt.autolog.R;
import com.taramt.autolog.R.layout;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class TemperatureActivity extends Activity {
    TextView temperatureTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_temperature);

		//TextView in TemperatureActivity
		temperatureTV = (TextView) findViewById(R.id.temperatureTV);
		//enableTemperatureSensing();
		displayTemperatureSensingData();
		//Intent it = new Intent(this, SensorActivity.class);
		//startActivity(it);
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
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		ArrayList<String> temperatureData = dba.getTemperatureDetails();
		String result = "";
		
		//Display results only if there are data in DB.
		//Otherwise result should show "No Temperature Change"
		if (temperatureData.size() > 0) {
			for (String temper: temperatureData) {
				result += temper + "\n\n";
			}
			temperatureTV.setText(result);
		} else {
			temperatureTV.setText("No Temperature Changes recorded"
					+ " so far");
		}
		dba.close();
	}
	
}
