package com.taramt.ambientlight;

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
		Log.d("TEST", "HELLOO");
		RecordLightIntensity();
		DBAdapter db = new DBAdapter(this);
		 db.open();
		 db.insertLightSensorValue(""+lastsensedvalue, new Date().toString());
		 db.close();
		return START_STICKY;
	}
	
	private void RecordLightIntensity() {
		  SensorManager sensorManager 
	        = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	        Sensor lightSensor 
	        = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

		// TODO Auto-generated method stub
		  if (lightSensor == null){
		         Toast.makeText(this, 
		           "No Light Sensor! quit-", 
		           Toast.LENGTH_LONG).show();
		        } else {
		      
		          
		 sensorManager.registerListener(lightSensorEventListener, 
		         lightSensor, 
		         SensorManager.SENSOR_DELAY_NORMAL);
		 

		      }
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}


	SensorEventListener lightSensorEventListener
	= new SensorEventListener(){

@Override
public void onSensorChanged(SensorEvent event) {
	// TODO Auto-generated method stub
	// TODO Auto-generated method stub
       if(event.sensor.getType()==Sensor.TYPE_LIGHT){
        float currentReading = event.values[0];
        lastsensedvalue = currentReading;
        Toast.makeText(Ambientlightservice.this, 
                "light sensor value "+currentReading, 
                Toast.LENGTH_LONG).show();
             
        }
	
}

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	
}

};

}