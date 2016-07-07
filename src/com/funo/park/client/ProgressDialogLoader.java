package com.funo.park.client;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

public class ProgressDialogLoader {

	public ProgressDialog progressDialogLoader(final int time, final ProgressDialog progressDialog,
			final CloseProgressDialog closeProgressDialog) {
		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				closeProgressDialog.closeDialog(progressDialog);
			}

		};
		new Thread() {

			@Override
			public void run() {
				try {
					sleep(time * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Message msg = new Message();
				msg.what = 1;
				handler.sendMessage(msg);
			}

		}.start();
		return progressDialog;
	}

	public interface CloseProgressDialog {

		public void closeDialog(ProgressDialog progressDialog);

	}

}
