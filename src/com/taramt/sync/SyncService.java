package com.taramt.sync;


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

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class SyncService  extends Service
{
	 
	@Override
	public IBinder onBind(Intent arg0)
	{
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{	 
		 
		return START_STICKY;
	}

	 	@Override
	public void onDestroy() {
		super.onDestroy();
		 
	}
//	 	private class SendDataToServer extends AsyncTask<Cursor, Void, String> {
//	        
//	        String resp = "";
//	       
//	        @Override
//	        protected String doInBackground(Cursor... params) {
//
//	            Log.d("toServer", "sending user data to server...");
//	            HttpClient httpclient = new DefaultHttpClient();
//	            HttpPost httppost = new HttpPost("https://autocode.pythonanywhere.com/AAT/webapi/dumpGps");
//	           
//	           
//	            try {
//
//	                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
//	                // Add your data
//	                for (int i=0; i<arr.size(); i++)
//	                    nameValuePairs.add(new BasicNameValuePair("jdata"+i, arr.get(i)));
//	               
//	               
//	               
//	/*                nameValuePairs.add(new BasicNameValuePair("jdat2", "niranjan@gmail.com,17.235667,78.1231145,2014-09-24 14:23:44"));
//	                nameValuePairs.add(new BasicNameValuePair("jdat3", "ashok@taramt.com,17.235667,78.1231145,2014-09-24 14:23:44"));
//	                nameValuePairs.add(new BasicNameValuePair("jdat4", "niranjan@taramt.com,17.235667,78.1231145,2014-09-24 14:23:44"));
//	*/
//	               
//	                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//	                // Execute HTTP Post Request
//	                HttpResponse response = httpclient.execute(httppost);
//	                int responseCode = response.getStatusLine().getStatusCode();
//	                switch(responseCode)
//	                {
//	                    case 200:
//	                        HttpEntity entity = response.getEntity();
//	                        if(entity != null)
//	                        {
//	                            resp = EntityUtils.toString(entity);
//	                            Log.d("toServer",resp);
//	                                
//	                        }
//	                        break;
//	                    default:
//	                        resp = "";
//	                        break;
//	                }
//	               
//	                Log.d("toServer","Resp: "+resp);
//	                return response.toString();
//
//	            } catch (ClientProtocolException e) {
//	                // TODO Auto-generated catch block
//	            } catch (IOException e) {
//	                // TODO Auto-generated catch block
//	            }
//	            return resp;
//
//	        }
//	       
//	        // onPostExecute displays the results of the AsyncTask.
//	        @Override
//	        protected void onPostExecute(String result) {
//	            resp = resp.trim();
//	            if(resp.equals("success"))
//	            {
//	                //Do Something
//	            }
//	        }
//	    }


	 
	 
}
