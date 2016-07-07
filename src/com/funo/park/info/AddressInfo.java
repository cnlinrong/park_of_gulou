package com.funo.park.info;

import com.amap.api.services.core.LatLonPoint;

public class AddressInfo {

	private String address;
	private LatLonPoint geoPoint;
	private String endStr;

	public String getEndStr() {
		return endStr;
	}

	public void setEndStr(String endStr) {
		this.endStr = endStr;
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
