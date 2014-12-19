package com.taramt.autolog;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;

public class SensorActivity extends Activity implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mPressure;

	@Override
	public final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensor);

		// Get an instance of the sensor service, and use that to get an instance of
		// a particular sensor.
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
	}

	@Override
	public final void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do something here if sensor accuracy changes.
		Log.d("sensor","sensor: "+sensor.toString());
		Log.d("sensor","acc: "+accuracy);
	}

	@Override
	public final void onSensorChanged(SensorEvent event) {
		float millibars_of_pressure = event.values[0];
		// Do something with this sensor data.
		Log.d("sensor","milli: "+millibars_of_pressure);
		Log.d("sensor","event: "+event.toString());
		Log.d("sensor","event: "+event.sensor+" ~ "+event.values);
	}

	@Override
	protected void onResume() {
		// Register a listener for the sensor.
		super.onResume();
		mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		// Be sure to unregister the sensor when the activity pauses.
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

}