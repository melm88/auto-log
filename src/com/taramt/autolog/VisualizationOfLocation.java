package com.taramt.autolog;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
