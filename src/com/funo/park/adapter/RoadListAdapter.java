package com.funo.park.adapter;

import java.util.ArrayList;

import com.funo.park.R;
import com.funo.park.info.LocationInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RoadListAdapter extends BaseAdapter {

	private LayoutInflater myInflater;
	private Context context;
	private ArrayList<LocationInfo> itemList;
	String road;

	public RoadListAdapter(Context context, ArrayList<LocationInfo> itemList) {
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
		convertView = myInflater.inflate(R.layout.road_list_item, null);
		final ViewHolder viewHolder = new ViewHolder();
		viewHolder.roadText = (TextView) convertView.findViewById(R.id.roadText);
		viewHolder.titleText = (TextView) convertView.findViewById(R.id.titleText);
		viewHolder.roadText.setText(itemList.get(position).getAddress());
		viewHolder.titleText.setText(itemList.get(position).getTitle());
		convertView.setTag(viewHolder);
		return convertView;
	}

	class ViewHolder {
		
		TextView roadText;
		TextView titleText;
		
	}
	
}
