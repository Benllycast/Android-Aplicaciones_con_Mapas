package com.benlly.maps;

import java.util.List;
import java.util.Locale;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends MapActivity implements LocationListener{

	MapView map;	//objeto para dibujar mapas de google
	long start, stop;
	MyLocationOverlay compass;	//objetos para el dibujo de la localizacion actual de uusario????
	MapController controller;	//objeto para los controles del mapa
	int x, y;
	GeoPoint touchedPoint; 		//objeto que mantiene las coordenadas latitud longitud de iun punto
	Drawable d;		//objeto que mantiene un recurso dibujable
	List<Overlay> oveList;		//lista de objetos que depliegan en un mapa
	LocationManager lm;			//administra la localizacion GPS
	String towers;
	int lat = 0;
	int lon = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);
		Touchy t = new Touchy();	//Overlay para detectar toques en lapantalla
		oveList = map.getOverlays();	//obtiene todos los overlays que se dibujan en el mapa
		oveList.add(t);		//agrega nuestro overlay de ultimo en la lista
		compass = new MyLocationOverlay(MainActivity.this, map);	//se crea un nuevo Overlay de la localizacion actual
		oveList.add(compass);	//se agrega el overlay de la localizacion
		controller = map.getController();	//se obtinenen los controles del mapa
		GeoPoint point = new GeoPoint(51643234, 7848593);	//se crea un nuevo Geopoint
		controller.animateTo(point);	//se indica a los controles que nos lleven al punto indicado
		controller.setZoom(6);		//se hace un zoon de nivel 6
		d = getResources().getDrawable(R.drawable.ic_launcher);		//se crea un objeto dibujable por medio de un recurso
		
		//placing pintpoint at location
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);	//manager para la localizacion GPS
		Criteria crit = new Criteria();		//un objeto criterio que maneja el mejor criterio de selleccion del provider
		towers = lm.getBestProvider(crit, false);		//se obtiene un proveedor de localizacion
		Location location = lm.getLastKnownLocation(towers);	//se obtine la localizacion del provedor seleccionado
		
		//si hay una localizacion
		if (location != null) {
			lat = (int) location.getLatitude();		//se obtiene la lat  y long
			lon = (int) location.getLongitude();
			GeoPoint ourLocation = new GeoPoint(lat, lon);		//se crea un nuevo punto
			//se crea un overlay para mostrar un dibujo en el punto indicado por la lat y lon
			OverlayItem overlayItem = new OverlayItem(ourLocation, "what's up", "2nd string");	
			CustonPinpoint custom = new CustonPinpoint(d, MainActivity.this); //se crea un punto personal con el objeto dibujable
			custom.insertPinpoint(overlayItem);		//se le dan los datos de la posicion en el mapa
			oveList.add(custom); //se agrega el punto en la lista de overlays
		}else{
			//cuando todo se va al carajo
			Toast.makeText(MainActivity.this, "no se puede encontrar un provider", Toast.LENGTH_LONG).show();
		}
		
		
		
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
		
		//metodo para cuando se toca el area del mapa
		public boolean onTouchEvent(MotionEvent e, MapView m) {
			
			//si es accion de bajada
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = map.getProjection().fromPixels(x, y);	//se obtine las coordenadas del punto de toque en el mapa
				}
			//si es accion de subida
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			//se calcula la duaracion del click
			if ((stop - start) > 1500) {
				AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();	//se crea un nuevo AlertDialog
				alert.setTitle("Pick an Option");
				alert.setMessage("I told to pick an option");
				//se agregan 3 botones
				alert.setButton("place a pinpoint",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								// TODO Auto-generated method stub
								//se crea un nuevo Overlay para colocar el punto en el mapa
								OverlayItem overlayItem = new OverlayItem(touchedPoint, "what's up", "2nd string");
								//se crea un punto
								CustonPinpoint custom = new CustonPinpoint(d, MainActivity.this);
								//agrega coordenadas
								custom.insertPinpoint(overlayItem);
								//se agrega en las lista de Overlays
								oveList.add(custom);
								
							}
						});
				alert.setButton2("get addres",
						//debe probarse en el celular
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,	int which) {
								// TODO Auto-generated method stub
								String display = "";
								Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());	//Objeto para transformar una direccion a coordenadas y viseversa
								try {
									//se obtinen todas las direcciones que se describen en las coordenadas dadas
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
									e2.printStackTrace();
								}
							}
						});
				alert.setButton3("Toggle View",
						new DialogInterface.OnClickListener() {
							//cambia entre el mapa satelital y el de calles
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
		//desabilita las actualizaciones del compass
		//compass.disableCompass();
		super.onPause();
		//remueve las actualizaciones de LocationManager
		lm.removeUpdates(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//habilita las actualizaciones del compass
		//compass.enableCompass();
		super.onResume();
		//reanuda las actualizaciones del LocationManager
		lm.requestLocationUpdates(towers, 500, 1, this);
	}

	//provar con un dispositivo
	//se llama en una actualizacion de la localizacion
	public void onLocationChanged(Location l) {
		// TODO Auto-generated method stub
		lat = (int) l.getLatitude();
		lon = (int) l.getLongitude();
		//se crea un nuevo punto y se coloca en la lista de Overlyas con la localizacion actual
		GeoPoint ourLocation = new GeoPoint(lat, lon);
		OverlayItem overlayItem = new OverlayItem(ourLocation, "what's up", "2nd string");
		CustonPinpoint custom = new CustonPinpoint(d, MainActivity.this);
		custom.insertPinpoint(overlayItem);
		oveList.add(custom);
		
	}

	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
