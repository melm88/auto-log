package com.taramt.autolog;

import java.util.ArrayList;

import com.taramt.utils.DBAdapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ForeGroundApp extends ActionBarActivity{

	
	DBAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_currentapp);
		try {
		dbAdapter =new DBAdapter(this);
		ArrayList<String> app_data=dbAdapter.getForeGroundApp();

		//displaying the log from database on list view 
				ListView listView = (ListView) findViewById(R.id.list);

				if(app_data!=null){
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, android.R.id.text1, app_data);

				listView.setAdapter(adapter); 
				}
		} catch(Exception e) {
			e.printStackTrace();
		}
		

	}

}
