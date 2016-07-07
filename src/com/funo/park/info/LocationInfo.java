package com.funo.park.info;

import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;

public class LocationInfo implements Comparable<LocationInfo> {

	private String address;
	private int number;
	private LatLonPoint geoPoint;
	private List<PoiItem> poiItem;
	private String title;
	private String snippet;
	private String tel;
	private int distance;

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSnippet() {
		return snippet;
	}

	public void setSnippet(String snippet) {
		this.snippet = snippet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<PoiItem> getPoiItem() {
		return poiItem;
	}

	public void setPoiItem(List<PoiItem> poiItem) {
		this.poiItem = poiItem;
	}

	public LatLonPoint getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(LatLonPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int compareTo(LocationInfo another) {
		int result = (another.getDistance() < this.distance) ? 1 : ((another.getDistance() == this.distance) ? 0 : -1);
		return result;
	}

}
