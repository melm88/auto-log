package com.taramt.autolog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.games.request.Requests.SendRequestResult;
import com.taramt.ambientlight.AmbientlightActivity;
import com.taramt.audiolevel.AudiolevelActivity;
import com.taramt.autologalarm.Alarmactivity;
import com.taramt.autologdatausage.DataUsage;
import com.taramt.autolognotification.NotificationActivity;
import com.taramt.autologscreenstate.ScreenActivity;
import com.taramt.boot.BootActivity;
import com.taramt.logmedia.MediaActivity;
import com.taramt.power.PowerActivity;
import com.taramt.temperature.TemperatureActivity;
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
		
		preferences=PreferenceManager.getDefaultSharedPreferences(this);
		if(preferences.getString("autologmail","false").equals("false")) {
			Log.d("toServer", "EMAIL: "+getEmail(this));
			SharedPreferences.Editor editor=preferences.edit();
			editor.putString("autologmail", getEmail(this));
			editor.commit();
		}
		autologemailid = preferences.getString("autologmail","n/a");
		
		new SendDataToServer().execute();
		
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
					if(preferences.getBoolean("activity",false)){
						myIntent = new Intent(getApplicationContext(), ActivityRecognitionActivity.class);
						
					}else{
						myIntent = new Intent(getApplicationContext(), ActivityRecognitionActivity.class);
						
						AlarmManager am= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
						Intent intent=new Intent(getApplicationContext(),ActivityRecognitionService.class);
						PendingIntent pintent = PendingIntent
								.getService(getApplicationContext(), 0, intent, 0);
						am.setRepeating(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis(),
								5*60*1000, pintent);
						
						SharedPreferences.Editor editor=preferences.edit();
						editor.putBoolean("activity", true);
						editor.commit();
					}
					break;
				case 8: 
					myIntent = new Intent(getApplicationContext(), Calllog.class);
					break;
				case 9: 
					if(preferences.getBoolean("location",false)){
						myIntent = new Intent(getApplicationContext(), LocationActivity.class);
						
					}else{
						myIntent = new Intent(getApplicationContext(),LocationActivity .class);
						
						AlarmManager am= (AlarmManager)getSystemService(Context.ALARM_SERVICE);
						Intent intent=new Intent(getApplicationContext(),LocationClass.class);
						PendingIntent pintent = PendingIntent
								.getService(getApplicationContext(), 0, intent, 0);
						am.setRepeating(AlarmManager.RTC_WAKEUP,
								System.currentTimeMillis(),
								5*60*1000, pintent);
						
						SharedPreferences.Editor editor=preferences.edit();
						editor.putBoolean("location", true);
						editor.commit();
					}
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
		}
		return super.onOptionsItemSelected(item);
	}
	
	static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context); 
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}
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
		    ArrayList<String> devicestate_log = dba.getdevicestatelogSYNC();
		    webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/devicestate", devicestate_log);
		    
		    ArrayList<String> lightsensor_log = dba.getLightSensorlogSYNC();
		    webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/lightsensor", lightsensor_log);
		    
		    ArrayList<String> wifianddata_log = dba.getwifianddatalogSYNC();
		    webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/wifianddata", wifianddata_log);
		    
		    ArrayList<String> locationdata_log = dba.getLocationDetailsSYNC();
		    webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/locationtable", locationdata_log);
		    
		    ArrayList<String> activities_log = dba.getActivitiesSYNC();
		    webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/acitivities", activities_log);
		    
		    ArrayList<String> audiolevel_log = dba.getaudiologSYNC();
		    webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/audiolevel", audiolevel_log);
		    
		    
		    
		    dba.close();
			return resp;

		}
		
		// onPostExecute displays the results of the AsyncTask.
		@Override
		protected void onPostExecute(String result) {
			resp = resp.trim();
			if(resp.equals("success"))
			{
				//Do Something
				Log.d("toServer","Success is returned");
			}
		}
	}

	public void webRequest(String url, ArrayList<String> datas) {
		
		HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
	    String resp="";
	    
	    try {
	    	if(datas.size()<=0)
	        	Log.d("toServer", "SEND_REQUEST-result "+datas.size());
	        else {
	        	for (int i=0; i<datas.size(); i++)
		    		nameValuePairs.add(new BasicNameValuePair("jdata"+i, autologemailid+"|"+datas.get(i)));
	    		Log.d("toServer","SEND_REQUEST:"+datas.size());
	        }
	        
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        int responseCode = response.getStatusLine().getStatusCode();
	        switch(responseCode)
	        {
	            case 200:
	                HttpEntity entity = response.getEntity();
	                if(entity != null)
	        		{
	        			resp = EntityUtils.toString(entity);
	        			Log.d("toServer",resp);
	                         
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