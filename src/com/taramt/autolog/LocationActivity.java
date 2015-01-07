package com.taramt.autolog;

import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.os.Bundle;
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

		ldata=(TextView)findViewById(R.id.ldata);

		dbAdapter=new DBAdapter(this);
		// get the log from database
		String[][] data=dbAdapter.getLocationDetails();
		String locationdata="";
		// loop through the log if previous activities are there.
		if(data.length>0){
			for(int i=0;i<data.length;i++){
				locationdata=locationdata+data[i][0]+" "+data[i][1]+" "+data[i][2]+" "+data[i][3]+" "+data[i][4]+" "+data[i][5]+"\n";
			}
		}else{
			locationdata=locationdata+"Log started just now";
		}

		ldata.setText(locationdata);
	}


}
