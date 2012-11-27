package com.benlly.maps;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;

public class MainActivity extends MapActivity{
	
	MapView map;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        map = (MapView) findViewById(R.id.mvMain);
        map.setBuiltInZoomControls(true);
        Touchy t = new Touchy();
        List<Overlay> oveList = map.getOverlays();
        oveList.add(t);
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
	
	class Touchy extends Overlay{
		public boolean onTouchEvent(MotionEvent e, MapView m){
			return false;
		}
	}
}
