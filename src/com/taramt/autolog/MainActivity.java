package com.taramt.autolog;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

/**
 * 
 * @author ASHOK
 *
 * This class acts as a interface to other activities, it contains buttons that fecilitates to open
 * consent activities(projects)
 */
public class MainActivity extends Activity {

	Context context;
	SharedPreferences details;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main);
		context=this;
		details=PreferenceManager.getDefaultSharedPreferences(this);
	
	}
	
/*
 * GetCallLog method is used to open the calllog class which shows is call log.
 */
	public void getCallLog(View view){
		
		Log.d("main","calllog");
		Intent intent =new Intent(context,Calllog.class);
		startActivity(intent);
	
	}

/*
 * getActivity method is used to open and Activity recognition . If activity recognition already started,
 * it just opens the log. Otherwise it opens the log along with starting the recognition service.
 */
	public void getActivity(View view){
		
		//if activity recognition already started open the log else start the recognition and open the log also.
		if(details.getBoolean("activity", false)){
			
			Intent activityIntent=new Intent(context,ActivityRecognitionActivity.class);
			startActivity(activityIntent);
		
		}else{
		
			Intent intent=new Intent(context,ActivityRecognitionService.class);
			
			PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			// alarm for repeating recognition of activities at an about every 5 min interval
			AlarmManager am = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
					1000*60*5,pendingIntent);
			
			Intent activityIntent=new Intent(context,ActivityRecognitionActivity.class);
			startActivity(activityIntent);
			
			//save the preference to sharedpreferences .
			SharedPreferences.Editor editor=details.edit();
			editor.putBoolean("activity", true);
			editor.commit();
		
		}
	
		Log.d("main","getActivity");
		
		
	}

/*
 * getLocation method is used to open the location log if location updates already started, Otherwise
 * it starts the location updates service and open the log.
 */
	public void getLocation(View view){
		
		Log.d("main","get location");
		
		if (details.getBoolean("location", false)){
			Intent activityIntent=new Intent(context,LocationActivity.class);
			startActivity(activityIntent);
		}else{
		
			Intent intent =new Intent(context,LocationClass.class);
			PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager am = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
					1000*60*5,pendingIntent);
			
			Intent activityIntent=new Intent(context,LocationActivity.class);
			startActivity(activityIntent);
			
			SharedPreferences.Editor editor=details.edit();
			editor.putBoolean("location", true);
			editor.commit();
		}
		
	}
	
/*
 *  visualizationOfLocationData method to start visualization activity to show markers on map
 */
	public void visualizationOfLocationData(View view){
		
		Intent intent=new Intent(context,VisualizationOfLocation.class);
		startActivity(intent);
	}
}
