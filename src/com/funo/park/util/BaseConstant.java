package com.funo.park.util;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.services.core.LatLonPoint;
import com.funo.park.client.po.ParkListReturnInfo.ParkListReturnData;
import com.funo.park.info.WeatherInfo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class BaseConstant {

	public static final String DATA_BASE_PATH = "/data/data/com.funo.park/databases/";
	public static final String DATA_BASE_NAME = "indexDb.db";
	public static final String DATA_TABLE_NAME_W = "weather_info";
	public static final String DATA_TABLE_NAME_S = "searchKey_info";
	public static final int dbVersion = 1;

	public static int width;
	public static int height;

	public static final String cityName = "福州";
	public static final String cityCode = "0591";
	public static final int POISEARCH = 1000;
	public static final int POI_OTHER_SEARCH = 1001;
	public static final int ERROR = 1001;
	public static final int FIRST_LOCATION = 1002;
	public static final int SEARCH = 1003;
	public static final int NOTHING = 1004;
	public static final int HIDE = 1005;

	public static final String UPDATE_GETVERSION_URL = "http://218.207.217.139/travel_system/update/park_of_gulou.txt";
	public static final String UPDATE_DOWNLOAD_URL = "http://218.207.217.139/travel_system/update/";
	public static boolean isRunningUpdate = false;
	public static final String APK_NAME = "park_of_gulou.apk";

	public static final String WEATHER_URL = "http://218.207.182.81/CxtAdmin/httpClient/weather.jsp";
	public static int imageId = 0;
	public static String wash = "";
	public static String temperature = "";
	public static int weatherSize = 0;
	public static ArrayList<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();

	public static String distance;
	public static String homePage;
	public static String nearDistance;
	public static StringBuilder voiceStr;

	public static LatLonPoint geoPoint;
	public static List<ParkListReturnData> prdList;

	public static int routeInt = 0;
	public static long fileLength;

	public static int index = 0;

	public static boolean detect(Activity act) {
		ConnectivityManager manager = (ConnectivityManager) act.getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (manager == null) {
			return false;
		}
		NetworkInfo networkinfo = manager.getActiveNetworkInfo();
		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}
		return true;
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static String changeLength(String str, int average) {
		int length = str.length();
		int lump = length / average;
		String indexStr = "";
		if (lump == 0) {
			indexStr = str;
		} else {
			for (int i = 0; i < lump; i++) {
				indexStr = indexStr + str.substring(i * average, (i + 1) * average) + "\n";
			}
			indexStr = indexStr + str.substring(lump * average, length);
		}
		return indexStr;
	}

	public static void init(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("data", 0);
		String distanceStr = sharedPreferences.getString("distance", "");
		String homePageStr = sharedPreferences.getString("homePage", "");
		String nearDistanceStr = sharedPreferences.getString("nearDistance", "");
		if (nearDistanceStr == "") {
			BaseConstant.nearDistance = "3000米以内";
		} else {
			BaseConstant.nearDistance = nearDistanceStr;
		}

		if (distanceStr == "") {
			BaseConstant.distance = "500米以内";
		} else {
			BaseConstant.distance = distanceStr;
		}

		if (homePageStr == "") {
			BaseConstant.homePage = "一键停车";
		} else {
			// BaseConstant.homePage = homePageStr;
			BaseConstant.homePage = "一键停车";
		}
		// System.out.println(BaseConstant.distance + "----" +
		// BaseConstant.homePage);
	}

}
