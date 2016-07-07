package com.funo.park;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * <p>
 * Copyright: (c) 2013, 福建福诺移动通信技术有限公司 All rights reserved。
 * </p>
 * <p>
 * 文件名称：ApplicationException.java
 * </p>
 * <p>
 * 描 述：应用程序异常捕获机制
 * </p>
 * 
 * @author chenph
 * @date 2013-5-14
 * @version 1.0
 ******************************************************************************** 
 *          <p>
 *          修改记录
 *          </p>
 *          <p>
 *          修改时间：
 *          </p>
 *          <p>
 *          修 改 人：
 *          </p>
 *          <p>
 *          修改内容：
 *          </p>
 */
public class ApplicationException implements UncaughtExceptionHandler {

	private final String TAG = "wis";

	public static String ERRORREPORTEMAIL_SUBJECT = "email_subject";

	public static String ERRORREPORTEMAIL_TEXT = "email_text";

	private static ApplicationException applicationException;

	private Context context;

	private UncaughtExceptionHandler defaultExceptionHandler;

	private ApplicationException() {
	}

	public static ApplicationException getInstance() {
		if (applicationException == null)
			applicationException = new ApplicationException();
		return applicationException;
	}

	@Override
	public void uncaughtException(Thread thread, final Throwable t) {
		if (defaultExceptionHandler != null) {
			Log.e(TAG, t.getMessage(), t);
			Intent intent = new Intent(context, GridActivity.class);
			// 如果没有NEW_TASK标识且是UI线程抛的异常则界面卡死直到ANR
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
			System.exit(0);
		}
	}

	public void init(Context context) {
		this.context = context;
		defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
}
