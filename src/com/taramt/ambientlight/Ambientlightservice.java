package com.taramt.ambientlight;

import java.util.Date;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;


public class Ambientlightservice  extends Service
{
	static float lastsensedvalue;

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{	
		try {
			SharedPreferences savedValues = PreferenceManager
					.getDefaultSharedPreferences(Ambientlightservice.this);
			//set initial value to 0 to get first time
			SharedPreferences.Editor editor = savedValues.edit();
			editor.putInt("issaved", 0);
			editor.commit();

			RecordLightIntensity();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
		return START_STICKY;
	}

	SensorManager sensorManager ;
	Sensor lightSensor;
	//Captures the first record of light sensor.
	private void RecordLightIntensity() {
		Log.d("TEST", "RecordLightIntensity");
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

		// If lightSensor is not available in device
		if (lightSensor == null){
			Toast.makeText(this, 
					"No Light Sensor! quit-", 
					Toast.LENGTH_LONG).show();
		} else {

			//register light sensor        
			sensorManager.registerListener(lightSensorEventListener, 
					lightSensor, 
					SensorManager.SENSOR_DELAY_NORMAL);


		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		//Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

	//light sensor event listener implementation
	SensorEventListener lightSensorEventListener
	= new SensorEventListener(){

		@Override
		public void onSensorChanged(SensorEvent event) {
			try {
				if(event.sensor.getType()==Sensor.TYPE_LIGHT){
					Log.d("TEST", "RecordLightIntensity");
					float currentReading = event.values[0];
					lastsensedvalue = currentReading;
					SharedPreferences savedValues = PreferenceManager
							.getDefaultSharedPreferences(Ambientlightservice.this);
					int issaved = savedValues.getInt("issaved", 0);
					//if first value is not yet saved in the service
					if (issaved == 0){

						DBAdapter db = new DBAdapter(Ambientlightservice.this);
						db.open();
						db.insertLightSensorValue(""+lastsensedvalue, new Date().toString());
						db.close();
						SharedPreferences.Editor editor = savedValues.edit();
						//set value to 1 so, it is not executed from second time 
						editor.putInt("issaved", 1);
						editor.commit();
						Log.d("TEST", "LightSensor value saved");

					}

					else{
						sensorManager.unregisterListener(lightSensorEventListener);
						stopSelf();
						Log.d("TEST", "LightSensor value duplicates");

					}
				}
			} catch(Exception e) {
				Utils.appendLog(e);
				
				e.printStackTrace();
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

	};

}