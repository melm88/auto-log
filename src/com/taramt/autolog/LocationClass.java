package com.taramt.autolog;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.taramt.utils.DBAdapter;

/**
 * 
 * @author ASHOK
 *
 * Location class for getting location updates and checking for google play services.
 */
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
	//DBAdapter dbAdapter;

	@Override
	public void onCreate() {
		super.onCreate();
		context=getApplicationContext();

		details=PreferenceManager.getDefaultSharedPreferences(this);

		Log.d("location class","oncreate");
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.d("locatin class","onstart");
		// start the location updates if not already processing.
		if (!currentlyProcessingLocation) {

			currentlyProcessingLocation = true;

			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String timeStamp=s.format(new Date());
			SharedPreferences.Editor editor=details.edit();
			editor.putString("timeStamp", new Date().toString());
			editor.commit();

			// start location tracking
			startLocationTracking();

		}

		return START_STICKY;
	}

	/*
	 * startLocationTracking method for getting location updates.
	 */
	private void startLocationTracking() {

		Log.d("location class","starttracking");

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
		Log.d("location class","on location changed");
		if (location != null) {


			//Log.d("location","yes");
			/*
			 * Get the set of accuracy values break the loop and continue to next if the accuracy is less than 20 else get the least 
			 * value and the corresponding latitude and longitude values from the set of 10 values.
			 */




			float acc=location.getAccuracy();
			lat=location.getLatitude();
			lon=location.getLongitude();				



			Log.d("lat",lat+"");
			Log.d("long",lon+"");
			Double[] params=new Double[3];
			params[0]= lat;
			params[1]= lon;
			params[2]=Double.valueOf(acc);

			// get address from the lat and lon
			GetCurrentAddress currentadd=new GetCurrentAddress();
			try {
				// start the async task for getting address.
				currentadd.execute(params);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.d("exception in calling asynctask",e.toString());
			}

			//remove the location updates after getting address
			locationClient.removeLocationUpdates(this);
			//stop the service
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

		Log.d("location class","onconnected");

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

	/*
	 * getCurrentAdderss is asynctask class for getting currentaddress.
	 */
	private class GetCurrentAddress extends AsyncTask<Double, Void, String> {

		DBAdapter dbAdapter;
		SharedPreferences details;

		double lat,lon,accuracy;

		@Override
		protected String doInBackground(Double... params) {

			dbAdapter=new DBAdapter(getApplicationContext());
			details=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			lat=params[0];
			lon=params[1];
			accuracy=params[2];
			String address= getAddress(getBaseContext(), params[0], params[1]);


			return address;

		}
		@Override
		protected void onPostExecute(String resultString) {

			Log.d("LocationClass","in post execute"+resultString);
			dbAdapter.insertLocationDetails(details.getString("timeStamp", ""), String.valueOf(lat),
					String.valueOf(lon), String.valueOf(accuracy), resultString, " ");


			ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
	        String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
			
			final PackageManager pm = getApplicationContext().getPackageManager();
			ApplicationInfo ai;
			try {
			    ai = pm.getApplicationInfo( packageName, 0);
			} catch (final NameNotFoundException e) {
			    ai = null;
			}
			final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
			
			dbAdapter.insertForeGroundApp(details.getString("timeStamp"," "),applicationName);
			
			Log.d("name",applicationName);

		}
	}

	/*
	 * getAddress method for getting adderss from geocoder.
	 */
	public  String getAddress(Context ctx, double latitude, double longitude) {

		String first = "";
		try {

			Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());

			List<Address> addresses=null;

			addresses = geocoder.getFromLocation(latitude, longitude, 5);

			Log.d("size",addresses.size()+"");

			if (addresses.size() > 0) {

				Address address = addresses.get(0);

				first=address.getAddressLine(0);

				if(first.contains(",")){

					first = first.replaceAll(",", " ");
					Log.d("address",first);
				}
				if(first.contains("null")){

					first = first.replaceAll("null", "");
					Log.d("address",first);
				}
			}
		} catch (IOException e) {
			Log.d("tag", e.getMessage());
		}

		return first;
	}


}
