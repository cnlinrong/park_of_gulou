package com.funo.park.info;

import com.amap.api.services.core.LatLonPoint;

public class RouteInfo {

	private String address;
	private LatLonPoint geoPoint;
	private int imageId;

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LatLonPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(LatLonPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

}
