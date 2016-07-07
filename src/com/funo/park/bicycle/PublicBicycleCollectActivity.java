package com.funo.park.bicycle;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.funo.park.R;
import com.funo.park.adapter.BicycleCollectAdapter;
import com.funo.park.data.DBManager;
import com.funo.park.mode.BicycleStation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PublicBicycleCollectActivity extends Activity implements OnClickListener {

	private Context context = this;
	private ListView listview;
	private TextView remarkTv;
	private Button returnBtn;
	private LatLonPoint geoPoint;
	private BicycleCollectAdapter bicycleCollectAdapter;
	private DBManager dbManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_publicbicycle_collect);

		dbManager = new DBManager(context);

		listview = (ListView) findViewById(R.id.publicbicycle_collect_list);
		remarkTv = (TextView) findViewById(R.id.publicbicycle_collect_remark);
		returnBtn = (Button) findViewById(R.id.back);
		returnBtn.setOnClickListener(this);
		List<BicycleStation> list = new ArrayList<BicycleStation>();
		list = dbManager.query("1", 1);
		bicycleCollectAdapter = new BicycleCollectAdapter(context, R.layout.item_bicycle_collect, list, dbManager);
		listview.setAdapter(bicycleCollectAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				View v = arg1.findViewById(R.id.bicycle_collect_pic_l);
				View tv = arg1.findViewById(R.id.bicycle_listcollect_text_l);
				if (v.getVisibility() == 0) {
					v.setVisibility(8);
				} else {
					v.setVisibility(0);
				}
			}

		});
		if (list.size() == 0) {
			remarkTv.setVisibility(0);
			remarkTv.setText("您目前收藏0条数据！");
		} else {
			remarkTv.setVisibility(8);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		}
	}

}
