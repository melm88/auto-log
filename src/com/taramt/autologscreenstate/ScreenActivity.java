package com.taramt.autologscreenstate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

public class ScreenActivity extends Activity {

	DBAdapter db;
	TextView log, Average, Total;
	BroadcastReceiver mReceiver;
	String total, average;
	SharedPreferences prefs;
	Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        db = new DBAdapter(this);
        utils = new Utils(this);
        log = (TextView)findViewById(R.id.log);
        Average = (TextView)findViewById(R.id.Average);
        Total = (TextView)findViewById(R.id.Total_duration);
        ArrayList<String> sDetails = new ArrayList<String>();
        db.open();
        sDetails = db.getScreenStateDetails();
        db.close();
        log.setText(utils.getDetails(db, sDetails));
        registerReceiver();
       
    }
    
    public void registerReceiver() {
    	 IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
         filter.addAction(Intent.ACTION_USER_PRESENT);
          mReceiver = new ScreenReceiver();
         registerReceiver(mReceiver, filter);
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(mReceiver);
    }

    
    
    public void Sort(View v) {
    	
    	Log.i("Clicked", "Clicked");
    	
    }
    public void Top3(View v) {
    	Log.i("Clicked", "Clicked");
    }
    
    
    public void average() {
    	
    }
    
    public void TotalDuration() {
    	
    }
    
}
