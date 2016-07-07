package com.funo.park.location;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.tencent.bugly.crashreport.CrashReport;

import android.content.Context;
import android.util.Log;

public class MyLocationSource implements LocationSource, AMapLocationListener {

	private Context mContext;

	private OnLocationChangedListener mListener;
	private AMapLocationClient mlocationClient;
	private AMapLocationClientOption mLocationOption;

	private LocationCallback mLocationCallback;

	private boolean hasCallback = false;
	
	private boolean justLocateOnce = false;
	
	public MyLocationSource(Context context, boolean justLocateOnce) {
		this.mContext = context;
		this.justLocateOnce = justLocateOnce;
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

				if (mLocationCallback != null) {
					mLocationCallback.syncLocation(amapLocation);
					if (!hasCallback) {
						mLocationCallback.doCallback(amapLocation);
						
						hasCallback = true;
					}
				}
			} else {
				String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
				Log.e("AmapErr", errText);
				CrashReport.postCatchedException(new RuntimeException(errText));
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		this.mListener = listener;
		if (mlocationClient == null) {
			mlocationClient = new AMapLocationClient(mContext);
			mLocationOption = new AMapLocationClientOption();
			// 设置定位监听
			mlocationClient.setLocationListener(this);
			// 设置为高精度定位模式
			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
			mLocationOption.setOnceLocation(justLocateOnce);
			// 设置定位参数
			mlocationClient.setLocationOption(mLocationOption);

//			requestLocation(mLocationCallback);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mlocationClient != null) {
			mlocationClient.stopLocation();
			mlocationClient.onDestroy();
		}
		mlocationClient = null;
	}

	/**
	 * 请求定位
	 */
	public void requestLocation(LocationCallback locationCallback) {
		this.hasCallback = false;
		
		this.mLocationCallback = locationCallback;

		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用onDestroy()方法
		// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
		if (mlocationClient != null) {
			mlocationClient.startLocation();
		}
	}

	/**
	 * 定位回调接口
	 */
	public interface LocationCallback {

		public void doCallback(AMapLocation amapLocation);

		public void syncLocation(AMapLocation amapLocation);
		
	}
	
}
