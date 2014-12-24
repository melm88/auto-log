package com.taramt.autolog;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class RecognitionService extends IntentService {

	public final String KEY_PREVIOUS_ACTIVITY_TYPE="KEY";
	private ActivityRecognitionClient arclient;
	//SharedPreferences details;

	public RecognitionService() {
		super("My Activity Recognition Service");
		//details = PreferenceManager.getDefaultSharedPreferences(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		Log.d("rs","onhandle intent");
		ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

		DetectedActivity mostProbableActivity = result.getMostProbableActivity();
		int activityType = mostProbableActivity.getType();
		int confidence=mostProbableActivity.getConfidence();
		String Activity=getType(activityType);
		//           SharedPreferences.Editor editor = details.edit();
		//Log.e("ActivityRecognitionService",Activity+"  confidence is  "+confidence);
		if(ActivityRecognitionResult.hasResult(intent)){ 

			//			editor.putString("Activity1", Activity);
			//			editor.commit();

			Log.d("activity",Activity);

			Intent i = new Intent("stopupdates");
			i.putExtra("Activity", Activity );
			i.putExtra("confidence", confidence+"");
			

			sendBroadcast(i);

		}

	}

	private String getType(int type){
		if(type == DetectedActivity.UNKNOWN)
			return "Unknown";
		else if(type == DetectedActivity.IN_VEHICLE)
			return "In Vehicle";
		else if(type == DetectedActivity.ON_BICYCLE)
			return "On Bicycle";
		else if(type == DetectedActivity.ON_FOOT)
			return "On Foot";
		else if(type == DetectedActivity.STILL)
			return "Still";
		else if(type == DetectedActivity.TILTING)
			return "Tilting";
		else if(type == DetectedActivity.WALKING)
			return "Walking";
		else if(type == DetectedActivity.RUNNING)
			return "Running";
		else
			return "Unknown";
	}



}
