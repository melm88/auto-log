package com.taramt.power;

import java.util.ArrayList;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class PowerActivity extends Activity {
	TextView powerTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_power);

		//TextView in PowerActivity
		powerTV = (TextView) findViewById(R.id.powerActTV);
		displayPowerResults();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	//Display data from PowerTable in PowerActivity
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
		if (displayArray != null) {
			for (String ele: displayArray) {
				content += ele + "\n\n";
			}

			powerTV.setText(content);
		}

	}
}
