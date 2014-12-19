package com.taramt.autologdatausage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.taramt.utils.DBAdapter;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class DataService extends Service{
	DBAdapter db;
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

	}
	@Override
	public void onStart(Intent intent, int startId) {
		// getting the datausage of each app every 5 minutes.
		Log.d("DATAUSAGE", "in on start service");
	}

	
}
