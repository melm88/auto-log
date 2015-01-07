package com.taramt.autolog;

import com.taramt.utils.DBAdapter;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class ForeGroundApp extends ActionBarActivity{
	
	TextView txt;
	
	DBAdapter dbAdapter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currentapp);
        
        txt=(TextView) findViewById(R.id.app);
        
        dbAdapter =new DBAdapter(this);
        String[][] app_data=dbAdapter.getForeGroundApp();
        
        
        if(app_data.length>0){
        	
        	String data_text="";
        	for(int i=0;i<app_data.length;i++){
        		data_text=data_text+app_data[i][0]+" "+app_data[i][1]+"\n";
        	}
        	txt.setText(data_text);
        }else{
        	txt.setText("Log started just now");
        }
        
        
    }

}
