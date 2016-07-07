package com.funo.park.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

@SuppressWarnings("deprecation")
public class HttpManager {

	private String url;
	private DefaultHttpClient httpclient;
	private HttpPost httpPost;
	private HttpResponse httpResponse;

	public HttpManager(String url_in) {
		init(url_in);
	}

	/**
	 * 初始化参数
	 * 
	 * @param url
	 */
	private void init(String url_in) {
		this.url = url_in;
		try {
			httpclient = new DefaultHttpClient(); // 设置本地为客户端
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);// 请求超时
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000); // 读取超时
			httpPost = new HttpPost(url); // 新建一个Post请求
		} catch (Exception e) {
			httpPost.abort();
		}
	}

	/**
	 * 提交HTTP请求
	 * 
	 * @param params
	 *            请求参数
	 * @return
	 */
	public String submitRequest(List<NameValuePair> params) {

		String res_str = null; // 收到的结果

		if (httpclient != null && httpPost != null && !httpPost.isAborted()) {

			// 设置httpPost请求参数及编码
			try {
				// 用确定编码格式来把参数进行编码
				httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			} catch (UnsupportedEncodingException e1) {
				Log.e("Exception", "输入参数编码有问题:UnsupportedEncodingException");
				return res_str;
			}

			// 第二步，使用execute方法发送HTTP POST请求，并返回HttpResponse对象
			try {
				httpResponse = httpclient.execute(httpPost);
			} catch (ClientProtocolException e) {
				Log.e("Exception", "服务器无应答:ClientProtocolException");
				return res_str;
			} catch (IOException e) {
				Log.e("Exception", "服务器无应答:IOException");
				return res_str;
			} catch (Exception e) {
				Log.e("Exception", "服务器无应答:OtherException");
				return res_str;
			}

			// 返回结果为200时，处于正常状态。
			if (httpResponse != null && (httpResponse.getStatusLine().getStatusCode() == 200)) {

				// 第三步，使用getEntity方法活得返回结果
				try {
					res_str = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				} catch (ParseException e) {
					Log.e("Exception", "返回结果转换异常:ParseException");
					e.printStackTrace();
				} catch (IOException e) {
					Log.e("Exception", "返回结果输出异常:IOException");
				} catch (Exception e) {
					Log.e("Exception", "返回结果异常:OtherException");

				}
			}

			httpPost.abort();

			// 强制关闭连接
			httpclient.getConnectionManager().closeExpiredConnections();

		}

		Log.d("Exception", "HttpManager.res_str=" + res_str);

		return res_str;
	}

}
