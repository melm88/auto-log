package com.taramt.boot;

import java.util.Date;

import com.taramt.utils.DBAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.d("RECIEVER>>>>>>>>>>>>>>>>>>>", "");
   if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
	   Log.d("Booted>>>>>>>>>>>>>>>>>>>", "");
	   Toast.makeText(context, "Device Rebooted", Toast.LENGTH_LONG).show();
	   DBAdapter db = new DBAdapter(context);
	   db.open();
	   db.insertDeviceState("ACTION_BOOT_COMPLETED", new Date().toString());
	   db.close();
   } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
	   Log.d(">>>>>>>>>>>>>>>>>>>Shutdown", "");
	   Toast.makeText(context, "Device ShutDown", Toast.LENGTH_LONG).show();
	   DBAdapter db = new DBAdapter(context);
	   db.open();
	   db.insertDeviceState("ACTION_SHUTDOWN", new Date().toString());
	   db.close();
   }
   }
   

}