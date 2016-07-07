package com.funo.park.bicycle;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.funo.park.R;
import com.funo.park.adapter.BicycleListAdapter;
import com.funo.park.client.PDialogClass;
import com.funo.park.data.DBManager;
import com.funo.park.data.FunoConst;
import com.funo.park.data.PublicVar;
import com.funo.park.location.MyLocationSource;
import com.funo.park.location.MyLocationSource.LocationCallback;
import com.funo.park.mode.BicycleStation;
import com.funo.park.overlay.BicycleLocationOverlay;
import com.funo.park.util.AMapServicesUtil;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PublicBicycleRoadLineActivity extends Activity implements OnClickListener, OnMapClickListener {

	private MyLocationSource myLocationSource;

	private AMap aMap;
	private MapView aMapView;
	private PDialogClass pDialogClass;
	private ProgressDialog progressDialog;
	private Context context = this;
	private BicycleLocationOverlay mBicycleLocationOverlay;
	private ImageView collectIv;
	private ImageView helpIv;
	private ImageView mapIv;
	private LatLonPoint mGeoPoint;
	private LatLonPoint startPoint;
	private LatLonPoint endPoint;
	private Intent it = new Intent();
	private ListView bicycleListView;
	private ImageView locationIv;
	private ImageView listIv;
	private View floatToolView;
	private TextView searchBtn;
	private EditText searchContentEt;
	private List<BicycleStation> searchList = new ArrayList();
	private List<BicycleStation> mBicycleStationList = new ArrayList();
	private BicycleListAdapter bicycleListAdapter;
	private boolean is_initMap = false;
	private boolean is_dragEnd = false;
	private int mode = RouteSearch.WalkDefault;
	private DBManager dbManager;

	private RouteSearch mRouteSearch;
	private WalkRouteOverlay walkRouteOverlay;

	private LatLonPoint indexG[] = { new LatLonPoint(26.082592, 119.304217) };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_publicbicycle);

		mRouteSearch = new RouteSearch(this);

		LayoutInflater inflater = LayoutInflater.from(context);
		floatToolView = findViewById(R.id.publicbicycle_float_tool);
		searchBtn = (TextView) findViewById(R.id.publicbicycle_search_btn);
		searchBtn.setOnClickListener(this);
		searchContentEt = (EditText) findViewById(R.id.publicbicycle_search_content);
		dbManager = new DBManager(context);
		aMapView = (MapView) this.findViewById(R.id.bicycle_mapView);
		aMapView.onCreate(savedInstanceState);// 此方法必须重写
		aMap = aMapView.getMap();
		aMap.setOnMapClickListener(this);
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMapServicesUtil.convertToLatLng(indexG[0]), 14));
		myLocationSource = new MyLocationSource(this, false);
		aMap.setLocationSource(myLocationSource);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		collectIv = (ImageView) findViewById(R.id.publicbicycle_collect);
		collectIv.setOnClickListener(this);
		helpIv = (ImageView) findViewById(R.id.publicbicycle_help);
		helpIv.setOnClickListener(this);
		mapIv = (ImageView) findViewById(R.id.publicbicycle_map);
		mapIv.setOnClickListener(this);
		locationIv = (ImageView) findViewById(R.id.publicbicycle_location);
		locationIv.setOnClickListener(this);
		listIv = (ImageView) findViewById(R.id.publicbicycle_list);
		listIv.setOnClickListener(this);
		initMapListData(dbManager.query("", 0));
		Bundle bd = this.getIntent().getExtras();
		if (bd != null) {
			double lat = Double.valueOf(bd.getString("lat"));
			double lon = Double.valueOf(bd.getString("lon"));
			LatLonPoint geoPoint = new LatLonPoint(lat, lon);
			pDialogClass = new PDialogClass(context, mRoadLineHandler, true);
			progressDialog = pDialogClass.showProgressDialogWithB(15, "正在获取路线,请稍后...");
			searchRouteResult(PublicVar.loationPoint, geoPoint, mRoadLineHandler);
		}
		
		myLocationSource.requestLocation(new LocationCallback() {

			@Override
			public void syncLocation(AMapLocation amapLocation) {
				mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
				PublicVar.loationPoint = mGeoPoint;
				PublicVar.currentLocationName = amapLocation.getPoiName();
			}
			
			@Override
			public void doCallback(AMapLocation amapLocation) {
				aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
				new Thread(new Runnable() {

					@Override
					public void run() {
						Message msg = new Message();
						msg.what = FunoConst.Location_Ok;
						handler.sendMessage(msg);
					}

				}).start();
			}

		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.publicbicycle_collect:
			it.putExtra("lat", PublicVar.loationPoint.getLatitude());
			it.putExtra("lon", PublicVar.loationPoint.getLongitude());
			it.setClass(context, PublicBicycleCollectActivity.class);
			startActivity(it);
			break;
		case R.id.publicbicycle_list:
			aMapView.setVisibility(8);
			floatToolView.setVisibility(8);
			bicycleListView.setVisibility(0);
			break;
		case R.id.publicbicycle_map:
			aMapView.setVisibility(0);
			floatToolView.setVisibility(0);
			bicycleListView.setVisibility(8);
			break;
		case R.id.publicbicycle_search_btn:
			String content = searchContentEt.getText().toString().trim();
			if (content.equals("")) {
				Toast.makeText(context, "请输入您要查找的站点名称", Toast.LENGTH_SHORT).show();
			} else {
				searchBicycleStation(content);
			}
			break;
		case R.id.publicbicycle_location:
			pDialogClass = new PDialogClass(context, handler, true);
			progressDialog = pDialogClass.showProgressDialogWithB(15, "正在定位您的位置,请稍后...");
			myLocationSource.requestLocation(new LocationCallback() {

				@Override
				public void syncLocation(AMapLocation amapLocation) {
					mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
					PublicVar.loationPoint = mGeoPoint;
					PublicVar.currentLocationName = amapLocation.getPoiName();
				}
				
				@Override
				public void doCallback(AMapLocation amapLocation) {
					aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
					new Thread(new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							msg.what = FunoConst.Location_Ok;
							handler.sendMessage(msg);
							
							if (walkRouteOverlay != null) {
								walkRouteOverlay.removeFromMap();
							}
						}

					}).start();
				}

			});
			break;
		case R.id.publicbicycle_help:
			it.setClass(context, PublicBicycleHelpActivity.class);
			startActivity(it);
			break;
		}
	}

	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FunoConst.RequestSuccess:
				if (((List<BicycleStation>) msg.obj).size() == 0) {
					Toast.makeText(context, "抱歉,没有找到您要的站点", Toast.LENGTH_SHORT).show();
				} else {
					if (walkRouteOverlay != null) {
						walkRouteOverlay.removeFromMap();
					}
					
					mBicycleLocationOverlay.removeFromMap();
					mBicycleLocationOverlay.addOverlayItems((List<BicycleStation>) msg.obj);
					mBicycleLocationOverlay.addToMap();
					mBicycleLocationOverlay.zoomToSpan();
					mBicycleStationList.clear();
					mBicycleStationList.addAll((List<BicycleStation>) msg.obj);
					bicycleListAdapter.notifyDataSetChanged();
					double lat = Double.valueOf(((List<BicycleStation>) msg.obj).get(0).getLat());
					double lon = Double.valueOf(((List<BicycleStation>) msg.obj).get(0).getLon());
					aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(lat, lon)));
				}
				// 先隐藏键盘
				((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
						PublicBicycleRoadLineActivity.this.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
				break;
			case FunoConst.Location_Ok:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					pDialogClass.setMb(false);
					initMapListData(dbManager.query("", 0));
				}
				break;
			}
		}

	};

	private Handler roadLineHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FunoConst.RequestSuccess:
				if (mBicycleLocationOverlay.getProgressDialog() != null
						&& mBicycleLocationOverlay.getProgressDialog().isShowing()) {
					mBicycleLocationOverlay.getProgressDialog().dismiss();
					mBicycleLocationOverlay.getpDialogClass().setMb(false);
					WalkRouteResult walkRouteResult = (WalkRouteResult) msg.obj;
					WalkPath walkPath = walkRouteResult.getPaths().get(0);
					if (walkRouteOverlay != null) {
						walkRouteOverlay.removeFromMap();
					}
					walkRouteOverlay = new WalkRouteOverlay(context, aMap, walkPath,
							walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
				}

				if (is_dragEnd) {
					WalkRouteResult walkRouteResult = (WalkRouteResult) msg.obj;
					WalkPath walkPath = walkRouteResult.getPaths().get(0);
					if (walkRouteOverlay != null) {
						walkRouteOverlay.removeFromMap();
					}
					walkRouteOverlay = new WalkRouteOverlay(context, aMap, walkPath,
							walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
					is_dragEnd = false;
				}
				break;
			case FunoConst.RequestFail:
				if (mBicycleLocationOverlay.getProgressDialog() != null
						&& mBicycleLocationOverlay.getProgressDialog().isShowing()) {
					mBicycleLocationOverlay.getProgressDialog().dismiss();
					mBicycleLocationOverlay.getpDialogClass().setMb(false);
					Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
				}
				if (is_dragEnd) {
					Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
					is_dragEnd = false;
				}
				break;
			case FunoConst.TimeOut:
				if (mBicycleLocationOverlay.getProgressDialog().isShowing()) {
					mBicycleLocationOverlay.getProgressDialog().dismiss();
					Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
				}
				break;
			case FunoConst.NetWorkIsDown:
				if (mBicycleLocationOverlay.getProgressDialog().isShowing()) {
					mBicycleLocationOverlay.getProgressDialog().dismiss();
					mBicycleLocationOverlay.getpDialogClass().setMb(false);
					Toast.makeText(context, "连接服务器失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	};

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint, final Handler mhandler) {
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				try {
					WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
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
				mhandler.sendMessage(msg);
			}

		}).start();

	}

	public void searchBicycleStation(final String str) {
		searchList.clear();
		searchList = dbManager.query(str, 0);
		Message msg = new Message();
		msg.what = FunoConst.RequestSuccess;
		msg.obj = searchList;
		handler.sendMessage(msg);
	}

	public void initMapListData(List<BicycleStation> list) {
		if (mBicycleLocationOverlay == null) {
			mBicycleLocationOverlay = new BicycleLocationOverlay(context, aMap, list, mGeoPoint, roadLineHandler,
					dbManager);
		}
		mBicycleLocationOverlay.removeFromMap();
		mBicycleLocationOverlay.addToMap();
		mBicycleLocationOverlay.zoomToSpan();
		bicycleListView = (ListView) findViewById(R.id.publicbicycle_lv);
		mBicycleStationList.clear();
		mBicycleStationList.addAll(list);
		bicycleListAdapter = new BicycleListAdapter(context, R.layout.item_bicycle_list, mBicycleStationList, dbManager,
				PublicVar.loationPoint, mmRoadLineHandler, aMapView, floatToolView, bicycleListView);
		bicycleListView.setAdapter(bicycleListAdapter);
		bicycleListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				View v = arg1.findViewById(R.id.bicycle_list_pic_l);//
				View tv = arg1.findViewById(R.id.bicycle_list_text_l);
				if (v.getVisibility() == 0) {
					v.setVisibility(8);
				} else {
					v.setVisibility(0);
				}
			}

		});
	}

	private Handler mRoadLineHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FunoConst.RequestSuccess:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					pDialogClass.setMb(false);
					WalkRouteResult walkRouteResult = (WalkRouteResult) msg.obj;
					WalkPath walkPath = walkRouteResult.getPaths().get(0);
					if (walkRouteOverlay != null) {
						walkRouteOverlay.removeFromMap();
					}
					walkRouteOverlay = new WalkRouteOverlay(context, aMap, walkPath,
							walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
				}
				break;
			case FunoConst.RequestFail:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					pDialogClass.setMb(false);
					Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
				}
				break;
			case FunoConst.TimeOut:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
				}
				break;
			case FunoConst.NetWorkIsDown:
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
					pDialogClass.setMb(false);
					Toast.makeText(context, "连接服务器失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}

	};

	private Handler mmRoadLineHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case FunoConst.RequestSuccess:
				if (bicycleListAdapter.getProgressDialog().isShowing()) {
					bicycleListAdapter.getProgressDialog().dismiss();
					bicycleListAdapter.getpDialogClass().setMb(false);
					WalkRouteResult walkRouteResult = (WalkRouteResult) msg.obj;
					WalkPath walkPath = walkRouteResult.getPaths().get(0);
					if (walkRouteOverlay != null) {
						walkRouteOverlay.removeFromMap();
					}
					walkRouteOverlay = new WalkRouteOverlay(context, aMap, walkPath,
							walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
					walkRouteOverlay.removeFromMap();
					walkRouteOverlay.addToMap();
					walkRouteOverlay.zoomToSpan();
				}
				break;
			case FunoConst.RequestFail:
				if (bicycleListAdapter.getProgressDialog().isShowing()) {
					bicycleListAdapter.getProgressDialog().dismiss();
					bicycleListAdapter.getpDialogClass().setMb(false);
					Toast.makeText(context, "查询失败", Toast.LENGTH_SHORT).show();
				}
				break;
			case FunoConst.TimeOut:
				if (bicycleListAdapter.getProgressDialog().isShowing()) {
					bicycleListAdapter.getProgressDialog().dismiss();
					Toast.makeText(context, "连接服务器超时", Toast.LENGTH_SHORT).show();
				}
				break;
			case FunoConst.NetWorkIsDown:
				if (bicycleListAdapter.getProgressDialog().isShowing()) {
					bicycleListAdapter.getProgressDialog().dismiss();
					bicycleListAdapter.getpDialogClass().setMb(false);
					Toast.makeText(context, "连接服务器失败", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}

	};

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		super.onResume();

		aMapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		super.onPause();

		aMapView.onPause();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		aMapView.onSaveInstanceState(outState);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		aMapView.onDestroy();
	}

	@Override
	public void onMapClick(LatLng arg0) {
		mBicycleLocationOverlay.hideInfoWindow();
	}

}
