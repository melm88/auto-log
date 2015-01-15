package com.taramt.boot;

import java.util.Date;

import com.taramt.autolog.ControllerActivity;
import com.taramt.autolog.R;
import com.taramt.autologdatausage.DataService;
import com.taramt.sync.SyncService;
import com.taramt.utils.DBAdapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
			SharedPreferences savedValues = PreferenceManager
					.getDefaultSharedPreferences(context);
			SharedPreferences.Editor editor = savedValues.edit();
			//Initialize the Start Audio to false
			editor.putBoolean(context.getString(R.string.Start), true);
			editor.commit();
			DBAdapter db = new DBAdapter(context);
			db.open();
			try{
				ControllerActivity.LaunchServices(context);
				
				//Launch SyncService
				Intent intentt = new Intent(context, SyncService.class);
				PendingIntent pendingIntent = PendingIntent.getService(context,0,intentt,PendingIntent.FLAG_UPDATE_CURRENT);
				AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
				am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
						10000*60*1, pendingIntent);
				Log.d("service","Service Initiated - BootReceiver");

			} catch(Exception e){
				// db.insertDeviceState(e+"", new Date().toString());
			}


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