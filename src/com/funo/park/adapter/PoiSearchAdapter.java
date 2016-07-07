package com.funo.park.adapter;

import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.funo.park.MainActivity;
import com.funo.park.R;
import com.funo.park.info.LocationInfo;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PoiSearchAdapter extends BaseAdapter {

	private LayoutInflater myInflater;
	private List<LocationInfo> itemList;
	private Context context;

	public PoiSearchAdapter(Context context, List<LocationInfo> itemList) {
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
		viewHolder.naviImageButton.setVisibility(View.GONE);
		viewHolder.fastpark_list_itemRl = (RelativeLayout) convertView.findViewById(R.id.park_list_item);
		final LocationInfo lif = itemList.get(position);
		viewHolder.fastpark_list_itemRl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LocationInfo lif = itemList.get(position);
				((MainActivity) context).fast_list_rl.setVisibility(View.GONE);
				((MainActivity) context).mMapView.setVisibility(View.VISIBLE);
				((MainActivity) context).weatherIv.setVisibility(View.VISIBLE);
				((MainActivity) context).listBtn.setVisibility(View.VISIBLE);
				LatLonPoint pt = lif.getGeoPoint();
				((MainActivity) context).moveTo(lif, pt);
			}

		});
		viewHolder.naviImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LocationInfo lif = itemList.get(position);
				((MainActivity) (context)).fast_list_rl.setVisibility(View.GONE);
				((MainActivity) (context)).mMapView.setVisibility(View.VISIBLE);
				((MainActivity) (context)).weatherIv.setVisibility(View.VISIBLE);
				((MainActivity) (context)).listBtn.setVisibility(View.VISIBLE);
				LatLonPoint pt = lif.getGeoPoint();
				((MainActivity) (context)).popPoiSearchRoute(pt, lif);
			}

		});
		if (position != 0 && position % 2 == 1) {
			viewHolder.fastpark_list_itemRl.setBackgroundResource(R.drawable.listbg);
		}

		viewHolder.nameIv.setText(lif.getTitle());
		textSpan(lif, viewHolder.parkIv, "电话：", 1);
		textSpan(lif, viewHolder.costIv, "地址：", 2);
		textSpan(lif, viewHolder.addressIv, "距离：", 0);
		return convertView;
	}

	private void textSpan(LocationInfo lif, TextView textView, String str, int index) {
		String strContent = "";
		if (index == 0) {
			strContent = str + lif.getDistance() + "米";
		} else if (index == 2) {
			if (lif.getSnippet() == null || "".equals(lif.getSnippet().trim())) {
				strContent = str + "无";
			} else {
				strContent = str + lif.getSnippet();
			}
		} else if (index == 1) {
			if (lif.getTel() == null || "".equals(lif.getTel().trim())) {
				strContent = str + "无";
			} else {
				strContent = str + lif.getTel();
			}
		}

		SpannableStringBuilder style = new SpannableStringBuilder(strContent);
		style.setSpan(new ForegroundColorSpan(Color.BLACK), str.length(), strContent.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(style);
	}

	class ViewHolder {

		TextView nameIv;
		TextView parkIv;
		TextView costIv;
		TextView addressIv;
		ImageButton naviImageButton;
		RelativeLayout fastpark_list_itemRl;

	}

}
