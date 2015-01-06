package com.taramt.autolog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.taramt.autologalarm.Alarmactivity;
import com.taramt.autologdatausage.DataUsage;
import com.taramt.autolognotification.NotificationActivity;
import com.taramt.autologscreenstate.ScreenActivity;

public class ControllerActivity extends Activity {
	ListView listView ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		listView = (ListView) findViewById(R.id.list);
		String[] values = new String[] { "Notification", 
				"Datausage",
				"ScreenActivity",
				"Alarm"
		};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		listView.setAdapter(adapter); 

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// ListView Clicked item index
				int itemPosition     = position;

				// ListView Clicked item value
				String  itemValue    = (String) listView.getItemAtPosition(position);
				Intent myIntent = null;

				if (itemValue.equals("Notification")) {
					myIntent = new Intent(getApplicationContext(), NotificationActivity.class);
				} else if (itemValue.equals("Datausage")) {
					myIntent=new Intent(getApplicationContext(), DataUsage.class);
				} else if (itemValue.equals("ScreenActivity")) {
					myIntent=new Intent(getApplicationContext(), ScreenActivity.class);
				} else if (itemValue.equals("Alarm")) {
					myIntent=new Intent(getApplicationContext(), Alarmactivity.class);	
				}
				startActivity(myIntent);
				finish();
			}

		}); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.controller, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
