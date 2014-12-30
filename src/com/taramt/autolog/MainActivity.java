package com.taramt.autolog;

import java.util.ArrayList;
import com.taramt.utils.DBAdapter;
import android.app.Activity;
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
		displayPowerResults();	
		
	}	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	//Display data from PowerTable in MainActivity
	public void displayPowerResults() {
		
		ArrayList<String> displayArray = null;
		String content = "";
		
		//Retrieve data from DB (PowerTable)
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		displayArray = dba.getPowerDetails();
		dba.close();
		
		//Code to display the database results on screen
		//(if any)
		if(displayArray != null) {
			for(String ele: displayArray) {
				content += ele + "\n\n";
			}
			
			tv.setText(content);				
		}
		
	}
	
}
