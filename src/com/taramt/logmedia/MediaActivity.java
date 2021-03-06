package com.taramt.logmedia;

import java.util.ArrayList;
import java.util.Date;

import com.taramt.autolog.MainActivity;
import com.taramt.autolog.R;
import com.taramt.autolog.R.layout;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MediaActivity extends Activity {

	private FileObserver mFileObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);
		//mFileObserver.startWatching();
		//onLaunch of the activity, initiate FileObserver
		//addObserver();
		try {
			displayMediaData();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}

	//Launch the service for capturing audio files created in sdcard/Sounds
	private void addObserver() {
		//Log.d("audiorec","in addObserver");
		Intent audioservice = new Intent(MediaActivity.this,
				AudioService.class);
		MediaActivity.this.startService(audioservice);

		//Display Media data from DB into MainActivity
		displayMediaData();
	}

	//Display MediaTable data
	public void displayMediaData() {
		try {
			//Retrieve an ArrayList<String> of results from
			//MediaTable
			DBAdapter dba = new DBAdapter(this);
			dba.open();
			ArrayList<String> mediaData = dba.getMediaDetails();
			//displaying the log from database on list view 
			ListView listView = (ListView) findViewById(R.id.list);


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, mediaData);

			listView.setAdapter(adapter); 

			dba.close();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
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
