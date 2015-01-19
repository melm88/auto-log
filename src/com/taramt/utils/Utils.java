package com.taramt.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;

public class Utils {
	Context context;

	public Utils(Context context) {
		this.context = context;
	}

	/*
	 *  Retrieving the appName using packageName
	 */
	public String getAppName(String packageName) {
		PackageManager packageManager = context.getPackageManager();
		ApplicationInfo applicationInfo = null;
		try {
			applicationInfo = packageManager.getApplicationInfo(packageName, 0);
		} catch (final NameNotFoundException e) {
			Utils.appendLog(e);
			
		}
		final String appName = (String)((applicationInfo != null)
				? packageManager.getApplicationLabel(applicationInfo) : "???");
		return appName;
	}

	/*
	 * a method for getting the details from the database
	 */
	public String getDetails(DBAdapter db, ArrayList<String> details) {
		db.open();
		String detailss = "";
		try {		
			for (int i = 0; i<details.size(); i++) {
				detailss = details.get(i) + "\n\n" + detailss ;
			}
			db.close();
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
				
		}
		return detailss;
	}


	public String convert2Time(long total) {
		
		String timeStr = "";
		try {
			long totalSec = total/1000;
			//new date object with time difference

			
			if(totalSec/3600 > 0) {
				timeStr = timeStr +  totalSec/3600+"h  : ";
			}
			if ((totalSec%3600)/60 > 0) {
				timeStr = timeStr + (totalSec%3600)/60 + "m  : ";
			}

			timeStr = timeStr + (totalSec%3600)%60 + "s";
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}
		return timeStr;
	}
	//adds a log entry into log text file
	public static void appendLog(Throwable ex)
	{  String text = new Date().toString()+":";
	   text+="-"+ex.toString();
	   text+="-"+ex.getStackTrace()[0].getFileName();
	   text+="-"+ex.getStackTrace()[0].getClassName();
	   text+="-"+ex.getStackTrace()[0].getMethodName();
	   text+="-"+ex.getStackTrace()[0].getLineNumber()+"\n";
	   
	   File logFile = new File(Environment.getExternalStorageDirectory()+"/Autolog_log.txt");
	   if (!logFile.exists())
	   {
	      try
	      {
	         logFile.createNewFile();
	      } 
	      catch (IOException e)
	      {
	         e.printStackTrace();
	     	Utils.appendLog(e);
			
	      }
	   }
	   try
	   {
	      //BufferedWriter for performance, true to set append to file flag
	      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
	      buf.append(text);
	      buf.newLine();
	      buf.close();
	   }
	   catch (IOException e)
	   {
	      e.printStackTrace();
	  	Utils.appendLog(e);
		
	   }
	}
	

	public static void appendLog(String text) {
		File logFile = new File(Environment.getExternalStorageDirectory()+"/Autolog_log.txt");
		   if (!logFile.exists())
		   {
		      try
		      {
		         logFile.createNewFile();
		      } 
		      catch (IOException e)
		      {
		         e.printStackTrace();
		     	Utils.appendLog(e);
				
		      }
		   }
		   try
		   {
		      //BufferedWriter for performance, true to set append to file flag
		      BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true)); 
		      buf.append(text);
		      buf.newLine();
		      buf.newLine();
		      buf.close();
		   }
		   catch (IOException e)
		   {
		      e.printStackTrace();
		  	Utils.appendLog(e);
			
		   }
	}
}
