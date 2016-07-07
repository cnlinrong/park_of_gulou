package com.funo.park.update;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.util.ByteArrayBuffer;

import com.funo.park.R;
import com.funo.park.base.ZxDataException;
import com.funo.park.util.BaseConstant;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

public class UpdateService extends Service {

	private NotificationManager mNM;
	private volatile Looper mServiceLooper;
	private volatile ServiceHandler mServiceHandler;
	private Logger log = Logger.getLogger(UpdateService.class.getName());
	int autoUpdateFlag = 1;// 1-提示是否自动更新 2-自动更新不提示
	// SharedPreferences defaultShared;
	// boolean manualUpdate = false;

	@Override
	public IBinder onBind(Intent intent) {
		System.out.println("service onbind..");
		return null;
	}

	@Override
	public void onCreate() {
		System.out.println("service oncreate...");
		// defaultShared = PreferenceManager.getDefaultSharedPreferences(this);
		// defaultShared.getBoolean("auto_update", false);
		// autoUpdateFlag =
		// Integer.parseInt(defaultShared.getString("auto_update_detail", "1"));
		// manualUpdate = defaultShared.getBoolean("manual_update", false);

		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
		thread.start();

		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
		Message msg = mServiceHandler.obtainMessage();
		// msg.arg1 = startId;
		// msg.arg2 = flags;
		// msg.obj = intent.getExtras();
		mServiceHandler.sendMessage(msg);
	}

	@Override
	public void onDestroy() {
		System.out.println("service ondestroy...");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("service onStartCommand...");
		return START_STICKY;
	}

	public ProgressDialog pBar;

	private int versionCode;

	private String versionName;

	private void check() {
		System.out.println("into check...");
		String result = null;
		String fileName = "";
		boolean isSuccess = false;
		try {
			/* 定义需要读取内容的URL链接地址. */
			URL myURL = new URL(BaseConstant.UPDATE_GETVERSION_URL);
			/* 对于该URL地址, 初始化并打开连接. */
			URLConnection ucon = myURL.openConnection();
			ucon.setConnectTimeout(60 * 1000);
			/*
			 * 初始化一个InputStreams实例用以读取 从URLConnection返回的数据
			 */
			InputStream is = ucon.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);

			/*
			 * 从缓存流实例中读取数据 直到其返回值为-1
			 */
			ByteArrayBuffer baf = new ByteArrayBuffer(50);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			/* 将读取的数据从字节形式转换为字符串. */
			result = new String(baf.toByteArray(), "gbk");
			String[] resultArry = result.split(";");
			if ("0".equals(resultArry[0])) {
				getVersion();
				isSuccess = true;
				if (Integer.parseInt(resultArry[1]) > versionCode
						|| Float.parseFloat(resultArry[3]) > Float.parseFloat(versionName)) {// 与服务器版本号比较
					fileName = resultArry[2];
					result = "发现新版本是否更新？\n" + resultArry[4];
				} else {
					result = null;
				}
			} else {
				throw new ZxDataException("获取服务器应用版本失败");
			}
		} catch (IOException e) {
			Log.e("", "", e);
			log.log(Level.SEVERE, "自动更新-与更新服务器IO异常", e);
			result = "请确认您已开启上网功能，与服务器的连接是否正常";
			isSuccess = false;
		} catch (ZxDataException e) {
			Log.e("", "", e);
			result = e.getMessage();
			log.log(Level.SEVERE, "自动更新-异常", e);
			isSuccess = false;
		} catch (Exception e) {
			Log.e("", "", e);
			isSuccess = false;
			log.log(Level.SEVERE, "自动更新-更新中出现异常", e);
			result = "掌信更新中出现异常";
		}
		if (result != null) {
			if (autoUpdateFlag == 1 || !isSuccess) {
				showNotification(result, isSuccess, fileName);
			} else {
				Intent alertIntent = new Intent(this, UpdatePrompt.class);
				Bundle bu = new Bundle();
				bu.putString("msg", result);
				bu.putBoolean("error_flag", isSuccess);
				bu.putString("file_name", fileName);
				bu.putBoolean("isnt_notify", true);// 不提示
				alertIntent.putExtras(bu);
				alertIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(alertIntent);
			}
		}

		stopSelf();
	}

	/**
	 * 获取当前版本信息
	 * 
	 * @throws Exception
	 */
	private void getVersion() throws Exception {
		try {
			PackageInfo packg = UpdateService.this.getPackageManager()
					.getPackageInfo(UpdateService.this.getPackageName(), PackageManager.GET_CONFIGURATIONS);
			versionCode = packg.versionCode;
			versionName = packg.versionName;
			System.out.println("versionCode = " + versionCode);
			System.out.println("versionName = " + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			throw new ZxDataException("获取当前应用版本失败");
		}
	}

	/**
	 * 提示信息
	 * 
	 * @param text
	 * @param isSuccess
	 * @param fileName
	 */
	@SuppressLint("NewApi")
	private void showNotification(String text, boolean isSuccess, String fileName) {

		// Set the icon, scrolling text and timestamp
		int drawId = isSuccess ? R.drawable.update_alert : R.drawable.update_err;
		Notification notification = null;

		// The PendingIntent to launch our activity if the user selects this
		// notification
		Intent alertIntent = new Intent(this, UpdatePrompt.class);
		Bundle bu = new Bundle();
		bu.putString("msg", text);
		bu.putBoolean("error_flag", isSuccess);
		bu.putString("file_name", fileName);
		alertIntent.putExtras(bu);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, alertIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			notification = new Notification.Builder(this).setAutoCancel(true).setContentTitle("更新提示")
					.setContentText(text).setContentIntent(contentIntent).setSmallIcon(drawId)
					.setWhen(System.currentTimeMillis()).build();
		} else {
			Notification.Builder builder = new Notification.Builder(this).setAutoCancel(true).setContentTitle("title")
					.setContentText("describe").setContentIntent(contentIntent).setSmallIcon(R.drawable.ic_launcher)
					.setWhen(System.currentTimeMillis()).setOngoing(true);
			notification = builder.getNotification();
		}

		// notification.flags |= Notification.FLAG_ONGOING_EVENT;
		// Send the notification.
		// We use a layout id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(R.string.update_notification, notification);
		Uri notifiRing = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_NOTIFICATION);
		if (notifiRing != null) {
			// Ringtone ringTone = RingtoneManager.getRingtone(this,notifiRing);
			// ringTone.play();
			MediaPlayer media = new MediaPlayer();
			try {
				media.setDataSource(this, notifiRing);
				media.prepare();
				media.setLooping(false);
				// media.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// Intent ringIntent = new
		// Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		// ringIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// ringIntent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_NOTIFICATION);
		// startActivity(ringIntent);
	}

	private final class ServiceHandler extends Handler {
		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			check();
		}

	};
}
