package com.taramt.logmedia;

import java.util.ArrayList;

import com.taramt.autolog.MainActivity;
import com.taramt.autolog.R;
import com.taramt.autolog.R.layout;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.FileObserver;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MediaActivity extends Activity {

	TextView mediaTV;
	private FileObserver mFileObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media);

		//mFileObserver.startWatching();
		//onLaunch of the activity, initiate FileObserver
		addObserver();
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

			//Retrieve an ArrayList<String> of results from
			//MediaTable
			DBAdapter dba = new DBAdapter(this);
			dba.open();
			ArrayList<String> mediaData = dba.getMediaDetails();
			dba.close();

			//If there are contents in DB then display else
			//show a message "No Media data captured"
			if (mediaData.size() > 0) {
				String result = "";
				for (String med: mediaData) {
					result += med + "\n\n";
				}
				mediaTV.setText(result);
			} else {
				mediaTV.setText("No Media data captured.");
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
