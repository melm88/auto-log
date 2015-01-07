package com.taramt.autolog;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.taramt.utils.DBAdapter;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

public class VisualizationOfLocation extends FragmentActivity {

	private GoogleMap mMap;
	static String resp="hai";

	DBAdapter dbAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visualization);

		dbAdapter =new DBAdapter(this);

		// get location data from database and store it in an array.		
		String[][] data=dbAdapter.getLocationDetails();
		String[][] spaceSplit=new String[data.length][4];
		if(data.length>0){
			for(int i=0;i<data.length;i++){
				spaceSplit[i][0]=data[i][1];
				spaceSplit[i][1]=data[i][2];
				spaceSplit[i][2]=data[i][3];
				spaceSplit[i][3]=data[i][0];
			}
		}else{
			Toast.makeText(this, "no Autolog data to show", Toast.LENGTH_LONG).show();
		}



		// plot the data on map with markers
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

		for(int i=0;i<data.length;i++){
			LatLng a=new LatLng(Double.parseDouble(spaceSplit[i][0]),Double.parseDouble(spaceSplit[i][1]));
			Marker marker = mMap.addMarker(new MarkerOptions()
			.position(a)
			.title(spaceSplit[i][0]+" "+spaceSplit[i][1]+" "+spaceSplit[i][2]+" "+spaceSplit[i][3])
			.flat(true).icon(BitmapDescriptorFactory.
					fromResource(R.drawable.red)));
		}
	}

}

