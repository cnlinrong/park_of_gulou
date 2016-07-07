package com.funo.park.overlay;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkRouteResult;
import com.funo.park.R;
import com.funo.park.client.PDialogClass;
import com.funo.park.data.DBManager;
import com.funo.park.data.FunoConst;
import com.funo.park.data.PublicVar;
import com.funo.park.mode.BicycleStation;
import com.funo.park.util.AMapServicesUtil;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BicycleLocationOverlay implements Overlay, InfoWindowAdapter, OnClickListener, OnMarkerClickListener {

	private List<BicycleStation> mBicycleStations = new ArrayList<BicycleStation>();

	private AMap mAMap;
	private LatLonPoint mGeoPoint;

	private Context mContext;
	private DBManager dbManager;
	private Handler mHandler;
	private PDialogClass pDialogClass;
	private ProgressDialog progressDialog;

	private Marker selectedMarker;
	private LatLonPoint selectedPoint;

	private List<Marker> stationMarkers = new ArrayList<Marker>();

	private RouteSearch mRouteSearch;

	public BicycleLocationOverlay(Context context, AMap amap, List<BicycleStation> bicycleStations,
			LatLonPoint mGeoPoint, Handler handler, DBManager dbManager) {
		this.mContext = context;
		this.mAMap = amap;
		this.mBicycleStations = bicycleStations;
		this.mGeoPoint = mGeoPoint;
		this.mHandler = handler;
		this.dbManager = dbManager;

		mRouteSearch = new RouteSearch(context);
		
		mAMap.setOnMarkerClickListener(this);
		mAMap.setInfoWindowAdapter(this);
	}

	public void addOverlayItems(List<BicycleStation> bicycleStations) {
		mBicycleStations.clear();
		mBicycleStations.addAll(bicycleStations);
	}

	@Override
	public void addToMap() {
		for (int i = 0; i < mBicycleStations.size(); i++) {
			addBicycleMarker(mBicycleStations.get(i));
		}
	}

	@Override
	public void removeFromMap() {
		for (Marker marker : stationMarkers) {
			marker.remove();
		}
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

	private BitmapDescriptor getBicycleBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.bicycle_marker);
	}

	private void addBicycleMarker(BicycleStation bicycleStation) {
		LatLng position = new LatLng(Double.parseDouble(bicycleStation.getLat()),
				Double.parseDouble(bicycleStation.getLon()));
		Marker bicycleMarker = mAMap.addMarker(new MarkerOptions().position(position).icon(getBicycleBitmapDescriptor())
				.title(bicycleStation.getStationName()).snippet(bicycleStation.getStationAddr()));
		stationMarkers.add(bicycleMarker);
	}

	@Override
	public View getInfoContents(Marker marker) {
		if (!stationMarkers.contains(marker)) {
			return null;
		}
		View infoWindow = LayoutInflater.from(mContext).inflate(R.layout.bicycle_popup, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		if (!stationMarkers.contains(marker)) {
			return null;
		}
		View infoWindow = LayoutInflater.from(mContext).inflate(R.layout.bicycle_popup, null);
		render(marker, infoWindow);
		return infoWindow;
	}

	public void render(Marker marker, View view) {
		TextView stationTv = (TextView) view.findViewById(R.id.bicycle_popup_title);
		stationTv.setText(marker.getTitle());
		TextView addrTv = (TextView) view.findViewById(R.id.bicycle_popup_addr);
		TextView numTv = (TextView) view.findViewById(R.id.bicycle_popup_num);
		String snippet = marker.getSnippet();
		if (!TextUtils.isEmpty(snippet)) {
			String[] array = snippet.split(",");
			if (array.length > 0) {
				addrTv.setText(array[0]);
				if (array.length > 1) {
					numTv.setText("锁住总数量:" + array[1]);
				}
			}
		}
		ImageView collectIv = (ImageView) view.findViewById(R.id.bicycle_popup_collectsmall);
		collectIv.setOnClickListener(this);
		ImageView roadIv = (ImageView) view.findViewById(R.id.bicycle_popup_roadline);
		roadIv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bicycle_popup_collectsmall:
			if (dbManager.query(selectedMarker.getTitle(), 0).get(0).getCollectStatus().equals("1")) {
				Toast.makeText(mContext, "该站点已收藏", Toast.LENGTH_SHORT).show();
			} else {
				BicycleStation bs = dbManager.query(selectedMarker.getTitle(), 0).get(0);
				bs.setCollectStatus("1");
				dbManager.update(bs);
				bs = dbManager.query(selectedMarker.getTitle(), 0).get(0);
				System.out.println("bs-->" + bs.getCollectStatus());
				Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.bicycle_popup_roadline:
			pDialogClass = new PDialogClass(mContext, mHandler, true);
			progressDialog = pDialogClass.showProgressDialogWithB(15, "正在获取路线,请稍后...");
			PublicVar.map_end = true;
			searchRouteResult(mGeoPoint, selectedPoint);
			
			selectedMarker.hideInfoWindow();
			break;
		}
	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				try {
					WalkRouteQuery query = new WalkRouteQuery(fromAndTo, RouteSearch.WalkDefault);
					WalkRouteResult walkRouteResult = mRouteSearch.calculateWalkRoute(query);
					if (walkRouteResult != null && walkRouteResult.getPaths() != null
							&& !walkRouteResult.getPaths().isEmpty()) {
						msg.what = FunoConst.RequestSuccess;
						msg.obj = walkRouteResult;
					} else {
						msg.what = FunoConst.RequestFail;
					}
				} catch (AMapException e) {
					CrashReport.postCatchedException(e);
					
					msg.what = FunoConst.RequestFail;
				}
				mHandler.sendMessage(msg);
			}

		}).start();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		selectedMarker = marker;
		selectedPoint = new LatLonPoint(marker.getPosition().latitude, marker.getPosition().longitude);
		return false;
	}

	public PDialogClass getpDialogClass() {
		return pDialogClass;
	}

	public void setpDialogClass(PDialogClass pDialogClass) {
		this.pDialogClass = pDialogClass;
	}

	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public void hideInfoWindow() {
		if (selectedMarker != null && selectedMarker.isInfoWindowShown()) {
			selectedMarker.hideInfoWindow();
		}
	}
	
}
