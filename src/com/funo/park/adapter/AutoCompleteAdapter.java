package com.funo.park.adapter;

import java.util.ArrayList;
import java.util.List;

import com.funo.park.R;
import com.funo.park.info.SearchInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

	private Context context;
	private ArrayFilter mFilter;
	private ArrayList<SearchInfo> mOriginalValues;// 所有的Item
	private List<SearchInfo> mObjects;// 过滤后的item
	private final Object mLock = new Object();

	public AutoCompleteAdapter(Context context, ArrayList<SearchInfo> mOriginalValues) {
		this.context = context;
		this.mOriginalValues = mOriginalValues;
		this.mObjects = mOriginalValues;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	private class ArrayFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();
			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
					ArrayList<SearchInfo> list = new ArrayList<SearchInfo>(mOriginalValues);
					results.values = list;
					results.count = list.size();
					return results;
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();
				final int count = mOriginalValues.size();
				final ArrayList<SearchInfo> newValues = new ArrayList<SearchInfo>(count);
				for (int i = 0; i < count; i++) {
					SearchInfo mSearchInfo = mOriginalValues.get(i);
					final String value = mSearchInfo.getKey();
					final String valueText = value.toLowerCase();
					if (valueText.startsWith(prefixString)) { // 源码 ,匹配开头
						newValues.add(mSearchInfo);
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			mObjects = (List<SearchInfo>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}

	@Override
	public int getCount() {
		return mObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return mObjects.get(position).getKey();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_for_autocomplete, null);
			holder.tv = (TextView) convertView.findViewById(R.id.simple_item_tv);
			holder.iv = (ImageView) convertView.findViewById(R.id.simple_item_iv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv.setText(mObjects.get(position).getKey());
		holder.iv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchInfo obj = mObjects.remove(position);
				mOriginalValues.remove(obj);
				notifyDataSetChanged();
			}

		});
		return convertView;
	}

	class ViewHolder {

		TextView tv;
		ImageView iv;

	}

	public ArrayList<SearchInfo> getAllItems() {
		return mOriginalValues;
	}

}
