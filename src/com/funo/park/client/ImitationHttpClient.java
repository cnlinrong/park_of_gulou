package com.funo.park.client;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.funo.park.R;
import com.funo.park.util.CommonUtil;
import com.funo.park.util.PropertiesUtil;

import android.content.Context;
import android.os.Handler;

/**
 * @author: 胡 辉
 * @time ：2011-10-26 上午10:25:38 类说明
 */
public class ImitationHttpClient {

	private DefaultHttpClient client;
	private String defaultCharset = "UTF-8";
	private String defaltUrl = null;
	private String projectPackage = null;

	private Handler handler;
	private Timer timer;
	private TimerTask task = null;

	// private Thread thread=null;

	public ImitationHttpClient(Context context, String url) {
		// travelApp =TravelApp.getInstance();
		// if(!travelApp.checkNetworkInfo()){
		// Message msg = new Message();
		// //发送消息
		// handler.sendMessage(msg);
		// }
		// timer = new Timer();
		// task = new TimerTask()
		// {
		// @Override
		// public void run()
		// {
		// //新启动的线程无法访问该Activity里的组件
		// //所以需要通过Handler发送信息
		// travelApp =TravelApp.getInstance();
		// if(!travelApp.checkNetworkInfo()){
		// Message msg = new Message();
		// //发送消息
		// handler.sendMessage(msg);
		// }
		// while(!travelApp.checkNetworkInfo())
		// {
		//
		//
		// }
		// timer.cancel();
		// client = new DefaultHttpClient();
		// defaltUrl = PropertiesUtil.readPropsValue( context,R.raw.config,
		// url);
		// projectPackage = PropertiesUtil.readPropsValue( context,R.raw.config,
		// "projectPackage");
		// }
		// };
		// timer.schedule(task, 0 , 1000);
		//
		//
		// handler = new Handler() {
		//
		//
		// @Override
		// public void handleMessage(Message msg) {
		//
		// travelApp.openNetworkSettings(0,"本操作需要使用网络资源，是否开启网络？") ;
		//
		// }
		// };
		client = new DefaultHttpClient();
		defaltUrl = PropertiesUtil.readPropsValue(context, R.raw.config, url);
		projectPackage = PropertiesUtil.readPropsValue(context, R.raw.config, "projectPackage");

	}

