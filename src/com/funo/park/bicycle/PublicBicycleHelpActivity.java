package com.funo.park.bicycle;

import com.funo.park.R;
import com.funo.park.adapter.PublicBicycleHelpAdapter;
import com.funo.park.data.DataFile;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class PublicBicycleHelpActivity extends Activity implements OnClickListener {

	private Context context = this;
	private ListView listview;
	private Button returnBtn;
	private PublicBicycleHelpAdapter publicBicycleHelpAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publicbicycle_help);

		returnBtn = (Button) findViewById(R.id.back);
		returnBtn.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.publicbicycle_help_lv);

		publicBicycleHelpAdapter = new PublicBicycleHelpAdapter(context, R.layout.item_bicycle_help,
				DataFile.getHelpBeans());
		listview.setAdapter(publicBicycleHelpAdapter);
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
