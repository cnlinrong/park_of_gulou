package com.funo.park.adapter;

import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.funo.park.MainActivity;
import com.funo.park.R;
import com.funo.park.client.po.ParkListReturnInfo.ParkListReturnData;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ParkAdapter extends BaseAdapter {

	private LayoutInflater myInflater;
	private List<ParkListReturnData> itemList;
	private Context context;
	private PopupWindow pw;
	private int width;
	private int height;
	private Button switchBtn;

	public ParkAdapter(Context context, List<ParkListReturnData> itemList, int width, int height) {
		this.context = context;
		this.itemList = itemList;
		myInflater = LayoutInflater.from(context);
		this.width = width;
		this.height = height;
		this.switchBtn = switchBtn;
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
		convertView = myInflater.inflate(R.layout.park_list_item, null);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.parkText = (TextView) convertView.findViewById(R.id.parkText);
		viewHolder.parkImageButton = (ImageButton) convertView.findViewById(R.id.parkIv);
		viewHolder.park_list_itemRl = (RelativeLayout) convertView.findViewById(R.id.park_list_item);
		final ParkListReturnData prd = itemList.get(position);
		viewHolder.parkText.setText(prd.getName());

		if (position != 0 && position % 2 == 1) {
			viewHolder.park_list_itemRl.setBackgroundResource(R.drawable.listbg);
		}
		viewHolder.parkImageButton.setOnClickListener(new OnClickListener() {
			ParkListReturnData prd = itemList.get(position);

			@Override
			public void onClick(View v) {
				((MainActivity) context).autoSearch.setVisibility(View.GONE);
				((MainActivity) context).mMapView.setVisibility(View.VISIBLE);
				((MainActivity) context).weatherIv.setVisibility(View.VISIBLE);
				((MainActivity) context).changeLl.setVisibility(View.VISIBLE);
				((MainActivity) context).listBtn.setVisibility(View.VISIBLE);
				double lat = Double.valueOf(prd.getLatitude());
				double lon = Double.valueOf(prd.getLongitude());
				LatLonPoint pt = new LatLonPoint(lat, lon);
				((MainActivity) context).popAutoPark(pt, prd);
			}
			
		});
		viewHolder.parkText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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

				pw = new PopupWindow(view, (int) (width * 0.95), (int) (height * 0.8), true);
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
					style.setSpan(new ForegroundColorSpan(Color.BLACK), (str + freeNumStr).length(),
							strContent.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} else {
					style.setSpan(new ForegroundColorSpan(Color.BLACK), str.length(), strContent.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				textView.setText(style);
			}
		});
		// convertView.setTag(viewHolder);
		return convertView;
	}

	class ViewHolder {

		TextView parkText;
		ImageButton parkImageButton;
		RelativeLayout park_list_itemRl;

	}

}
