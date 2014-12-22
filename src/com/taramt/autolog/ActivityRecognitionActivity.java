package com.taramt.autolog;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;

public class ActivityRecognitionActivity extends Activity implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener{

	
	private BroadcastReceiver receiver1;
	private ActivityRecognitionClient arclient;
	private String Activity = "";
	private int Confidence = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activityrecognition);
		
		int resp =GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(resp == ConnectionResult.SUCCESS){
			arclient = new ActivityRecognitionClient(this, this, this);
			arclient.connect();			
		}
		else{
			Toast.makeText(this,  "Please install Google Play Service.",Toast.LENGTH_LONG).show();
		}		
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (connectionResult.hasResolution()) {
			try {

				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(
						this,
						9000);//CONNECTION_FAILURE_RESOLUTION_REQUEST

			} catch (IntentSender.SendIntentException e) {

				e.printStackTrace();
			}
		} 

	}

	@Override
	public void onConnected(Bundle arg0) {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this, ActivityRecognitionService.class);
		PendingIntent pIntent = PendingIntent.getService(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
		arclient.requestActivityUpdates(10*1000, pIntent); 
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}

	
}
