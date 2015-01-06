package com.taramt.temperature;

import com.taramt.autolog.R;
import com.taramt.autolog.R.id;
import com.taramt.autolog.R.layout;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class SensorActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mTemperature;
	TextView tv;
	private float prev_amb_temp;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (TextView) findViewById(R.id.temperatureTV);
		prev_amb_temp = 0;

		// Get an instance of the sensor service, and use that to get an instance of
		// a particular sensor.
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
		Log.d("sensor","sensor: "+sensor.toString());
		Log.d("sensor","acc: "+accuracy);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		float centigrade_of_temperature = event.values[0];
		
		//If temperature change is observed then display it
		if(centigrade_of_temperature != prev_amb_temp) {
			prev_amb_temp = centigrade_of_temperature;
			tv.setText("Temperature: "+prev_amb_temp);
			Log.d("sensor","TemperatureB: "+centigrade_of_temperature+" | "+prev_amb_temp);
		}
		
		// Do something with this sensor data.
		Log.d("sensor","TemperatureA: "+centigrade_of_temperature);		
		
	}

	@Override
	protected void onResume() {
		// Register a listener for the sensor.
		super.onResume();
		mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

}