package com.taramt.wifi;

import java.util.ArrayList;

import com.taramt.autolog.R;
import com.taramt.autolog.R.id;
import com.taramt.autolog.R.layout;
import com.taramt.autolog.R.menu;
import com.taramt.utils.DBAdapter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class WifiActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wifi);
		
		TextView tv = (TextView)findViewById(R.id.wifi);
		  DBAdapter db = new DBAdapter(this);
		   db.open();
		   ArrayList<String> row = db.getwifianddatalog();
		   String content = "";
		   for (int i = 0; i < row.size(); i++) {
			   content = content +  row.get(i)+ "\n\n";
		}
		   db.close();
		   tv.setText(content);
	
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
