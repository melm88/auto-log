package com.taramt.logmedia;

import java.util.ArrayList;
import java.util.Date;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

public class CameraReceiver extends BroadcastReceiver {
	ArrayList<String> PICTURE_FORMATS;
	ArrayList<String> VIDEO_FORMATS;
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.i("INFO", "Enter BroadcastReceiver CAMERA event");
		try {

			if (PICTURE_FORMATS == null) {
				Log.d("picvid", "picvid nuller");
				PICTURE_FORMATS = new ArrayList<String>();
				PICTURE_FORMATS.add("jpg");
				PICTURE_FORMATS.add("gif");
				PICTURE_FORMATS.add("png");
				PICTURE_FORMATS.add("bmp");
				PICTURE_FORMATS.add("webp");
				VIDEO_FORMATS = new ArrayList<String>();
				VIDEO_FORMATS.add("3gp");
				VIDEO_FORMATS.add("mp4");
				VIDEO_FORMATS.add("webm");
				VIDEO_FORMATS.add("mkv");
				VIDEO_FORMATS.add("ts");
			}

			Cursor cursor = arg0.getContentResolver().query(
					arg1.getData(), null, null, null, null);
			cursor.moveToFirst();
			String image_path = cursor.getString(cursor.getColumnIndex(
					"_data"));

			int index = image_path.indexOf(".");
			String tempFormat = (String) image_path.subSequence(
					index+1, image_path.length());

			Log.d("picvid", "Format: "+ tempFormat+ " | "
					+ arg1.getData());

			if (PICTURE_FORMATS.contains(tempFormat)) {
				Toast.makeText(arg0, "New Photo is Saved as : "
						+ image_path, Toast.LENGTH_SHORT).show();
				Log.d("picvid", "New Image Media: "
						+ image_path+ "|"+ new Date().toString());
				DBAdapter dba = new DBAdapter(arg0);
				dba.open();
				long n = dba.insertMediaDetails(image_path,
						"Image", new Date().toString());
				Log.d("picvid", "insertImage: "+ n);
				dba.close();
			} else if (VIDEO_FORMATS.contains(tempFormat)) {
				Toast.makeText(arg0, "New Video is Saved as : "
						+ image_path, Toast.LENGTH_SHORT).show();
				Log.d("picvid", "New Video Media: "+ image_path);
				DBAdapter dba = new DBAdapter(arg0);
				dba.open();
				long n = dba.insertMediaDetails(image_path,
						"Video", new Date().toString());
				Log.d("picvid","insertVideo: "+n);
				dba.close();
			}

		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
	}
}
