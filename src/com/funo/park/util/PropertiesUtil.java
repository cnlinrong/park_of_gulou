package com.funo.park.util;

import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

public class PropertiesUtil {
	
	private static Properties props = null;

	/*
	 * 获取Properties文件的值
	 */
	public static String readPropsValue(Context context, int rawId, String key) {
		if (props == null) {
			props = new Properties();
		}
		try {
			InputStream in = context.getResources().openRawResource(rawId);
			props.load(in);
			String value = props.getProperty(key);
			System.out.println(key + value);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
