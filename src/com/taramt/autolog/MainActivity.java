package com.taramt.autolog;

import java.util.ArrayList;
import java.util.Date;

import com.taramt.temperature.SensorActivity;
import com.taramt.temperature.TemperatureSensor;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	TextView tv;
	private FileObserver mFileObserver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//TextView in MainActivity
		tv = (TextView) findViewById(R.id.mainactTV);
		//displayPowerResults();
		//enableTemperatureSensing();
		
		//Intent it = new Intent(this, SensorActivity.class);
		//startActivity(it);
		
		//mFileObserver.startWatching();
		addObserver();
		
		
	}
	
	private void addObserver() {
		//Log.d("audiorec","in addObserver");
	    this.mFileObserver = new FileObserver("/sdcard/Sounds/") {
	        @Override
	        public void onEvent(int event, String path) {
	        	//Log.d("audiorec","inOnEvent "+event);
	        	//Log.d("audiorec", "allevents: c:"+FileObserver.CREATE+" | d:"+FileObserver.DELETE+" | o:"+FileObserver.OPEN+" | ac:"+FileObserver.ACCESS+" | clo:"+FileObserver.CLOSE_WRITE+" | mod:"+FileObserver.MODIFY+" | movto:"+FileObserver.MOVED_TO+" | movf"+FileObserver.MOVED_FROM);
	            if (event == FileObserver.MOVED_TO) {
	            	Log.d("audiorec","inside CREATE");
	                if (path != null) {
	                    int index = path.indexOf(".");
	                    String tempFileName = (String) path.subSequence(0,
	                            index);
	                    //audioFileNames.add(tempFileName);
	                    Log.d("audiorec","AudioCreate: "+tempFileName + "||" + path);
	                    DBAdapter dba = new DBAdapter(getApplicationContext());
	                    dba.open();
	                    dba.insertMediaDetails(path, "Audio", new Date().toString());
	                    dba.close();

	                }
	            } else if (event == FileObserver.DELETE) {
	            	Log.d("audiorec","inside DELETE");
	                if (path != null) {
	                    int index = path.indexOf(".");
	                    String tempFileName = (String) path.subSequence(0,
	                            index);
/*	                    if (audioFileNames.contains(tempFileName)) {
	                        audioFileNames.remove(tempFileName);
	                    }*/
	                    Log.d("audiorec","AudioDelete: "+tempFileName + "||" + path);
	                }

	            }
	        }
	    };
	    mFileObserver.startWatching();
	}
	
	public void stopFileObserverWatch() {
		if (mFileObserver != null) {
            mFileObserver.stopWatching();
        }
	}
	
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 mFileObserver.startWatching();
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
	
	public void enableTemperatureSensing() {
		Intent iServe = new Intent(MainActivity.this, TemperatureSensor.class);
		MainActivity.this.startService(iServe);
	}
}
