package com.funo.park.client;

import com.funo.park.data.FunoConst;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class PDialogClass {

	private Context context;
	private Handler handler;
	private ProgressDialog progressDialog;
	private static boolean mb;

	public PDialogClass(Context mcontext, Handler mHanlder, boolean b) {
		context = mcontext;
		handler = mHanlder;
		mb = b;
	}

	public boolean isMb() {
		return mb;
	}

	public void setMb(boolean mb) {
		this.mb = mb;
	}

	public ProgressDialog showProgressDialogWithB(int time, String text) {
		progressDialog = new ProgressDialog(context);
		progressDialog = new ProgressDialogLoader().progressDialogLoader(time, progressDialog,
				new ProgressDialogLoader.CloseProgressDialog() {

					@Override
					public void closeDialog(ProgressDialog progressDialog) {
						if (progressDialog.isShowing()) {
							progressDialog.dismiss();
							if (mb) {
								Message msg = new Message();
								msg.what = FunoConst.TimeOut;
								handler.sendMessage(msg);
							}
						}
					}

				});
		progressDialog.setMessage(text);
		progressDialog.show();
		return progressDialog;
	}

}
