package com.benlly.maps;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends MapActivity {

	MapView map;
	long start, stop;
	MyLocationOverlay compass;
	MapController controller;
	int x, y;
	GeoPoint touchedPoint;
	Drawable d;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);
		Touchy t = new Touchy();
		List<Overlay> oveList = map.getOverlays();
		oveList.add(t);
		compass = new MyLocationOverlay(MainActivity.this, map);
		oveList.add(compass);
		controller = map.getController();
		GeoPoint point = new GeoPoint(51643234, 7848593);
		controller.animateTo(point);
		controller.setZoom(6);
		d = getResources().getDrawable(R.drawable.ic_launcher);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_maps, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touchy extends Overlay {
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = map.getProjection().fromPixels(x, y);
			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			if ((stop - start) > 1500) {
				AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
						.create();
				alert.setTitle("Pick an Option");
				alert.setMessage("I told to pick an option");
				alert.setButton("place a pinpoint",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								// TODO Auto-generated method stub
								CustonPinpoint custom = new CustonPinpoint(d, MainActivity.this);
							}
						});
				alert.setButton2("get addres",
						//debe probarse en el celular
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,	int which) {
								// TODO Auto-generated method stub
								String display = "";
								Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
								try {
									List<Address> addresses = geocoder.getFromLocation(touchedPoint.getLatitudeE6()/1E6, touchedPoint.getLongitudeE6()/1E6, 1);
									if (addresses.size() > 0) {
										for (int i = 0; i < addresses.get(0).getMaxAddressLineIndex(); i++) {
											
											display += addresses.get(0).getAddressLine(i)+"\n";
										}
										Toast toast = Toast.makeText(getBaseContext(), display, Toast.LENGTH_LONG);
										toast.show();
										
									}
								} catch (Exception e2) {
									// TODO: handle exception
								}
							}
						});
				alert.setButton3("Toggle View",
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,	int which) {
								// TODO Auto-generated method stub
								if (map.isSatellite()) {
									map.setSatellite(false);
									map.setStreetView(true);
								}else{
									map.setSatellite(true);
									map.setStreetView(false);
								}
							}
						});
				alert.show();
				return true;
			}
			return false;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//compass.disableCompass();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//compass.enableCompass();
		super.onResume();
	}
}
