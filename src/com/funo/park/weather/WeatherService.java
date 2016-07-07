package com.funo.park.weather;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.amap.api.services.core.w;
import com.amap.api.services.weather.LocalDayWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecast;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearch.OnWeatherSearchListener;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.funo.park.R;
import com.funo.park.info.WeatherInfo;
import com.funo.park.util.ToastUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class WeatherService implements OnWeatherSearchListener {
	
	private List<WeatherInfo> weatherInfos = new ArrayList<WeatherInfo>();
	
	private Context mContext;
	private WeatherCallback weatherCallback;
	
	private WeatherSearchQuery mquery;
	private WeatherSearch mweathersearch;
	private LocalWeatherLive weatherlive;
	private LocalWeatherForecast weatherforecast;
	private List<LocalDayWeatherForecast> forecastlist = null;
	private String cityname = "福州市";// 天气搜索的城市，可以写名称或adcode；
	
	private Handler handler;
	
	private boolean forecast_flag = false;
	private boolean live_flag = false;
	private final int FORECAST_SUCCESS = 0;
	private final int FORECAST_FAIL = 1;
	private final int LIVE_SUCCESS = 2;
	private final int LIVE_FAIL = 3;
	
	public WeatherService(Context context, final WeatherCallback weatherCallback) {
		this.mContext = context;
		this.weatherCallback = weatherCallback;
		
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case FORECAST_SUCCESS:
					forecast_flag = true;
					if (forecast_flag && live_flag) {
						if (weatherCallback != null) {
							weatherCallback.doSuccessCallback(weatherInfos);
						}
					}
					break;
				case FORECAST_FAIL:
					forecast_flag = true;
					if (forecast_flag && live_flag) {
						if (weatherCallback != null) {
							weatherCallback.doFailCallback((String) msg.obj);
						}
					}
					break;
				case LIVE_SUCCESS:
					live_flag = true;
					if (forecast_flag && live_flag) {
						if (weatherCallback != null) {
							weatherCallback.doSuccessCallback(weatherInfos);
						}
					}
					break;
				case LIVE_FAIL:
					live_flag = true;
					if (forecast_flag && live_flag) {
						if (weatherCallback != null) {
							weatherCallback.doFailCallback((String) msg.obj);
						}
					}
					break;
				}
			}

		};
	}

	public void search() {
		searchliveweather();
		searchforecastsweather();
	}
	
	private void searchforecastsweather() {
		mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_FORECAST);// 检索参数为城市和天气类型，实时天气为1、天气预报为2
		mweathersearch = new WeatherSearch(mContext);
		mweathersearch.setOnWeatherSearchListener(this);
		mweathersearch.setQuery(mquery);
		mweathersearch.searchWeatherAsyn(); // 异步搜索
	}

	private void searchliveweather() {
		mquery = new WeatherSearchQuery(cityname, WeatherSearchQuery.WEATHER_TYPE_LIVE);// 检索参数为城市和天气类型，实时天气为1、天气预报为2
		mweathersearch = new WeatherSearch(mContext);
		mweathersearch.setOnWeatherSearchListener(this);
		mweathersearch.setQuery(mquery);
		mweathersearch.searchWeatherAsyn(); // 异步搜索
	}

	/**
	 * 实时天气查询回调
	 */
	@Override
	public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
		Message msg = new Message();
		if (rCode == 1000) {
			if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
				weatherlive = weatherLiveResult.getLiveResult();
				WeatherInfo weatherInfo = new WeatherInfo();
				weatherInfo.setCname(cityname);
				weatherInfo.setWeather(weatherlive.getWeather());
				weatherInfo.setLowTemperature(weatherlive.getTemperature());
				weatherInfo.setHighTemperature(weatherlive.getTemperature());
				weatherInfos.add(weatherInfo);
				msg.what = LIVE_SUCCESS;
			} else {
				msg.what = LIVE_FAIL;
				msg.obj = mContext.getString(R.string.no_result);
			}
		} else {
			msg.what = LIVE_FAIL;
			msg.obj = "查询实时天气信息失败";
		}
		handler.sendMessage(msg);
	}

	/**
	 * 天气预报查询结果回调
	 */
	@Override
	public void onWeatherForecastSearched(LocalWeatherForecastResult weatherForecastResult, int rCode) {
		Message msg = new Message();
		if (rCode == 1000) {
			if (weatherForecastResult != null && weatherForecastResult.getForecastResult() != null
					&& weatherForecastResult.getForecastResult().getWeatherForecast() != null
					&& weatherForecastResult.getForecastResult().getWeatherForecast().size() > 0) {
				weatherforecast = weatherForecastResult.getForecastResult();
				forecastlist = weatherforecast.getWeatherForecast();
				fillforecast();
				msg.what = FORECAST_SUCCESS;
			} else {
				msg.what = FORECAST_FAIL;
				msg.obj = mContext.getString(R.string.no_result);
			}
		} else {
			msg.what = FORECAST_FAIL;
			msg.obj = "查询天气预报信息失败";
		}
		handler.sendMessage(msg);
	}

	private void fillforecast() {
		for (int i = 0; i < forecastlist.size(); i++) {
			LocalDayWeatherForecast localdayweatherforecast = forecastlist.get(i);
			String week = null;
			switch (Integer.valueOf(localdayweatherforecast.getWeek())) {
			case 1:
				week = "周一";
				break;
			case 2:
				week = "周二";
				break;
			case 3:
				week = "周三";
				break;
			case 4:
				week = "周四";
				break;
			case 5:
				week = "周五";
				break;
			case 6:
				week = "周六";
				break;
			case 7:
				week = "周日";
				break;
			default:
				break;
			}
			if (i == 0) {
				WeatherInfo weatherInfo = weatherInfos.get(i);
				if (!TextUtils.isEmpty(localdayweatherforecast.getDayTemp())) {
					weatherInfo.setHighTemperature(localdayweatherforecast.getDayTemp());
				}
				weatherInfo.setLowTemperature(localdayweatherforecast.getNightTemp());
				weatherInfo.setCdate(week + " " + formatDate(localdayweatherforecast.getDate()));
			} else if (i == 1 || i == 2) {
				WeatherInfo weatherInfo = new WeatherInfo();
				weatherInfo.setWeather(localdayweatherforecast.getDayWeather());
				weatherInfo.setLowTemperature(localdayweatherforecast.getNightTemp());
				weatherInfo.setHighTemperature(localdayweatherforecast.getDayTemp());
				weatherInfo.setCdate(week + " " + formatDate(localdayweatherforecast.getDate()));
				weatherInfos.add(weatherInfo);
			}
		}
	}
	
	public interface WeatherCallback {
		
		public void doSuccessCallback(List<WeatherInfo> weatherInfos);
		
		public void doFailCallback(String msg);
		
	}
	
	private String formatDate(String source) {
		String dateStr = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = simpleDateFormat.parse(source);
			simpleDateFormat = new SimpleDateFormat("MM月dd日");
			dateStr = simpleDateFormat.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}
	
}
