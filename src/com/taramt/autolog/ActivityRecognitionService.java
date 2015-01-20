package com.taramt.autolog;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.LocationListener;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

/**
 * 
 * @author ASHOK
 *
 * ActivityRecognitionService class for handling recognition service(starts and stops after getting updates)
 * Uses google play services.
 */

public class ActivityRecognitionService extends Service implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener  {



	public final String KEY_PREVIOUS_ACTIVITY_TYPE="KEY";
	//SharedPreferences details;

	Context context;
	SharedPreferences details;

	private ActivityRecognitionClient arclient;


	BroadcastReceiver receiver;
	DBAdapter dbAdapter;

	@Override
	public void onCreate() {
		Utils.appendLog("ActivityRecognitionService : onCreate, Started");
		super.onCreate();
		try {
			context=getApplicationContext();

			details=PreferenceManager.getDefaultSharedPreferences(this);
			dbAdapter=new DBAdapter(this);

			Log.d("activity class","oncreate");
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
		Utils.appendLog("ActivityRecognitionService : onCreate, Ended");
		
	}

	/*
	 * (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Utils.appendLog("ActivityRecognitionService : onStartCommand, Started");
		Log.d("activity class","onstart");
		try {
			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timeStamp=s.format(new Date());
			SharedPreferences.Editor editor=details.edit();
			editor.putString("timeStamp", new Date().toString());
			editor.commit();

			// start the activity monitoring 
			Utils.appendLog("ActivityRecognitionService : startActivityMonitoring, Started");
			startActivityMonitoring();
			
			// receiver which receives activity updates from recognition service.
			receiver=new BroadcastReceiver(){

				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					//get activity and confidence from receiver
					String Activity=intent.getExtras().getString("Activity");
					String confidence=intent.getExtras().getString("confidence");

					// store activity data in database
					dbAdapter.insertActivity(details.getString("timeStamp", " "), Activity, confidence);

					// call stopmonitoring method to stop activity updates.
					stopMonitoring();

				}

			};
			IntentFilter filter1 = new IntentFilter();
			filter1.addAction("stopupdates");
			// registering of receiver.
			registerReceiver(receiver, filter1);
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
		Utils.appendLog("ActivityRecognitionService : onStartCommand, Ended");
		
		return START_STICKY;
	}

	/*
	 * stopMonitoring method to stop activity updates after getting the update.
	 */
	private void stopMonitoring(){

		Intent intentt = new Intent(this, RecognitionService.class);
		PendingIntent pIntent = PendingIntent.getService(this, 0, intentt,PendingIntent.FLAG_UPDATE_CURRENT);
		// remove updates.
		arclient.removeActivityUpdates(pIntent);
	}

	/*
	 * startActivityMonitoring method to start the activity updates.
	 */
	private void startActivityMonitoring() {

		Log.d("activity class","startmonitoring");

		// check for googleplayservices availablity in mobile, if present proceed further.
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
		Utils.appendLog("ActivityRecognitionService : onDestroy");
		
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
