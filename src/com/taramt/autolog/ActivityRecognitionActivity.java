package com.taramt.autolog;


import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.ActivityRecognitionClient;
import com.taramt.utils.DBAdapter;

public class ActivityRecognitionActivity extends Activity {

	TextView activitydata;
	DBAdapter dbAdapter;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activityrecognition);
		activitydata=(TextView)findViewById(R.id.activities);
		
		dbAdapter=new DBAdapter(this);
		String[][] data=dbAdapter.getActivities();
		String adata="";
		if(data.length>0){
			for(int i=0;i<data.length;i++){
				adata=adata+data[i][0]+" "+data[i][1]+" "+data[i][2]+"\n";
			}
		}else{
			adata=adata+"Log started just now";
		}
		
		activitydata.setText(adata);
	}
		
	


	
}
