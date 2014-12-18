package com.taramt.boot;

import java.util.ArrayList;
import com.taramt.autolog.R;
import com.taramt.autologdatausage.DBAdapter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class BootActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_boot);
		TextView tv = (TextView)findViewById(R.id.textview);
		  DBAdapter db = new DBAdapter(this);
		   db.open();
		   ArrayList<String> row = db.getdevicestatelog();
		   String content = "";
		   for (int i = 0; i < row.size(); i++) {
			   content = content + "\n\n\n" + row.get(i);
		}
		   db.close();
		   tv.setText(content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.boot, menu);
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
