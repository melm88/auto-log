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
/**
 * MobileDataConnectionReciever receives the broadcasts intents of Mobile connections and disconnections
 * 
 * @author AKIL
 *
 */
public class MobileDataConnectionReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) { 
		SharedPreferences savedValues = PreferenceManager
				.getDefaultSharedPreferences(context);
		//Shared preference for saving previous Mobile state in order to eliminate duplicate broadcasts 
		String Mobiledata = savedValues.getString("MobileData", "NotInvoked");
		if(!intent.getAction().equals("android.net.wifi.STATE_CHANGE")){
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
        	//Log.d("netInfo",netInfo.isConnectedOrConnecting()+"" );
                 if(netInfo.isConnectedOrConnecting()){
                	 if(!Mobiledata.equals("connected")||Mobiledata.equals("NotInvoked")){
                		 DBAdapter db = new DBAdapter(context);
                    	   db.open();
                    	   //save MOBILE connected event
                       	   db.insertWifiandData("MOBILE", "connected", new Date().toString());
                    	   db.close();
                    	   Log.d("3G Receiver", "Have 3G Connection "+netInfo.getDetailedState());
                    	   SharedPreferences.Editor editor = savedValues.edit();
                    	   //Update the state change value in shared preference
                    	   editor.putString("MobileData", "connected");
                    	   editor.commit();
             			
                	 }
                     }   
                 	
        }
        else {
        	if(!Mobiledata.equals("disconnected")||Mobiledata.equals("NotInvoked")){
                
        		DBAdapter db = new DBAdapter(context);
         	    db.open();
         	   //save MOBILE disconnected event
            	db.insertWifiandData("MOBILE", "disconnected", new Date().toString());
         	    db.close();
         		Log.d("3G Receiver", "Don't have 3G Connection ");    
              	SharedPreferences.Editor editor = savedValues.edit();
              	 //Update the state change value in shared preference
                editor.putString("MobileData", "disconnected");
     			editor.commit();
     			
        	 }
            
         	}
    }}
};