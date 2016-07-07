package com.funo.park.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.funo.park.MainActivity;
import com.funo.park.R;
import com.funo.park.data.PublicVar;
import com.funo.park.util.AMapServicesUtil;
import com.funo.park.util.BaseConstant;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyPoiOverlay implements Overlay, InfoWindowAdapter, OnMarkerClickListener {

	public final static int NAV_MODE_CAR = 0;
	public final static int NAV_MODE_CAR_AND_BUS = 1;
	
	private AMap mAMap;
	private LatLonPoint mGeoPoint;
	private List<Marker> markers = new ArrayList<Marker>();

	private MainActivity mContext;

	private Marker selectedMarker;
	private LatLonPoint selectedPoint;

	private int navMode;// 0表示只有驾车导航，1表示既有驾车又有公交导航
	private int count = 0;

	private LatLonPoint startPoint;
	private LatLonPoint endPoint;
	private String startStr;
	private String endStr;

	public void hideInfoWindow() {
		if (selectedMarker != null && selectedMarker.isInfoWindowShown()) {
			selectedMarker.hideInfoWindow();
		}
	}
	
	public int getNavMode() {
		return navMode;
	}

	public void setNavMode(int navMode) {
		this.navMode = navMode;
	}

	public LatLonPoint getStartPoint() {
		// return startPoint;
		return PublicVar.loationPoint;
	}

	public void setStartPoint(LatLonPoint startPoint) {
		this.startPoint = startPoint;
	}

	public LatLonPoint getEndPoint() {
		// return endPoint;
		return selectedPoint;
	}

	public void setEndPoint(LatLonPoint endPoint) {
		this.endPoint = endPoint;
	}

	public String getStartStr() {
		return startStr;
	}

	public void setStartStr(String startStr) {
		this.startStr = startStr;
	}

	public String getEndStr() {
		return endStr;
	}

	public void setEndStr(String endStr) {
		this.endStr = endStr;
	}

	public MyPoiOverlay(Context context, AMap amap, LatLonPoint mGeoPoint) {
		this.mContext = (MainActivity) context;
		this.mAMap = amap;
		this.mGeoPoint = mGeoPoint;
	}

	@Override
	public void addToMap() {

	}

	@Override
	public void removeFromMap() {
		for (Marker marker : markers) {
			marker.remove();
		}
		markers.clear();
	}

	@Override
	public void zoomToSpan() {
		if (mGeoPoint != null) {
			if (mAMap == null)
				return;
			try {
				mAMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapServicesUtil.convertToLatLng(mGeoPoint)));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public void addMarker(Object data, LatLng position, String title) {
		Marker marker = mAMap.addMarker(new MarkerOptions().position(position).title(title));
		marker.setObject(data);
		markers.add(marker);
	}

	public void addMarker(Object data, LatLng position, int icon, String title) {
		Marker marker = mAMap.addMarker(
				new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(icon)).title(title));
		marker.setObject(data);
		markers.add(marker);
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		count = 0;
		selectedMarker = marker;
		selectedPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		if (!markers.contains(marker)) {
			return null;
		}
		View infoWindow = null;
		if (navMode == 0) {
			infoWindow = LayoutInflater.from(mContext).inflate(R.layout.overlay_popup, null);
		} else if (navMode == 1) {
			infoWindow = LayoutInflater.from(mContext).inflate(R.layout.navibox, null);
		}
		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		if (!markers.contains(marker)) {
			return null;
		}
		View infoWindow = null;
		if (navMode == 0) {
			infoWindow = LayoutInflater.from(mContext).inflate(R.layout.overlay_popup, null);
		} else if (navMode == 1) {
			infoWindow = LayoutInflater.from(mContext).inflate(R.layout.navibox, null);
		}
		render(marker, infoWindow);
		return infoWindow;
	}

	public void render(Marker marker, View view) {
		if (null != selectedMarker && navMode == 0) {
			RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.popLl);
			RelativeLayout textRl = (RelativeLayout) view.findViewById(R.id.text);
			textRl.setClickable(false);
			// if (overlays.size() < 4 && overlays.size() > 0) {
			// if (selectedMarker.equals(overlays.get(0))) {
			// rl.setBackgroundResource(R.drawable.box_green);
			// } else if (selectedMarker.equals(overlays.get(1))) {
			// rl.setBackgroundResource(R.drawable.box_red);
			// } else if (selectedMarker.equals(overlays.get(2))) {
			// rl.setBackgroundResource(R.drawable.box_orange);
			// }
			// }

			final Map<String, Object> map = (Map<String, Object>) selectedMarker.getObject();
			final String name = (String) map.get("title");
			String freeNum = map.get("tel") + "";
			String price = (String) map.get("snippet") + "";
			String distance = (Integer) map.get("distance") + "米";

			TextView nameTv = (TextView) view.findViewById(R.id.park_name);
			TextView numTv = (TextView) view.findViewById(R.id.park_num);
			TextView costTv = (TextView) view.findViewById(R.id.park_cost);
			TextView distanceTv = (TextView) view.findViewById(R.id.park_distance);
			TextView park_addressTv = (TextView) view.findViewById(R.id.park_address);
			park_addressTv.setVisibility(View.GONE);
			ImageButton naviIb = (ImageButton) view.findViewById(R.id.naviIb);
//			naviIb.setVisibility(View.GONE);
			naviIb.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					ParkListReturnData prd = new ParkListReturnData();
//					prd.setName(name);
//					((MainActivity) (mContext)).popSearchRoute(selectedPoint, prd, null);
					
					mContext.searchRouteResult(getStartPoint(), getEndPoint(), RouteSearch.DrivingDefault);
					BaseConstant.routeInt = 0;
					
					selectedMarker.hideInfoWindow();
				}

			});

			String nameStr = "名称：" + name;
			String numStr = "电话：" + freeNum;
			String costStr = "地址：" + price;
			String distanceStr = "距离：" + distance;
			if ("null".equals(freeNum) || "".equals(freeNum.trim())) {
				numStr = "电话：" + "无";
			}
			if ("null".equals(price) || "".equals(price.trim())) {
				costStr = "地址：" + "无";
			}

			SpannableStringBuilder nameStyle = new SpannableStringBuilder(nameStr);
			SpannableStringBuilder numStyle = new SpannableStringBuilder(numStr);
			SpannableStringBuilder costStyle = new SpannableStringBuilder(costStr);
			SpannableStringBuilder distanceStyle = new SpannableStringBuilder(distanceStr);

			nameStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "名称：".length(), nameStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			numStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "电话：".length(), numStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			costStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "地址：".length(), costStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			distanceStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "距离：".length(), distanceStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			nameTv.setText(nameStyle);
			nameTv.setVisibility(View.VISIBLE);
			numTv.setText(numStyle);
			numTv.setVisibility(View.VISIBLE);
			costTv.setText(costStyle);
			costTv.setVisibility(View.VISIBLE);
			distanceTv.setText(distanceStyle);
			distanceTv.setVisibility(View.VISIBLE);
		} else if (null != selectedMarker && navMode == 1) {
			final TextView startTv = (TextView) view.findViewById(R.id.start);
			final TextView endTv = (TextView) view.findViewById(R.id.end);
			startTv.setText(PublicVar.currentLocationName);
			endTv.setText(getSelectedMarkerTitle());

			Button carBtn = (Button) view.findViewById(R.id.caring);
			Button busBtn = (Button) view.findViewById(R.id.bus);
			Button walkBtn = (Button) view.findViewById(R.id.walk);
			Button swapBtn = (Button) view.findViewById(R.id.swap);

			carBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (count % 2 == 0) {
						mContext.searchRouteResult(getStartPoint(), getEndPoint(), RouteSearch.DrivingDefault);
					} else {
						mContext.searchRouteResult(getEndPoint(), getStartPoint(), RouteSearch.DrivingDefault);
					}
					BaseConstant.routeInt = 0;
					
					selectedMarker.hideInfoWindow();
				}

			});
			walkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "对不起，此功能尚未实现！", Toast.LENGTH_LONG).show();
				}

			});
			busBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (count % 2 == 0) {
						mContext.searchRouteResult(getStartPoint(), getEndPoint(), RouteSearch.BusDefault);
					} else {
						mContext.searchRouteResult(getEndPoint(), getStartPoint(), RouteSearch.BusDefault);
					}
					BaseConstant.routeInt = 1;
					
					selectedMarker.hideInfoWindow();
				}

			});
			swapBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					count++;
					String indexStr = startTv.getText().toString();
					startTv.setText(endTv.getText().toString());
					endTv.setText(indexStr);
				}

			});
		}
	}

	public String getSelectedMarkerTitle() {
		String selectedMarkerTitle = "";
		if (selectedMarker != null) {
			selectedMarkerTitle = selectedMarker.getTitle();
		}
		return selectedMarkerTitle;
	}
	
	public boolean isInverted() {
		if (count % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}
	
}
