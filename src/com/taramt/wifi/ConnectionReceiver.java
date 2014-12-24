package com.taramt.wifi;

import java.util.Date;

import com.taramt.utils.DBAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public class ConnectionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    	SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(context);
		String WIFI = savedValues.getString("WIFI", "NotInvoked");

    	if(!intent.getAction().equals("android.net.wifi.STATE_CHANGE")){
            
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
           if(netInfo.isConnectedOrConnecting()){
            if(!WIFI.equals("connected")||WIFI.equals("NotInvoked")){
            	   DBAdapter db = new DBAdapter(context);
            	   db.open();
            	   db.insertWifiandData("WIFI", "connected", new Date().toString());
            	   db.close();
            	  Log.d("WifiReceiver", "Have Wifi Connection");
                  	SharedPreferences.Editor editor = savedValues.edit();
     			editor.putString("WIFI", "connected");
     			editor.commit();
     			
        	 }
            } 
        }
        else  {
            
        	if(!WIFI.equals("disconnected")||WIFI.equals("NotInvoked")){
                
        	   DBAdapter db = new DBAdapter(context);
          	   db.open();
          	   db.insertWifiandData("WIFI", "disconnected", new Date().toString());
          	   db.close();
          	  
        		Log.d("WifiReceiver", "Don't have Wifi Connection");    
        		SharedPreferences.Editor editor = savedValues.edit();
     			editor.putString("WIFI", "disconnected");
     			editor.commit();
     			
        	 }
            
        } 
    	} }
};