package com.taramt.autologdatausage;

import java.text.SimpleDateFormat;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.IBinder;
import android.util.Log;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

public class DataService extends Service{
	DBAdapter db;
	Utils utils;
	String dataTable = "DataUsage";
	public DataService() {
	}
	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	@Override
	public void onCreate() {
		db = new DBAdapter(getApplicationContext());
		utils = new Utils(getApplicationContext());

	}
	@Override
	public void onStart(Intent intent, int startId) {
		// getting the datausage of each app every 5 minutes.
		Log.d("DATAUSAGE", "in on start service");
		getDatausageperApp();
	}
	
	/*
	 *  getting the datausage of each app
	 *  1. getting the list of apps
	 *  2. getting the Tx and Rx of each app since boot
	 *  3. Inserting the data into the phone database
	 *  4. inserts at every 5 minutes (set by alarm manager)
	 */
	public void getDatausageperApp() {
		final PackageManager pm = getPackageManager();
		// get a list of installed apps.
		List<ApplicationInfo> packages = pm.getInstalledApplications(0);

		db.open();
		for (ApplicationInfo packageInfo : packages) {
			// get the UID for the selected app
			int uid = packageInfo.uid;
			
			// getting the package_name
			String package_name = packageInfo.packageName;
			String appName = utils.getAppName(package_name);
		//	Drawable icon = pm.getApplicationIcon(app);
			
			double received = 
					(double) TrafficStats.getUidRxBytes(uid)/ (1024 * 1024);
			double send = 
					(double) TrafficStats.getUidTxBytes(uid)/ (1024 * 1024);
			double total = received + send;

			if(total>0) {
				long date = System.currentTimeMillis();
				SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy HH:mm");
				String timeStamp = formatter.format(date);
				// Logging the datausage of each app
				db.insertDataUsage(dataTable, appName, 
						send+"", received+"", total+"", timeStamp);
			}
		}
		db.close();
	}
	
}
