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
	
	public void getCallLog(View view){
		Log.d("main","calllog");
		Intent intent =new Intent(context,Calllog.class);
		startActivity(intent);
	}
	
	public void getActivity(View view){
	
		Log.d("main","getActivity");
		Intent intent=new Intent(context,ActivityRecognitionActivity.class);
		//startActivity(intent);
	}
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
					1000*60*1,pendingIntent);
			
			Intent activityIntent=new Intent(context,LocationActivity.class);
			startActivity(activityIntent);
			
			SharedPreferences.Editor editor=details.edit();
			editor.putBoolean("location", true);
			editor.commit();
		}
		
	}
}
