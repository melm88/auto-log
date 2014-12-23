package com.taramt.autolog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
	}
	
	public void getCallLog(View view){
		Log.d("main","calllog");
		Intent intent =new Intent(context,Calllog.class);
		startActivity(intent);
	}
	
	public void getActivity(View view){
	
		Log.d("main","getActivity");
		Intent intent=new Intent(context,ActivityRecognitionActivity.class);
		//startActivity(intent);
	}
	public void getLocation(View view){
		Log.d("main","get location");
	}
}
