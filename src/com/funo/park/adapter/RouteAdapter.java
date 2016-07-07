package com.funo.park.adapter;

import java.util.ArrayList;

import com.funo.park.R;
import com.funo.park.info.RouteInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RouteAdapter extends BaseAdapter {

	private LayoutInflater myInflater;
	private ArrayList<RouteInfo> itemList;
	private Context context;

	public RouteAdapter(Context context, ArrayList<RouteInfo> itemList) {
		this.context = context;
		this.itemList = itemList;
		
		myInflater = LayoutInflater.from(context);
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
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
		convertView = myInflater.inflate(R.layout.route_list_item, null);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.routeText = (TextView) convertView.findViewById(R.id.routeText);
		viewHolder.indexIv = (ImageView) convertView.findViewById(R.id.indexIv);
		RouteInfo routeInfo = itemList.get(position);
		viewHolder.routeText.setText(routeInfo.getAddress());
		viewHolder.indexIv.setImageResource(routeInfo.getImageId());
		return convertView;
	}

	class ViewHolder {

		TextView routeText;
		ImageView indexIv;

	}

}
