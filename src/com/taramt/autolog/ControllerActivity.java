package com.taramt.autolog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.accounts.Account;
import android.accounts.AccountManager;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.games.request.Requests.SendRequestResult;
import com.taramt.ambientlight.AmbientlightActivity;
import com.taramt.ambientlight.Ambientlightservice;
import com.taramt.audiolevel.AudioLevelService;
import com.taramt.audiolevel.AudiolevelActivity;
import com.taramt.autologalarm.AlarmService;
import com.taramt.autologalarm.Alarmactivity;
import com.taramt.autologdatausage.DataService;
import com.taramt.autologdatausage.DataUsage;
import com.taramt.autolognotification.NotificationActivity;
import com.taramt.autologscreenstate.ScreenActivity;
import com.taramt.autologscreenstate.ScreenService;
import com.taramt.boot.BootActivity;
import com.taramt.logmedia.AudioService;
import com.taramt.logmedia.MediaActivity;
import com.taramt.power.PowerActivity;
import com.taramt.temperature.TemperatureActivity;
import com.taramt.temperature.TemperatureSensor;
import com.taramt.utils.DBAdapter;
import com.taramt.wifi.WifiActivity;

public class ControllerActivity extends Activity {
	ListView listView ;
	String autologemailid;

	SharedPreferences preferences;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		listView = (ListView) findViewById(R.id.list);

		//SharedPreference to store user's register email id (from device)
		preferences=PreferenceManager.getDefaultSharedPreferences(this);
		if(preferences.getString("autologmail","false").equals("false")) {
			Log.d("toServer", "EMAIL: "+getEmail(this));
			SharedPreferences.Editor editor=preferences.edit();
			editor.putString("autologmail", getEmail(this));
			editor.commit();
		}
		autologemailid = preferences.getString("autologmail","n/a");

		//SharedPreference to keep track of sync
		if(preferences.getString("autologsync","false").equals("false")) {
			SharedPreferences.Editor editor=preferences.edit();
			editor.putString("autologsync", "no");
			editor.commit();
		}


		String enabledAppList = Settings.Secure.getString(
				this.getContentResolver(), "enabled_notification_listeners");
		System.out.println(enabledAppList);
		boolean temp = enabledAppList.contains("com.taramt.autolog/com.taramt.autolognotification.NLService");
		if(temp){

		}
		else{
			Toast.makeText(this,"Please check/enable the Notification Listener setting for AutoLog." +
					"so that AutoLog can read the Notifications. Press back to see the App", 1*1000*60).show();

			Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
			startActivity(intent);	
		}

