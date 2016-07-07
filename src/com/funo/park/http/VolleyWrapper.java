package com.funo.park.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.content.Context;

public class VolleyWrapper {

	/**
	 * 最大重试次数
	 */
	public static final int MAX_RETRY_NUM = 1;

	/**
	 * 不重试
	 */
	public static final int NO_RETRY_NUM = 0;

	private int timeoutMs = 10 * 1000;// 超时时间，默认10秒

	private RequestQueue requestQueue;

	public VolleyWrapper(Context context) {
		requestQueue = Volley.newRequestQueue(context);
	}

	public void post(String url, final HashMap<String, String> params, Listener<String> listener,
			ErrorListener errorListener, int retryNum) {
		StringRequest request = new StringRequest(Method.POST, url, listener, errorListener) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				for (Entry<String, String> entry : params.entrySet()) {
					if (entry.getValue() == null) {
						paramsMap.put(entry.getKey(), "");
					} else {
						paramsMap.put(entry.getKey(), entry.getValue());
					}
				}
				return paramsMap;
			}

		};
		request.setRetryPolicy(new DefaultRetryPolicy(timeoutMs, retryNum, 1f));
		requestQueue.add(request);
	}

}
