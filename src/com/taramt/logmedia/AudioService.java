package com.taramt.logmedia;

import java.util.Date;

import com.taramt.utils.DBAdapter;

import android.app.Service;
import android.content.Intent;
import android.os.FileObserver;
import android.os.IBinder;
import android.util.Log;

public class AudioService extends Service {
	
	private FileObserver mFileObserver;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		// Get an instance of the sensor service, and use that to get an instance of
		// a particular sensor.
		//mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		//mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
		Log.d("audiorec","inAudioService");				
		return Service.START_STICKY;
	}
	
	//Destroy the FileObserver object on stopping the service
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (mFileObserver != null) {
            mFileObserver.stopWatching();
        }
		Log.d("audiorec","inDestroy");
		super.onDestroy();
	}
	
	//Initiate the watcher over sdcards/Sounds folder
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		this.mFileObserver = new FileObserver("/sdcard/Sounds/") {
	        @Override
	        public void onEvent(int event, String path) {
	        	Log.d("audiorec","inOnEvent "+event);
	        	Log.d("audiorec", "allevents: c:"+FileObserver.CREATE+" | d:"+FileObserver.DELETE+" | o:"+FileObserver.OPEN+" | ac:"+FileObserver.ACCESS+" | clo:"+FileObserver.CLOSE_WRITE+" | mod:"+FileObserver.MODIFY+" | movto:"+FileObserver.MOVED_TO+" | movf"+FileObserver.MOVED_FROM);
	            //Identify if the event that occured is New_File_Created or File_Renamed and if so then insert into DB.
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

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
