package com.funo.park.location;

import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.funo.park.R;

import android.content.Context;

/**
 * 地理编码与逆地理编码功能介绍
 */
public class GeocoderService implements OnGeocodeSearchListener {

	private Context mContext;
	private GeocodeSearch geocoderSearch;
	
	private String cityCode = "0591";
	
	private GetLoationPointCallback getLoationPointCallback;
	private GetLoationNameCallback getLoationNameCallback;

	public GeocoderService(Context context) {
		this.mContext = context;
		
		geocoderSearch = new GeocodeSearch(context);
		geocoderSearch.setOnGeocodeSearchListener(this);
	}
	
	public void getLocationPoint(String address, GetLoationPointCallback getLoationPointCallback) {
		this.getLoationPointCallback = getLoationPointCallback;
		
		GeocodeQuery query = new GeocodeQuery(address, cityCode);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
		geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
	}
	
	public void getLocationName(LatLonPoint point, GetLoationNameCallback getLoationNameCallback) {
		this.getLoationNameCallback = getLoationNameCallback;
		
		RegeocodeQuery query = new RegeocodeQuery(point, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
	}

	/**
	 * 地理编码查询回调
	 */
	@Override
	public void onGeocodeSearched(GeocodeResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getGeocodeAddressList() != null && result.getGeocodeAddressList().size() > 0) {
				GeocodeAddress address = result.getGeocodeAddressList().get(0);
				if (getLoationPointCallback != null) {
					getLoationPointCallback.doSuccessCallback(address.getLatLonPoint());
				}
			} else {
				if (getLoationPointCallback != null) {
					getLoationPointCallback.doFailCallback(mContext.getString(R.string.no_result));
				}
			}
		} else {
			if (getLoationPointCallback != null) {
				getLoationPointCallback.doFailCallback("查询位置经纬度信息失败");
			}
		}
	}

	/**
	 * 逆地理编码回调
	 */
	@Override
	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
		if (rCode == 1000) {
			if (result != null && result.getRegeocodeAddress() != null
					&& result.getRegeocodeAddress().getFormatAddress() != null) {
//				String addressName = result.getRegeocodeAddress().getFormatAddress() + "附近";
				String addressName = "未知地址";
				List<PoiItem> poiItems = result.getRegeocodeAddress().getPois();
				if (poiItems != null && !poiItems.isEmpty()) {
					PoiItem poiItem = poiItems.get(0);
					addressName = poiItem.getProvinceName() + poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet();
				}
				if (getLoationNameCallback != null) {
					getLoationNameCallback.doSuccessCallback(addressName);
				}
			} else {
				if (getLoationNameCallback != null) {
					getLoationNameCallback.doFailCallback(mContext.getString(R.string.no_result));
				}
			}
		} else {
			if (getLoationNameCallback != null) {
				getLoationNameCallback.doFailCallback("查询经纬度位置信息失败");
			}
		}
	}

	public interface GetLoationPointCallback {
		
		public void doSuccessCallback(LatLonPoint point);
		
		public void doFailCallback(String msg);
		
	}
	
	public interface GetLoationNameCallback {
		
		public void doSuccessCallback(String address);
		
		public void doFailCallback(String msg);
		
	}
	
}
