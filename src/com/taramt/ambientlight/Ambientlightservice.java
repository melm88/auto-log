package com.taramt.ambientlight;

import java.util.Date;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;

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
		SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(Ambientlightservice.this);
		
		SharedPreferences.Editor editor = savedValues.edit();
		editor.putInt("issaved", 0);
		editor.commit();
		
		Log.d("TEST", "HELLOO");
		RecordLightIntensity();
		return START_STICKY;
	}
	
	private void RecordLightIntensity() {
		Log.d("TEST", "RecordLightIntensity");
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
    		Log.d("TEST", "RecordLightIntensity");
    	 float currentReading = event.values[0];
        lastsensedvalue = currentReading;
        SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(Ambientlightservice.this);
		int issaved = savedValues.getInt("issaved", 0);
		
        if (issaved == 0){
        	
         DBAdapter db = new DBAdapter(Ambientlightservice.this);
   		 db.open();
   		 db.insertLightSensorValue(""+lastsensedvalue, new Date().toString());
   		 db.close();
   		SharedPreferences.Editor editor = savedValues.edit();
		editor.putInt("issaved", 1);
		editor.commit();
		Log.d("TEST", "LightSensor value saved");
		
        }
           
        else{
        	Log.d("TEST", "LightSensor value duplicates");
    		
        }
        }
	
}

@Override
public void onAccuracyChanged(Sensor sensor, int accuracy) {
	// TODO Auto-generated method stub
	
}

};

}