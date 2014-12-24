package com.taramt.wifi;

import com.taramt.autolog.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

public class MobileDataConnectionReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) { 
		SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(context);
		String Mobiledata = savedValues.getString("MobileData", "NotInvoked");

    	if(!intent.getAction().equals("android.net.wifi.STATE_CHANGE")){
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if(netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_MOBILE){
        	//Log.d("netInfo",netInfo.isConnectedOrConnecting()+"" );
                 if(netInfo.isConnectedOrConnecting()){
                	 if(!Mobiledata.equals("connected")||Mobiledata.equals("NotInvoked")){
                     
                	    Log.d("3G Receiver", "Have 3G Connection "+netInfo.getDetailedState());
                		SharedPreferences.Editor editor = savedValues.edit();
             			editor.putString("MobileData", "connected");
             			editor.commit();
             			
                	 }
                     }   
                 	
        }
        else {
        	if(!Mobiledata.equals("disconnected")||Mobiledata.equals("NotInvoked")){
                
        		Log.d("3G Receiver", "Don't have 3G Connection ");    
              	SharedPreferences.Editor editor = savedValues.edit();
     			editor.putString("MobileData", "disconnected");
     			editor.commit();
     			
        	 }
            
         	}
    }}
};