		LaunchServices(this);
		String[] values = new String[] { 
				"Notification", 
				"MediaActivity",
				"Datausage",
				"PowerActivity",
				"ScreenActivity",
				"TemperatureActivity",
				"Alarm",
				"UserActivity", 
				"Callog", 
				"Location",
				"VisualizationofLocation",
				"WIFI and 3G data",
				"Ambient light",
				"Noise Level",
				"Boot and Reboot",
				"Fore ground apps"
		};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1, values);

		listView.setAdapter(adapter); 

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent myIntent = null;
				switch(position){
				case 0: 
					myIntent = new Intent(getApplicationContext(), NotificationActivity.class);
					break;
				case 1: 
					myIntent = new Intent(getApplicationContext(), MediaActivity.class);
					break;
				case 2: 
					myIntent = new Intent(getApplicationContext(), DataUsage.class);
					break;
				case 3: 
					myIntent = new Intent(getApplicationContext(), PowerActivity.class);
					break;
				case 4: 
					myIntent = new Intent(getApplicationContext(), ScreenActivity.class);
					break;
				case 5: 
					myIntent = new Intent(getApplicationContext(), TemperatureActivity.class);
					break;
				case 6: 
					myIntent = new Intent(getApplicationContext(), Alarmactivity.class);
					break;
				case 7: 
					myIntent = new Intent(getApplicationContext(), ActivityRecognitionActivity.class);
					break;
				case 8: 
					myIntent = new Intent(getApplicationContext(), Calllog.class);
					break;
				case 9: 
					myIntent = new Intent(getApplicationContext(), LocationActivity.class);
					break;
				case 10: 
					myIntent = new Intent(getApplicationContext(), VisualizationOfLocation.class);
					break;
				case 11: 
					myIntent = new Intent(getApplicationContext(), WifiActivity.class);
					break;
				case 12: 
					myIntent = new Intent(getApplicationContext(), AmbientlightActivity.class);
					break;
				case 13: 
					myIntent = new Intent(getApplicationContext(), AudiolevelActivity.class);
					break;
				case 14: 
					myIntent = new Intent(getApplicationContext(), BootActivity.class);
					break;
				case 15:
					myIntent = new Intent(getApplicationContext(), ForeGroundApp.class);

				} 
				startActivity(myIntent);
			}

		}); 
	}
	/*
	 * Launches all services.
	 */
	private static NotificationReceiver nReceiver;

	public static void LaunchServices(Context c){

		SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(c);
		//If this is the first launch
		if (savedValues.getBoolean(c.getString(R.string.Start), true)) {
			Log.d("Launch", "timer is being set");
			SharedPreferences.Editor editor = savedValues.edit();
			//Initialize the Start Audio to false
			editor.putBoolean(c.getString(R.string.StartAudio), false);
			editor.commit();

			//		"Notification"
			nReceiver = new NotificationReceiver();
			IntentFilter filter = new IntentFilter();
			filter.addAction("com.taramt.autolog.notification");
			c.registerReceiver(nReceiver,filter);		
			//		"MediaActivity",
			Intent audioservice = new Intent(c,
					AudioService.class);
			c.startService(audioservice);

			//		"Datausage",

			try {
				Intent intentt = new Intent(c, DataService.class);
				PendingIntent pendingIntent = PendingIntent.getService(c,0,intentt,PendingIntent.FLAG_UPDATE_CURRENT);
				AlarmManager am = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
				am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
						1000*60*1, pendingIntent);
			} catch (Exception e) {
				Log.d("exceptionasdfdf",""+e);
			}


			//		"PowerActivity",

			//		"ScreenActivity",
			DBAdapter db = new DBAdapter(c);
			db.open();
			if (db.getrowcount("Active") == 0) {
				SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String date =s.format(new Date());
				db.inserScreenstate("Active", date);
			}
			db.close();

			if (c.getResources().getConfiguration().orientation == 1) {

				try {

					Intent intent = new Intent(c, ScreenService.class);
					PendingIntent pendingIntent = PendingIntent.getService(c,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
					AlarmManager am = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
					am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
							1000*30, pendingIntent);
					Log.i("oon Alarm", "Alarm started");
				} catch (Exception e) {
					Log.d("exceptionasdfdf",""+e);
				}
			}

			//		"TemperatureActivity",
			Intent iServe = new Intent(c,
					TemperatureSensor.class);
			c.startService(iServe);

			//		"Alarm",
			try {

				Intent intent = new Intent(c, AlarmService.class);
				PendingIntent pendingIntent = PendingIntent.getService(c,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
				AlarmManager am = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
				am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 
						1000*60*5 , pendingIntent);
				Log.i("oon Alarm", "Alarm started");
			} catch (Exception e) {
				Log.d("exceptionasdfdf",""+e);
			}

			//		"UserActivity", 
			try {

				AlarmManager am= (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
				Intent intent=new Intent(c,ActivityRecognitionService.class);
				PendingIntent pintent = PendingIntent
						.getService(c, 0, intent, 0);
				am.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(),
						1*60*1000, pintent);

			} catch (Exception e) {
				Log.d("exceptionasdfdf",""+e);
			}

			//		"Callog", 

			//		"Location",
			try{
				AlarmManager am= (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
				Intent intent=new Intent(c,LocationClass.class);
				PendingIntent pintent = PendingIntent
						.getService(c, 0, intent, 0);
				am.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(),
						1*60*1000, pintent);

			}catch(Exception e){
				Log.d("exceptionasdfdf",""+e);
			}
			//		"VisualizationofLocation",

			//		"WIFI and 3G data",

			//		"Ambient light",
			try{
				Intent intent = new Intent(c, Ambientlightservice.class);
				PendingIntent pintent = PendingIntent
						.getService(c, 0, intent, 0);

				AlarmManager alarm = (AlarmManager) 
						c.getSystemService(Context.ALARM_SERVICE);
				alarm.cancel(pintent);
				//setting an alarm manager for interval of 5 minutes
				alarm.setRepeating(AlarmManager.RTC_WAKEUP, 
						System.currentTimeMillis(),
						5*60*1000, pintent); 
			}
			catch(Exception e){
				Log.d("Ambientlight", ""+e);
			}
			//		"Noise Level",
			try{
				Intent intent = new Intent(c, AudioLevelService.class);
				PendingIntent pintent = PendingIntent
						.getService(c, 0, intent, 0);
				AlarmManager alarm = (AlarmManager)
						c.getSystemService(Context.ALARM_SERVICE);
				alarm.cancel(pintent);
				//setting an alarm manager for interval of 5 minutes
				alarm.setRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(),
						5*60*1000, pintent);
			}
			catch(Exception e){
				Log.d("Noise Level", ""+e);
			}
			//		"Boot and Reboot",

			//		"Fore ground apps
		} else {
			Log.d("Launch", "Services already Started");
		}		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.controller, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_sync) {
			//Check sync state from SharedPreference
			if(preferences.getString("autologsync","false").equals("no")) {
				new SendDataToServer().execute();
				Toast.makeText(this, "Sync Initiated", Toast.LENGTH_SHORT/1500).show();
			} else {
				Toast.makeText(this, "Sync In Progress", Toast.LENGTH_SHORT/1500).show();
			}			
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	//Static function to retrieve email_id from the user's account
	//On the device
	static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context); 
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}
	//Part of getEmail function (above) that searches for registered
	//Google email id within the phone.
	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];      
		} else {
			account = null;
		}
		return account;
	}

	private class SendDataToServer extends AsyncTask<Void, Void, String> {

		String resp = "";
		DBAdapter dba = new DBAdapter(getApplicationContext());

		@Override
		protected String doInBackground(Void... params) {

			Log.d("toServer", "sending user data to server...");

			dba.open();

			/*ArrayList<String> devicestate_log = dba.getdevicestatelogSYNC();
		    webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/devicestate", devicestate_log);*/

			try {
				//SharedPreference to keep track of sync
				if(preferences.getString("autologsync","false").equals("no")) {
					SharedPreferences.Editor editor=preferences.edit();
					editor.putString("autologsync", "yes");
					editor.commit();
				}
				//Retrieve a HashMap(tablename: rows-to-sync) from DB
				HashMap<String, ArrayList<String>> sync_data_map = dba.getSYNCData();
				//Get a set of all tablenames (using KeySet)
				Set<String> entryKeys = sync_data_map.keySet();
				ArrayList<String> temp_array;
				Log.d("toServerNET","KeySet:"+entryKeys.toString());
				//Iterate through Keys and begin server interaction
				//only if rows-to-sync size is >0
				for(String key: entryKeys) {
					temp_array = sync_data_map.get(key);
					Log.d("toServerNET","Requesting: "+temp_array.size());
					if(temp_array.size()>0) {
						webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/synchandler", key, temp_array);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {		    	
				SharedPreferences.Editor editor=preferences.edit();
				editor.putString("autologsync", "no");
				editor.commit();

			}

			dba.close();
			return resp;

		}

		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {

			Toast.makeText(getApplicationContext(), "Autolog Sync -Completed", Toast.LENGTH_SHORT/1000).show();
			resp = resp.trim();
			if(resp.equals("success"))
			{
				//Do Something
				Log.d("toServer","Success is returned");
			}
		}
	}

	//Function that would take url, tablename and data_to_sync
	//And perform sync operation by passing the data_to_sync,tablename to the url
	public void webRequest(String url, String tablename, ArrayList<String> datas) {

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		String resp="";

		try {
			if(datas.size()<=0)
				Log.d("toServerNET", "SEND_REQUEST-result "+datas.size());
			else {
				//Add tablename as name-value pair
				nameValuePairs.add(new BasicNameValuePair("tablename", tablename));
				//Add all-rows that have to be synced (Sent to server)
				for (int i=0; i<datas.size(); i++)
					nameValuePairs.add(new BasicNameValuePair("jdata"+i, autologemailid+"|"+datas.get(i)));
				Log.d("toServerNET","SEND_REQUEST:"+datas.size());
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			int responseCode = response.getStatusLine().getStatusCode();
			//If responseCode is 200 then only proceed.
			//Response 200 - Server processed data successfully
			switch(responseCode)
			{
			case 200:
				HttpEntity entity = response.getEntity();
				if(entity != null)
				{
					resp = EntityUtils.toString(entity);
					Log.d("toServerRESP",resp);
					String temp_resp = resp.split("-")[0].trim();
					//String check to see if returned value is "FALSE" or empty or not
					if(!temp_resp.equalsIgnoreCase("false")&&!temp_resp.equalsIgnoreCase("")) {
						//Log.d("toServerRESP","--> "+temp_resp);
						//If the returned count of insertions (OnServer) is greater than 0
						//Proceed
						if(Integer.parseInt(temp_resp)>0) {
							//Log.d("toServerRESP","temp: "+temp_resp);
							DBAdapter dba = new DBAdapter(getApplicationContext());
							dba.open();
							//Iterate through the arraylist we received as parameter
							//And updates its sync values to 1 (TRUE)
							//A row is uniquely identified by its TIMESTAMP
							for(String data: datas) {
								String[] tempstr = data.split("\\|");
								//Log.d("toServerRESPfor","data:"+data+"|temp"+tempstr);
								for(int i=0; i<tempstr.length; i++) {
									//Log.d("toServerRESPif","tempdata: "+tempstr[i]+"|"+tempstr[i].contains(":"));
									//TIMESTAMP's contain ':'
									if(tempstr[i].contains(":")) {
										//Log.d("toServerSYNCUPD","contains--");
										dba.updateSYNCTable(tablename, tempstr[i]);
										break;
									}
								}		                    		
							}	                    		
							dba.close();
						}                   	
					}
				}
				break;
			default:
				resp = "";
				break;
			}

			Log.d("toServer","SEND_REQUEST -Resp: "+resp);


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}

	}
}

class NotificationReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//Log.d(TAG, "Notification received");	
		// Showing the notifications
		//showNotifications();
	}
}

