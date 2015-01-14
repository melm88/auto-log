package com.taramt.autolog;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;


/**
 * 
 * @author ASHOK
 *
 * This class acts as a interface to other activities, it contains buttons that fecilitates to open
 * consent activities(projects)
 */
public class MainActivity extends Activity {

	
	Context context;
	SharedPreferences details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		context=this;
		details=PreferenceManager.getDefaultSharedPreferences(this);

		//TextView in MainActivity

	
	}



}
