package com.funo.park.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.funo.park.MainActivity;
import com.funo.park.R;
import com.funo.park.client.po.ParkListReturnInfo.ParkListReturnData;
import com.funo.park.data.PublicVar;
import com.funo.park.util.AMapServicesUtil;
import com.funo.park.util.BaseConstant;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyItemizedOverlay implements Overlay, InfoWindowAdapter, OnMarkerClickListener {

	private AMap mAMap;
	private LatLonPoint mGeoPoint;
	private List<Marker> markers = new ArrayList<Marker>();
	
	private MainActivity mContext;
	
	private Marker selectedMarker;
	private LatLonPoint selectedPoint;
	
	private int index = 0;
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
	
	public LatLonPoint getStartPoint() {
//		return startPoint;
		return PublicVar.loationPoint;
	}

	public void setStartPoint(LatLonPoint startPoint) {
		this.startPoint = startPoint;
	}

	public LatLonPoint getEndPoint() {
//		return endPoint;
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
	
	public MyItemizedOverlay(Context context, AMap amap, LatLonPoint mGeoPoint, int index) {
		this.mContext = (MainActivity) context;
		this.mAMap = amap;
		this.mGeoPoint = mGeoPoint;
		this.index = index;
		
//		mAMap.setInfoWindowAdapter(this);
//		mAMap.setOnMarkerClickListener(this);
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
	
	private BitmapDescriptor getBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.bicycle_marker);
	}
	
	public void addMarker(Object data, LatLng position, String title) {
		Marker marker = mAMap.addMarker(new MarkerOptions().position(position).title(title));
		marker.setObject(data);
		markers.add(marker);
	}
	
	public void addMarker(Object data, LatLng position, int icon, String title) {
		Marker marker = mAMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(icon)).title(title));
		marker.setObject(data);
		markers.add(marker);
	}
	
	@Override
	public boolean onMarkerClick(Marker marker) {
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
		if (index == 0) {
			infoWindow = LayoutInflater.from(mContext).inflate(R.layout.overlay_popup, null);
		} else if (index == 1) {
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
		if (index == 0) {
			infoWindow = LayoutInflater.from(mContext).inflate(R.layout.overlay_popup, null);
		} else if (index == 1) {
			infoWindow = LayoutInflater.from(mContext).inflate(R.layout.navibox, null);
		}
		render(marker, infoWindow);
		return infoWindow;
	}

	public void render(Marker marker, View view) {
		if (null != selectedMarker && index == 0) {
			RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.popLl);
			RelativeLayout textRl = (RelativeLayout) view.findViewById(R.id.text);
			textRl.setClickable(true);
//			if (overlays.size() < 4 && overlays.size() > 0) {
//				if (newFocus.equals(overlays.get(0))) {
//					rl.setBackgroundResource(R.drawable.box_green);
//				} else if (newFocus.equals(overlays.get(1))) {
//					rl.setBackgroundResource(R.drawable.box_red);
//				} else if (newFocus.equals(overlays.get(2))) {
//					rl.setBackgroundResource(R.drawable.box_orange);
//				}
//			} else {
//				rl.setBackgroundResource(R.drawable.box_green);
//			}

			final Map<String, Object> map = (Map<String, Object>) selectedMarker.getObject();
			final ParkListReturnData prd1 = new ParkListReturnData();
			if ((Integer) map.get("type") == 3) {
				prd1.setIsqy((Integer) map.get("typeSecond"));
			} else {
				prd1.setIsqy((Integer) map.get("type"));
			}
			prd1.setAddress((String) map.get("address"));
			prd1.setDistance((Double) map.get("distance"));
			prd1.setName((String) map.get("name"));
			prd1.setFreeNum((Long) map.get("freeNum"));
			prd1.setTotalNum((Long) map.get("totalNum"));
			prd1.setChargeSet((String) map.get("price"));
			final String name = (String) map.get("name");

			String freeNum = map.get("freeNum") + "";
			String totalNum = map.get("totalNum") + "";
			String price = (String) map.get("price") + "";
			String distance = map.get("distance") + "米";
			String address = map.get("address") + "";
			int type = (Integer) map.get("type");
			int typeSecond = 0;
			if (type == 3) {
				typeSecond = (Integer) map.get("typeSecond");
			}

			if (type == 0) {
				rl.setBackgroundResource(R.drawable.box_gray);
			} else if (type == 1) {
				rl.setBackgroundResource(R.drawable.box_green);
			}

			TextView nameTv = (TextView) view.findViewById(R.id.park_name);
			TextView numTv = (TextView) view.findViewById(R.id.park_num);
			TextView costTv = (TextView) view.findViewById(R.id.park_cost);
			TextView distanceTv = (TextView) view.findViewById(R.id.park_distance);
			TextView addressTv = (TextView) view.findViewById(R.id.park_address);
			ImageButton naviIb = (ImageButton) view.findViewById(R.id.naviIb);
			naviIb.setVisibility(View.VISIBLE);
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
			String numStr = "车位（空/总）：" + freeNum + "/" + totalNum;
			if (type == 0) {
				numStr = "总车位：" + totalNum;
				;
			} else if (type == 3) {
				if (typeSecond == 0) {
					numStr = "总车位：" + totalNum;
				} else {
					numStr = "车位（空/总）：" + freeNum + "/" + totalNum;
				}
			} else {
				numStr = "车位（空/总）：" + freeNum + "/" + totalNum;
			}
			String costStr = "收费：" + price;
			String distanceStr = "距离：" + distance;
			String addressStr = "地址：" + address;
			final ParkListReturnData prd = new ParkListReturnData();
			prd.setNumStr(freeNum + "/" + totalNum);
			prd.setChargeSet(price);
			prd.setAddress(address);
			prd.setName(name);
			prd.setDis(distance);
			prd.setIsqy(type);
			if ("null".equals(freeNum)) {
				numStr = "车位（空/总）：0/0";
				prd.setNumStr("0/0");
				if (type == 0) {
					numStr = "总车位：0";
					prd.setNumStr("0");
				} else if (type == 3) {
					if (typeSecond == 0) {
						numStr = "总车位：0";
						prd.setNumStr("0");
					} else {
						numStr = "车位（空/总）：0/0";
						prd.setNumStr("0/0");
					}
				} else {
					numStr = "车位（空/总）：0/0";
					prd.setNumStr("0/0");
				}
			}
			if ("null".equals(price)) {
				costStr = "收费：" + "0元";
				prd.setChargeSet("0元");
			}

			SpannableStringBuilder nameStyle = new SpannableStringBuilder(nameStr);
			SpannableStringBuilder numStyle = new SpannableStringBuilder(numStr);
			SpannableStringBuilder costStyle = new SpannableStringBuilder(costStr);
			SpannableStringBuilder distanceStyle = new SpannableStringBuilder(distanceStr);
			SpannableStringBuilder addressStyle = new SpannableStringBuilder(addressStr);

			nameStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "名称：".length(), nameStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			if (type == 0) {
				numStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "总车位：".length(), numStr.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} else if (type == 3) {
				if (typeSecond == 0) {
					numStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "总车位：".length(), numStr.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} else {
					numStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "车位（空/总）：".length(), numStr.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} else {
				numStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "车位（空/总）：".length(), numStr.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
			costStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "收费：".length(), costStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			distanceStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "距离：".length(), distanceStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			addressStyle.setSpan(new ForegroundColorSpan(Color.WHITE), "地址：".length(), addressStr.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

			nameTv.setText(nameStyle);
			nameTv.setVisibility(View.VISIBLE);
			numTv.setText(numStyle);
			numTv.setVisibility(View.VISIBLE);
			costTv.setText(costStyle);
			costTv.setVisibility(View.GONE);
			distanceTv.setText(distanceStyle);
			distanceTv.setVisibility(View.VISIBLE);
			addressTv.setText(addressStyle);
			addressTv.setVisibility(View.VISIBLE);

			textRl.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					LayoutInflater myInflater = LayoutInflater.from(mContext);
					View view = myInflater.inflate(R.layout.parkdetail, null);

					TextView nameIv = (TextView) view.findViewById(R.id.nameTv);
					TextView parkIv = (TextView) view.findViewById(R.id.parkIv);
					TextView costIv = (TextView) view.findViewById(R.id.costIv);
					TextView addressIv = (TextView) view.findViewById(R.id.addressIv);
					TextView markIv = (TextView) view.findViewById(R.id.markIv);
					TextView distanceIv = (TextView) view.findViewById(R.id.distanceIv);

					textSpan(prd1, nameIv, "名称：", 0);
					textSpan(prd1, parkIv, "车位（空/总）：", 1);
					textSpan(prd1, costIv, "收费标准：", 2);
					textSpan(prd1, addressIv, "地址：", 3);
					textSpan(prd1, markIv, "备注：", 4);
					textSpan(prd1, distanceIv, "距离：", 5);

					final PopupWindow pw = new PopupWindow(view, (int) (BaseConstant.width * 0.95),
							(int) (BaseConstant.height * 0.8), true);
					pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					pw.setOutsideTouchable(true); // 设置是否允许在外点击使其消失
					pw.showAtLocation(view.findViewById(R.id.nameTv), Gravity.CENTER, 0, 0);

					ImageView closeIv = (ImageView) view.findViewById(R.id.weather_quit);
					closeIv.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							pw.dismiss();
						}
						
					});
				}
			});
		} else if (null != selectedMarker && index == 1) {
			final TextView startTv = (TextView) view.findViewById(R.id.start);
			final TextView endTv = (TextView) view.findViewById(R.id.end);
			startTv.setText(getStartStr());
			endTv.setText(getEndStr());
			String startStr = startTv.getText().toString();
			String endStr = endTv.getText().toString();

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
				}
				
			});
			walkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Toast.makeText(mContext, "对不起，此功能尚未实现！", Toast.LENGTH_LONG).show();
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

	private void textSpan(final ParkListReturnData prd, TextView textView, String str, int index) {
		String strContent = "";
		String freeNumStr = "";
		if (index == 0) {
			strContent = str + prd.getName();
		} else if (index == 1) {
			if (prd.getIsqy() == 1) {
				if (prd.getFreeNum() == null) {
					freeNumStr = "0";
					strContent = str + freeNumStr + "/" + prd.getTotalNum();
				} else {
					freeNumStr = prd.getFreeNum() + "";
					strContent = str + freeNumStr + "/" + prd.getTotalNum();
				}
			} else if (prd.getIsqy() == 0 || prd.getIsqy() == 3) {
				str = "车位（总数）:";
				strContent = str + prd.getTotalNum();
			}

		} else if (index == 2) {
			if (prd.getChargeSet() == null) {
				strContent = str + "0元";
			} else {
				strContent = str + prd.getChargeSet();
			}
		} else if (index == 3) {
			strContent = str + prd.getAddress();
		} else if (index == 4) {
			strContent = str + prd.getName();
		} else if (index == 5) {
			strContent = str + prd.getDistance() + "米";
		}

		SpannableStringBuilder style = new SpannableStringBuilder(strContent);
		if (prd.getIsqy() == 1 && str.indexOf("车位（空") != -1) {
			style.setSpan(new ForegroundColorSpan(Color.RED), "车位(".length(), "车位(".length() + 1,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			style.setSpan(new ForegroundColorSpan(Color.RED), str.length(), (str + freeNumStr).length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			style.setSpan(new ForegroundColorSpan(Color.BLACK), (str + freeNumStr).length(), strContent.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		} else {
			style.setSpan(new ForegroundColorSpan(Color.BLACK), str.length(), strContent.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		textView.setText(style);
	}
	
	public String getSelectedMarkerTitle() {
		String selectedMarkerTitle = "";
		if (selectedMarker != null) {
			selectedMarkerTitle = selectedMarker.getTitle();
		}
		return selectedMarkerTitle;
	}
	
}
