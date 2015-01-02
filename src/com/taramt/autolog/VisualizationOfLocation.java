package com.taramt.autolog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class VisualizationOfLocation extends FragmentActivity {

	private GoogleMap mMap;
	static String resp="hai";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualization);

		// call to locationOnMap class that gets data from server
		new LocationOnMap().execute();

		// wait until response get's data
		while(resp.equals("hai")){

		}

		// split string based on , and get the data in a array
		String[] commaSplit=resp.split(",");
		String[][] spaceSplit=new String[commaSplit.length][4];
		

		for(int i=0;i<commaSplit.length;i++){
			spaceSplit[i][0]=commaSplit[i].split(" ")[0];
			spaceSplit[i][1]=commaSplit[i].split(" ")[1];
			spaceSplit[i][2]=commaSplit[i].split(" ")[2];
			spaceSplit[i][3]=commaSplit[i].split(" ")[3]+" "+commaSplit[i].split(" ")[4];
		}

		// plot the data on map with markers
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		for(int i=0;i<commaSplit.length;i++){
			LatLng a=new LatLng(Double.parseDouble(spaceSplit[i][0]),Double.parseDouble(spaceSplit[i][1]));
			Marker marker = mMap.addMarker(new MarkerOptions()
			.position(a)
			.title(spaceSplit[i][0]+" "+spaceSplit[i][1]+" "+spaceSplit[i][2]+" "+spaceSplit[i][3])
			.flat(true).icon(BitmapDescriptorFactory.
					fromResource(R.drawable.red)));
		}
	}

}

class LocationOnMap extends AsyncTask<String, Integer,String>{


	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost("https://autocode.pythonanywhere.com/AAT/webapi/getCord");
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("email", "rrcmuedu@gmail.com"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse response = httpclient.execute(httppost);

			HttpEntity entity = response.getEntity();
			if(entity != null)
			{
				VisualizationOfLocation.resp = EntityUtils.toString(entity);
				Log.d("resp",VisualizationOfLocation.resp);

				//return MainActivity.resp;
			}


		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;


	}

	//@Override
	/*protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		 MapActivity.response=result;
		 
		 Intent myIntent=new Intent(MainActivity.context,MapActivity.class);
		 startActivity(myIntent);
		
	}*/
	

}

