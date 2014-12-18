package com.taramt.boot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {
      Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG).show();
      Log.d("RECIEVED>>>>>>>>>>>>>>>>>>>", "");
   if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())){
	   Log.d("Booted>>>>>>>>>>>>>>>>>>>", "");
	      
   }
   else if(Intent.ACTION_SHUTDOWN.equals(intent.getAction())){
	   Log.d(">>>>>>>>>>>>>>>>>>>Shutdown", "");
	   
   }
   }
   

}