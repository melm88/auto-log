package com.taramt.autolog;

import java.util.ArrayList;
import com.taramt.utils.DBAdapter;
import java.util.Date;
import com.taramt.temperature.SensorActivity;
import com.taramt.temperature.TemperatureSensor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


/**
 * 
 * @author ASHOK
 *
 * This class acts as a interface to other activities, it contains buttons that fecilitates to open
 * consent activities(projects)
 */
public class MainActivity extends Activity {

	TextView tv;

	Context context;
	SharedPreferences details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		context=this;
		details=PreferenceManager.getDefaultSharedPreferences(this);

		//TextView in MainActivity

		tv = (TextView) findViewById(R.id.mainactTV);

	}



}
