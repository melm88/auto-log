package com.taramt.autologscreenstate;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;

public class ScreenActivity extends Activity {

	DBAdapter db;
	TextView log, Average, Total;
	BroadcastReceiver mReceiver;
	String total, average;
	SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        db = new DBAdapter(this);
        log = (TextView)findViewById(R.id.log);
        Average = (TextView)findViewById(R.id.Average);
        Total = (TextView)findViewById(R.id.Total_duration);
        
    }
    

    
    @Override
    public void onDestroy() {
    	super.onDestroy();

    }
    public String showDetails(DBAdapter db, ArrayList<String> details) {
		return "";
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
