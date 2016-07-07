package com.funo.park;

import com.iflytek.cloud.SpeechUtility;
import com.tencent.bugly.crashreport.CrashReport;

import android.app.Application;

public class MyApplication extends Application {

	private String sd_path = "/data/data/com.funo.carclub/";

	private boolean isRunningUpdate = false;
	private boolean isCMCC = false;
	private boolean isfinish = false;
	private boolean isConnectCmcc = false;
	static String imageNames[];
	static String imageColors[];

	@Override
	public void onCreate() {
		SpeechUtility.createUtility(MyApplication.this, "appid=5775d092");
		
		super.onCreate();

//		ApplicationException.getInstance().init(getApplicationContext());

		CrashReport.initCrashReport(getApplicationContext(), "900035672", true);
	}

}
