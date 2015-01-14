package com.taramt.autolog;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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


	DBAdapter dbAdapter;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_activityrecognition);

		dbAdapter=new DBAdapter(this);

		// get the activity data from the database.
		ArrayList<String> rows = dbAdapter.getActivities();
		if(rows != null){
			//displaying the log from database on list view 
			ListView listView = (ListView) findViewById(R.id.list);


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, rows);

			listView.setAdapter(adapter); 
		}
	}





}
