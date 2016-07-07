package com.funo.park.location;

import java.io.IOException;
import java.util.List;

import com.amap.api.services.core.LatLonPoint;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

public class AndroidGeocoderService {

	private Geocoder geocoder;
	private Context mContext;
	private ProgressDialog progressDialog;

	private Handler handler;

	private GetAddressNameCallback getAddressNameCallback;

	public AndroidGeocoderService(Context context) {
		this.mContext = context;
		geocoder = new Geocoder(context);
		handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					if (getAddressNameCallback != null) {
						getAddressNameCallback.doCallback((String) msg.obj);
					}
				}
			}

		};
	}

	public String getLocationAddress(final LatLonPoint location) {
		String addressName = "未知地址";
		try {
			List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
					1);
			if (addresses != null && !addresses.isEmpty()) {
				for (Address addres : addresses) {
					String add = addres.getAddressLine(0);
					if (!TextUtils.isEmpty(add)) {
						addressName = add;
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return addressName;
	}
	
	public void getLocationAddress(final LatLonPoint location, GetAddressNameCallback getAddressNameCallback) {
		this.getAddressNameCallback = getAddressNameCallback;
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					String addressName = "未知地址";
					List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
							1);
					if (addresses != null && !addresses.isEmpty()) {
						for (Address addres : addresses) {
							String add = addres.getAddressLine(0);
							if (!TextUtils.isEmpty(add)) {
								addressName = add;
								break;
							}
						}
					}
					handler.obtainMessage(0, addressName).sendToTarget();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

	public void showProgressDialog(String title, String message) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(mContext);
		}
		progressDialog.setTitle(title);
		progressDialog.setMessage(message);
		progressDialog.show();
	}

	private void hideProgressDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
	
	public String getLocationAddressSync(final LatLonPoint location) {
		showProgressDialog(null, "获取地址信息中..."); 
		final MyHandler myHandler = new MyHandler();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),
							1);
					if (addresses != null && !addresses.isEmpty()) {
						for (Address addres : addresses) {
							String add = addres.getAddressLine(0);
							if (!TextUtils.isEmpty(add)) {
								Message msg = new Message();
								msg.what = 0;
								msg.obj = add;
								myHandler.sendMessage(msg);
								break;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}).start();
		while (true) {
			if (myHandler.successFlag) {
				break;
			}
		}
		hideProgressDialog();
		return myHandler.addressName;
	}
	
	public interface GetAddressNameCallback {

		public void doCallback(String addressName);

	}

	class MyHandler extends Handler {

		public boolean successFlag = false;
		public String addressName = "未知地址";
		
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {
				successFlag = true;
				addressName = (String) msg.obj;
			}
		}
		
	}
	
}
