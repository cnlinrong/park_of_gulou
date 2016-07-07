package com.funo.park;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMapLongClickListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.overlay.BusRouteOverlay;
import com.amap.api.maps.overlay.DrivingRouteOverlay;
import com.amap.api.maps.overlay.WalkRouteOverlay;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteBusWalkItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.amap.api.services.route.RouteSearch.WalkRouteQuery;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.funo.park.adapter.AutoCompleteAdapter;
import com.funo.park.adapter.FastParkAdapter;
import com.funo.park.adapter.ParkAdapter;
import com.funo.park.adapter.PoiSearchAdapter;
import com.funo.park.adapter.RoadListAdapter;
import com.funo.park.adapter.RouteAdapter;
import com.funo.park.client.po.ParkListReturnInfo;
import com.funo.park.client.po.ParkListReturnInfo.ParkListReturnData;
import com.funo.park.data.PublicVar;
import com.funo.park.info.AddressInfo;
import com.funo.park.info.LocationInfo;
import com.funo.park.info.RouteInfo;
import com.funo.park.info.SearchInfo;
import com.funo.park.info.WeatherInfo;
import com.funo.park.location.GeocoderService;
import com.funo.park.location.GeocoderService.GetLoationNameCallback;
import com.funo.park.location.MyLocationSource;
import com.funo.park.location.MyLocationSource.LocationCallback;
import com.funo.park.overlay.IndexOverlay;
import com.funo.park.overlay.MyItemizedOverlay;
import com.funo.park.overlay.MyPoiOverlay;
import com.funo.park.util.AMapServicesUtil;
import com.funo.park.util.AMapUtil;
import com.funo.park.util.BaseConstant;
import com.funo.park.util.DistanceUtil;
import com.funo.park.util.JsonParser;
import com.funo.park.util.ToastUtil;
import com.funo.park.weather.WeatherService;
import com.funo.park.weather.WeatherService.WeatherCallback;
import com.funo.park.widget.MyProgressBarImage;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;
import com.tencent.bugly.crashreport.CrashReport;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class MainActivity extends Activity
		implements OnMapClickListener, OnClickListener, SensorEventListener, OnMapLongClickListener {

	private static String TAG = MainActivity.class.getSimpleName();

	private final int ROUTE_TYPE_DRIVE = 0;
	private final int ROUTE_TYPE_BUS = 1;
	private final int ROUTE_TYPE_WALK = 2;

	private GeocoderService geocoderService;

	private MyLocationSource myLocationSource;

	private DrivingRouteOverlay drivingRouteOverlay;
	private BusRouteOverlay busRouteOverlay;
	private WalkRouteOverlay walkRouteOverlay;

	private Button fastIv;
	private Button voiceIv;
	private Button allIv;
	private Button setIv;
	public ImageView weatherIv;
	private ListView auto_list;
	public Button listBtn;
	public RelativeLayout autoSearch;
	private Button switchBtn;
	public LinearLayout changeLl;
	private ImageView indexIv;
	private Button change_okBtn;
	private Button change_cancelBtn;
	private Button nearBtn;
	private ImageView voiceIndex;
	private ImageView searchTv;
	private ImageView positionTv;
	private ImageButton acTv_down;
	private Button fastList_switchBtn;

	private ImageView today_weatherIv;
	private TextView today_dateTv;
	private TextView today_temperatureTv;
	private TextView today_weatherTv;
	private TextView today_washTv;

	private ImageView tom_weatherIv;
	private TextView tom_temperatureTv;

	private ImageView tom_after_weatherIv;
	private TextView tom_after_temperatureTv;

	private ListView fast_list;
	public RelativeLayout fast_list_rl;

	private AMap aMap;
	public MapView mMapView = null;
	private Handler handler;
	private ProgressDialog pd;

	private LatLonPoint mGeoPoint;

	public static final int PARK_LIST_GET_SUCCESS = 1;
	public static final int PARK_LIST_GET_FAILED = 2;
	public static final int PARK_DETAIL_GET_SUCCESS = 3;
	public static final int PARK_DETAIL_GET_FAILED = 4;
	public static final int SEARCH_ROUTE_RESULT_SUCCESS = 5;
	public static final int SEARCH_ROUTE_RESULT_FAILED = 6;
	public static final int MSG_VIEW_LONGPRESS = 7;
	public static final int MSG_VIEW_ADDRESSNAME_FAIL = 8;
	public static final int MSG_VIEW_ADDRESSNAME = 9;
	public static final int MSG_VIEW_GET_ADDRESSNAME = 10;
	public static final int POI_LIST_GET_SUCCESS = 11;
	public static final int MSG_VIEW_GET_POI_ADDRESSNAME = 12;
	public static final int MSG_VIEW_CLEAR = 13;
	public static final int MSG_WEATHER_DATA = 14;
	public static final int MSG_WEATHER_DATA_FAIL = 15;
	public static final int FIRST_LOCATION = 1002;
	private PopupWindow pw;
	private PopupWindow searchPw;
	private int width;
	private int height;

	private IndexOverlay indexOverlay;// 位置层
	private MyItemizedOverlay parkOverlay;// 停车场位置层
	private MyItemizedOverlay naviOverlay; // 停车场导航层
	private MyPoiOverlay myPoiOverlay; // poi导航层
	private DriveRouteResult mDriveRouteResult;
	private BusRouteResult mBusRouteResult;
	private WalkRouteResult mWalkRouteResult;
	private RouteSearch mRouteSearch;
	public LatLonPoint longPressPoint;

	private boolean isChange = false;

	private SensorManager mSensorManager;
	public SQLiteDatabase indexDB;
	private String dataIndexName = BaseConstant.DATA_BASE_NAME;
	private String dataPath = BaseConstant.DATA_BASE_PATH;
	private ArrayList<SearchInfo> arrSi = new ArrayList<SearchInfo>();
	private AutoCompleteAdapter adapter;
	private AutoCompleteTextView autoTextView;

	private Button searchIbtn;
	private EditText searchEt;
	private ListView listView;
	private PoiResult result02;
	private TextView searchTv02;
	private LinearLayout searchLL;
	private String searchStr;
	private ArrayList<LocationInfo> itemLocationList = new ArrayList<LocationInfo>();
	List<PoiItem> poi = new ArrayList<PoiItem>();
	InputMethodManager imm;

	private List<ParkListReturnData> list;
	private List<ParkListReturnData> filterList;
	private ArrayList<LocationInfo> alInfo;
	private TextView nearTv;

	private int draws[] = new int[4];

	private PopupWindow pathPw;
	View pathView;
	ImageView travelIv;
	TextView travelTv;
	TextView postionTv;
	TextView distanceTv;
	ListView pathListView;
	public Button pathBtn;
	ArrayList<RouteInfo> routeItemList = new ArrayList<RouteInfo>();
	private TextView longPressTv;
	private LatLonPoint searchGp;
	private int index = 0;
	private MediaPlayer media = new MediaPlayer();
	private LinearLayout nearLl;
	private Button greenBtn;
	private Button grayBtn;
	private LatLonPoint indexG[] = { new LatLonPoint(26.082592, 119.304217) };
	private Drawable indexDraws[] = new Drawable[1];
	private TextView continueTv;
	private ImageView reLoadIv;
	private LinearLayout sucLoad;
	private RelativeLayout failLoad;
	private MyProgressBarImage myProgress;

	MyProgressBarImage popMyProgress;
	ImageView popLoadIv;
	RelativeLayout popFailLoad;
	LinearLayout popSucLoad;
	TextView popDateTv;
	TextView popWash;
	ImageView popWeather;
	TextView popWeatherTv;
	TextView popTemperatureTv;
	ImageView popTom_weather;
	TextView popTom_temperatureTv;
	ImageView popTom_after_weather;
	TextView popTom_after_temperatureTv;

	private LinearLayout fastList_btn_ll;
	private Button preBtn;
	private Button nextBtn;
	private List<List<PoiItem>> poiArr = new ArrayList<List<PoiItem>>();
	private int selectCount = 1;

	private boolean is_poi = false;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		mRouteSearch = new RouteSearch(this);
		geocoderService = new GeocoderService(this);

		fastList_btn_ll = (LinearLayout) this.findViewById(R.id.fastList_btn_ll);
		preBtn = (Button) this.findViewById(R.id.preBtn);
		preBtn.setOnClickListener(this);
		nextBtn = (Button) this.findViewById(R.id.nextBtn);
		nextBtn.setOnClickListener(this);

		reLoadIv = (ImageView) this.findViewById(R.id.loadIv);
		reLoadIv.setOnClickListener(this);
		sucLoad = (LinearLayout) this.findViewById(R.id.sucLoad);
		failLoad = (RelativeLayout) this.findViewById(R.id.failLoad);
		myProgress = (MyProgressBarImage) this.findViewById(R.id.loadPb);

		continueTv = (TextView) this.findViewById(R.id.continueTv);
		continueTv.setOnClickListener(this);
		longPressTv = (TextView) this.findViewById(R.id.long_press_tv);

		draws[0] = R.drawable.p_green;
		draws[1] = R.drawable.p_red;
		draws[2] = R.drawable.p_orange;
		draws[3] = R.drawable.p_gray;

		indexDraws[0] = getResources().getDrawable(R.drawable.government);

		nearTv = (TextView) this.findViewById(R.id.nearTv);
		nearLl = (LinearLayout) this.findViewById(R.id.nearll);
		greenBtn = (Button) this.findViewById(R.id.greenBtn);
		grayBtn = (Button) this.findViewById(R.id.grayBtn);

		greenBtn.setOnClickListener(this);
		grayBtn.setOnClickListener(this);

		acTv_down = (ImageButton) this.findViewById(R.id.acTv_down);
		acTv_down.setOnClickListener(this);
		today_weatherIv = (ImageView) this.findViewById(R.id.weatherIv);
		today_dateTv = (TextView) this.findViewById(R.id.dateTv);
		today_temperatureTv = (TextView) this.findViewById(R.id.temperatureTv);
		today_weatherTv = (TextView) this.findViewById(R.id.weatherTv);
		today_washTv = (TextView) this.findViewById(R.id.wash);

		tom_weatherIv = (ImageView) this.findViewById(R.id.tom_weatherIv);
		tom_temperatureTv = (TextView) this.findViewById(R.id.tom_temperatureTv);

		tom_after_weatherIv = (ImageView) this.findViewById(R.id.tom_after_weather);
		tom_after_temperatureTv = (TextView) this.findViewById(R.id.tom_after_temperatureTv);

		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();

		BaseConstant.width = width;
		BaseConstant.height = height;
		initHandler();
		mMapView = (MapView) findViewById(R.id.mapView);
		mMapView.onCreate(savedInstanceState);// 此方法必须重写
		aMap = mMapView.getMap();
		aMap.setOnMapClickListener(this);
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMapServicesUtil.convertToLatLng(indexG[0]), 14));
		myLocationSource = new MyLocationSource(this, false);
		aMap.setLocationSource(myLocationSource);
		aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		aMap.setOnMapLongClickListener(this);

		parkOverlay = new MyItemizedOverlay(this, aMap, mGeoPoint, 0);
		naviOverlay = new MyItemizedOverlay(this, aMap, mGeoPoint, 1);

		indexOverlay = new IndexOverlay(aMap, indexG[0]);
		indexOverlay.addToMap();

		myPoiOverlay = new MyPoiOverlay(this, aMap, mGeoPoint);

		fastIv = (Button) findViewById(R.id.onekey_pak);
		voiceIv = (Button) findViewById(R.id.voice_pak);
		allIv = (Button) findViewById(R.id.all_pak);
		setIv = (Button) findViewById(R.id.set_park);

		weatherIv = (ImageView) this.findViewById(R.id.weather);

		fastIv.setOnClickListener(this);
		voiceIv.setOnClickListener(this);
		allIv.setOnClickListener(this);
		setIv.setOnClickListener(this);
		weatherIv.setOnClickListener(this);

		auto_list = (ListView) this.findViewById(R.id.auto_list);
		listBtn = (Button) this.findViewById(R.id.listBtn);
		listBtn.setOnClickListener(this);
		autoSearch = (RelativeLayout) this.findViewById(R.id.autosearch);

		switchBtn = (Button) this.findViewById(R.id.switchBtn);
		switchBtn.setOnClickListener(this);

		changeLl = (LinearLayout) this.findViewById(R.id.change);
		indexIv = (ImageView) this.findViewById(R.id.indexIv);
		indexIv.setTag(R.drawable.state_fast);
		indexIv.setOnClickListener(this);

		change_okBtn = (Button) this.findViewById(R.id.ok);
		change_cancelBtn = (Button) this.findViewById(R.id.cancel);
		change_okBtn.setOnClickListener(this);
		change_cancelBtn.setOnClickListener(this);

		fast_list = (ListView) this.findViewById(R.id.fastList_list);
		fast_list_rl = (RelativeLayout) this.findViewById(R.id.fastList);

		nearBtn = (Button) this.findViewById(R.id.near);
		nearBtn.setOnClickListener(this);

		voiceIndex = (ImageView) this.findViewById(R.id.voiceIv);

		positionTv = (ImageView) this.findViewById(R.id.position);
		positionTv.setOnClickListener(this);
		searchTv = (ImageView) this.findViewById(R.id.search);
		searchTv.setOnClickListener(this);

		fastList_switchBtn = (Button) this.findViewById(R.id.fastList_switchBtn);
		fastList_switchBtn.setOnClickListener(this);

		autoTextView = (AutoCompleteTextView) this.findViewById(R.id.acTv);

		pathBtn = (Button) this.findViewById(R.id.path);
		pathBtn.setOnClickListener(this);
		openDB();
		Cursor searchCur = indexDB.query(BaseConstant.DATA_TABLE_NAME_S, new String[] { "*" }, "isDel = ?",
				new String[] { "0" }, null, null, "_id asc");
		while (searchCur.moveToNext()) {
			if (arrSi.size() < 20) {
				SearchInfo searchInfo = new SearchInfo();
				searchInfo.setId(searchCur.getInt(0));
				searchInfo.setKey(searchCur.getString(1));
				searchInfo.setIsDel(searchCur.getInt(2));
				arrSi.add(searchInfo);
			}
		}
		searchCur.close();
		adapter = new AutoCompleteAdapter(this, arrSi);
		autoTextView.setAdapter(adapter);
		pathViewInit();
		imm = (InputMethodManager) autoTextView.getContext().getSystemService(INPUT_METHOD_SERVICE);
		indexIv.setFocusable(true);
		indexIv.setFocusableInTouchMode(true);
		indexIv.requestFocus();
		indexIv.requestFocusFromTouch();

		Uri uri = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.shake);
		try {
			media.setDataSource(this, uri);
			media.prepare();
			media.setLooping(false);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		WeatherService weatherService = new WeatherService(MainActivity.this, new WeatherCallback() {

			@Override
			public void doSuccessCallback(List<WeatherInfo> weatherInfos) {
				BaseConstant.weatherInfos.clear();
				BaseConstant.weatherInfos.addAll(weatherInfos);
				Message msg = new Message();
				msg.what = MSG_WEATHER_DATA;
				handler.sendMessage(msg);
			}

			@Override
			public void doFailCallback(String msg) {
				ToastUtil.show(MainActivity.this, msg);
			}

		});
		weatherService.search();

		getWeatherData(today_dateTv, today_washTv, today_weatherIv, today_weatherTv, today_temperatureTv, tom_weatherIv,
				tom_temperatureTv, tom_after_weatherIv, tom_after_temperatureTv);

		if (BaseConstant.index == 0) {
			initPark();

			showProgressDialog("", "正在定位中，请稍后！");
			myLocationSource.requestLocation(new LocationCallback() {

				@Override
				public void syncLocation(AMapLocation amapLocation) {
					mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
					PublicVar.loationPoint = mGeoPoint;
					PublicVar.currentLocationName = amapLocation.getPoiName();
				}

				@Override
				public void doCallback(AMapLocation amapLocation) {
					aMap.moveCamera(CameraUpdateFactory
							.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
					new Thread(new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							msg.what = FIRST_LOCATION;
							msg.arg1 = 1;
							handler.sendMessage(msg);
						}

					}).start();
				}

			});
		} else if (BaseConstant.index == 2) {
			initPoi(MyPoiOverlay.NAV_MODE_CAR);

			showProgressDialog("", "正在定位中，请稍后！");
			myLocationSource.requestLocation(new LocationCallback() {

				@Override
				public void syncLocation(AMapLocation amapLocation) {
					mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
					PublicVar.loationPoint = mGeoPoint;
					PublicVar.currentLocationName = amapLocation.getPoiName();
				}

				@Override
				public void doCallback(AMapLocation amapLocation) {
					aMap.moveCamera(CameraUpdateFactory
							.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
					new Thread(new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							msg.what = FIRST_LOCATION;
							msg.arg1 = 3;
							handler.sendMessage(msg);
						}

					}).start();
				}

			});
		} else if (BaseConstant.index == 3) {
			showProgressDialog("", "正在定位中，请稍后！");
			myLocationSource.requestLocation(new LocationCallback() {

				@Override
				public void syncLocation(AMapLocation amapLocation) {
					mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
					PublicVar.loationPoint = mGeoPoint;
					PublicVar.currentLocationName = amapLocation.getPoiName();
				}

				@Override
				public void doCallback(AMapLocation amapLocation) {
					aMap.moveCamera(CameraUpdateFactory
							.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
					new Thread(new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							msg.what = FIRST_LOCATION;
							msg.arg1 = 4;
							handler.sendMessage(msg);
						}

					}).start();
				}

			});
		}
	}

	private void getParkListData() {
		BaseConstant.init(MainActivity.this);
		if ("停车场自动搜索".equals(BaseConstant.homePage)) {
			indexIv.setImageResource(R.drawable.state_fast);
			indexIv.setTag(R.drawable.state_fast);
			showProgressDialog("", "停车场查询中，请稍后...");
			autoSearch.setVisibility(View.VISIBLE);
			mMapView.setVisibility(View.GONE);
			weatherIv.setVisibility(View.GONE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
			voiceIndex.setVisibility(View.GONE);
			nearTv.setText("周边停车场");
			imm.hideSoftInputFromWindow(autoTextView.getWindowToken(), 0);
			getWeatherData(today_dateTv, today_washTv, today_weatherIv, today_weatherTv, today_temperatureTv,
					tom_weatherIv, tom_temperatureTv, tom_after_weatherIv, tom_after_temperatureTv);
			getParkData(9);
		} else if ("一键停车".equals(BaseConstant.homePage)) {
			indexIv.setImageResource(R.drawable.state_fast);
			indexIv.setTag(R.drawable.state_fast);
			autoSearch.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
			voiceIndex.setVisibility(View.GONE);
			mMapView.post(new Runnable() {

				@Override
				public void run() {
					fastPop();
				}

			});
		} else if ("语音停车".equals(BaseConstant.homePage)) {
			indexIv.setImageResource(R.drawable.state_speech);
			indexIv.setTag(R.drawable.state_speech);
			autoSearch.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
			voiceIndex.setVisibility(View.VISIBLE);
			voiceData();
		}
	}

	private void pathViewInit() {
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View pathView = inflater.inflate(R.layout.routebox, null);
		travelIv = (ImageView) pathView.findViewById(R.id.travelIv);
		travelTv = (TextView) pathView.findViewById(R.id.travelTv);
		postionTv = (TextView) pathView.findViewById(R.id.postionTv);
		distanceTv = (TextView) pathView.findViewById(R.id.distance);
		pathListView = (ListView) pathView.findViewById(R.id.pathlv);

		pathPw = new PopupWindow(pathView, (int) (width * 0.9), (int) (height * 0.7), true);
		pathPw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pathPw.setOutsideTouchable(false); // 设置是否允许在外点击使其消失

		ImageView closeIv = (ImageView) pathView.findViewById(R.id.path_quit);
		closeIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pathPw.dismiss();
			}

		});
	}

	private void fastPop() {
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View view = inflater.inflate(R.layout.fastbox, null);

		pw = new PopupWindow(view, 380, 330, true);
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pw.setOutsideTouchable(true); // 设置是否允许在外点击使其消失
		pw.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER, 0, 0);

		ImageView narrowIv = (ImageView) view.findViewById(R.id.narrow);
		narrowIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}

		});

		ImageView parkingIv = (ImageView) view.findViewById(R.id.parking);
		parkingIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
				index = 0;
				showProgressDialog("", "正在定位中，请稍后！");
				myLocationSource.requestLocation(new LocationCallback() {

					@Override
					public void syncLocation(AMapLocation amapLocation) {
						mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
						PublicVar.loationPoint = mGeoPoint;
						PublicVar.currentLocationName = amapLocation.getPoiName();
					}

					@Override
					public void doCallback(AMapLocation amapLocation) {
						aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
								new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
						new Thread(new Runnable() {

							@Override
							public void run() {
								Message msg = new Message();
								msg.what = FIRST_LOCATION;
								msg.arg1 = 1;
								handler.sendMessage(msg);
							}

						}).start();
					}

				});
			}

		});
	}

	public void showProgressDialog(String title, String message) {
		hideProgressDialog();
		pd = new ProgressDialog(MainActivity.this);
		pd.setTitle(title);
		pd.setMessage(message);
		pd.show();
	}

	private void hideProgressDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}

	public void popAutoPark(final LatLonPoint pt, ParkListReturnData prd) {
		Message msg = new Message();
		msg.what = MSG_VIEW_CLEAR;
		msg.arg1 = 10001;
		msg.obj = prd;
		handler.sendMessage(msg);
	}

	public void moveTo(LocationInfo lif, final LatLonPoint pt) {
		Message msg = new Message();
		msg.what = MSG_VIEW_CLEAR;
		msg.arg1 = 10002;
		msg.obj = lif;
		handler.sendMessage(msg);
	}

	public void popPoiSearchRoute(final LatLonPoint pt, final LocationInfo lif) {
		Message msg = new Message();
		msg.what = MSG_VIEW_CLEAR;
		handler.sendMessage(msg);

		aMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapServicesUtil.convertToLatLng(pt)));

		showProgressDialog("", "获取当前地址中,请稍后...");
		myPoiOverlay.setStartPoint(mGeoPoint);
		myPoiOverlay.setEndPoint(pt);
		new Thread(new Runnable() {

			@Override
			public void run() {
				String addressName = "";

				int count = 0;
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					addressName = PublicVar.currentLocationName;
					// 请求五次获取不到结果就返回
					if ("".equals(addressName) && count > 5) {
						Message msg1 = new Message();
						msg1.what = MSG_VIEW_ADDRESSNAME_FAIL;
						msg1.obj = "网络连接异常，请重新获取数据!";
						handler.sendMessage(msg1);
						break;
					} else if ("".equals(addressName)) {
						continue;
					} else {
						break;
					}
				}
				if (!"".equals(addressName) || count < 5) {
					AddressInfo addressInfo = new AddressInfo();
					Message msg = new Message();
					msg.what = MSG_VIEW_GET_POI_ADDRESSNAME;
					addressInfo.setGeoPoint(pt);
					addressInfo.setAddress(addressName);
					addressInfo.setEndStr(lif.getTitle());
					msg.obj = addressInfo;
					handler.sendMessage(msg);
				}
			}

		}).start();
	}

	public void popSearchRoute(final LatLonPoint pt, ParkListReturnData prd, LocationInfo lif) {
		Message msg = new Message();
		msg.what = MSG_VIEW_CLEAR;
		handler.sendMessage(msg);
		aMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapServicesUtil.convertToLatLng(pt)));
		showProgressDialog("", "获取当前地址中,请稍后...");// z正在获取当前地址,请耐心等待...
		naviOverlay.setStartPoint(mGeoPoint);
		naviOverlay.setEndPoint(pt);
		if (prd != null) {
			naviOverlay.setEndStr(BaseConstant.changeLength(prd.getName(), 8));
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				String addressName = "";

				int count = 0;
				while (true) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					count++;
					addressName = PublicVar.currentLocationName;
					// 请求五次获取不到结果就返回
					if ("".equals(addressName) && count > 5) {
						Message msg1 = new Message();
						msg1.what = MSG_VIEW_ADDRESSNAME_FAIL;
						msg1.obj = "网络连接异常，请重新获取数据!";
						handler.sendMessage(msg1);
						break;
					} else if ("".equals(addressName)) {
						continue;
					} else {
						break;
					}
				}
				if (!"".equals(addressName) || count < 5) {
					AddressInfo addressInfo = new AddressInfo();
					Message msg = new Message();
					msg.what = MSG_VIEW_GET_ADDRESSNAME;
					addressInfo.setGeoPoint(pt);
					addressInfo.setAddress(addressName);
					msg.obj = addressInfo;
					handler.sendMessage(msg);
				}
			}

		}).start();
	}

	public void searchRouteResult(LatLonPoint endPoint, int mode) {
		searchRouteResult(mGeoPoint, endPoint, mode);
		aMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapServicesUtil.convertToLatLng(mGeoPoint)));
	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint, final int mode) {
		showProgressDialog("", "搜索最佳路线中,请稍后...");// 正在搜索最佳路线,请耐心等待...
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (BaseConstant.routeInt == ROUTE_TYPE_BUS) {
						BusRouteQuery query = new BusRouteQuery(fromAndTo, mode, BaseConstant.cityCode, 0);
						mBusRouteResult = mRouteSearch.calculateBusRoute(query);
						if (mBusRouteResult != null && mBusRouteResult.getPaths() != null
								&& !mBusRouteResult.getPaths().isEmpty()) {
							handler.sendEmptyMessage(SEARCH_ROUTE_RESULT_SUCCESS);
						} else {
							Message msg = new Message();
							msg.what = SEARCH_ROUTE_RESULT_FAILED;
							msg.arg1 = mode;
							handler.sendMessage(msg);
						}
					} else if (BaseConstant.routeInt == ROUTE_TYPE_DRIVE) {
						DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null, null, "");
						mDriveRouteResult = mRouteSearch.calculateDriveRoute(query);
						if (mDriveRouteResult != null && mDriveRouteResult.getPaths() != null
								&& !mDriveRouteResult.getPaths().isEmpty()) {
							handler.sendEmptyMessage(SEARCH_ROUTE_RESULT_SUCCESS);
						} else {
							Message msg = new Message();
							msg.what = SEARCH_ROUTE_RESULT_FAILED;
							msg.arg1 = mode;
							handler.sendMessage(msg);
						}
					} else if (BaseConstant.routeInt == ROUTE_TYPE_WALK) {
						WalkRouteQuery query = new WalkRouteQuery(fromAndTo, mode);
						mWalkRouteResult = mRouteSearch.calculateWalkRoute(query);
						if (mWalkRouteResult != null && mWalkRouteResult.getPaths() != null
								&& !mWalkRouteResult.getPaths().isEmpty()) {
							handler.sendEmptyMessage(SEARCH_ROUTE_RESULT_SUCCESS);
						} else {
							Message msg = new Message();
							msg.what = SEARCH_ROUTE_RESULT_FAILED;
							msg.arg1 = mode;
							handler.sendMessage(msg);
						}
					}
				} catch (AMapException e) {
					CrashReport.postCatchedException(e);

					Message msg = new Message();
					msg.what = SEARCH_ROUTE_RESULT_FAILED;
					msg.arg1 = mode;
					handler.sendMessage(msg);
				}
			}

		}).start();
		aMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapServicesUtil.convertToLatLng(startPoint)));
	}

	public void poiGetData(final int index) {
		showProgressDialog("", "停车场查询中，请稍后...");
		getParkData(index);
	}

	private void getParkData(final int index) {
		new Thread() {

			@Override
			public void run() {
				// 获取停车场列表数据，返回list
				try {
					SoapObject soapObject = new SoapObject("http://tempuri.org/", "GetParkInfo");
					SoapSerializationEnvelope soapSerializationEnvelope = new SoapSerializationEnvelope(120);
					soapSerializationEnvelope.bodyOut = soapObject;
					soapSerializationEnvelope.dotNet = true;
					soapSerializationEnvelope.setOutputSoapObject(soapObject);
					HttpTransportSE localHttpTransportSE = new HttpTransportSE("http://api.park.gl.gov.cn/Android/ParkInfo.asmx?WSDL");
					localHttpTransportSE.call(null, soapSerializationEnvelope);
					String str1 = soapSerializationEnvelope.getResponse().toString();
					String str2 = "{\"list\":" + str1 + "}";
					Gson gson = new Gson();
					ParkListReturnInfo parkListReturnInfo = (ParkListReturnInfo) gson.fromJson(str2, ParkListReturnInfo.class);
					if (parkListReturnInfo != null) {
						Message msg = new Message();
						msg.what = PARK_LIST_GET_SUCCESS;
						msg.obj = parkListReturnInfo;
						msg.arg1 = index;
						handler.sendMessage(msg);
					} else {
						handler.sendEmptyMessage(PARK_LIST_GET_FAILED);
					}
				} catch (Exception e) {
					CrashReport.postCatchedException(e);

					handler.sendEmptyMessage(PARK_LIST_GET_FAILED);
				}
			}

		}.start();
	}

	private List<ParkListReturnData> dealList(List<ParkListReturnData> list, int indexD) {
		List<ParkListReturnData> resultPrd = new ArrayList<ParkListReturnData>();
		for (int i = 0; i < list.size(); i++) {
			ParkListReturnData prd = list.get(i);
			if (prd.getIsqy() == null) {
				prd.setIsqy(1);
			}
			if (prd.getIsqy() == indexD) {
				resultPrd.add(prd);
			}
		}
		return resultPrd;
	}

	private List<ParkListReturnData> distanceCopmpare(List<ParkListReturnData> list, double indexD, boolean isAll) {
		double distance = 0;
		List<ParkListReturnData> resultPrd = new ArrayList<ParkListReturnData>();
		LatLonPoint geoPoint = BaseConstant.geoPoint;
		for (int i = 0; i < list.size(); i++) {
			ParkListReturnData prd = list.get(i);
			if (null != prd && null != geoPoint) {
				if (!prd.getLongitude().equals("") && !prd.getLatitude().equals(""))
					distance = DistanceUtil.getDistance(Double.valueOf(prd.getLongitude()),
							Double.valueOf(prd.getLatitude()), geoPoint.getLongitude(), geoPoint.getLatitude());
				prd.setDistance(distance);
				if (isAll) {
					resultPrd.add(prd);
				} else {
					if (distance < indexD) {
						resultPrd.add(prd);
					}
				}
			}
		}
		return resultPrd;
	}

	@SuppressLint("HandlerLeak")
	private void initHandler() {
		handler = new Handler() {

			@SuppressWarnings({ "deprecation", "unchecked" })
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == PARK_LIST_GET_SUCCESS) {
					pathBtn.setVisibility(View.GONE);
					fastList_btn_ll.setVisibility(View.GONE);
					hideProgressDialog();
					BaseConstant.init(MainActivity.this);
					if (!"停车场自动搜索".equals(BaseConstant.homePage) || isChange) {
						changeLl.setVisibility(View.VISIBLE);
						listBtn.setVisibility(View.VISIBLE);
					}
					ParkListReturnInfo parkListReturnInfo = (ParkListReturnInfo) msg.obj;
					if (parkListReturnInfo != null && parkListReturnInfo.getList() != null) {
						list = parkListReturnInfo.getList();
						if (filterList != null) {
							filterList.clear();
						}
						if ("500米以内".equals(BaseConstant.distance)) {
							filterList = distanceCopmpare(list, 500d, false);
						} else if ("200米以内".equals(BaseConstant.distance)) {
							filterList = distanceCopmpare(list, 200d, false);
						} else if ("1000米以内".equals(BaseConstant.distance)) {
							filterList = distanceCopmpare(list, 1000d, false);
						} else {
							filterList = distanceCopmpare(list, 200d, false);
						}
						list = distanceCopmpare(list, 0, true);
						ParkAdapter parkAdapter = new ParkAdapter(MainActivity.this, list, width, height);
						auto_list.setAdapter(parkAdapter);
						FastParkAdapter fastParkAdapter = new FastParkAdapter(MainActivity.this, filterList);
						fast_list.setAdapter(fastParkAdapter);
						Collections.sort(list);
						Collections.sort(filterList);
						Map<String, Object> map;
						if (msg.arg1 == 0) {
							nearTv.setVisibility(View.VISIBLE);
							nearLl.setVisibility(View.GONE);
							nearTv.setText("周边停车场");
							if (filterList != null && filterList.size() > 0) {
								int size = filterList.size();
								List<ParkListReturnData> sortList = new ArrayList<ParkListReturnData>();
								for (int i = 0; i < 3; i++) {
									if (index >= size) {
										index = 0;
										Toast.makeText(MainActivity.this, "周边停车场已搜索完毕，继续搜索将出现重复的停车场信息！",
												Toast.LENGTH_LONG).show();
										break;
									} else {
										ParkListReturnData park = filterList.get(index);
										sortList.add(park);
										index++;
									}
								}
								Collections.sort(sortList);
								int sortSize = sortList.size();
								for (int i = sortSize - 1; i >= 0; i--) {
									ParkListReturnData park = sortList.get(i);
									map = new HashMap<String, Object>();
									map.put("name", park.getName());
									map.put("price", park.getChargeSet());
									map.put("freeNum", park.getFreeNum());
									map.put("totalNum", park.getTotalNum());
									map.put("address", park.getAddress());
									map.put("type", 3);
									map.put("typeSecond", park.getIsqy());
									map.put("distance", park.getDistance());
									if (TextUtils.isEmpty(park.getLongitude())) {
										park.setLongitude("0");
									}
									if (TextUtils.isEmpty(park.getLatitude())) {
										park.setLatitude("0");
									}
									double lat = Double.valueOf(park.getLatitude());
									double lon = Double.valueOf(park.getLongitude());
									parkOverlay.addMarker(map, new LatLng(lat, lon), draws[i], park.getName());
								}
								if (pw != null) {
									pw.dismiss();
								}
							} else {
								changeLl.setVisibility(View.GONE);
								Toast.makeText(MainActivity.this, "对不起，附近没有停车场,请修改搜索范围！", Toast.LENGTH_LONG).show();
							}
						} else if (msg.arg1 == 9) {

						} else {
							if (filterList != null) {
								filterList.clear();
							}
							filterList = dealList(list, 1);
							fast_list.setAdapter(new FastParkAdapter(MainActivity.this, filterList));
							Collections.sort(filterList);
							greenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_fouce));
							greenBtn.setTextColor(getResources().getColor(R.color.white));
							grayBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab));
							grayBtn.setTextColor(getResources().getColor(R.color.black2));
							listBtn.setVisibility(View.VISIBLE);
							nearTv.setVisibility(View.GONE);
							nearLl.setVisibility(View.VISIBLE);
							int size = list.size();
							for (int i = size - 1; i >= 0; i--) {
								ParkListReturnData park = list.get(i);
								int type = 1;
								if (park.getIsqy() != null) {
									type = park.getIsqy();
								}
								map = new HashMap<String, Object>();
								map.put("name", park.getName());
								map.put("price", park.getChargeSet());
								map.put("freeNum", park.getFreeNum());
								map.put("totalNum", park.getTotalNum());
								map.put("address", park.getAddress());
								map.put("type", type);
								map.put("distance", park.getDistance());
								if (park.getLongitude().equals("") || park.getLongitude() == null)
									park.setLongitude("0");
								if (park.getLatitude().equals("") || park.getLatitude() == null)
									park.setLatitude("0");
								double lat = Double.valueOf(park.getLatitude());
								double lon = Double.valueOf(park.getLongitude());
								if (type == 1) {
									parkOverlay.addMarker(map, new LatLng(lat, lon), R.drawable.p_green,
											park.getName());
								} else if (type == 0) {
									parkOverlay.addMarker(map, new LatLng(lat, lon), R.drawable.p_gray, park.getName());
								}
							}
							changeLl.setVisibility(View.GONE);
						}
					} else {
						Toast.makeText(MainActivity.this, "网络连接异常，请重新获取数据！", Toast.LENGTH_SHORT).show();
					}
				} else if (msg.what == POI_LIST_GET_SUCCESS) {
					fastList_btn_ll.setVisibility(View.VISIBLE);
					pathBtn.setVisibility(View.GONE);
					changeLl.setVisibility(View.GONE);
					hideProgressDialog();
					if (null != list) {
						list.clear();
					}
					if (null != alInfo) {
						alInfo.clear();
					}
					alInfo = (ArrayList<LocationInfo>) msg.obj;
					Map<String, Object> map;
					if (alInfo != null && alInfo.size() == 0) {
						Toast.makeText(MainActivity.this, "对不起，没有搜索到您需要的热点！", Toast.LENGTH_LONG).show();
						nextBtn.setEnabled(false);
					} else if (alInfo != null && alInfo.size() > 0) {
						nearTv.setVisibility(View.VISIBLE);
						nearLl.setVisibility(View.GONE);
						PoiSearchAdapter poiAdapter = new PoiSearchAdapter(MainActivity.this, alInfo);
						fast_list.setAdapter(poiAdapter);
						myPoiOverlay.removeFromMap();
						for (LocationInfo info : alInfo) {
							map = new HashMap<String, Object>();
							map.put("snippet", info.getSnippet());
							map.put("tel", info.getTel());
							map.put("title", info.getTitle());
							map.put("distance", info.getDistance());

							LatLonPoint geoPoint = info.getGeoPoint();
							myPoiOverlay.addMarker(map, AMapServicesUtil.convertToLatLng(geoPoint), info.getTitle());
						}

						aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(AMapServicesUtil.convertToLatLng(mGeoPoint), 14));

						fast_list_rl.setVisibility(View.VISIBLE);
						mMapView.setVisibility(View.GONE);
						weatherIv.setVisibility(View.GONE);
						changeLl.setVisibility(View.GONE);
						listBtn.setVisibility(View.GONE);
						if (poiArr.size() > 1) {
							if (selectCount == 1) {
								preBtn.setEnabled(false);
							} else {
								preBtn.setEnabled(true);
							}
						}
						if (selectCount == result02.getPageCount()) {
							nextBtn.setEnabled(false);
						} else if (result02.getPageCount() > selectCount) {
							nextBtn.setEnabled(true);
						}
					}
				} else if (msg.what == MSG_VIEW_CLEAR) {
					pathBtn.setVisibility(View.GONE);
					if (msg.arg1 == 10001) {
						ParkListReturnData prd = (ParkListReturnData) msg.obj;
						Map<String, Object> map = new HashMap<String, Object>();
						int type = 0;
						if (prd.getIsqy() == null) {
							type = 1;
						} else {
							type = prd.getIsqy();
						}
						map.put("name", prd.getName());
						map.put("price", prd.getChargeSet());
						map.put("distance", prd.getDistance());
						map.put("freeNum", prd.getFreeNum());
						map.put("totalNum", prd.getTotalNum());
						map.put("type", type);
						map.put("address", prd.getAddress());
						double lat = Double.valueOf(prd.getLatitude());
						double lon = Double.valueOf(prd.getLongitude());
						parkOverlay.addMarker(map, new LatLng(lat, lon), prd.getName());
					} else if (msg.arg1 == 10002) {
						LocationInfo info = (LocationInfo) msg.obj;
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("snippet", info.getSnippet());
						map.put("tel", info.getTel());
						map.put("title", info.getTitle());
						map.put("distance", info.getDistance());

						LatLonPoint geoPoint = info.getGeoPoint();
						myPoiOverlay.addMarker(map, AMapServicesUtil.convertToLatLng(geoPoint), info.getTitle());
					}
				} else if (msg.what == SEARCH_ROUTE_RESULT_SUCCESS) {
					hideProgressDialog();
					pathBtn.setVisibility(View.VISIBLE);
					routeItemList.clear();
					if (BaseConstant.routeInt == ROUTE_TYPE_DRIVE) {
						travelIv.setImageResource(R.drawable.icon_car);
						travelTv.setText("驾车方案");
						if (mDriveRouteResult == null || mDriveRouteResult.getPaths().isEmpty()) {
							if (is_poi && myPoiOverlay.getNavMode() == MyPoiOverlay.NAV_MODE_CAR_AND_BUS) {
								ToastUtil.show(MainActivity.this, "距离过近，改换成步行方案");
								BaseConstant.routeInt = 2;
								searchRouteResult(mGeoPoint, myPoiOverlay.getEndPoint(), RouteSearch.WalkDefault);
							} else {
								ToastUtil.show(MainActivity.this, "未找到合适的驾车线路");
							}
						} else {
							final DrivePath drivePath = mDriveRouteResult.getPaths().get(0);
							LatLonPoint startPos = mDriveRouteResult.getStartPos();
							final LatLonPoint targetPos = mDriveRouteResult.getTargetPos();

							clearRoute();

							drivingRouteOverlay = new DrivingRouteOverlay(MainActivity.this, aMap, drivePath, startPos,
									targetPos);
							drivingRouteOverlay.addToMap();
							drivingRouteOverlay.zoomToSpan();
							int dis = (int) drivePath.getDistance();
							String distance = AMapUtil.getFriendlyLength(dis);
							distance = distance.substring(distance.indexOf("\n") + 1, distance.length());
							distanceTv.setText("路程距离:" + distance);
							for (int i = 0; i < drivePath.getSteps().size(); i++) {
								String routeStr = drivePath.getSteps().get(i).getInstruction();
								RouteInfo routeInfo = new RouteInfo();
								if (i == 0) {
									if (is_poi && myPoiOverlay.isInverted()) {
										routeInfo.setAddress("起点" + ":" + myPoiOverlay.getSelectedMarkerTitle());
									} else {
										routeInfo.setAddress("起点" + ":" + PublicVar.currentLocationName);
									}
									routeInfo.setImageId(R.drawable.fa_start_icon);
								} else {
									routeInfo.setAddress(routeStr);
									if (BaseConstant.routeInt == ROUTE_TYPE_DRIVE) {
										routeInfo.setImageId(R.drawable.fa_yy);
									} else if (BaseConstant.routeInt == ROUTE_TYPE_BUS) {
										if (routeStr.indexOf("步行") == -1) {
											routeInfo.setImageId(R.drawable.gjbus_icon);
										} else {
											routeInfo.setImageId(R.drawable.bx_icon);
										}
									}
								}
								routeItemList.add(routeInfo);
							}
							RouteInfo endRouteInfo = new RouteInfo();
							String endAddressName = null;
							if (is_poi) {
								if (myPoiOverlay.isInverted()) {
									endAddressName = PublicVar.currentLocationName;
								} else {
									endAddressName = myPoiOverlay.getSelectedMarkerTitle();
								}
							} else {
								endAddressName = parkOverlay.getSelectedMarkerTitle();
							}
							endRouteInfo.setAddress("终点:" + endAddressName);
							endRouteInfo.setImageId(R.drawable.fa_finsh_icon);
							routeItemList.add(endRouteInfo);
							pathListView.setAdapter(new RouteAdapter(MainActivity.this, routeItemList));
						}
					} else if (BaseConstant.routeInt == ROUTE_TYPE_BUS) {
						travelIv.setImageResource(R.drawable.icon_bus);
						travelTv.setText("换乘方案");
						if (mBusRouteResult == null || mBusRouteResult.getPaths().isEmpty()) {
							if (is_poi && myPoiOverlay.getNavMode() == MyPoiOverlay.NAV_MODE_CAR_AND_BUS) {
								ToastUtil.show(MainActivity.this, "距离过近，改换成步行方案");
								BaseConstant.routeInt = 2;
								searchRouteResult(mGeoPoint, myPoiOverlay.getEndPoint(), RouteSearch.WalkDefault);
							} else {
								ToastUtil.show(MainActivity.this, "未找到合适的公交线路");
							}
						} else {
							final BusPath busPath = mBusRouteResult.getPaths().get(0);
							LatLonPoint startPos = mBusRouteResult.getStartPos();
							final LatLonPoint targetPos = mBusRouteResult.getTargetPos();

							clearRoute();

							busRouteOverlay = new BusRouteOverlay(MainActivity.this, aMap, busPath, startPos,
									targetPos);
							busRouteOverlay.addToMap();
							busRouteOverlay.zoomToSpan();
							int dis = (int) busPath.getDistance();
							String distance = AMapUtil.getFriendlyLength(dis);
							distance = distance.substring(distance.indexOf("\n") + 1, distance.length());
							distanceTv.setText("路程距离:" + distance);
							List<BusStep> busSteps = busPath.getSteps();
							RouteInfo routeInfo = new RouteInfo();
							if (is_poi && myPoiOverlay.isInverted()) {
								routeInfo.setAddress("起点" + ":" + myPoiOverlay.getSelectedMarkerTitle());
							} else {
								routeInfo.setAddress("起点" + ":" + PublicVar.currentLocationName);
							}
							routeInfo.setImageId(R.drawable.fa_start_icon);
							routeItemList.add(routeInfo);
							for (int i = 0; i < busSteps.size(); i++) {
								BusStep busStep = busSteps.get(i);
								RouteBusWalkItem routeBusWalkItem = busStep.getWalk();
								if (routeBusWalkItem != null) {
									List<WalkStep> walkSteps = routeBusWalkItem.getSteps();
									if (walkSteps != null && !walkSteps.isEmpty()) {
										for (WalkStep walkStep : walkSteps) {
											routeInfo = new RouteInfo();
											routeInfo.setAddress(walkStep.getInstruction());
											routeInfo.setImageId(R.drawable.fa_yy);
											routeItemList.add(routeInfo);
										}
									}
								}
								List<RouteBusLineItem> routeBusLineItems = busStep.getBusLines();
								if (routeBusLineItems != null && !routeBusLineItems.isEmpty()) {
									for (RouteBusLineItem routeBusLineItem : routeBusLineItems) {
										String routeStr = routeBusLineItem.getBusLineName();
										routeInfo = new RouteInfo();
										routeInfo.setAddress(routeStr);
										routeInfo.setImageId(R.drawable.gjbus_icon);
										routeItemList.add(routeInfo);
										BusStationItem departureBusStation = routeBusLineItem.getDepartureBusStation();
										routeInfo = new RouteInfo();
										routeInfo.setAddress("起点站：" + departureBusStation.getBusStationName());
										routeInfo.setImageId(R.drawable.gjbus_icon);
										routeItemList.add(routeInfo);
										List<BusStationItem> busStationItems = routeBusLineItem.getPassStations();
										for (BusStationItem busStationItem : busStationItems) {
											routeInfo = new RouteInfo();
											routeInfo.setAddress("途经站点：" + busStationItem.getBusStationName());
											routeInfo.setImageId(R.drawable.gjbus_icon);
											routeItemList.add(routeInfo);
										}
										BusStationItem arrivalBusStation = routeBusLineItem.getArrivalBusStation();
										routeInfo = new RouteInfo();
										routeInfo.setAddress("终点站：" + arrivalBusStation.getBusStationName());
										routeInfo.setImageId(R.drawable.gjbus_icon);
										routeItemList.add(routeInfo);
									}
								}
							}
							RouteInfo endRouteInfo = new RouteInfo();
							String endAddressName = null;
							if (is_poi) {
								if (myPoiOverlay.isInverted()) {
									endAddressName = PublicVar.currentLocationName;
								} else {
									endAddressName = myPoiOverlay.getSelectedMarkerTitle();
								}
							} else {
								endAddressName = parkOverlay.getSelectedMarkerTitle();
							}
							endRouteInfo.setAddress("终点:" + endAddressName);
							endRouteInfo.setImageId(R.drawable.fa_finsh_icon);
							routeItemList.add(endRouteInfo);
							pathListView.setAdapter(new RouteAdapter(MainActivity.this, routeItemList));
						}
					} else if (BaseConstant.routeInt == ROUTE_TYPE_WALK) {
						travelIv.setImageResource(R.drawable.bx_icon);
						travelTv.setText("步行方案");
						if (mWalkRouteResult == null || mWalkRouteResult.getPaths().isEmpty()) {
							ToastUtil.show(MainActivity.this, "未找到合适的步行线路");
						} else {
							final WalkPath walkPath = mWalkRouteResult.getPaths().get(0);
							final LatLonPoint targetPos = mWalkRouteResult.getTargetPos();

							clearRoute();

							walkRouteOverlay = new WalkRouteOverlay(MainActivity.this, aMap, walkPath, targetPos,
									targetPos);
							walkRouteOverlay.addToMap();
							walkRouteOverlay.zoomToSpan();
							int dis = (int) walkPath.getDistance();
							String distance = AMapUtil.getFriendlyLength(dis);
							distance = distance.substring(distance.indexOf("\n") + 1, distance.length());
							distanceTv.setText("路程距离:" + distance);
							for (int i = 0; i < walkPath.getSteps().size(); i++) {
								String routeStr = walkPath.getSteps().get(i).getInstruction();
								RouteInfo routeInfo = new RouteInfo();
								if (i == 0) {
									if (is_poi && myPoiOverlay.isInverted()) {
										routeInfo.setAddress("起点" + ":" + myPoiOverlay.getSelectedMarkerTitle());
									} else {
										routeInfo.setAddress("起点" + ":" + PublicVar.currentLocationName);
									}
									routeInfo.setImageId(R.drawable.fa_start_icon);
								} else {
									routeInfo.setAddress(routeStr);
									if (BaseConstant.routeInt == ROUTE_TYPE_DRIVE) {
										routeInfo.setImageId(R.drawable.fa_yy);
									} else if (BaseConstant.routeInt == ROUTE_TYPE_BUS) {
										if (routeStr.indexOf("步行") == -1) {
											routeInfo.setImageId(R.drawable.gjbus_icon);
										} else {
											routeInfo.setImageId(R.drawable.bx_icon);
										}
									} else {
										routeInfo.setImageId(R.drawable.fa_yy);
									}
								}
								routeItemList.add(routeInfo);
							}
							RouteInfo endRouteInfo = new RouteInfo();
							String endAddressName = null;
							if (is_poi) {
								if (myPoiOverlay.isInverted()) {
									endAddressName = PublicVar.currentLocationName;
								} else {
									endAddressName = myPoiOverlay.getSelectedMarkerTitle();
								}
							} else {
								endAddressName = parkOverlay.getSelectedMarkerTitle();
							}
							endRouteInfo.setAddress("终点:" + endAddressName);
							endRouteInfo.setImageId(R.drawable.fa_finsh_icon);
							routeItemList.add(endRouteInfo);
							pathListView.setAdapter(new RouteAdapter(MainActivity.this, routeItemList));
						}
					}
				} else if (msg.what == SEARCH_ROUTE_RESULT_FAILED) {
					hideProgressDialog();
					if (RouteSearch.DrivingDefault == msg.arg1) {
						Toast.makeText(MainActivity.this, "没有查询到相关自驾线路，建议改换公交或步行前往!", Toast.LENGTH_SHORT).show();
					} else if (RouteSearch.BusDefault == msg.arg1) {
						Toast.makeText(MainActivity.this, "没有查询到相关公交线路，建议改换自驾或步行前往!", Toast.LENGTH_SHORT).show();
					}
				} else if (msg.what == PARK_LIST_GET_FAILED) {
					hideProgressDialog();
					Toast.makeText(MainActivity.this, "网络连接异常，请重新获取数据！", Toast.LENGTH_SHORT).show();
				} else if (msg.what == PARK_DETAIL_GET_SUCCESS) {
					hideProgressDialog();
					Toast.makeText(MainActivity.this, "获取详情成功", Toast.LENGTH_SHORT).show();
				} else if (msg.what == PARK_DETAIL_GET_FAILED) {
					hideProgressDialog();
					Toast.makeText(MainActivity.this, "网络连接异常，请重新获取数据", Toast.LENGTH_SHORT).show();
				} else if (msg.what == MSG_VIEW_ADDRESSNAME_FAIL) {
					hideProgressDialog();
					Toast.makeText(MainActivity.this, "网络连接异常，请重新获取数据", Toast.LENGTH_SHORT).show();
				} else if (msg.what == MSG_VIEW_GET_ADDRESSNAME) {
					changeLl.setVisibility(View.GONE);
					hideProgressDialog();
					AddressInfo addressInfo = (AddressInfo) msg.obj;
					naviOverlay.addMarker(null, AMapServicesUtil.convertToLatLng(addressInfo.getGeoPoint()), null);
					naviOverlay.setStartStr(BaseConstant.changeLength(addressInfo.getAddress(), 8));
				} else if (msg.what == MSG_VIEW_GET_POI_ADDRESSNAME) {
					hideProgressDialog();
					
					AddressInfo addressInfo = (AddressInfo) msg.obj;
					myPoiOverlay.addMarker(null, AMapServicesUtil.convertToLatLng(addressInfo.getGeoPoint()), null);
					myPoiOverlay.setStartStr(BaseConstant.changeLength(addressInfo.getAddress(), 8));
					myPoiOverlay.setEndStr(BaseConstant.changeLength(addressInfo.getEndStr(), 8));
				} else if (msg.what == MSG_VIEW_LONGPRESS) {
					if (null == longPressPoint)
						return;
					showProgressDialog("", "获取地址信息中...");
					geocoderService.getLocationName(longPressPoint, new GetLoationNameCallback() {

						@Override
						public void doSuccessCallback(String address) {
							hideProgressDialog();
							
							AddressInfo addressInfo = new AddressInfo();
							addressInfo.setAddress(address);
							addressInfo.setGeoPoint(longPressPoint);
							Message msg = new Message();
							msg.what = MSG_VIEW_ADDRESSNAME;
							msg.obj = addressInfo;
							handler.sendMessage(msg);
						}

						@Override
						public void doFailCallback(String msg) {

						}

					});
				} else if (msg.what == MSG_VIEW_ADDRESSNAME) {
					hideProgressDialog();

					isChange = false;
					final AddressInfo addressInfo = (AddressInfo) msg.obj;
					LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
					View view = inflater.inflate(R.layout.longpressbox, null);
					TextView addressTv = (TextView) view.findViewById(R.id.address);
					Button okBtn = (Button) view.findViewById(R.id.ok);
					Button exitBtn = (Button) view.findViewById(R.id.exit);
					ImageView closeIv = (ImageView) view.findViewById(R.id.close);
					searchGp = addressInfo.getGeoPoint();
					okBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pw.dismiss();
							BaseConstant.geoPoint = addressInfo.getGeoPoint();
							poiGetData(0);
						}

					});
					exitBtn.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pw.dismiss();
						}

					});
					closeIv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pw.dismiss();
						}

					});
					addressTv.setText(addressInfo.getAddress());
					pw = new PopupWindow(view, (int) (width * 0.7), (int) (height * 0.25), true);
					pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					pw.setOutsideTouchable(false); // 设置是否允许在外点击使其消失
					pw.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER, 0, 0);
					longPressTv.setVisibility(View.VISIBLE);
					changeLl.setVisibility(View.GONE);
					pw.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss() {
							longPressTv.setVisibility(View.GONE);
						}

					});
				} else if (msg.what == FIRST_LOCATION) {
					hideProgressDialog();
					
					BaseConstant.geoPoint = mGeoPoint;
					// aMap.animateCamera(CameraUpdateFactory.changeLatLng(AMapServicesUtil.convertToLatLng(mGeoPoint)));
					if (msg.arg1 == 0) {
						getParkListData();
					} else if (msg.arg1 == 1) {
						showProgressDialog("", "停车场查询中，请稍后...");
						getParkData(0);
					} else if (msg.arg1 == 2) {
						showProgressDialog("", "停车场查询中，请稍后...");
						getParkData(1);
					} else if (msg.arg1 == 3) {
						preBtn.setEnabled(false);
						nextBtn.setEnabled(true);
						poiSearch("加油站", "");
						nearTv.setText("附近加油站");
					} else if (msg.arg1 == 4) {
						getNearData();
					} else if (msg.arg1 == 5) {

					}
				} else if (msg.what == BaseConstant.ERROR) {
					hideProgressDialog();
					Toast.makeText(MainActivity.this, "当前网络连接异常，无法搜索,请稍后再试！", Toast.LENGTH_LONG).show();
					if (null != searchLL) {
						searchLL.setVisibility(View.GONE);
					}
				} else if (msg.what == BaseConstant.NOTHING) {
					hideProgressDialog();
					if (msg.arg1 == 1) {
						Toast.makeText(MainActivity.this, "对不起，没有搜索到任何数据，请修改范围重新搜索", Toast.LENGTH_LONG).show();
					} else {
						if (searchStr != null) {
							Toast.makeText(MainActivity.this, "对不起，当前地图搜索不到" + searchStr, Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(MainActivity.this, "对不起，当前地图搜索不到该地址", Toast.LENGTH_LONG).show();
						}
					}
					if (searchLL != null) {
						searchLL.setVisibility(View.GONE);
					}
				} else if (msg.what == BaseConstant.SEARCH) {
					hideProgressDialog();
					searchLL.setVisibility(View.GONE);
				} else if (msg.what == BaseConstant.POISEARCH) {
					hideProgressDialog();
					if (msg.arg1 == 1) {
						searchLL.setVisibility(View.GONE);
					}
					itemLocationList.add((LocationInfo) msg.obj);
					((RoadListAdapter) listView.getAdapter()).notifyDataSetChanged();
				} else if (msg.what == BaseConstant.POI_OTHER_SEARCH) {

				} else if (msg.what == MSG_WEATHER_DATA) {
					getWeatherData(today_dateTv, today_washTv, today_weatherIv, today_weatherTv, today_temperatureTv,
							tom_weatherIv, tom_temperatureTv, tom_after_weatherIv, tom_after_temperatureTv);
					if (popDateTv != null) {
						getWeatherData(popDateTv, popWash, popWeather, popWeatherTv, popTemperatureTv, popTom_weather,
								popTom_temperatureTv, popTom_after_weather, popTom_after_temperatureTv);
					}
				} else if (msg.what == MSG_WEATHER_DATA_FAIL) {
					BaseConstant.weatherInfos.clear();
					Toast.makeText(MainActivity.this, "网络连接异常，请重新获取数据!", Toast.LENGTH_LONG).show();
					handler.sendEmptyMessage(MSG_WEATHER_DATA);
				} else if (msg.what == BaseConstant.HIDE) {
					hideProgressDialog();
					showProgressDialog("", "热点搜索中，请稍后...");// 正在搜索,请稍后！
				}
			}

		};
	}

	public void poiSearch(final String type, final String key) {
		selectCount = 1;
		poiArr.clear();
		new Thread(new Runnable() {

			@Override
			public void run() {
				handler.sendEmptyMessage(BaseConstant.HIDE);
				try {
					PoiSearch poiSearch = new PoiSearch(MainActivity.this,
							new PoiSearch.Query(key, type, BaseConstant.cityCode)); // 设置搜索字符串，"010为城市区号"
					BaseConstant.init(MainActivity.this);
					PoiSearch.SearchBound ps = new SearchBound(BaseConstant.geoPoint, 3000);
					if ("1000米以内".equals(BaseConstant.nearDistance)) {
						ps = new SearchBound(BaseConstant.geoPoint, 1000);
					} else if ("2000米以内".equals(BaseConstant.nearDistance)) {
						ps = new SearchBound(BaseConstant.geoPoint, 2000);
					} else if ("3000米以内".equals(BaseConstant.nearDistance)) {
						ps = new SearchBound(BaseConstant.geoPoint, 3000);
					}
					poiSearch.setBound(ps);
					result02 = poiSearch.searchPOI();
				} catch (AMapException e) {
					CrashReport.postCatchedException(e);

					Message errorMsg = new Message();
					errorMsg.what = BaseConstant.ERROR;
					handler.sendMessage(errorMsg);
				}
				if (result02 != null) {
					getMorePoi(1, true);
				} else {
					Message errorMsg = new Message();
					errorMsg.what = BaseConstant.ERROR;
					handler.sendMessage(errorMsg);
				}
			}

		}).start();
	}

	private void getMorePoi(int i, boolean isLink) {
		List<PoiItem> poi = new ArrayList<PoiItem>();
		try {
			if (isLink) {
				poi = result02.getPois();
			} else {
				poi = poiArr.get(i);
			}
			int size = poi.size();
			if (size != 0 && isLink) {
				poiArr.add(poi);
			}
			ArrayList<LocationInfo> alInfo = new ArrayList<LocationInfo>();
			for (int j = 0; j < size; j++) {
				PoiItem poiItem = poi.get(j);
				String title = poiItem.getTitle();
				List<PoiItem> poiItems = new ArrayList<PoiItem>();
				LatLonPoint geoPoint = poiItem.getLatLonPoint();
				String snippet = poiItem.getSnippet();
				String tel = poiItem.getTel();
				String addressLocName = "";

				poiItems.add(poiItem);

				LocationInfo locationInfo = new LocationInfo();
				locationInfo.setTitle(title);
				locationInfo.setAddress(addressLocName);
				locationInfo.setGeoPoint(geoPoint);
				locationInfo.setNumber(j);
				locationInfo.setPoiItem(poiItems);//
				locationInfo.setSnippet(snippet);
				locationInfo.setTel(tel);
				locationInfo.setDistance(
						(int) (AMapUtils.calculateLineDistance(AMapServicesUtil.convertToLatLng(BaseConstant.geoPoint),
								AMapServicesUtil.convertToLatLng(poiItem.getLatLonPoint()))));
				alInfo.add(locationInfo);
			}
			Collections.sort(alInfo);

			Message message = new Message();
			message.obj = alInfo;
			message.what = POI_LIST_GET_SUCCESS;
			handler.sendMessage(message);
		} catch (NullPointerException e) {
			e.printStackTrace();
			Message errorMsg = new Message();
			errorMsg.what = BaseConstant.NOTHING;
			errorMsg.arg1 = 1;
			handler.sendMessage(errorMsg);
		} catch (Exception e) {
			e.printStackTrace();
			Message errorMsg = new Message();
			errorMsg.what = BaseConstant.ERROR;
			handler.sendMessage(errorMsg);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (R.id.onekey_pak == id) {
			clearMap();

			initPark();

			indexIv.setImageResource(R.drawable.state_fast);
			indexIv.setTag(R.drawable.state_fast);
			fastIv.setBackgroundResource(R.drawable.fast_fouce);
			voiceIv.setBackgroundResource(R.drawable.voice);
			allIv.setBackgroundResource(R.drawable.all);
			setIv.setBackgroundResource(R.drawable.set);

			autoSearch.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
			voiceIndex.setVisibility(View.GONE);
			fast_list_rl.setVisibility(View.GONE);
			isChange = true;
			fastPop();
		} else if (R.id.voice_pak == id) {
			clearMap();

			initPark();

			indexIv.setImageResource(R.drawable.state_speech);
			indexIv.setTag(R.drawable.state_speech);
			isChange = true;
			fastIv.setBackgroundResource(R.drawable.fast);
			voiceIv.setBackgroundResource(R.drawable.voice_fouce);
			allIv.setBackgroundResource(R.drawable.all);
			setIv.setBackgroundResource(R.drawable.set);

			autoSearch.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
			voiceIndex.setVisibility(View.VISIBLE);
			fast_list_rl.setVisibility(View.GONE);
			voiceData();
		} else if (R.id.all_pak == id) {
			clearMap();

			initPark();

			indexIv.setImageResource(R.drawable.state_all);
			indexIv.setTag(R.drawable.state_all);
			fastIv.setBackgroundResource(R.drawable.fast);
			voiceIv.setBackgroundResource(R.drawable.voice);
			allIv.setBackgroundResource(R.drawable.all_fouce);
			setIv.setBackgroundResource(R.drawable.set);

			autoSearch.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
			voiceIndex.setVisibility(View.GONE);
			fast_list_rl.setVisibility(View.GONE);
			showProgressDialog("", "正在定位中，请稍后！");
			myLocationSource.requestLocation(new LocationCallback() {

				@Override
				public void syncLocation(AMapLocation amapLocation) {
					mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
					PublicVar.loationPoint = mGeoPoint;
					PublicVar.currentLocationName = amapLocation.getPoiName();
				}

				@Override
				public void doCallback(AMapLocation amapLocation) {
					aMap.moveCamera(CameraUpdateFactory
							.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
					new Thread(new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							msg.what = FIRST_LOCATION;
							msg.arg1 = 2;
							handler.sendMessage(msg);
						}

					}).start();
				}

			});

			Message msg = new Message();
			msg.what = MSG_VIEW_CLEAR;
			handler.sendMessage(msg);
			getParkData(1);
		} else if (R.id.set_park == id) {
			fastIv.setBackgroundResource(R.drawable.fast);
			voiceIv.setBackgroundResource(R.drawable.voice);
			allIv.setBackgroundResource(R.drawable.all);
			setIv.setBackgroundResource(R.drawable.set_fouce);

			autoSearch.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
			voiceIndex.setVisibility(View.GONE);
			fast_list_rl.setVisibility(View.GONE);

			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View view = inflater.inflate(R.layout.setbox, null);

			Button confirmBtn = (Button) view.findViewById(R.id.confirm);
			Button cancelBtn = (Button) view.findViewById(R.id.cancel);

			final RadioButton rb1 = (RadioButton) view.findViewById(R.id.radio01);
			final RadioButton rb2 = (RadioButton) view.findViewById(R.id.radio02);
			final RadioButton rb3 = (RadioButton) view.findViewById(R.id.radio03);
			final RadioButton rb4 = (RadioButton) view.findViewById(R.id.radio04);
			final RadioButton rb5 = (RadioButton) view.findViewById(R.id.radio05);
			final RadioButton rb6 = (RadioButton) view.findViewById(R.id.radio06);
			final RadioButton rb7 = (RadioButton) view.findViewById(R.id.radio021);
			final RadioButton rb8 = (RadioButton) view.findViewById(R.id.radio022);
			final RadioButton rb9 = (RadioButton) view.findViewById(R.id.radio023);

			BaseConstant.init(MainActivity.this);
			if ("200米以内".equals(BaseConstant.distance)) {
				rb1.setChecked(true);
			} else if ("500米以内".equals(BaseConstant.distance)) {
				rb2.setChecked(true);
			} else if ("1000米以内".equals(BaseConstant.distance)) {
				rb3.setChecked(true);
			}

			if ("停车场自动搜索".equals(BaseConstant.homePage)) {
				rb4.setChecked(true);
			} else if ("一键停车".equals(BaseConstant.homePage)) {
				rb5.setChecked(true);
			} else if ("语音停车".equals(BaseConstant.homePage)) {
				rb6.setChecked(true);
			}
			if ("1000米以内".equals(BaseConstant.nearDistance)) {
				rb7.setChecked(true);
			} else if ("2000米以内".equals(BaseConstant.nearDistance)) {
				rb8.setChecked(true);
			} else if ("3000米以内".equals(BaseConstant.nearDistance)) {
				rb9.setChecked(true);
			}

			pw = new PopupWindow(view, (int) (width * 0.9), (int) (height * 0.7), true);
			pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			pw.setOutsideTouchable(false); // 设置是否允许在外点击使其消失
			pw.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER, 0, 0);

			cancelBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pw.dismiss();
					changeLl.setVisibility(View.VISIBLE);
					listBtn.setVisibility(View.VISIBLE);
				}

			});
			confirmBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					changeLl.setVisibility(View.GONE);
					listBtn.setVisibility(View.VISIBLE);
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("提示")
							.setMessage("你确定要保存这些设置吗？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferences sharedPreferences = getSharedPreferences("data", 0);
							Editor editor = sharedPreferences.edit();
							editor.remove("distance");
							editor.remove("homePage");
							editor.commit();

							if (rb1.isChecked()) {
								editor.putString("distance", "200米以内");
							} else if (rb2.isChecked()) {
								editor.putString("distance", "500米以内");
							} else if (rb3.isChecked()) {
								editor.putString("distance", "1000米以内");
							}
							if (rb4.isChecked()) {
								editor.putString("homePage", "停车场自动搜索");
							} else if (rb5.isChecked()) {
								editor.putString("homePage", "一键停车");
							} else if (rb6.isChecked()) {
								editor.putString("homePage", "语音停车");
							}
							if (rb7.isChecked()) {
								editor.putString("nearDistance", "1000米以内");
							} else if (rb8.isChecked()) {
								editor.putString("nearDistance", "2000米以内");
							} else if (rb9.isChecked()) {
								editor.putString("nearDistance", "3000米以内");
							}
							editor.commit();
							BaseConstant.init(MainActivity.this);
							pw.dismiss();
						}

					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}

					}).create();
					dialog.show();
				}

			});
		} else if (R.id.weather == id) {
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			View view = inflater.inflate(R.layout.weatherbox, null);

			popDateTv = (TextView) view.findViewById(R.id.dateTv);
			popWash = (TextView) view.findViewById(R.id.wash);
			popWeather = (ImageView) view.findViewById(R.id.weatherIv);
			popWeatherTv = (TextView) view.findViewById(R.id.weatherTv);
			popFailLoad = (RelativeLayout) view.findViewById(R.id.failLoad);
			popMyProgress = (MyProgressBarImage) view.findViewById(R.id.loadPb);
			popLoadIv = (ImageView) view.findViewById(R.id.loadIv);
			popSucLoad = (LinearLayout) view.findViewById(R.id.successLoad);

			popTemperatureTv = (TextView) view.findViewById(R.id.temperatureTv);

			popTom_weather = (ImageView) view.findViewById(R.id.tom_weatherIv);
			popTom_temperatureTv = (TextView) view.findViewById(R.id.tom_temperatureTv);

			popTom_after_weather = (ImageView) view.findViewById(R.id.tom_after_weather);
			popTom_after_temperatureTv = (TextView) view.findViewById(R.id.tom_after_temperatureTv);

			getWeatherData(popDateTv, popWash, popWeather, popWeatherTv, popTemperatureTv, popTom_weather,
					popTom_temperatureTv, popTom_after_weather, popTom_after_temperatureTv);

			pw = new PopupWindow(view, (int) (width * 0.9), (int) (height * 0.3), true);
			pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			pw.setOutsideTouchable(false); // 设置是否允许在外点击使其消失
			pw.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER, 0, 0);

			ImageView closeIv = (ImageView) view.findViewById(R.id.weather_quit);
			popLoadIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					popLoadIv.setVisibility(View.GONE);
					popMyProgress.setVisibility(View.VISIBLE);
					WeatherService weatherService = new WeatherService(MainActivity.this, new WeatherCallback() {

						@Override
						public void doSuccessCallback(List<WeatherInfo> weatherInfos) {
							BaseConstant.weatherInfos.clear();
							BaseConstant.weatherInfos.addAll(weatherInfos);
							Message msg = new Message();
							msg.what = MSG_WEATHER_DATA;
							handler.sendMessage(msg);
						}

						@Override
						public void doFailCallback(String msg) {
							ToastUtil.show(MainActivity.this, msg);
						}

					});
					weatherService.search();
				}

			});
			closeIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					pw.dismiss();
				}

			});
		} else if (R.id.listBtn == id) {
			fast_list_rl.setVisibility(View.VISIBLE);
			mMapView.setVisibility(View.GONE);
			weatherIv.setVisibility(View.GONE);
			changeLl.setVisibility(View.GONE);
			listBtn.setVisibility(View.GONE);
		} else if (R.id.switchBtn == id) {
			autoSearch.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			changeLl.setVisibility(View.VISIBLE);
			listBtn.setVisibility(View.VISIBLE);
		} else if (R.id.indexIv == id) {
			int tag = (Integer) indexIv.getTag();
			if (R.drawable.state_fast == tag) {
				if (autoSearch.getVisibility() != View.VISIBLE) {
					fastPop();
				}
			} else if (R.drawable.state_all == tag) {

			} else if (R.drawable.state_speech == tag) {
				voiceIndex.setVisibility(View.VISIBLE);
				voiceData();
			}
		} else if (R.id.ok == id) {
			showProgressDialog("", "停车场查询中，请稍后...");
			Message msg = new Message();
			msg.what = MSG_VIEW_CLEAR;
			handler.sendMessage(msg);

			getParkData(0);
		} else if (R.id.continueTv == id) {
			showProgressDialog("", "停车场查询中，请稍后...");
			Message msg = new Message();
			msg.what = MSG_VIEW_CLEAR;
			handler.sendMessage(msg);

			getParkData(0);
		} else if (R.id.position == id) {
			showProgressDialog("", "正在定位,请稍等...");
			myLocationSource.requestLocation(new LocationCallback() {

				@Override
				public void syncLocation(AMapLocation amapLocation) {
					mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
					PublicVar.loationPoint = mGeoPoint;
					PublicVar.currentLocationName = amapLocation.getPoiName();
				}

				@Override
				public void doCallback(AMapLocation amapLocation) {
					aMap.moveCamera(CameraUpdateFactory
							.newLatLngZoom(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
					new Thread(new Runnable() {

						@Override
						public void run() {
							Message msg = new Message();
							msg.what = FIRST_LOCATION;
							msg.arg1 = 5;
							handler.sendMessage(msg);
						}

					}).start();
				}

			});
		} else if (R.id.fastList_switchBtn == id) {
			if ("周边停车场".equals(nearTv.getText().toString()) && nearTv.getVisibility() == View.VISIBLE) {
				changeLl.setVisibility(View.VISIBLE);
			} else {
				changeLl.setVisibility(View.GONE);
			}
			fast_list_rl.setVisibility(View.GONE);
			mMapView.setVisibility(View.VISIBLE);
			weatherIv.setVisibility(View.VISIBLE);
			listBtn.setVisibility(View.VISIBLE);
		} else if (R.id.cancel == id) {
			changeLl.setVisibility(View.GONE);
		} else if (R.id.near == id) {
			getNearData();
		} else if (R.id.search == id) {
			final String searchKeyStr = autoTextView.getText().toString();
			imm.hideSoftInputFromWindow(autoTextView.getWindowToken(), 0);
			if (searchKeyStr.trim().equals("")) {
				Toast.makeText(MainActivity.this, "请输入搜索条件", Toast.LENGTH_SHORT).show();
			} else {
				itemLocationList.clear();
				Cursor cur = indexDB.query(BaseConstant.DATA_TABLE_NAME_S, new String[] { "*" }, "key = ?",
						new String[] { searchKeyStr }, null, null, null);
				if (cur.getCount() == 0) {
					ContentValues values = new ContentValues();
					values.put("key", searchKeyStr);
					values.put("isDel", 0);
					indexDB.insert(BaseConstant.DATA_TABLE_NAME_S, null, values);

					if (arrSi.size() == 20) {
						arrSi.remove(0);
					}
					SearchInfo sInfo = new SearchInfo();
					sInfo.setIsDel(0);
					sInfo.setKey(searchKeyStr);
					arrSi.add(sInfo);
				}

				LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
				final View searchView = inflater.inflate(R.layout.search, null);
				searchIbtn = (Button) searchView.findViewById(R.id.pointwhat);
				searchLL = (LinearLayout) searchView.findViewById(R.id.search_ll);
				searchTv02 = (TextView) searchView.findViewById(R.id.search_of_tv);
				searchEt = (EditText) searchView.findViewById(R.id.search);
				searchEt.setText(searchKeyStr);
				imm = (InputMethodManager) searchEt.getContext().getSystemService(INPUT_METHOD_SERVICE);

				listView = (ListView) searchView.findViewById(R.id.listview);
				listView.setAdapter(new RoadListAdapter(MainActivity.this, itemLocationList));
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						LocationInfo locationInfo = ((RoadListAdapter) listView.getAdapter()).getItem(arg2);
						try {
							if (poi != null && poi.size() > 0) {
								changeLl.setVisibility(View.GONE);
								fast_list_rl.setVisibility(View.GONE);
								mMapView.setVisibility(View.VISIBLE);
								weatherIv.setVisibility(View.VISIBLE);
								listBtn.setVisibility(View.VISIBLE);
								
								clearMap();
								initPark();

								Message msg = new Message();
								msg.what = MSG_VIEW_CLEAR;
								handler.sendMessage(msg);
								
								BaseConstant.geoPoint = locationInfo.getGeoPoint();
								showProgressDialog("", "停车场查询中，请稍后...");
								getParkData(0);
								
								LatLng position = AMapServicesUtil.convertToLatLng(locationInfo.getGeoPoint());
								parkOverlay.addMarker(null, position, R.drawable.icon_search, locationInfo.getTitle());
								aMap.animateCamera(CameraUpdateFactory.changeLatLng(position));
								
								searchPw.dismiss();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				});
				searchIbtn.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						searchStr = searchEt.getText().toString();
						searchTv02.setText("正在搜索：" + searchStr);
						imm.hideSoftInputFromWindow(searchEt.getWindowToken(), 0);
						if ("".equals(searchStr.trim())) {
							Toast.makeText(MainActivity.this, "搜索条件不能为空！", Toast.LENGTH_LONG).show();
						} else {
							itemLocationList.clear();
							searchLL.setVisibility(View.VISIBLE);
							new Thread(new Runnable() {

								@Override
								public void run() {
									try {
										PoiSearch poiSearch = new PoiSearch(MainActivity.this,
												new PoiSearch.Query(searchStr, "", BaseConstant.cityCode)); // 设置搜索字符串，"010为城市区号"
										result02 = poiSearch.searchPOI();
									} catch (AMapException e) {
										CrashReport.postCatchedException(e);

										Message errorMsg = new Message();
										errorMsg.what = BaseConstant.ERROR;
										handler.sendMessage(errorMsg);
									}
									if (result02 != null) {
										poi = result02.getPois();
										if (poi == null) {
											Message Msg = new Message();
											Msg.what = BaseConstant.NOTHING;
											handler.sendMessage(Msg);
										} else {
											int size = poi.size();
											if (size == 0) {
												Message Msg = new Message();
												Msg.what = BaseConstant.NOTHING;
												handler.sendMessage(Msg);
											}
											for (int j = 0; j < size; j++) {
												PoiItem poiItem = poi.get(j);
												String title = poiItem.getTitle();
												List<PoiItem> poiItems = new ArrayList<PoiItem>();
												LatLonPoint geoPoint = poiItem.getLatLonPoint();
												String addressLocName = poiItem.getProvinceName()
														+ poiItem.getCityName() + poiItem.getAdName()
														+ poiItem.getSnippet();
												poiItems.add(poiItem);

												LocationInfo locationInfo = new LocationInfo();
												locationInfo.setTitle(title);
												locationInfo.setAddress(addressLocName);
												locationInfo.setGeoPoint(geoPoint);
												locationInfo.setNumber(j);
												locationInfo.setPoiItem(poiItems);
												Message message = new Message();
												message.obj = locationInfo;
												message.what = BaseConstant.POISEARCH;
												if (j == size - 1) {
													message.arg1 = 1;
												} else {
													message.arg1 = 0;
												}
												handler.sendMessage(message);
											}
											if (listView.getCount() >= 1) {
												Message searchMsg = new Message();
												searchMsg.what = BaseConstant.SEARCH;
												handler.sendMessage(searchMsg);
											}
										}
									} else {
										Message errorMsg = new Message();
										errorMsg.what = BaseConstant.ERROR;
										handler.sendMessage(errorMsg);
									}
								}

							}).start();
						}
					}

				});

				searchPw = new PopupWindow(searchView, (int) (width * 0.9), (int) (height * 0.7), true);
				searchPw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
				searchPw.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER, 0, 0);

				searchLL.setVisibility(View.VISIBLE);
				searchTv02.setText("正在搜索：" + searchKeyStr);
				try {
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								PoiSearch poiSearch = new PoiSearch(MainActivity.this,
										new PoiSearch.Query(searchKeyStr, "", BaseConstant.cityCode)); // 设置搜索字符串，"010为城市区号"
								result02 = poiSearch.searchPOI();
							} catch (AMapException e) {
								CrashReport.postCatchedException(e);

								Message errorMsg = new Message();
								errorMsg.what = BaseConstant.ERROR;
								handler.sendMessage(errorMsg);
							}
							if (result02 != null) {
								poi = result02.getPois();
								if (poi == null) {
									Message Msg = new Message();
									Msg.what = BaseConstant.NOTHING;
									handler.sendMessage(Msg);
								} else {
									int size = poi.size();
									if (size == 0) {
										Message Msg = new Message();
										Msg.what = BaseConstant.NOTHING;
										handler.sendMessage(Msg);
									}
									for (int j = 0; j < size; j++) {
										PoiItem poiItem = poi.get(j);
										String title = poiItem.getTitle();
										List<PoiItem> poiItems = new ArrayList<PoiItem>();
										LatLonPoint geoPoint = poiItem.getLatLonPoint();
										String addressLocName = poiItem.getProvinceName() + poiItem.getCityName()
												+ poiItem.getAdName() + poiItem.getSnippet();
										poiItems.add(poiItem);

										LocationInfo locationInfo = new LocationInfo();
										locationInfo.setTitle(title);
										locationInfo.setAddress(addressLocName);
										locationInfo.setGeoPoint(geoPoint);
										locationInfo.setNumber(j);
										locationInfo.setPoiItem(poiItems);
										Message message = new Message();
										message.obj = locationInfo;
										message.what = BaseConstant.POISEARCH;
										if (j == size - 1) {
											message.arg1 = 1;
										} else {
											message.arg1 = 0;
										}
										handler.sendMessage(message);
										if (listView.getCount() >= 1) {
											Message searchMsg = new Message();
											searchMsg.what = BaseConstant.SEARCH;
											handler.sendMessage(searchMsg);
										}
									}
								}
							} else {
								Message errorMsg = new Message();
								errorMsg.what = BaseConstant.ERROR;
								handler.sendMessage(errorMsg);
							}
						}

					}).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (R.id.acTv_down == id) {
			if (autoTextView.isPopupShowing()) {
				autoTextView.dismissDropDown();
			} else {
				autoTextView.showDropDown();
			}
		} else if (R.id.grayBtn == id) {
			grayBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_fouce));
			grayBtn.setTextColor(getResources().getColor(R.color.white));
			greenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab));
			greenBtn.setTextColor(getResources().getColor(R.color.black2));

			if (filterList != null) {
				filterList.clear();
			}
			filterList = dealList(list, 0);
			fast_list.setAdapter(new FastParkAdapter(MainActivity.this, filterList));
			Collections.sort(filterList);
		} else if (R.id.greenBtn == id) {
			greenBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_fouce));
			greenBtn.setTextColor(getResources().getColor(R.color.white));
			grayBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab));
			grayBtn.setTextColor(getResources().getColor(R.color.black2));
			if (filterList != null) {
				filterList.clear();
			}
			filterList = dealList(list, 1);
			fast_list.setAdapter(new FastParkAdapter(MainActivity.this, filterList));
			Collections.sort(filterList);
		} else if (R.id.path == id) {
			pathPw.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER, 0, 0);
		} else if (R.id.loadIv == id) {
			myProgress.setVisibility(View.VISIBLE);
			reLoadIv.setVisibility(View.GONE);
			WeatherService weatherService = new WeatherService(MainActivity.this, new WeatherCallback() {

				@Override
				public void doSuccessCallback(List<WeatherInfo> weatherInfos) {
					BaseConstant.weatherInfos.clear();
					BaseConstant.weatherInfos.addAll(weatherInfos);
					Message msg = new Message();
					msg.what = MSG_WEATHER_DATA;
					handler.sendMessage(msg);
				}

				@Override
				public void doFailCallback(String msg) {
					ToastUtil.show(MainActivity.this, msg);
				}

			});
			weatherService.search();
		} else if (R.id.preBtn == id) {
			selectCount--;
			getMorePoi(selectCount - 1, false);
		} else if (R.id.nextBtn == id) {
			selectCount++;
			if (poiArr.size() > selectCount) {
				getMorePoi(selectCount - 1, false);
				if (poiArr.size() == selectCount) {
					nextBtn.setEnabled(false);
				}
			} else {
				if (selectCount <= result02.getPageCount()) {
					new Thread() {

						@Override
						public void run() {
							handler.sendEmptyMessage(BaseConstant.HIDE);
							getMorePoi(selectCount, true);
						}

					}.start();
				}
			}
		}
	}

	private void getNearData() {
		indexIv.setImageResource(R.drawable.state_near);
		indexIv.setTag(R.drawable.state_near);
		autoSearch.setVisibility(View.GONE);
		mMapView.setVisibility(View.VISIBLE);
		weatherIv.setVisibility(View.VISIBLE);
		changeLl.setVisibility(View.GONE);
		listBtn.setVisibility(View.VISIBLE);
		fast_list_rl.setVisibility(View.GONE);

		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View view = inflater.inflate(R.layout.circumbox, null);
		Button cateringBtn = (Button) view.findViewById(R.id.catering);
		Button playBtn = (Button) view.findViewById(R.id.play);
		Button shopBtn = (Button) view.findViewById(R.id.shop);
		Button attractionsBtn = (Button) view.findViewById(R.id.attractions);
		Button hotelBtn = (Button) view.findViewById(R.id.hotel);
		Button gasBtn = (Button) view.findViewById(R.id.gas);

		cateringBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearMap();

				initPoi(MyPoiOverlay.NAV_MODE_CAR_AND_BUS);

				pw.dismiss();
				poiSearch("餐饮", "");
				preBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				nearTv.setText("附近餐厅");
			}

		});
		playBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearMap();

				initPoi(MyPoiOverlay.NAV_MODE_CAR_AND_BUS);

				pw.dismiss();
				preBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				poiSearch("休闲", "");
				nearTv.setText("附近娱乐");
			}

		});
		shopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearMap();

				initPoi(MyPoiOverlay.NAV_MODE_CAR_AND_BUS);

				pw.dismiss();
				preBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				poiSearch("购物", "");
				nearTv.setText("附近购物");
			}

		});
		attractionsBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearMap();

				initPoi(MyPoiOverlay.NAV_MODE_CAR_AND_BUS);

				pw.dismiss();
				preBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				poiSearch("景点", "");
				nearTv.setText("附近景点");
			}

		});
		hotelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearMap();

				initPoi(MyPoiOverlay.NAV_MODE_CAR_AND_BUS);

				pw.dismiss();
				preBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				poiSearch("酒店", "");
				nearTv.setText("附近酒店");
			}

		});
		gasBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				clearMap();

				initPoi(MyPoiOverlay.NAV_MODE_CAR);

				pw.dismiss();
				preBtn.setEnabled(false);
				nextBtn.setEnabled(true);
				poiSearch("加油站", "");
				nearTv.setText("附近加油站");
			}

		});
		pw = new PopupWindow(view, (int) (width * 0.9), (int) (height * 0.7), true);
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		pw.setOutsideTouchable(false); // 设置是否允许在外点击使其消失
		pw.showAtLocation(findViewById(R.id.mapView), Gravity.CENTER, 0, 0);

		ImageView closeIv = (ImageView) view.findViewById(R.id.close);
		closeIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pw.dismiss();
			}

		});
	}

	private void getWeatherData(TextView dateTv, TextView wash, ImageView weather, TextView weatherTv,
			TextView temperatureTv, ImageView tom_weather, TextView tom_temperatureTv, ImageView tom_after_weather,
			TextView tom_after_temperatureTv) {
		int size = BaseConstant.weatherInfos.size();
		if (size == 0) {
			if ("停车场自动搜索".equals(BaseConstant.homePage)) {
				if (autoSearch.getVisibility() == View.GONE) {
					if (popLoadIv != null) {
						popLoadIv.setVisibility(View.VISIBLE);
						popMyProgress.setVisibility(View.GONE);
						popSucLoad.setVisibility(View.GONE);
						popFailLoad.setVisibility(View.VISIBLE);
					} else {
						failLoad.setVisibility(View.VISIBLE);
						reLoadIv.setVisibility(View.VISIBLE);
						myProgress.setVisibility(View.GONE);
						sucLoad.setVisibility(View.GONE);
					}
				} else {
					failLoad.setVisibility(View.VISIBLE);
					reLoadIv.setVisibility(View.VISIBLE);
					sucLoad.setVisibility(View.GONE);
					myProgress.setVisibility(View.GONE);
				}
			} else {
				if (popLoadIv != null) {
					popLoadIv.setVisibility(View.VISIBLE);
					popSucLoad.setVisibility(View.GONE);
					popMyProgress.setVisibility(View.GONE);
					popFailLoad.setVisibility(View.VISIBLE);
				}
			}
		} else {
			if ("停车场自动搜索".equals(BaseConstant.homePage)) {
				if (autoSearch.getVisibility() == View.GONE) {
					if (popLoadIv != null) {
						popLoadIv.setVisibility(View.GONE);
						popSucLoad.setVisibility(View.VISIBLE);
						popMyProgress.setVisibility(View.GONE);
						popFailLoad.setVisibility(View.GONE);
					}
				} else {
					failLoad.setVisibility(View.GONE);
					reLoadIv.setVisibility(View.GONE);
					myProgress.setVisibility(View.GONE);
					sucLoad.setVisibility(View.VISIBLE);
				}
			} else {
				if (popLoadIv != null) {
					popLoadIv.setVisibility(View.GONE);
					popSucLoad.setVisibility(View.VISIBLE);
					popFailLoad.setVisibility(View.GONE);
					popMyProgress.setVisibility(View.GONE);
				}
			}
		}
		for (int i = 0; i < size; i++) {
			WeatherInfo weatherInfo = BaseConstant.weatherInfos.get(i);
			String cdateStr = weatherInfo.getCdate();
			String temperatureStr = weatherInfo.getLowTemperature() + "°C ~ " + weatherInfo.getHighTemperature() + "°C";
			String weatherStr = weatherInfo.getWeather();
			String washStr = "洗车的好天气";
			int imgId = LoadingActivity.getWeatherImg(weatherStr);
			if (weatherStr.indexOf("雨") >= 0) {
				washStr = "今天不宜洗车";
			}
			if (i == 0) {
				this.weatherIv.setImageResource(imgId);
				scanBitmap(imgId, weather, 1.2f);
				weatherTv.setText(weatherStr);
				temperatureTv.setText(temperatureStr);
				dateTv.setText(cdateStr);
				wash.setText(washStr);
			} else if (i == 1) {
				scanBitmap(imgId, tom_weather, 0.7f);
				tom_temperatureTv.setText(temperatureStr);
			} else if (i == 2) {
				scanBitmap(imgId, tom_after_weather, 0.7f);
				tom_after_temperatureTv.setText(temperatureStr);
			}
		}
	}

	/**
	 * 语音是否识别成功
	 */
	boolean hideVoiceImg = true;

	private void voiceData() {
		// 移动数据分析，收集开始听写事件
		FlowerCollector.onEvent(MainActivity.this, "iat_recognize");
		InitListener mInitListener = new InitListener() {

			@Override
			public void onInit(int code) {
				Log.d(TAG, "SpeechRecognizer init() code = " + code);
				if (code != ErrorCode.SUCCESS) {
					CrashReport.postCatchedException(new RuntimeException("初始化失败，错误码：" + code));
				}
			}

		};
		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		// 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
		final RecognizerDialog mIatDialog = new RecognizerDialog(MainActivity.this, mInitListener);
		mIatDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				voiceIndex.setVisibility(View.GONE);
			}

		});
		mIatDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (hideVoiceImg) {
					voiceIndex.setVisibility(View.GONE);
				}
				hideVoiceImg = true;
			}

		});
		RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {

			StringBuilder sb = new StringBuilder();

			@Override
			public void onResult(RecognizerResult results, boolean isLast) {
				String text = JsonParser.parseIatResult(results.getResultString());
				sb.append(text);
				if (isLast) {
					if (!TextUtils.isEmpty(sb) && sb.indexOf("停车") != -1) {
						hideVoiceImg = true;

						index = 0;
						nearTv.setText("周边停车场");

						Message msg = new Message();
						msg.what = MSG_VIEW_CLEAR;
						handler.sendMessage(msg);

						showProgressDialog("", "正在定位中，请稍后！");
						myLocationSource.requestLocation(new LocationCallback() {

							@Override
							public void syncLocation(AMapLocation amapLocation) {
								mGeoPoint = new LatLonPoint(amapLocation.getLatitude(), amapLocation.getLongitude());
								PublicVar.loationPoint = mGeoPoint;
								PublicVar.currentLocationName = amapLocation.getPoiName();
							}

							@Override
							public void doCallback(AMapLocation amapLocation) {
								aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
										new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()), 14));
								new Thread(new Runnable() {

									@Override
									public void run() {
										Message msg = new Message();
										msg.what = FIRST_LOCATION;
										msg.arg1 = 1;
										handler.sendMessage(msg);
									}

								}).start();
							}

						});
					} else {
						sb = new StringBuilder();
						hideVoiceImg = false;
						Toast.makeText(MainActivity.this, "请重新对手机说：'我要停车'、'找停车场'", Toast.LENGTH_LONG).show();
						mIatDialog.show();
					}
				}
			}

			/**
			 * 识别回调错误.
			 */
			@Override
			public void onError(SpeechError error) {
				CrashReport.postCatchedException(new RuntimeException(error.getPlainDescription(true)));
			}

		};
		mIatDialog.setListener(mRecognizerDialogListener);
		mIatDialog.show();
	}

	public void scanBitmap(int id, ImageView imageView, float scan) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), id);
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale(scan, scan);
		if (imageView != null) {
			imageView.setImageBitmap(Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true));
		}
	}

	public void openDB() {
		if (indexDB == null || !indexDB.isOpen()) {
			indexDB = SQLiteDatabase.openDatabase(dataPath + dataIndexName, null, SQLiteDatabase.OPEN_READWRITE);
		}
	}

	public void closeDB() {
		if (indexDB.isOpen()) {
			indexDB.close();
		}
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		closeDB();

		mMapView.onDestroy();
	}

	@Override
	protected void onStart() {
		openDB();

		super.onStart();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onPause() {
		// 开放统计 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(MainActivity.this);

		super.onPause();

		mMapView.onPause();

		mSensorManager.unregisterListener(this);
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onResume() {
		// 开放统计 移动数据统计分析
		FlowerCollector.onResume(MainActivity.this);
		FlowerCollector.onPageStart(TAG);

		super.onResume();

		mMapView.onResume();

		openDB();

		// 加速度传感器
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				// 还有SENSOR_DELAY_UI、SENSOR_DELAY_FASTEST、SENSOR_DELAY_GAME等，
				// 根据不同应用，需要的反应速率不同，具体根据实际情况设定
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		super.onStop();

		closeDB();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		int sensorType = event.sensor.getType();
		float[] values = event.values;
		if (sensorType == Sensor.TYPE_PROXIMITY) {
			System.out.println("------" + values.toString());
		}
		if (sensorType == Sensor.TYPE_ACCELEROMETER) {
			/*
			 * 因为一般正常情况下，任意轴数值最大就在9.8~10之间，只有在你突然摇动手机的时候，瞬时加速度才会突然增大或减少。
			 * 所以，经过实际测试，只需监听任一轴的加速度大于14的时候，改变你需要的设置就OK了~~~
			 */
			if (changeLl.getVisibility() == View.VISIBLE || longPressTv.getVisibility() == View.VISIBLE) {
				if ("Motorola".equals(android.os.Build.MANUFACTURER)) {
					if ((Math.abs(values[0]) > 30 || Math.abs(values[1]) > 30 || Math.abs(values[2]) > 30)
							&& !media.isPlaying()) {
						if (longPressTv.getVisibility() == View.VISIBLE) {
							BaseConstant.geoPoint = searchGp;
						}
						try {
							media.start();
							showProgressDialog("", "停车场查询中，请稍后...");
							Message msg = new Message();
							msg.what = MSG_VIEW_CLEAR;
							handler.sendMessage(msg);
							getParkData(0);

							clearMap();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
					}
				} else {
					if ((Math.abs(values[0]) > 19 || Math.abs(values[1]) > 19 || Math.abs(values[2]) > 19)
							&& !media.isPlaying()) {
						if (longPressTv.getVisibility() == View.VISIBLE) {
							BaseConstant.geoPoint = searchGp;
						}
						try {
							media.start();
							showProgressDialog("", "停车场查询中，请稍后...");
							Message msg = new Message();
							msg.what = MSG_VIEW_CLEAR;
							handler.sendMessage(msg);
							getParkData(0);

							clearMap();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (IllegalStateException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	@Override
	public void onMapLongClick(LatLng point) {
		showProgressDialog("", "智慧鼓楼，无线生活体验...");// 正在获取当前地址,请耐心等待...
		longPressPoint = AMapServicesUtil.convertToLatLonPoint(point);
		handler.sendEmptyMessage(MSG_VIEW_CLEAR);
		handler.sendEmptyMessage(MSG_VIEW_LONGPRESS);

		clearMap();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		mMapView.onSaveInstanceState(outState);
	}

	private void clearMap() {
		parkOverlay.removeFromMap();
		naviOverlay.removeFromMap();
		myPoiOverlay.removeFromMap();

		clearRoute();
	}

	private void clearRoute() {
		if (drivingRouteOverlay != null) {
			drivingRouteOverlay.removeFromMap();
		}
		if (busRouteOverlay != null) {
			busRouteOverlay.removeFromMap();
		}
		if (walkRouteOverlay != null) {
			walkRouteOverlay.removeFromMap();
		}
	}

	public void hideInfoWindow() {
		parkOverlay.hideInfoWindow();
		naviOverlay.hideInfoWindow();
		myPoiOverlay.hideInfoWindow();
	}

	@Override
	public void onMapClick(LatLng arg0) {
		hideInfoWindow();
	}

	private void initPoi(int navMode) {
		is_poi = true;

		myPoiOverlay.setNavMode(navMode);

		aMap.setOnMarkerClickListener(myPoiOverlay);
		aMap.setInfoWindowAdapter(myPoiOverlay);
	}

	private void initPark() {
		is_poi = false;

		aMap.setOnMarkerClickListener(parkOverlay);
		aMap.setInfoWindowAdapter(parkOverlay);
	}

}
