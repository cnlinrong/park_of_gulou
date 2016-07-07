package com.funo.park.util;

import java.util.List;

import com.amap.api.maps.AMap;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.funo.park.MainActivity;
import com.funo.park.R;
import com.funo.park.info.LocationInfo;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MyPoiOverlay extends PoiOverlay {

	private MainActivity context;
	private Drawable drawable;
	private int number = 0;
	private List<PoiItem> poiItem;
	private LayoutInflater mInflater;
	private int height;
	private LocationInfo locationInfo;
	private int mWidth;
	private int mHeight;

	public MyPoiOverlay(MainActivity context, Drawable drawable, AMap amap, List<PoiItem> poiItem) {
		super(amap, poiItem);

		this.context = context;
		this.poiItem = poiItem;
		mInflater = LayoutInflater.from(context);
		height = drawable.getIntrinsicHeight();
	}

	public MyPoiOverlay(MainActivity context, Drawable drawable, AMap amap, List<PoiItem> poiItem,
			LocationInfo locationInfo, int mWidth, int mHeight) {
		super(amap, poiItem);

		this.context = context;
		this.poiItem = poiItem;
		mInflater = LayoutInflater.from(context);
		height = drawable.getIntrinsicHeight();
		this.locationInfo = locationInfo;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
	}

	protected Drawable getPopupBackground() {
		drawable = context.getResources().getDrawable(R.drawable.boxbg);
		return drawable;
	}

	protected View getPopupView(final PoiItem item) {
		final View view = mInflater.inflate(R.layout.longpressbox, null);
		TextView addressTextView = (TextView) view.findViewById(R.id.address);
		Button okBtn = (Button) view.findViewById(R.id.ok);
		Button exitBtn = (Button) view.findViewById(R.id.exit);
		ImageView closeIv = (ImageView) view.findViewById(R.id.close);
		okBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BaseConstant.geoPoint = locationInfo.getGeoPoint();
				view.setVisibility(View.GONE);
				context.poiGetData(0);
			}

		});
		exitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.setVisibility(View.GONE);
			}

		});
		closeIv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				view.setVisibility(View.GONE);
			}

		});
		if (locationInfo != null) {
			addressTextView.setText(locationInfo.getAddress());
		}

		return view;
	}

}
