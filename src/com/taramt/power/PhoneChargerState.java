package com.taramt.power;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneChargerState extends BroadcastReceiver {
    private boolean mCharging;
    private boolean mUsb;
    private boolean mAC;
    private Context cc;
    private Intent batteryStatus;

    public final void start(final Context c) {
    	cc = c;
        //c.registerReceiver(this, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = c.registerReceiver(this, ifilter);
    }

    public final void stop(final Context c) {
        c.unregisterReceiver(this);
    }

    public final boolean isCharging() {
        return mCharging;
    }

    public final boolean isUsb() {
        return mUsb;
    }

    public final boolean isAC() {
        return mAC;
    }

    @Override
	public final void onReceive(final Context context, final Intent intent) {
    	Log.d("PCS","onReceive");
    	String power = "unknown";
    	
    	//If POWER_CONNECTED
    	if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
    		Log.d("PCS","PowerConnected");
    		power = "Connected";
    		ChargerClass mycc = new ChargerClass(context);
    		mycc.getChargeStatus(power);
    	} else if (Intent.ACTION_POWER_DISCONNECTED.equals(intent.getAction())) {
    		//If POWER_DISCONNECTED
    		Log.d("PCS", "PowerDisconnected");
    		power = "Disconnected";
    		ChargerClass mycc = new ChargerClass(context);
    		mycc.getChargeStatus(power);
    	}
    }
}