	/**
	 * doPost请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应数据
	 * @throws JSONException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws InstantiationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws java.text.ParseException
	 */
	@SuppressWarnings("deprecation")
	public Object doPost(Object reqObj, Class rspClass) {
		// 传递的参数
		// Object rspObj = null;
		// try {
		// rspObj = rspClass.newInstance();
		// List<NameValuePair> params = new ArrayList<NameValuePair>();
		// JSONObject obj
		// =(JSONObject)CommonUtil.getJsonFromObject(reqObj,this.projectPackage);
		// params.add(new BasicNameValuePair("jsonStr",obj.toString()));
		// String s = new HttpManager(defaltUrl).submitRequest(params);
		// JSONObject jsonObj = new JSONObject(s);
		// CommonUtil.getObjectFromJson(jsonObj, rspObj,this.projectPackage);
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InstantiationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (NoSuchMethodException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (InvocationTargetException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (java.text.ParseException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		HttpPost httpPost = null;
		Object rspObj = null;
		try {
			rspObj = rspClass.newInstance();
			// CommonUtil.setProjectPackage(projectPackage);
			JSONObject obj = (JSONObject) CommonUtil.getJsonFromObject(reqObj, this.projectPackage);
			httpPost = new HttpPost(defaltUrl);
			HttpParams httpParams = httpPost.getParams();
			// 设置网络超时参数
			HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
			HttpConnectionParams.setSoTimeout(httpParams, 20000);
			System.out.println("httpPost.isAborted()---->" + httpPost.isAborted());
			if (client != null && httpPost != null && !httpPost.isAborted()) {
				if (obj != null) {
					List<NameValuePair> list = new ArrayList<NameValuePair>();
					list.add(new BasicNameValuePair("jsonStr", obj.toString()));
					System.out.println(new UrlEncodedFormEntity(list, defaultCharset).toString());
					httpPost.setEntity(new UrlEncodedFormEntity(list, defaultCharset));
				}
				HttpResponse response = client.execute(httpPost);
				JSONObject jsonObj = new JSONObject(
						EntityUtils.toString(response.getEntity(), defaultCharset).toString());
				System.out.println("jsonObj--->" + jsonObj);
				CommonUtil.getObjectFromJson(jsonObj, rspObj, this.projectPackage);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpPost.abort();
			client.getConnectionManager().closeExpiredConnections();
		}
		return rspObj;
	}

	public Object doPost(Object reqObj, Class rspClass, String packageStr) throws SecurityException,
			IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
			JSONException, InstantiationException, ClientProtocolException, IOException, java.text.ParseException {

		Object rspObj = rspClass.newInstance();
		// CommonUtil.setProjectPackage(projectPackage);
		JSONObject obj = (JSONObject) CommonUtil.getJsonFromObject(reqObj, packageStr);
		HttpPost httpPost = new HttpPost(defaltUrl);
		HttpParams httpParams = httpPost.getParams();
		// 设置网络超时参数
		HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
		HttpConnectionParams.setSoTimeout(httpParams, 60000);
		if (obj != null) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();

			list.add(new BasicNameValuePair("jsonStr", obj.toString()));
			httpPost.setEntity(new UrlEncodedFormEntity(list, defaultCharset));
		}

		HttpResponse response = client.execute(httpPost);
		JSONObject jsonObj = new JSONObject(EntityUtils.toString(response.getEntity(), defaultCharset).toString());

		CommonUtil.getObjectFromJson(jsonObj, rspObj, packageStr);

		return rspObj;
	}

	/**
	 * doGet请求
	 * 
	 * @param url
	 *            请求地址
	 * @return 响应数据
	 */
	public String doGet(String url) {
		try {
			HttpResponse response = client.execute(new HttpGet(url));
			String content = EntityUtils.toString(response.getEntity(), defaultCharset);
			return content;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * doPost请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应数据
	 */
	public String doPost(String jsonStr, String url, String business) {
		try {
			if (url == null) {
				url = defaltUrl;
			}
			HttpPost httpPost = new HttpPost(url);
			if (jsonStr != null && !jsonStr.equals("")) {
				List<NameValuePair> list = new ArrayList<NameValuePair>();

				list.add(new BasicNameValuePair("jsonStr", jsonStr));
				list.add(new BasicNameValuePair("business", business));
				httpPost.setEntity(new UrlEncodedFormEntity(list, defaultCharset));
			}

			HttpResponse response = client.execute(httpPost);
			return EntityUtils.toString(response.getEntity(), defaultCharset);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * doPut 请求
	 * 
	 * @param url
	 *            请求地址
	 * @param params
	 *            请求参数
	 * @return 响应数据
	 * @return
	 */
	public String doPut(String url, Map<String, String> params) {
		try {
			HttpPut httpPut = new HttpPut(url);
			if (params != null && params.size() > 0) {
				List<NameValuePair> list = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entity : params.entrySet()) {
					System.out.println(entity.getKey() + "--" + entity.getValue());
					list.add(new BasicNameValuePair(entity.getKey(), entity.getValue()));
				}
				httpPut.setEntity(new UrlEncodedFormEntity(list, defaultCharset));
			}
			HttpResponse httpResponse = client.execute(httpPut);
			return EntityUtils.toString(httpResponse.getEntity(), defaultCharset);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * HpptDelete请求
	 * 
	 * @param delete
	 *            请求地址
	 * @return 响应数据
	 */
	public String doDelete(HttpDelete delete) {
		try {
			HttpResponse httpResponse = client.execute(delete);
			return EntityUtils.toString(httpResponse.getEntity(), defaultCharset);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
