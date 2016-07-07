package com.funo.park.update;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.ByteArrayBuffer;

import com.funo.park.base.ZxDataException;
import com.funo.park.util.BaseConstant;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class UpdateOperation {

	private Context context;
	private int versionCode;
	private String versionName;
	private Handler handler;
	private Logger log = Logger.getLogger(this.getClass().getName());

	public UpdateOperation(Context context, Handler handler) {
		this.context = context;
		this.handler = handler;
	}

	/**
	 * 校验本地版本与服务器版本号
	 * 
	 * @return String[] {isSuccess,result,filename}
	 */
	public String[] check() {
		String[] resultArr = new String[3];
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
					result = "您的版本已经是最新版本，无需更新";
					isSuccess = false;
				}
			} else {
				throw new ZxDataException("获取服务器应用版本失败");
			}
		} catch (IOException e) {
			Log.e("", "", e);
			isSuccess = false;
			log.log(Level.SEVERE, "更新获取版本信息时异常", e);
			result = "请确认您已开启上网功能，与服务器的连接是否正常";
		} catch (ZxDataException e) {
			Log.e("", "", e);
			isSuccess = false;
			log.log(Level.SEVERE, "更新获取版本信息时异常", e);
			result = e.getMessage();
		} catch (Exception e) {
			Log.e("", "", e);
			isSuccess = false;
			log.log(Level.SEVERE, "更新获取版本信息时异常", e);
			result = "掌信更新中出现异常";
		}
		resultArr[0] = String.valueOf(isSuccess);
		resultArr[1] = result;
		resultArr[2] = fileName;
		return resultArr;
	}

	/**
	 * 获取本地版本信息
	 * 
	 * @throws ZxDataException
	 */
	private void getVersion() throws ZxDataException {
		try {
			PackageInfo packg = this.context.getPackageManager().getPackageInfo(this.context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			versionCode = packg.versionCode;
			versionName = packg.versionName;
			System.out.println("versionCode = " + versionCode);
			System.out.println("versionName = " + versionName);
		} catch (NameNotFoundException e) {
			log.log(Level.SEVERE, "更新获取本地版本信息时异常", e);
			throw new ZxDataException("获取当前应用版本失败");
		}
	}

	/**
	 * 从服务器下载更新版本
	 * 
	 * @param fileName
	 * @return String[]{isSuccess,result}
	 */
	public String[] update(final String fileName) {
		int compeleteSize = 0;
		String[] retArr = new String[3];
		boolean isDownSuccess = false;
		System.out.println(BaseConstant.UPDATE_DOWNLOAD_URL + fileName);
		try {
			URL myURL = new URL(BaseConstant.UPDATE_DOWNLOAD_URL + fileName);
			/* 对于该URL地址, 初始化并打开连接. */
			URLConnection ucon = myURL.openConnection();
			// ucon.setConnectTimeout(30*1000);
			/*
			 * 初始化一个InputStreams实例用以读取 从URLConnection返回的数据
			 */
			InputStream is = ucon.getInputStream();
			System.out.println("ucon.getContentLength()--->" + ucon.getContentLength());
			;
			FileOutputStream fileOutputStream = null;
			if (is != null) {
				File file = new File(Environment.getExternalStorageDirectory(), fileName);
				System.out.println("file.length()---->" + file.length());
				float length = (float) file.length();
				String lengthStr = String.valueOf(file.length());
				retArr[2] = lengthStr;
				fileOutputStream = new FileOutputStream(file);
				byte[] buf = new byte[1024];
				int ch = -1;
				while ((ch = is.read(buf)) != -1) {
					compeleteSize += ch;
					Message msg = handler.obtainMessage();
					msg.arg1 = 10001;
					msg.obj = ((float) compeleteSize) / length;
					handler.sendMessage(msg);
					fileOutputStream.write(buf, 0, ch);
				}
			}
			fileOutputStream.flush();

			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
			isDownSuccess = true;
		} catch (ClientProtocolException e) {
			isDownSuccess = false;
			log.log(Level.SEVERE, "更新获取服务器版本时异常", e);
			retArr[1] = "下载服务器版本失败，请确认您已开启上网功能，与服务器的连接是否正常！";
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			isDownSuccess = false;
			retArr[1] = "下载服务器版本失败，请确认您的sd卡是否安装正确！";
			e.printStackTrace();
		} catch (IOException e) {
			isDownSuccess = false;
			e.printStackTrace();
			log.log(Level.SEVERE, "更新获取服务器版本时异常", e);
			retArr[1] = "下载服务器版本失败，请确认您已开启上网功能，与服务器的连接是否正常！";
		}
		retArr[0] = String.valueOf(isDownSuccess);
		return retArr;
	}
}
