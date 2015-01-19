package com.taramt.power;

import java.util.Date;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class ChargerClass {

	Context cc;

	public ChargerClass(Context c) {
		cc = c;
	}


	public void getChargeStatus(String power) {

		try {
			IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			Intent batteryStatus = cc.registerReceiver(null, ifilter);

			// Are we charging / charged?
			int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
			boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
					status == BatteryManager.BATTERY_STATUS_FULL;

			// How are we charging?
			int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
			boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
			boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

			//Set the port (USB/AC/removed)
			String chargingport;
			if (usbCharge == false) {
				if (acCharge == false) {
					chargingport = "removed";
				} else {
					chargingport = "A/C";
				}
			} else {
				chargingport = "USB";
			}

			//Check battery level
			int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
			float batteryPct = level / (float)scale;

			Log.d("PCS","Main: "+isCharging+" | "+usbCharge+" | "+acCharge + "|~ "+batteryPct);
			//Toast.makeText(cc, ""+isCharging+" | "+usbCharge+" | "+acCharge+ "|~ "+batteryPct, Toast.LENGTH_SHORT/1000).show();

			//Insert Details into Database
			DBAdapter dba = new DBAdapter(cc);
			dba.open();
			dba.insertPowerDetails(power, isCharging==true?"Charging":"Not Charging", chargingport, ""+batteryPct, new Date().toString());
			dba.close();
			Utils.appendLog(new Date().toString()+","+power+", "+batteryPct);
			
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}

}
