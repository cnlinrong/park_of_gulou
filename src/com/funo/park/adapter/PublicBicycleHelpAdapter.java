package com.funo.park.adapter;

import java.util.List;

import com.funo.park.R;
import com.funo.park.mode.HelpBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PublicBicycleHelpAdapter extends BaseAdapter {

	private Context mContext;
	private int mresLayoutId;
	private List<HelpBean> mHelpBeanList;
	private ViewHolder vh;

	public PublicBicycleHelpAdapter(Context context, int resLayoutId, List<HelpBean> helpBeanList) {
		this.mContext = context;
		mresLayoutId = resLayoutId;
		mHelpBeanList = helpBeanList;
	}

	public int getCount() {
		return mHelpBeanList.size();
	}

	public Object getItem(int position) {
		return mHelpBeanList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup parent) {

		if (view == null) {
			vh = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(mresLayoutId, null);
			vh.titleTv = (TextView) view.findViewById(R.id.bicycle_help_title);
			vh.contentTv = (TextView) view.findViewById(R.id.bicycle_help_content);
			view.setTag(vh);
		} else {
			vh = (ViewHolder) view.getTag();
		}

		if (mHelpBeanList.size() > 0) {
			vh.titleTv.setText(mHelpBeanList.get(position).getTitle());
			vh.contentTv.setText(mHelpBeanList.get(position).getContent());
		}
		return view;
	}

	private class ViewHolder {
		
		TextView titleTv;
		TextView contentTv;

	}
}
