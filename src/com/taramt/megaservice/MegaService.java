package com.taramt.megaservice;


import java.math.BigDecimal;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.net.TrafficStats;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;


@SuppressLint("NewApi")public class MegaService  extends Service {
	DBAdapter db;
	Utils utils;
	String dataTable = "DataUsage";
	Date timestamp = null;
	SharedPreferences prefs;
	static String Tag = "NEXT_ALARM";
	public final String KEY_PREVIOUS_ACTIVITY_TYPE="KEY";
	//SharedPreferences details;
	static int caller = 1;

	BroadcastReceiver receiver;

	static Context context;

	SharedPreferences details;

	float accuracy=0;
	double lat=0;
	double lon=0;

	private static final int sampleRate = 8000;
	private AudioRecord audio;
	private int bufferSize;
	private double lastLevel = 0;

	static float lastsensedvalue;


	@Override
	public IBinder onBind(Intent arg0)
	{
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();	
		try {
			db = new DBAdapter(getApplicationContext());
			utils = new Utils(getApplicationContext());
			context=getApplicationContext();
			details=PreferenceManager.getDefaultSharedPreferences(this);

			Log.d("activity class","oncreate");
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}


	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		timestamp = new Date();
		// getting the datausage of each app every 5 minutes.
		Log.d("DATAUSAGE", "in on start service");
		try {
			Log.d("megaservice", "startingDataUSAGE");
			Date timer1=new Date();
			getDatausageperApp();
			Date timer2 = new Date();
			Log.d("megaservice", "starting megaservice -- DataUsage: "+(timer2.getTime()-timer1.getTime())+" ~"+caller);
			timer1 = new Date();
			StartAlarmService();
			timer2 = new Date();		
			Log.d("megaservice", "LocationService -- Alarm: "+(timer2.getTime()-timer1.getTime())+" ~"+caller);
//			timer1 = new Date();
//			//locationService();
//			timer2 = new Date();
//			Log.d("megaservice", "ambientService -- Location: "+(timer2.getTime()-timer1.getTime())+" ~"+caller);
			timer1 = new Date();
			ambientService();
			timer2 = new Date();
			Log.d("megaservice", "AudioLevel -- Ambient"+(timer2.getTime()-timer1.getTime())+" ~"+caller);
			timer1 = new Date();
			AudioLevelService();
			timer2 = new Date();
			Log.d("megaservice", "Ended--MegaServices -- Audio: "+(timer2.getTime()-timer1.getTime())+" ~"+caller);
			caller++;
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}


		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

	}
	/*
	 *  getting the datausage of each app
	 *  1. getting the list of apps
	 *  2. getting the Tx and Rx of each app since boot
	 *  3. Inserting the data into the phone database
	 *  4. inserts at every 5 minutes (set by alarm manager)
	 */
	public void getDatausageperApp() {
		try {
			final PackageManager pm = getPackageManager();
			// get a list of installed apps.
			List<ApplicationInfo> packages = pm.getInstalledApplications(0);

			db.open();
			for (ApplicationInfo packageInfo : packages) {
				// get the UID for the selected app
				int uid = packageInfo.uid;

				// getting the package_name
				String package_name = packageInfo.packageName;
				String appName = utils.getAppName(package_name);
				//	Drawable icon = pm.getApplicationIcon(app);

				float received = 
						(float) TrafficStats.getUidRxBytes(uid)/ (1024 * 1024);
				float send = 
						(float) TrafficStats.getUidTxBytes(uid)/ (1024 * 1024);
				float total = received + send;

				if(total>0) {
					//				long date = System.currentTimeMillis();
					//				SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm");
					//				String timeStamp = formatter.format(date);
					// Logging the datausage of each app
					db.insertDataUsage(dataTable, appName, 
							round(send,2)+"kB", round(received,2)+"kB", round(total,2)+"kB",timestamp.toString() );
				}
			}
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}
	public static BigDecimal round(float d, int decimalPlace) {
		BigDecimal bd = new BigDecimal(Float.toString(d));
		bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
		return bd;
	}	

	public void StartAlarmService(){
		try {
			prefs = getSharedPreferences("ALARM", MODE_PRIVATE);

			String nextAlarm = " ";
			nextAlarm = getNextAlarm(getApplicationContext());

			/* getting nextalarmclock information 
			 * through getNextAlarmClock
			 * works only for api level 21 and above
			 */
			try {
				AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
				nextAlarm = am.getNextAlarmClock().toString();
				Log.d(Tag + "_AI", nextAlarm);
			} catch (NoSuchMethodError e) {
				Utils.appendLog(e);
				
			}
			try{
				Log.d(Tag + "Repeat", "next alarm is:  " + nextAlarm+" "+prefs);
				if (!nextAlarm.equals(prefs.getString("nextAlarm", " ")) 
						&& !nextAlarm.equals("")
						&& !nextAlarm.equals(" ")) {
					db.open();
					db.insertAlarmDetails("alarmset", nextAlarm);
					Log.d(Tag, "next alarm is:  " + nextAlarm);
					Editor editor = prefs.edit();
					editor.putString("nextAlarm", nextAlarm);
					editor.commit();
					db.close();
				}
			}
			catch(NullPointerException e){
				Log.d("Alarm", "No next Active Alarm");	
				Log.d("Null pointer exception", e+"");
				//Utils.appendLog(e);
				Utils.appendLog(e);

				

			}
		} catch(Exception e) {
			e.printStackTrace();
//			Utils.appendLog(e);
			Utils.appendLog(e);

		}

	}
	public static String getNextAlarm(Context context) {
		// collecting short names of days    
		DateFormatSymbols symbols = new DateFormatSymbols();
		// and fill with those names map...
		Map<String, Integer> map = new HashMap<String, Integer>();
		String[] dayNames = symbols.getShortWeekdays();


		map.put(dayNames[Calendar.MONDAY], Calendar.TUESDAY);
		map.put(dayNames[Calendar.TUESDAY], Calendar.WEDNESDAY);
		map.put(dayNames[Calendar.WEDNESDAY], Calendar.THURSDAY);
		map.put(dayNames[Calendar.THURSDAY], Calendar.FRIDAY);
		map.put(dayNames[Calendar.FRIDAY], Calendar.SATURDAY);
		map.put(dayNames[Calendar.SATURDAY], Calendar.SUNDAY);
		map.put(dayNames[Calendar.SUNDAY], Calendar.MONDAY);

		String nextAlarm = Settings.System.getString(context.getContentResolver(),
				Settings.System.NEXT_ALARM_FORMATTED);

		Log.d(Tag + "original", nextAlarm);

		// In case if alarm isn't set.....
		if ((nextAlarm==null) || ("".equals(nextAlarm))) return null;
		// day
		String nextAlarmDay = nextAlarm.split(" ")[0];
		// getting number....
		int alarmDay = map.get(nextAlarmDay);

		Date now = new Date();      
		String dayOfWeek = new SimpleDateFormat("EE", Locale.getDefault()).format(now);     
		int today = map.get(dayOfWeek);

		// calculating no of days we have to next alarm :-)
		int daysToAlarm = alarmDay-today;
		// sometimes it will  be negtive number so add 7.
		if (daysToAlarm<0) {
			daysToAlarm += 7;
		}



		//  building date, and parse it.....
		try {
			Calendar cal2 = Calendar.getInstance();
			String str = cal2.get(Calendar.YEAR)+"-"+(cal2.get(Calendar.MONTH)+1)+"-"+(cal2.get(Calendar.DAY_OF_MONTH));

			SimpleDateFormat df  = new SimpleDateFormat("yyyy-MM-d hh:mm");

			cal2.setTime(df.parse(str+nextAlarm.substring(nextAlarm.indexOf(" "))));
			cal2.add(Calendar.DAY_OF_YEAR, daysToAlarm);

			new SimpleDateFormat("dd-MM-yyyy");

			//return s.format(cal2.getTime()) + " "+ nextAlarm.split(" ")[1];
			return cal2.getTime().toString();
		} catch (Exception e) {
			Utils.appendLog(e);
			

		}
		// in case if cannot calculate...
		return null;
	}
		
	public void ambientService(){
		try {
			SharedPreferences savedValues = PreferenceManager
					.getDefaultSharedPreferences(this);
			//set initial value to 0 to get first time
			SharedPreferences.Editor editor = savedValues.edit();
			editor.putInt("issaved", 0);
			editor.commit();

			RecordLightIntensity();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}

	}
	SensorManager sensorManager ;
	Sensor lightSensor;
	//Captures the first record of light sensor.
	private void RecordLightIntensity() {
		Log.d("TEST", "RecordLightIntensity");
		sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

		// If lightSensor is not available in device
		if (lightSensor == null){
			Toast.makeText(this, 
					"No Light Sensor! quit-", 
					Toast.LENGTH_LONG).show();
		} else {

			//register light sensor        
			sensorManager.registerListener(lightSensorEventListener, 
					lightSensor, 
					SensorManager.SENSOR_DELAY_NORMAL);


		}
	}
	//light sensor event listener implementation
	SensorEventListener lightSensorEventListener
	= new SensorEventListener(){

		@Override
		public void onSensorChanged(SensorEvent event) {
			try {
				if(event.sensor.getType()==Sensor.TYPE_LIGHT){
					Log.d("TEST", "RecordLightIntensity");
					float currentReading = event.values[0];
					lastsensedvalue = currentReading;
					SharedPreferences savedValues = PreferenceManager
							.getDefaultSharedPreferences(MegaService.this);
					int issaved = savedValues.getInt("issaved", 0);
					//if first value is not yet saved in the service
					if (issaved == 0){

						DBAdapter db = new DBAdapter(MegaService.this);
						db.open();
						db.insertLightSensorValue(""+lastsensedvalue, timestamp.toString());
						db.close();
						SharedPreferences.Editor editor = savedValues.edit();
						//set value to 1 so, it is not executed from second time 
						editor.putInt("issaved", 1);
						editor.commit();
						Log.d("TEST", "LightSensor value saved");

					}

					else{
						sensorManager.unregisterListener(lightSensorEventListener);
						//stopSelf();
						Log.d("TEST", "LightSensor value duplicates");

					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				Utils.appendLog(e);
				
			}

		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub

		}

	};
	public void AudioLevelService(){
		try {
			bufferSize = AudioRecord
					.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
		} catch (Exception e) {
			android.util.Log.e("TrackingFlow", "Exception", e);
			Utils.appendLog(e);
			
		}
		try {
			audio = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate,
					AudioFormat.CHANNEL_IN_MONO,
					AudioFormat.ENCODING_PCM_16BIT, bufferSize);
			audio.startRecording();
			//After this call we can get the last value assigned to the lastLevel variable
			readAudioBuffer();
			audio.stop();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}

	}
	/**
	 * Functionality that gets the sound level out of the sample
	 */
	private void readAudioBuffer() {
		try {
			
			
			short[] buffer = new short[bufferSize];
			int bufferReadResult = 1;
			if (audio != null) {
				// Sense the voice...
				bufferReadResult = audio.read(buffer, 0, bufferSize);
				double sumLevel = 0;
				for (int i = 0; i < bufferReadResult; i++) {
					sumLevel += buffer[i];
					Log.d("Audio Level", buffer[i]+"");
				}
				lastLevel = Math.abs((sumLevel / bufferReadResult));
				Log.d("Audio Level", lastLevel+"");
				DBAdapter db = new DBAdapter(this);
				db.open();
				db.insertAudioLevelValue(lastLevel+"", timestamp.toString());
				db.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}

}
