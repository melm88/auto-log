package com.taramt.wifi;

import java.util.ArrayList;
import java.util.Date;

import com.taramt.autolog.R;
import com.taramt.autolog.R.id;
import com.taramt.autolog.R.layout;
import com.taramt.autolog.R.menu;
import com.taramt.utils.DBAdapter;
import com.taramt.utils.Utils;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
/**
 * launches WifiActivity
 * @author AKIL
 *
 */
public class WifiActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);
		try {
			//displaying the log from database on text view 
			DBAdapter db = new DBAdapter(this);
			db.open();
			ArrayList<String> rows = db.getwifianddatalog();
			//displaying the log from database on list view 
			ListView listView = (ListView) findViewById(R.id.list);


			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, android.R.id.text1, rows);

			listView.setAdapter(adapter); 
		} catch(Exception e) {
			e.printStackTrace();
			Utils.appendLog(e);
			
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wifi, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
