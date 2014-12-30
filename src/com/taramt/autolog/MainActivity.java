package com.taramt.autolog;

import java.util.ArrayList;
import com.taramt.logmedia.AudioService;
import com.taramt.utils.DBAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
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
		
		//mFileObserver.startWatching();
		//onLaunch of the activity, initiate FileObserver
		addObserver();
				
		
	}
	
	//Launch the service for capturing audio files created in sdcard/Sounds
	private void addObserver() {
		//Log.d("audiorec","in addObserver");
	    Intent audioservice = new Intent(MainActivity.this, AudioService.class);
	    MainActivity.this.startService(audioservice);
	    
	    //Display Media data from DB into MainActivity
	    displayMediaData();
	}
	
	//Display MediaTable data
	public void displayMediaData() {
		
		//Retrieve an ArrayList<String> of results from MediaTable
		DBAdapter dba = new DBAdapter(this);
		dba.open();
		ArrayList<String> mediaData = dba.getMediaDetails();
		dba.close();
		
		//If there are contents in DB then display else show a message "No Media data captured"
		if(mediaData.size() > 0) {
			String result = "";
			for (String med: mediaData) {
				result += med + "\n\n";
			}
			tv.setText(result);
		} else {
			tv.setText("No Media data captured.");
		}
	}
	
	//Stop listening to audio files being created in sdcard/Sounds
	public void stopFileObserverWatch() {
		if (mFileObserver != null) {
            mFileObserver.stopWatching();
        }
	}
	
		
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//mFileObserver.startWatching();
	}

}
