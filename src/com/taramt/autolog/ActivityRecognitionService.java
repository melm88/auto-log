package com.taramt.autolog;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.taramt.utils.DBAdapter;

import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class ActivityRecognitionService extends Service implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener  {



	public final String KEY_PREVIOUS_ACTIVITY_TYPE="KEY";
	//SharedPreferences details;

	Context context;
	SharedPreferences details;
	private boolean currentlyProcessingActivity = false;
	private ActivityRecognitionClient arclient;
	
	private ActivityRecognition activityRequest;
	private ActivityRecognitionClient activityClient;
	BroadcastReceiver receiver;
	DBAdapter dbAdapter;
	@Override
	public void onCreate() {
		super.onCreate();
		context=getApplicationContext();

		details=PreferenceManager.getDefaultSharedPreferences(this);
		dbAdapter=new DBAdapter(this);
		
		Log.d("activity class","oncreate");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("activity class","onstart");
					
			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timeStamp=s.format(new Date());
			SharedPreferences.Editor editor=details.edit();
			editor.putString("timeStamp", timeStamp);
			editor.commit();
			startActivityMonitoring();
			
		
			
			receiver=new BroadcastReceiver(){

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					String Activity=intent.getExtras().getString("Activity");
					String confidence=intent.getExtras().getString("confidence");
					
					dbAdapter.insertActivity(details.getString("timeStamp", " "), Activity, confidence);
					stopMonitoring();
				}

			};
			IntentFilter filter1 = new IntentFilter();
			filter1.addAction("stopupdates");
			registerReceiver(receiver, filter1);
		return START_STICKY;
	}
	private void stopMonitoring(){
		Intent intentt = new Intent(this, RecognitionService.class);
		PendingIntent pIntent = PendingIntent.getService(this, 0, intentt,PendingIntent.FLAG_UPDATE_CURRENT);
		arclient.removeActivityUpdates(pIntent);
	}
	
	private void startActivityMonitoring() {

		Log.d("activity class","startmonitoring");

		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
			arclient = new ActivityRecognitionClient(this, this, this);
			
				arclient.connect();
			
		} else {
			Log.d("TAG", "unable to connect to google play services.");
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
		
		Log.d("activityrecognition","onconnected");
		Intent intent = new Intent(this, RecognitionService.class);
		PendingIntent pIntent = PendingIntent.getService(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		arclient.requestActivityUpdates(5*1000, pIntent); 
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}
	


}
