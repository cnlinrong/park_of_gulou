package com.funo.park.update;

import java.io.File;

import com.funo.park.R;
import com.funo.park.util.BaseConstant;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateManulActivity extends Activity {

	private ProgressDialog progress;
	private Handler handler;
	private boolean isUpdate = true;
	private final int UPDATE_ERROR = 0;
	private final int CHECK_SUCCESS = 1;
	private final int DOWNLOAD = 10001;
	ProgressDialog pro;
	UpdateOperation updateOperation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		nm.cancel(R.string.update_notification);
		progress = new ProgressDialog(this);
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (isUpdate) {
					switch (msg.arg1) {
					case UPDATE_ERROR:
						progress.cancel();
						Intent intent = new Intent(UpdateManulActivity.this, UpdatePrompt.class);
						intent.putExtra("msg", msg.obj.toString());
						intent.putExtra("error_flag", false);
						startActivity(intent);
						UpdateManulActivity.this.finish();
						BaseConstant.isRunningUpdate = false;
						break;
					case CHECK_SUCCESS:
						progress.hide();
						pro = new ProgressDialog(UpdateManulActivity.this);
						pro.setOnCancelListener(new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								isUpdate = false;
								BaseConstant.isRunningUpdate = false;
								UpdateManulActivity.this.finish();
							}
							
						});
						final String[] ret = (String[]) msg.obj;
						new AlertDialog.Builder(UpdateManulActivity.this).setTitle(R.string.update_notification_title)
								.setMessage(ret[1])
								.setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
									
							@Override
							public void onClick(DialogInterface dialog, int which) {

								pro.setTitle("正在下载更新版本");
								pro.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
								pro.show();
								pro.setMax(100);
								pro.setProgress(0);
								pro.setIndeterminate(false);
								pro.setCancelable(true);

								new Thread() {
									
									@Override
									public void run() {
										// 下载服务器版本
										if (isUpdate) {
											String[] opRet = updateOperation.update(ret[2]);
											if (Boolean.parseBoolean(opRet[0])) {// 下载成功
												Message msg2 = handler.obtainMessage();
												msg2.arg1 = 2;
												msg2.obj = ret[2];
												handler.sendMessage(msg2);
											} else {// 下载失败
												Message msg2 = handler.obtainMessage();
												msg2.arg1 = UPDATE_ERROR;
												msg2.obj = opRet[1];
												handler.sendMessage(msg2);
											}
										}
									}
								}.start();
							}
						}).setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								progress.cancel();
								isUpdate = false;
								BaseConstant.isRunningUpdate = false;
							}
						}).create().show();
						break;
					case DOWNLOAD:
						int progressL = (int) (((Float) msg.obj) * 100);
						pro.setProgress(progressL);
						break;
					default:
						if (progress.isShowing()) {
							progress.cancel();
						}
						Intent intent2 = new Intent(Intent.ACTION_VIEW);
						intent2.setDataAndType(Uri.fromFile(new File("/sdcard/" + msg.obj.toString())),
								"application/vnd.android.package-archive");
						startActivity(intent2);
						UpdateManulActivity.this.finish();
						BaseConstant.isRunningUpdate = false;
						break;
					}
				}
			}
		};
		updateOperation = new UpdateOperation(this, handler);
		if (BaseConstant.isRunningUpdate) {
			Message msg = handler.obtainMessage();
			msg.arg1 = UPDATE_ERROR;
			msg.obj = "已经正在更新中";
			handler.sendMessage(msg);
			return;
		}
		BaseConstant.isRunningUpdate = true;
		final Thread updaeThread = new Thread() {
			@Override
			public void run() {
				// 比较版本
				final String[] ret = updateOperation.check();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message message = handler.obtainMessage();
				message.obj = ret[1];
				if (Boolean.parseBoolean(ret[0])) {// 校验版本成功
					message.arg1 = CHECK_SUCCESS;
					message.obj = ret;
				} else {// 校验版本失败
					message.arg1 = 0;
				}
				handler.sendMessage(message);
			}
		};
		progress.setTitle("正在获取版本信息");
		progress.setMessage("请稍候...");
		progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progress.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				// UpdateManulActivity.this.finish();
				isUpdate = false;
				BaseConstant.isRunningUpdate = false;
				UpdateManulActivity.this.finish();
				System.out.println("progress cancel..");
			}
		});
		progress.setButton("取消更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isUpdate = false;
				// if(updaeThread.isAlive()){
				// System.out.println("stop thread..");
				// updaeThread.interrupt();
				// android.os.Process.killProcess(android.os.Process.myPid());
				// }
				BaseConstant.isRunningUpdate = false;
				UpdateManulActivity.this.finish();
			}
		});
		progress.setButton2("后台运行", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				UpdateManulActivity.this.finish();
			}
		});
		progress.show();
		updaeThread.start();
	}
}
