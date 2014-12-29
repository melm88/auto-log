package com.taramt.autolog;

import java.util.ArrayList;

import com.taramt.temperature.TemperatureSensor;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//TextView in MainActivity
		tv = (TextView) findViewById(R.id.mainactTV);

		enableTemperatureSensing();
		
		//Intent it = new Intent(this, SensorActivity.class);
		//startActivity(it);		
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	
	public void enableTemperatureSensing() {
		Intent iServe = new Intent(MainActivity.this, TemperatureSensor.class);
		MainActivity.this.startService(iServe);
		displayTemperatureSensingData();
	}
	
	public void stopTemperatureSensing() {
		Intent iServe = new Intent(MainActivity.this, TemperatureSensor.class);
		MainActivity.this.stopService(iServe);
	}
	
	public void displayTemperatureSensingData() {
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		ArrayList<String> TemperatureData = dba.getTemperatureDetails();
		String result = "";
		if(TemperatureData.size() > 0) {
			for(String temper: TemperatureData) {
				result += temper + "\n\n";
			}
			tv.setText(result);
		} else {
			tv.setText("No Temperature Changes recorded so far");
		}
		dba.close();
	}
}
