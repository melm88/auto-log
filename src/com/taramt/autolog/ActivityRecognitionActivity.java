package com.taramt.autolog;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.taramt.utils.DBAdapter;

/**
 * 
 * @author ASHOK
 *
 * ActivityRecognitionActivity class is used to show the activity log to user and starts the recognition service 
 * 
 */
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

		// get the activity data from the database.
		String[][] data=dbAdapter.getActivities();
		String adata="";
		// if activity data is there in the database loop through the data to append that to textview.
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
