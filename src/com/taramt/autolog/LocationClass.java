package com.taramt.autolog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationClass extends Service implements
GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener,
LocationListener {
	private static final String TAG = "LocationService";

	private boolean currentlyProcessingLocation = false;
	private LocationRequest locationRequest;
	private LocationClient locationClient;
	static Context context;
	SharedPreferences details;
	float accuracy=0;
	double lat=0;
	double lon=0;
	//DatabaseAdapter dbAdapter;
	@Override
	public void onCreate() {
		super.onCreate();
		context=getApplicationContext();
		try {
			//dbAdapter=new DatabaseAdapter(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		if (!currentlyProcessingLocation) {
			currentlyProcessingLocation = true;
			startTracking();
		}
		//	displayCurrentLocation();
		return START_STICKY;
	}
	private void startTracking() {
		
		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) == ConnectionResult.SUCCESS) {
			locationClient = new LocationClient(this,this,this);
			if (!locationClient.isConnected() || !locationClient.isConnecting()) {
				locationClient.connect();
			}
		} else {
			Log.e(TAG, "unable to connect to google play services.");
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
	public void onLocationChanged(Location location) {
		if (location != null) {
			
				SharedPreferences.Editor editor=details.edit();
				//Log.d("location","yes");
			/*
			 * Get the set of accuracy values break the loop and continue to next if the accuracy is less than 20 else get the least 
			 * value and the corresponding latitude and longitude values from the set of 10 values.
			 */
				for(int i=0;i<10;i++){
					float acc=location.getAccuracy();
					if(details.getString("firstLoc", "yes").equals("yes")){
						lat=location.getLatitude();
						lon=location.getLongitude();
						accuracy=acc;
					}else{
						if(accuracy>acc){
							lat=location.getLatitude();
							lon=location.getLongitude();
							accuracy=acc;
								if(acc<=20){
									break;
								}
						}
					}
				}
				editor.putString("accuracy", accuracy+"");
				Double[] params=new Double[2];
				params[0]= lat;
				params[1]= lon;
				
				
				locationClient.removeLocationUpdates(this);
				stopSelf();
				
		}
			
		}
	
	

	/**
	 * Called by Location Services when the request to connect the
	 * client finishes successfully. At this point, you can
	 * request the current location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle bundle) {
	
		locationRequest = LocationRequest.create();
		locationRequest.setInterval(1); // milliseconds
		locationRequest.setFastestInterval(0); // the fastest rate in milliseconds at which your app can handle location updates
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationClient.requestLocationUpdates(locationRequest, this);
	}
	/**
	 * Called by Location Services if the connection to the
	 * location client drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		Log.e(TAG, "onDisconnected");
	}
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		Log.e(TAG, "onConnectionFailed");
	}
	
	private class GetCurrentAddress extends AsyncTask<Double, Void, String> {



		@Override
		protected String doInBackground(Double... params) {

			String address= getAddress(getBaseContext(), params[0], params[1]);

			return address;

		}
		@Override
		protected void onPostExecute(String resultString) {
		
			
			
			
			
		}
	}

	public  String getAddress(Context ctx, double latitude, double longitude) {
		//StringBuilder result = new StringBuilder();
		String first = null;
		try {
			Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
			List<Address> addresses=null;
			addresses = geocoder.getFromLocation(latitude, longitude, 5);
			if (addresses.size() > 0) {
				Address address = addresses.get(0);
				first=address.getAddressLine(0);

				if(first.contains(",")){
					//first.replace(",", " ");
					first = first.replaceAll(",", " ");
				}
				if(first.contains("null")){
					//first.replace("null", "");
					first = first.replaceAll("null", "");
				}
			}
		} catch (IOException e) {
			Log.e("tag", e.getMessage());
		}

		return first;
	}
	

}
