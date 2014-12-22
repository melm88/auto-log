package com.taramt.logmedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class CameraReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.i("INFO", "Enter BroadcastReceiver CAMERA event");
		

		Cursor cursor = arg0.getContentResolver().query(arg1.getData(), null, null, null, null);
		cursor.moveToFirst();
		String image_path = cursor.getString(cursor.getColumnIndex("_data"));
		Toast.makeText(arg0, "New Photo is Saved as : " + image_path, Toast.LENGTH_SHORT).show();
		Log.d("picvid","New Media: "+image_path);

	}
}
