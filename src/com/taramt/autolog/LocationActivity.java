package com.taramt.autolog;

import java.util.ArrayList;
import java.util.Date;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author ASHOK
 *
 * LocationActivity that starts the location updates and show the log.
 */
public class LocationActivity extends Activity {

	TextView ldata;
	DBAdapter dbAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		try {
			dbAdapter=new DBAdapter(this);
			// get the log from database
			ArrayList<String> data=dbAdapter.getLocationDetailsArrayList();
			//displaying the log from database on list view 
			ListView listView = (ListView) findViewById(R.id.list);

			if(data!=null) {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, android.R.id.text1, data);

				listView.setAdapter(adapter); 
			}
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}


}
