package com.funo.park.adapter;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.maps.MapView;
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
import com.tencent.bugly.crashreport.CrashReport;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BicycleListAdapter extends BaseAdapter {

	private Context mContext;

	public List<BicycleStation> mBicycleStationList = new ArrayList<BicycleStation>();
	private ViewHolder vh;
	private int mresLayoutId;
	private TextView tv;
	private String picUrl;
	private String[] imagesUrl;
	private DBManager mdbManager;
	private PDialogClass pDialogClass;
	private ProgressDialog progressDialog;
	private Handler mHandler;
	private MapView mapView;
	private View mFloatTool;
	private ListView mListview;
	private LatLonPoint mGeoPoint;
	private int mode = RouteSearch.WalkDefault;

	private RouteSearch mRouteSearch;
	
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

	public BicycleListAdapter(Context context, int resLayoutId, List<BicycleStation> bicycleStationList,
			DBManager dbManager, LatLonPoint startPoint, Handler handler, MapView aMapView, View floatTool,
			ListView listview) {
		this.mContext = context;
		mresLayoutId = resLayoutId;
		mBicycleStationList = bicycleStationList;
		mdbManager = dbManager;
		mGeoPoint = startPoint;
		mHandler = handler;
		mapView = aMapView;
		mFloatTool = floatTool;
		mListview = listview;
		
		mRouteSearch = new RouteSearch(context);
	}

	@Override
	public int getCount() {
		return mBicycleStationList.size();
	}

	@Override
	public Object getItem(int position) {
		return mBicycleStationList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder vh = new ViewHolder();
		view = View.inflate(mContext, mresLayoutId, null);
		vh.picView = view.findViewById(R.id.bicycle_list_pic_l);
		vh.picView.setVisibility(8);

		vh.roadlineImg = (ImageView) view.findViewById(R.id.bicycle_roadline);

		vh.collectImg = (ImageView) view.findViewById(R.id.bicycle_collect);
		vh.nameTv = (TextView) view.findViewById(R.id.bicycle_collect_station);
		vh.addrTv = (TextView) view.findViewById(R.id.bicycle_collect_addr);
		vh.numTv = (TextView) view.findViewById(R.id.bicycle_collect_num);
		if (mBicycleStationList.size() > 0) {
			vh.nameTv.setText(mBicycleStationList.get(position).getStationName());
			vh.addrTv.setText(mBicycleStationList.get(position).getStationAddr());
			vh.numTv.setText("锁住总数量:" + mBicycleStationList.get(position).getStationNum());
			vh.collectImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (mBicycleStationList.get(position).getCollectStatus().equals("1")) {
						Toast.makeText(mContext, "该站点已收藏", Toast.LENGTH_SHORT).show();
					} else {
						BicycleStation bs = mBicycleStationList.get(position);
						bs.setCollectStatus("1");
						mdbManager.update(bs);
						Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			vh.roadlineImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					pDialogClass = new PDialogClass(mContext, mHandler, true);
					progressDialog = pDialogClass.showProgressDialogWithB(15, "正在获取路线,请稍后...");
					PublicVar.map_end = true;
					double lat = Double.valueOf(mBicycleStationList.get(position).getLat());
					double lon = Double.valueOf(mBicycleStationList.get(position).getLon());
					LatLonPoint geoPoint = new LatLonPoint(lat, lon);
					searchRouteResult(mGeoPoint, geoPoint);
					mapView.setVisibility(0);
					mFloatTool.setVisibility(0);
					mListview.setVisibility(8);
				}
				
			});
		}
		return view;
	}

	private class ViewHolder {

		ImageView roadlineImg;
		ImageView collectImg;
		TextView nameTv;
		TextView addrTv;
		TextView numTv;
		View picView;

	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
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
				mHandler.sendMessage(msg);
			}

		}).start();
	}

}
