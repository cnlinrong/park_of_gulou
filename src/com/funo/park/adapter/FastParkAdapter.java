package com.funo.park.adapter;

import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.funo.park.MainActivity;
import com.funo.park.R;
import com.funo.park.client.po.ParkListReturnInfo.ParkListReturnData;
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
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FastParkAdapter extends BaseAdapter {

	private LayoutInflater myInflater;
	private List<ParkListReturnData> itemList;
	private Context context;

	public FastParkAdapter(Context context, List<ParkListReturnData> itemList) {
		this.context = context;
		this.itemList = itemList;
		myInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return itemList.size();
	}

	@Override
	public Object getItem(int position) {
		return itemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		convertView = myInflater.inflate(R.layout.fastpark_list_item, null);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.nameIv = (TextView) convertView.findViewById(R.id.nameTv);
		viewHolder.parkIv = (TextView) convertView.findViewById(R.id.parkIv);
		viewHolder.costIv = (TextView) convertView.findViewById(R.id.costIv);
		viewHolder.addressIv = (TextView) convertView.findViewById(R.id.addressIv);
		viewHolder.naviImageButton = (ImageButton) convertView.findViewById(R.id.naviIb);
		viewHolder.textLl = (LinearLayout) convertView.findViewById(R.id.text);

		final ParkListReturnData prd = itemList.get(position);

		viewHolder.nameIv.setText(prd.getName());
		if (prd.getIsqy() == 1) {
			textSpan(prd, viewHolder.parkIv, "车位（空/总）：", 1);
		} else if (prd.getIsqy() == 0) {
			textSpan(prd, viewHolder.parkIv, "车位（总数）：", 1);
		}
		textSpan(prd, viewHolder.costIv, "收费标准：", 2);
		textSpan(prd, viewHolder.addressIv, "地址：", 3);
		viewHolder.fastpark_list_itemRl = (RelativeLayout) convertView.findViewById(R.id.park_list_item);

		viewHolder.textLl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ParkListReturnData prd = itemList.get(position);
				LayoutInflater myInflater = LayoutInflater.from(context);
				View view = myInflater.inflate(R.layout.parkdetail, null);

				TextView nameIv = (TextView) view.findViewById(R.id.nameTv);
				TextView parkIv = (TextView) view.findViewById(R.id.parkIv);
				TextView costIv = (TextView) view.findViewById(R.id.costIv);
				TextView addressIv = (TextView) view.findViewById(R.id.addressIv);
				TextView markIv = (TextView) view.findViewById(R.id.markIv);
				TextView distanceIv = (TextView) view.findViewById(R.id.distanceIv);

				textSpan(prd, nameIv, "名称：", 0);
				textSpan(prd, parkIv, "车位（空/总）：", 1);
				textSpan(prd, costIv, "收费标准：", 2);
				textSpan(prd, addressIv, "地址：", 3);
				textSpan(prd, markIv, "备注：", 4);
				textSpan(prd, distanceIv, "距离：", 5);

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
		if (position != 0 && position % 2 == 1) {
			viewHolder.fastpark_list_itemRl.setBackgroundResource(R.drawable.listbg);
		}
		viewHolder.naviImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) context).fast_list_rl.setVisibility(View.GONE);
				((MainActivity) context).mMapView.setVisibility(View.VISIBLE);
				((MainActivity) context).weatherIv.setVisibility(View.VISIBLE);
				((MainActivity) context).changeLl.setVisibility(View.VISIBLE);
				((MainActivity) context).listBtn.setVisibility(View.VISIBLE);
				double lat = Double.valueOf(prd.getLatitude());
				double lon = Double.valueOf(prd.getLongitude());
				LatLonPoint pt = new LatLonPoint(lat, lon);
//				((MainActivity) context).popSearchRoute(pt, prd, null);
				((MainActivity) context).searchRouteResult(pt, RouteSearch.DrivingDefault);
				((MainActivity) context).hideInfoWindow();
				BaseConstant.routeInt = 0;
			}

		});
		return convertView;
	}

	private void textSpan(ParkListReturnData prd, TextView textView, String str, int index) {
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
			} else if (prd.getIsqy() == 0) {
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

	class ViewHolder {

		TextView nameIv;
		TextView parkIv;
		TextView costIv;
		TextView addressIv;
		ImageButton naviImageButton;
		LinearLayout textLl;
		RelativeLayout fastpark_list_itemRl;

	}

}
