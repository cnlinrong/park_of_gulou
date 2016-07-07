package com.funo.park.update;

import java.io.File;

import com.funo.park.R;
import com.funo.park.util.BaseConstant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class UpdatePrompt extends Activity {

	private String msg = "";
	private NotificationManager nm;
	private ProgressDialog pBar;
	private Handler handler;
	private String fileName;
	private boolean isUpdate = true;
	private final int NOTIFY_WITH_CHOICE = 0;
	private final int NOTIFY_WITHOUT_CHOICE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Bundle bu = getIntent().getExtras();
		msg = bu.getString("msg");
		fileName = bu.getString("file_name");
		boolean isSuccess = bu.getBoolean("error_flag");
		boolean isntNotify = bu.getBoolean("isnt_notify");
		if (isSuccess) {
			if (!isntNotify)
				showDialog(NOTIFY_WITH_CHOICE);
			else
				downFile();
		} else {
			showDialog(NOTIFY_WITHOUT_CHOICE);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case NOTIFY_WITH_CHOICE:
			return new AlertDialog.Builder(UpdatePrompt.this).setTitle(R.string.update_notification_title)
					.setMessage(msg)
					.setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							nm.cancel(R.string.update_notification);
							UpdatePrompt.this.finish();
						}
					}).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							nm.cancel(R.string.update_notification);
							downFile();
						}
					}).create();
		default:
			return new AlertDialog.Builder(UpdatePrompt.this).setTitle(R.string.update_notification_title)
					.setMessage(msg).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							nm.cancel(R.string.update_notification);
							UpdatePrompt.this.finish();
						}
					}).create();

		}

	}

	void downFile() {
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (isUpdate) {
					switch (msg.arg1) {
					case 0:
						pBar.cancel();
						Intent intent = new Intent(UpdatePrompt.this, UpdatePrompt.class);
						intent.putExtra("msg", msg.obj.toString());
						intent.putExtra("error_flag", false);
						startActivity(intent);
						UpdatePrompt.this.finish();
						break;
					case 1:
						break;
					case 10001:
						System.out.println("(Float)msg.obj--->" + (Float) msg.obj);
						int progressL = (int) (((Float) msg.obj) * 100);
						pBar.setProgress(progressL);
						break;
					default:
						pBar.cancel();
						Intent intent2 = new Intent(Intent.ACTION_VIEW);
						intent2.setDataAndType(Uri.fromFile(new File("/sdcard/" + msg.obj.toString())),
								"application/vnd.android.package-archive");
						startActivity(intent2);
						UpdatePrompt.this.finish();
						break;
					}
				}
			}
		};
		if (BaseConstant.isRunningUpdate) {
			Message msg = handler.obtainMessage();
			msg.arg1 = 0;
			msg.obj = "已经正在更新中";
			handler.sendMessage(msg);
			return;
		}
		BaseConstant.isRunningUpdate = true;

		pBar = new ProgressDialog(this);

		pBar.setTitle("正在下载更新");

		pBar.setMessage("请稍候...");
		pBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pBar.setMax(100);
		pBar.setProgress(0);
		pBar.setIndeterminate(false);
		pBar.setCancelable(true);
		pBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				isUpdate = false;
				BaseConstant.isRunningUpdate = false;
				UpdatePrompt.this.finish();
				System.out.println("progress cancel..");
			}
		});
		pBar.setButton("取消更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isUpdate = false;
				BaseConstant.isRunningUpdate = false;
				UpdatePrompt.this.finish();
			}
		});
		pBar.setButton2("后台运行", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UpdatePrompt.this.finish();
			}
		});

		pBar.show();
		final UpdateOperation updateOperation = new UpdateOperation(this, handler);
		final Thread updaeThread = new Thread() {
			@Override
			public void run() {
				if (isUpdate) {
					String[] opRet = updateOperation.update(fileName);
					if (Boolean.parseBoolean(opRet[0])) {// 下载成功
						Message msg = handler.obtainMessage();
						msg.arg1 = 2;
						msg.obj = fileName;
						handler.sendMessage(msg);
					} else {// 下载失败
						Message msg = handler.obtainMessage();
						msg.arg1 = 0;
						msg.obj = opRet[1];
						handler.sendMessage(msg);
					}
				}
				BaseConstant.isRunningUpdate = false;
			}
		};
		updaeThread.start();
	}
}
