package com.funo.park;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import com.funo.park.database.SQLiteDataBaseHelper;
import com.funo.park.info.WeatherInfo;
import com.funo.park.util.BaseConstant;
import com.funo.park.util.LoadingView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;

public class LoadingActivity extends Activity {

	private LoadingView main_imageview;
	private SQLiteDataBaseHelper indexDBHelper;
	private String dataName = BaseConstant.DATA_BASE_NAME;
	private String dataPath = BaseConstant.DATA_BASE_PATH;
	private int nowVersion = BaseConstant.dbVersion;
	public SQLiteDatabase indexDB;
	private boolean isError = false;
	private String result;
	private boolean isStarted;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.loading);
		
		main_imageview = (LoadingView) findViewById(R.id.main_imageview);
		System.out.println("手机品牌----->" + android.os.Build.MANUFACTURER);
		setNetwork();
	}

	public void setNetwork() {
		boolean networkState = BaseConstant.detect(LoadingActivity.this);
		if (!networkState) {
			AlertDialog dialog = new AlertDialog.Builder(this).setTitle("提示").setMessage("网络不可用，是否现在设置网络？？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
						}
						
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							finish();
						}
						
					}).show();
		} else {
			initLoadingImages();
			new Thread() {
				
				@Override
				public void run() {
					main_imageview.startAnim();
				}
				
			}.start();
			new LoadingDataTask().execute("");
			new LoadingIndex().execute("");
		}
	}

	class LoadingIndex extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			try {
				Thread.sleep(10000);
				if (!isStarted) {
					startActivity(new Intent().setClass(LoadingActivity.this, GridActivity.class));
					isStarted = true;
					finish();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	private void initLoadingImages() {
		int[] imageIds = new int[12];
		imageIds[0] = R.drawable.loader_frame_1;
		imageIds[1] = R.drawable.loader_frame_2;
		imageIds[2] = R.drawable.loader_frame_3;
		imageIds[3] = R.drawable.loader_frame_4;
		imageIds[4] = R.drawable.loader_frame_5;
		imageIds[5] = R.drawable.loader_frame_6;
		imageIds[6] = R.drawable.loader_frame_7;
		imageIds[7] = R.drawable.loader_frame_8;
		imageIds[8] = R.drawable.loader_frame_9;
		imageIds[9] = R.drawable.loader_frame_10;
		imageIds[10] = R.drawable.loader_frame_11;
		imageIds[11] = R.drawable.loader_frame_12;

		main_imageview.setImageIds(imageIds);
	}

	@Override
	protected void onResume() {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// Thread.currentThread().sleep(4000);
		// } catch (InterruptedException e) {
		//
		// e.printStackTrace();
		// }
		// }
		// }).start();
		super.onResume();
		// setNetwork();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		
		setNetwork();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (indexDBHelper != null) {
			indexDBHelper.close();
		}

		closeDB();
	}

	public void openDB() {
		if (indexDB == null || !indexDB.isOpen()) {
			indexDB = SQLiteDatabase.openDatabase(dataPath + dataName, null, SQLiteDatabase.OPEN_READWRITE);
		}
	}

	public void closeDB() {
		if (indexDB != null && indexDB.isOpen()) {
			indexDB.close();
		}
	}

	class LoadingDataTask extends AsyncTask<String, String, String> {
		
		@Override
		protected String doInBackground(String... params) {
			BaseConstant.weatherInfos.clear();
			BaseConstant.temperature = "";
			BaseConstant.init(LoadingActivity.this);
			indexDBHelper = new SQLiteDataBaseHelper(LoadingActivity.this, dataName, null, nowVersion);
			try {
				indexDBHelper.createDataBase(false, dataPath, dataName);
			} catch (Exception e) {
				e.printStackTrace();
			}
			openDB();

			try {
				/* 定义需要读取内容的URL链接地址. */
				URL myURL = new URL(BaseConstant.WEATHER_URL);
				/* 对于该URL地址, 初始化并打开连接. */
				URLConnection ucon = myURL.openConnection();
				ucon.setConnectTimeout(10 * 1000);
				/*
				 * 初始化一个InputStreams实例用以读取 从URLConnection返回的数据
				 */
				ucon.setDoOutput(true);
				java.io.BufferedOutputStream o = new java.io.BufferedOutputStream(ucon.getOutputStream(), 8 * 1024);
				o.write(("cityname=" + BaseConstant.cityName).getBytes("utf-8"));
				o.close();
				InputStream is = ucon.getInputStream();
				BufferedInputStream bis = new BufferedInputStream(is, 8 * 1024);
				/*
				 * 从缓存流实例中读取数据 直到其返回值为-1
				 */
				ByteArrayBuffer baf = new ByteArrayBuffer(50);
				int current = 0;
				while ((current = bis.read()) != -1) {
					baf.append((byte) current);
				}
				result = new String(baf.toByteArray());
				JSONObject jsonObject = new JSONObject(result);
				String cityName = jsonObject.getString("cityName");
				JSONArray jsonArray = jsonObject.getJSONArray("itemList");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObjectArray = null;
					for (int j = 0; j < jsonArray.length(); j++) {
						jsonObjectArray = (JSONObject) jsonArray.opt(j);
						if (jsonObjectArray.getInt("SEQNUM") == i + 1) {
							break;
						}
					}
					WeatherInfo weatherInfo = new WeatherInfo();
					weatherInfo.setCityName(cityName);
					// weatherInfo.setAddTime(jsonObjectArray.getString("addTime"));
					weatherInfo.setCdate(jsonObjectArray.getString("CDATE"));
					weatherInfo.setLowTemperature(jsonObjectArray.getString("LOWTEMPERATURE"));
					weatherInfo.setHighTemperature(jsonObjectArray.getString("HIGHTEMPERATURE"));
					weatherInfo.setWeatdate(jsonObjectArray.getString("WEATDATE"));
					weatherInfo.setWeather(jsonObjectArray.getString("WEATHER"));
					// weatherInfo.setWeathervane(jsonObjectArray.getString("weathervane"));
					// weatherInfo.setWindpower(jsonObjectArray.getString("windpower"));
					BaseConstant.weatherInfos.add(weatherInfo);
				}
				System.out.println("result--------->" + result);
				// BaseConstant.weatherInfos.clear();
			} catch (Exception e) {
				e.printStackTrace();
				isError = true;
			}

			// if (isError) {
			// Cursor cursor = indexDB.query(BaseConstant.DATA_TABLE_NAME_W,
			// new String[] { "*" }, "city = ?",
			// new String[] { BaseConstant.cityName }, null, null,
			// "id asc");
			// while (cursor.moveToNext()) {
			// int position = cursor.getPosition();
			// String temperature = cursor.getString(3);
			// String wash = cursor.getString(5);
			// int imgId = cursor.getInt(1);
			// if (position == 0) {
			// BaseConstant.imageId = imgId;
			// BaseConstant.temperature = temperature;
			// BaseConstant.wash = wash;
			// }
			// }
			// cursor.close();
			// } else {
			// int size = BaseConstant.weatherInfos.size();
			// for (int i = 0; i < size; i++) {
			// WeatherInfo weatherInfo = BaseConstant.weatherInfos.get(i);
			// String cdate = weatherInfo.getCdate();
			// String temperature = weatherInfo.getLowTemperature()
			// + "°C ~ " + weatherInfo.getHighTemperature() + "°C";
			// String weather = weatherInfo.getWeather();
			// String wash = "洗车的好天气";
			// int imgId = getWeatherImg(weather);
			// if (weather.indexOf("雨") >= 0) {
			// wash = "今天不宜洗车";
			// }
			// String sql = "update weather_info set imgId = '" + imgId
			// + "',cdate = '" + cdate + "',temperature = '"
			// + temperature + "',weather = '" + weather
			// + "',wash = '" + wash + "' where city = '"
			// + BaseConstant.cityName + "' and ";
			// if (i == 0) {
			// BaseConstant.imageId = imgId;
			// BaseConstant.temperature = temperature;
			// BaseConstant.wash = wash;
			// try {
			// indexDB.execSQL(sql
			// + "id = (select min(id) from weather_info where city = '"
			// + BaseConstant.cityName + "')");
			// } catch (Exception e) {
			// // TODO: handle exception
			// e.printStackTrace();
			// }
			// } else if (i == 1) {
			// try {
			// indexDB.execSQL(sql
			// + "id > (select min(id) from weather_info where city = '"
			// + BaseConstant.cityName
			// + "' )"
			// + " and id < (select max(id) from weather_info where city = '"
			// + BaseConstant.cityName + "')");
			// } catch (Exception e) {
			// // TODO: handle exception
			// e.printStackTrace();
			// }
			//
			// } else if (i == 2) {
			// try {
			// indexDB.execSQL(sql
			// + "id = (select max(id) from weather_info where city = '"
			// + BaseConstant.cityName + "')");
			// } catch (Exception e) {
			// // TODO: handle exception
			// e.printStackTrace();
			// }
			//
			// }
			// }
			// // }
			// }
			System.out.println("startActivity---MainActivity");
			if (!isStarted) {
				startActivity(new Intent().setClass(LoadingActivity.this, GridActivity.class));
				isStarted = true;
				finish();
			}
			return "";
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		public void onPostExecute(String Re) {

		}
		
	}

	public static int getWeatherImg(String weather) {
		int imgId;
		if (weather.indexOf("多云") >= 0) {
			if (weather.indexOf("晴") >= 0) {
				imgId = getImageId("weather07");
			} else {
				imgId = getImageId("weather09");
			}
		} else if (weather.indexOf("雪") >= 0) {
			imgId = getImageId("weather01");
		} else if (weather.indexOf("雷") >= 0) {
			imgId = getImageId("weather02");
		} else if (weather.indexOf("晴") >= 0) {
			imgId = getImageId("weather04");
		} else if (weather.indexOf("阴") >= 0) {
			imgId = getImageId("weather03");
		} else if (weather.indexOf("雨") >= 0) {
			if (weather.indexOf("暴") >= 0) {
				imgId = getImageId("weather06");
			} else {
				imgId = getImageId("weather05");
			}
		} else if (weather.indexOf("雾") >= 0) {
			imgId = getImageId("weather08");
		} else {
			imgId = getImageId("weather04");
		}
		return imgId;
	}

	public static int getImageId(String str) {
		int imgId = 0;
		try {
			imgId = R.drawable.class.getDeclaredField(str).getInt(new R.drawable());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		return imgId;
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
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());
						System.exit(0);
					}
					
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
					
				}).show();
		dialog.setCanceledOnTouchOutside(false);
	}
	
}
