package com.taramt.sync;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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

import com.taramt.autolog.Calllog;
import com.taramt.utils.DBAdapter;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.util.Log;
import android.widget.Toast;


public class SyncService  extends Service
{
	SharedPreferences preferences;
	String autologemailid;

	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();		
		preferences=PreferenceManager.getDefaultSharedPreferences(this);
		autologemailid = preferences.getString("autologmail","n/a");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		try {
			preferences=PreferenceManager.getDefaultSharedPreferences(this);
			autologemailid = preferences.getString("autologmail","n/a");
			new SendDataToServer().execute();
			Log.d("service","Service Running - onStartCommand");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

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
				Log.d("CALLLOG", "Sync starting");
				String lastFetch = preferences.getString("lastFetch"," ");
				Log.d("CALLLOG", "lastFetch "+lastFetch+".");
				
				if(lastFetch.equals(" ")) {
					Log.d("CALLLOG", "First Sync");
					temp_array = dba.getCallDetails();
					SharedPreferences.Editor editor=preferences.edit();
					editor.putString("lastFetch", new Date().getTime()+"");
					editor.commit();
				}
				else{
					Log.d("CALLLOG", "Update Sync");
				temp_array = dba.getCallDetailsLatest(lastFetch);
				}

			webRequest("https://autocode.pythonanywhere.com/Autolog/webadmin/synchandler", "calllog", temp_array);
			Log.d("CALLLOG", "Sync completed");
			
			
			} catch(Exception e) {
				e.printStackTrace();
				Log.d("CALLLOG", "errr "+e);
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
