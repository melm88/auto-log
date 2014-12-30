package com.taramt.temperature;

import java.util.Date;

import com.taramt.utils.DBAdapter;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;

public class TemperatureSensor extends Service implements SensorEventListener {
	private SensorManager mSensorManager;
	private Sensor mTemperature;
	private float prev_amb_temp;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		// Get an instance of the sensor service, and use that to get an instance of
		// a particular sensor.
		//mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		Log.d("sensor","inService");				
		return Service.START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mSensorManager.unregisterListener(this);
		//Log.d("sensor","inDestroy");
		super.onDestroy();
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		// Get an instance of the sensor service, and use that to get an instance of
		// a particular sensor.	
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		//Log.d("sensor","inCreate");
		super.onCreate();
		if(mTemperature != null)
			mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
		else {
			Log.d("sensor","NO TEMPERATURE SENSOR");
			DBAdapter dba = new DBAdapter(getApplicationContext());
			dba.open();
			dba.insertTemperatureDetails("NO TEMPERATURE SENSOR", new Date().toString());
			dba.close();
		}		
		
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
		float centigrade_of_temperature = event.values[0];
		
		//Insert temperature if its different from previous entered data
		if(centigrade_of_temperature != prev_amb_temp) {
			Log.d("sensor","Temperature: "+centigrade_of_temperature+" | "+prev_amb_temp);
			prev_amb_temp = centigrade_of_temperature;
			DBAdapter dba = new DBAdapter(getApplicationContext());
			dba.open();
			dba.insertTemperatureDetails(""+centigrade_of_temperature, new Date().toString());
			dba.close();
		}
		
		// Do something with this sensor data.
		//Log.d("sensor","TemperatureA: "+centigrade_of_temperature);
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

}
