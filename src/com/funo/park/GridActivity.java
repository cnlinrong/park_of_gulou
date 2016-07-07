package com.funo.park;

import java.util.ArrayList;
import java.util.HashMap;

import com.funo.park.bicycle.PublicBicycleActivity;
import com.funo.park.data.DBManager;
import com.funo.park.data.DataFile;
import com.funo.park.util.BaseConstant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GridActivity extends Activity {

	private String items[] = { "智能停车", "公共自行车", "加油站查询", "周边消费", "公交查询", "地图查询", "出租电召", "旅游信息" };
	private GridView gridView;
	private Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.my_gridview);
		DisplayMetrics dm = new DisplayMetrics();
		dm = getResources().getDisplayMetrics();

		int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：480px）
		int screenHeight = dm.heightPixels; // 屏幕高（像素，如：800px）
		gridView = (GridView) this.findViewById(R.id.gridview_id);

		ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < items.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemImage", R.drawable.icon01 + i);
			map.put("ItemText", items[i]);
			lstImageItem.add(map);
		}
		SimpleAdapter saImageItems = new SimpleAdapter(this, lstImageItem, R.layout.gridview_item,
				new String[] { "ItemImage", "ItemText" }, new int[] { R.id.ItemImage, R.id.ItemText });
		gridView.setAdapter(saImageItems);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				BaseConstant.index = arg2;
				if (arg2 == 0) {
					startActivity(new Intent().setClass(GridActivity.this, MainActivity.class));
				} else if (arg2 == 1) {
					startActivity(new Intent().setClass(GridActivity.this, PublicBicycleActivity.class));
				} else if (arg2 == 2) {
					startActivity(new Intent().setClass(GridActivity.this, MainActivity.class));
				} else if (arg2 == 3) {
					startActivity(new Intent().setClass(GridActivity.this, MainActivity.class));
				} else if (arg2 == 4) {
					Toast.makeText(GridActivity.this, "此功能尚在开发中，敬请期待！", Toast.LENGTH_SHORT).show();
				} else if (arg2 == 5) {
					Toast.makeText(GridActivity.this, "此功能尚在开发中，敬请期待！", Toast.LENGTH_SHORT).show();
				} else if (arg2 == 6) {
					Toast.makeText(GridActivity.this, "此功能尚在开发中，敬请期待！", Toast.LENGTH_SHORT).show();
				} else if (arg2 == 7) {
					Toast.makeText(GridActivity.this, "此功能尚在开发中，敬请期待！", Toast.LENGTH_SHORT).show();
				}

			}
		});

		DBManager dbManager = new DBManager(context);
		if (!dbManager.getHelper().checkDataBase(dbManager.getDbPath())) {
			System.out.println("gggggggggggsssssssssssssss");
			dbManager = new DBManager(context);
			dbManager.add(DataFile.getGoogleBicycleStationList());
			dbManager.closeDB(dbManager.getDb());
		} else {
			if (dbManager.query("", 0).size() == 0) {
				System.out.println("gggggggggggsssssssssssssss111");
				dbManager.add(DataFile.getGoogleBicycleStationList());
				dbManager.closeDB(dbManager.getDb());
			}
		}
		if (screenWidth == 480 && screenHeight == 800) {
			gridView.post(new Runnable() {

				@Override
				public void run() {
					gridView.scrollTo(0, 35);
				}

			});
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // 按下的如果是BACK，同时没有重复

			quitHandler();

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void quitHandler() {

		AlertDialog dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("你确定要退出吗？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						// new
						// PublicVar(context).saveData(PublicVar.bicycleStationList);
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

}
