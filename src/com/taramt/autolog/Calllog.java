package com.taramt.autolog;

import java.util.ArrayList;
import java.util.Date;
import java.util.SortedSet;

import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 
 * @author ASHOK
 *
 * This class is used to get the call log from the android phone .
 * 
 */
public class Calllog extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calllog);
		try {
			// get the call log
			DBAdapter db = new DBAdapter(this);
			ArrayList<String> rows = db.getCallDetails(); 
			//displaying the log from database on list view 
			if(rows != null){
				ListView listView = (ListView) findViewById(R.id.list);


				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1, android.R.id.text1, rows);

				listView.setAdapter(adapter); 
			}
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}

	}


}
