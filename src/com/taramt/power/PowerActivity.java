package com.taramt.power;

import java.util.ArrayList;
import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class PowerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_power);
		try {
			//TextView in PowerActivity
			displayPowerResults();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	//Display data from PowerTable in PowerActivity
	public void displayPowerResults() {

		ArrayList<String> displayArray = null;

		//Retrieve data from DB (PowerTable)
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		displayArray = dba.getPowerDetails();
		dba.close();
		//displaying the log from database on list view 
		ListView listView = (ListView) findViewById(R.id.list);


		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, displayArray);

		listView.setAdapter(adapter); 



	}
}
