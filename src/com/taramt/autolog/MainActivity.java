package com.taramt.autolog;

import java.util.ArrayList;

import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView tv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//TextView in MainActivity
		tv = (TextView) findViewById(R.id.mainactTV);
		//displayPowerResults();
		
		Intent it = new Intent(this, SensorActivity.class);
		startActivity(it);
		
	}
	
	public void displayPowerResults() {
		
		ArrayList<String> displayArray = null;
		String content = "";
		
		//Code to display the database results on screen
		//(if any)
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		displayArray = dba.getPowerDetails();
		dba.close();
		
		if(displayArray != null) {
			for(String ele: displayArray) {
				content += ele + "\n\n";
			}
			
			tv.setText(content);				
		}
		
	}
}
