package com.taramt.boot;

import java.util.Date;

import com.taramt.utils.DBAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
/**
 * BootReceiver receives the boot and shutdown intents from the system
 * @author AKIL
 *
 */
public class BootReceiver extends BroadcastReceiver {

   @Override
   public void onReceive(Context context, Intent intent) {
      Log.d("BootReceiver", "RECIEVER Invoked");
      //If intent of type ACTION_BOOT_COMPLETED
   if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
	   Log.d("BootReceiver", "ACTION_BOOT_COMPLETED");
	   Toast.makeText(context, "Device Rebooted", Toast.LENGTH_LONG).show();
	   DBAdapter db = new DBAdapter(context);
	   db.open();
	   //save the event to database
	   db.insertDeviceState("ACTION_BOOT_COMPLETED", new Date().toString());
	   db.close();
   } else if (Intent.ACTION_SHUTDOWN.equals(intent.getAction())) {
	   //If intent of type ACTION_SHUTDOWN
	   Log.d("BootReceiver", "ACTION_SHUTDOWN");
	   Toast.makeText(context, "Device ShutDown", Toast.LENGTH_LONG).show();
	   DBAdapter db = new DBAdapter(context);
	   db.open();
	   //save the event to database
	   db.insertDeviceState("ACTION_SHUTDOWN", new Date().toString());
	   db.close();
   }
   }
   

}