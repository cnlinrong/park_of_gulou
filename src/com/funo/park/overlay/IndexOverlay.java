package com.funo.park.overlay;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.funo.park.R;
import com.funo.park.util.AMapServicesUtil;

public class IndexOverlay implements Overlay {

	private AMap mAMap;
	private LatLonPoint position;
	private Marker indexMarker;
	
	public IndexOverlay(AMap mAMap, LatLonPoint position) {
		this.mAMap = mAMap;
		this.position = position;
	}
	
	@Override
	public void addToMap() {
		addIndexMarker();
	}

	@Override
	public void removeFromMap() {
		indexMarker.remove();
	}

	@Override
	public void zoomToSpan() {
		if (position != null) {
			if (mAMap == null)
				return;
			try {
				mAMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapServicesUtil.convertToLatLng(position)));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}
	
	private BitmapDescriptor getIndexBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.government);
	}

	private void addIndexMarker() {
		indexMarker = mAMap.addMarker(new MarkerOptions().position(AMapServicesUtil.convertToLatLng(position)).icon(getIndexBitmapDescriptor()));
	}
	
}
