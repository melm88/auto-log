package com.taramt.audiolevel;

import java.util.ArrayList;

import com.taramt.autolog.R;
import com.taramt.utils.DBAdapter;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

	//private ImageView mouthImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audiolevel);
		
//		mouthImage = (ImageView)findViewById(R.id.mounthHolder);
//		mouthImage.setKeepScreenOn(true);
		Log.d("Activity", "onCreate");
		
		SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(this);
				//If this is the first launch
				if (savedValues.getBoolean(getString(R.string.StartAudio), true)) {
				Log.d("flag", "timer is being set");
				SharedPreferences.Editor editor = savedValues.edit();
				editor.putBoolean(getString(R.string.StartAudio), false);
				editor.commit();
				//Starting a AudioLevelService
				Intent intent = new Intent(this, AudioLevelService.class);
				PendingIntent pintent = PendingIntent
				.getService(this, 0, intent, 0);
				AlarmManager alarm = (AlarmManager)
				getSystemService(Context.ALARM_SERVICE);
				alarm.cancel(pintent);
				//setting an alarm manager for interval of 5 minutes
				alarm.setRepeating(AlarmManager.RTC_WAKEUP,
				System.currentTimeMillis(),
				5*60*1000, pintent);
				} else {
				Log.d("flag", "timer is already set");
				}
				//displaying the log from database on text view
				TextView tv = (TextView)findViewById(R.id.audiolevellog);
				DBAdapter db = new DBAdapter(this);
				db.open();
				ArrayList<String> row = db.getaudiolog();
				String content = "";
				for (int i = 0; i < row.size(); i++) {
				content = content + row.get(i)+ "\n\n";
				}
				db.close();
				tv.setText(content);

		
	}

	protected void onResume() {
		super.onResume();
		
			}
		@Override
	protected void onPause() {
		super.onPause();
	}
}
