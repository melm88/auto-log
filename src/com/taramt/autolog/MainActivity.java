package com.taramt.autolog;

import java.util.ArrayList;
import com.taramt.utils.DBAdapter;
import java.util.Date;
import com.taramt.temperature.SensorActivity;
import com.taramt.temperature.TemperatureSensor;
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
		
	}

}
