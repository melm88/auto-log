package com.taramt.wifi;

import java.util.Date;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;
/**
 * ConnectionReceiver receives the broadcasts intents of WIFI connections and disconnections
 * @author AKIL
 *
 */
public class ConnectionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			SharedPreferences savedValues = PreferenceManager
					.getDefaultSharedPreferences(context);
			String WIFI = savedValues.getString("WIFI", "NotInvoked");
			//Shared preference for saving previous WIFI state in order to eliminate duplicate broadcasts 
			if(!intent.getAction().equals("android.net.wifi.STATE_CHANGE")){
				ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
				NetworkInfo netInfo = conMan.getActiveNetworkInfo();
				if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					if(netInfo.isConnectedOrConnecting()){
						if(!WIFI.equals("connected")||WIFI.equals("NotInvoked")){
							DBAdapter db = new DBAdapter(context);
							db.open();
							//save wifi connected event
							db.insertWifiandData("WIFI", "connected", new Date().toString());
							db.close();
							Log.d("WifiReceiver", "Have Wifi Connection");
							SharedPreferences.Editor editor = savedValues.edit();
							//Update the state change value in shared preference
							editor.putString("WIFI", "connected");
							editor.commit();

						}
					} 
				}
				else  {
					//if wifi is disconnected
					if(!WIFI.equals("disconnected")||WIFI.equals("NotInvoked")){

						DBAdapter db = new DBAdapter(context);
						db.open();
						//save wifi disconnected event
						db.insertWifiandData("WIFI", "disconnected", new Date().toString());
						db.close();
						Log.d("WifiReceiver", "Don't have Wifi Connection");    
						SharedPreferences.Editor editor = savedValues.edit();
						//Update the state change value in shared preference
						editor.putString("WIFI", "disconnected");
						editor.commit();

					}

				}
			} 
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		} }
};