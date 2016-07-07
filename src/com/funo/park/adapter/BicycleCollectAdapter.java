package com.funo.park.adapter;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.DriveRouteQuery;
import com.funo.park.R;
import com.funo.park.bicycle.PublicBicycleRoadLineActivity;
import com.funo.park.client.PDialogClass;
import com.funo.park.data.DBManager;
import com.funo.park.data.FunoConst;
import com.funo.park.mode.BicycleStation;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BicycleCollectAdapter extends BaseAdapter {

	private Context mContext;

	public List<BicycleStation> mBicycleStationList = new ArrayList<BicycleStation>();
	private ViewHolder vh;
	private int mresLayoutId;
	private TextView tv;
	private String picUrl;
	private String[] imagesUrl;
	private int mode = RouteSearch.DrivingDefault;
	private PDialogClass pDialogClass;
	private ProgressDialog progressDialog;
	private Handler mHandler;
	private LatLonPoint startPoint;
	private LatLonPoint endPoint;
	private DBManager mdbManager;

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

	public BicycleCollectAdapter(Context context, int resLayoutId, List<BicycleStation> bicycleStationList,
			DBManager dbManager) {
		this.mContext = context;
		mresLayoutId = resLayoutId;
		mBicycleStationList = bicycleStationList;
		mdbManager = dbManager;

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

	public void deleteItem(int position) {
		mBicycleStationList.remove(position);
		this.notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHolder vh = new ViewHolder();
		view = LayoutInflater.from(mContext).inflate(mresLayoutId, null);
		vh.picView = view.findViewById(R.id.bicycle_collect_pic_l);
		vh.picView.setVisibility(8);

		vh.roadlineImg = (ImageView) view.findViewById(R.id.bicycle_roadline);

		vh.deleteImg = (ImageView) view.findViewById(R.id.bicycle_delete);
		vh.nameTv = (TextView) view.findViewById(R.id.bicycle_collect_station);
		vh.addrTv = (TextView) view.findViewById(R.id.bicycle_collect_addr);
		vh.numTv = (TextView) view.findViewById(R.id.bicycle_collect_num);
		if (mBicycleStationList.size() > 0) {
			vh.numTv.setText("锁住总数量:" + mBicycleStationList.get(position).getStationNum());
			vh.nameTv.setText(mBicycleStationList.get(position).getStationName());
			vh.addrTv.setText(mBicycleStationList.get(position).getStationAddr());
			vh.deleteImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					BicycleStation bs = mBicycleStationList.get(position);
					bs.setCollectStatus("0");
					mdbManager.update(bs);
					deleteItem(position);
					Toast.makeText(mContext, "删除成功!", Toast.LENGTH_SHORT).show();
				}

			});
			vh.roadlineImg.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent it = new Intent();
					String lat = mBicycleStationList.get(position).getLat();
					String lon = mBicycleStationList.get(position).getLon();
					it.putExtra("lat", lat);
					it.putExtra("lon", lon);
					it.setClass(mContext, PublicBicycleRoadLineActivity.class);
					mContext.startActivity(it);
				}
				
			});
		}
		return view;
	}

	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(startPoint, endPoint);
		new Thread(new Runnable() {

			@Override
			public void run() {
				Message msg = new Message();
				try {
					DriveRouteQuery query = new DriveRouteQuery(fromAndTo, mode, null, null, "");
					DriveRouteResult mDriveRouteResult = mRouteSearch.calculateDriveRoute(query);
					if (mDriveRouteResult != null && mDriveRouteResult.getPaths() != null
							&& !mDriveRouteResult.getPaths().isEmpty()) {
						msg.what = FunoConst.RequestSuccess;
						msg.obj = mDriveRouteResult;
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

	private class ViewHolder {

		ImageView roadlineImg;
		ImageView deleteImg;
		TextView nameTv;
		TextView addrTv;
		TextView numTv;
		View picView;

	}

}
