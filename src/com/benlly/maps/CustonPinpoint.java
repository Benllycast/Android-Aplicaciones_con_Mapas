package com.benlly.maps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class CustonPinpoint extends ItemizedOverlay<OverlayItem>{

	private  ArrayList<OverlayItem> pinpoints = new ArrayList<OverlayItem>();
	private Context c;
	
	public CustonPinpoint(Drawable defaultMarker) {
		super(boundCenter(defaultMarker));
		// TODO Auto-generated constructor stub
	}

	public CustonPinpoint(Drawable m, Context c) {
		this(m);
		this.c = c; 
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return pinpoints.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return pinpoints.size();
	}
	
	public void insertPinpoint(OverlayItem item){
		pinpoints.add(item);
		this.populate();
	}

}
