package com.funo.park.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CommonUtil {

	public static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

	public static String formatToDate(Date date) {
		if (date != null) {
			return simpleDateFormat2.format(date);
		} else {
			return null;
		}
	}

	public static String formatToTime(Date date) {
		if (date != null) {
			return simpleDateFormat1.format(date);
		} else {
			return null;
		}
	}

	public static String getDateString(Date date, String dateFormat) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
		if (date != null) {
			return simpleDateFormat.format(date);
		} else {
			return null;
		}
	}

	public static String getTimeStr(String inDate, String outDate) {
		try {
			java.util.Date now = simpleDateFormat1.parse(outDate);
			java.util.Date date = simpleDateFormat1.parse(inDate);
			long l = now.getTime() - date.getTime();
			long hour = (l / (60 * 60 * 1000));
			long min = ((l / (60 * 1000)) - hour * 60);
			long s = (l / 1000 - hour * 60 * 60 - min * 60);
			return hour + "小时" + min + "分" + s + "秒";
		} catch (ParseException e) {
			return "0小时0分0秒";
		}
	}

	public static boolean validatorStr(String validStr, String regex) {
		boolean flag = false;
		if (validStr == null)
			return false;
		return validStr.matches(regex);
	}

	/**
	 * 使用参数Format将字符串转为Date
	 * 
	 * @throws java.text.ParseException
	 */
	public static Date parse(String strDate, String pattern) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setLenient(false);
		// return format.parse(strDate);
		System.out.println("strDate--->" + strDate + "----");
		// if("".trim().equals(strDate)){
		// return format.parse("2012-01-29 12:23:07");
		// }else{
		// return format.parse(strDate);
		// }
		return format.parse("2012-01-29 12:23:07");
	}

	/*
	 * 把java对象转换为JSON对象
	 */
	public static JSONObject getJsonFromObject(Object obj, String projectPackage)
			throws SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, JSONException {
		JSONObject jsonObj = new JSONObject();
		List<Field> fieldList = new ArrayList<Field>();
		ReflectUtil.getFieldList(fieldList, obj.getClass(), projectPackage);
		if (fieldList != null && fieldList.size() > 0) {
			Iterator<Field> fieldIterator = fieldList.iterator();
			while (fieldIterator.hasNext()) {
				Field field = fieldIterator.next();
				Object fieldVal = ReflectUtil.getValueofField(field, obj);
				if (fieldVal != null) {
					if (ReflectUtil.isBasicTypeOfWrapper(fieldVal.getClass())) {
						if (fieldVal.getClass().equals(Date.class)) {
							jsonObj.put(field.getName(), formatToTime((Date) fieldVal));
						} else {
							jsonObj.put(field.getName(), fieldVal);
						}
					} else {
						if (List.class.isAssignableFrom(fieldVal.getClass())) {
							JSONArray arr = new JSONArray();
							List list = (List) fieldVal;
							Iterator it = list.iterator();
							int j = 0;
							while (it.hasNext()) {
								Object eleObj = it.next();
								JSONObject eleJsonObj = CommonUtil.getJsonFromObject(eleObj, projectPackage);
								arr.put(j++, eleJsonObj.toString());
							}
							jsonObj.put(field.getName(), arr);
						} else {
							jsonObj.put(field.getName(), getJsonFromObject(fieldVal, projectPackage));
						}
					}
				}
			}
		}
		return jsonObj;
	}

	/*
	 * 把JSON对象转换为java对象
	 */
	public static Object getObjectFromJson(JSONObject jsonObj, Object object, String projectPackage)
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, InstantiationException, JSONException, ParseException {
		Class clazz = object.getClass();
		List<Field> fieldList = new ArrayList<Field>();
		ReflectUtil.getFieldList(fieldList, clazz, projectPackage);
		if (fieldList != null && fieldList.size() > 0) {
			Iterator<Field> fieldIterator = fieldList.iterator();
			while (fieldIterator.hasNext()) {
				Field field = fieldIterator.next();
				String fieldName = field.getName();
				if (!jsonObj.isNull(fieldName)) {
					StringBuffer fsb = new StringBuffer();
					fsb.append("get").append(fieldName.substring(0, 1).toUpperCase())
							.append(fieldName.substring(1, fieldName.length()));
					Method method = clazz.getMethod(fsb.toString(), null);
					Class returnClass = method.getReturnType();
					if (ReflectUtil.isBasicTypeOfWrapper(returnClass)) {
						String returnClassName = returnClass.getSimpleName();
						if (returnClassName.equals("Integer")) {
							returnClassName = "Int";
						} else if (returnClassName.equals("Float")) {
							returnClassName = "Double";
						} else if (returnClassName.equals("Short")) {
							returnClassName = "Long";
						}
						Method jsonMethod = null;
						if (returnClassName.equals("Date")) {
							jsonMethod = jsonObj.getClass().getMethod("get", String.class);
						} else {
							jsonMethod = jsonObj.getClass().getMethod("get" + returnClassName, String.class);
						}
						Object fieldVal = jsonMethod.invoke(jsonObj, fieldName);
						if (fieldVal != null) {
							Object fieldValue = null;
							if (returnClass.getSimpleName().equals("Float")) {
								fieldValue = new Float(fieldVal.toString());
							} else if (returnClass.getSimpleName().equals("Short")) {
								fieldValue = new Short(fieldVal.toString());
							} else if (returnClassName.equals("Date")) {
								String dateStr = fieldVal.toString();
								Date date = parse(dateStr, "yyyy-MM-dd HH:mm:ss");

								fieldValue = date;
							} else {
								fieldValue = fieldVal;
							}
							ReflectUtil.setValueofField(field, object, fieldValue);
						}
					} else {
						if (ReflectUtil.isList(returnClass)) {
							Method jsonMethod = jsonObj.getClass().getMethod("getJSONArray", String.class);
							JSONArray fieldVal = (JSONArray) jsonMethod.invoke(jsonObj, fieldName);
							if (fieldVal != null) {
								List list = new ArrayList();
								for (int k = 0; k < fieldVal.length(); k++) {
									JSONObject eleJsonObj = new JSONObject(fieldVal.get(k).toString());
									Class eleClass = ReflectUtil.getListElementClass(field);
									Object eleObj = eleClass.newInstance();
									Object tmpObj = getObjectFromJson(eleJsonObj, eleObj, projectPackage);
									list.add(tmpObj);

								}
								// returnObj=list;
								ReflectUtil.setValueofField(field, object, list);
							}
						} else {
							Method jsonMethod = jsonObj.getClass().getMethod("getJSONObject", String.class);
							JSONObject fieldVal = (JSONObject) jsonMethod.invoke(jsonObj, fieldName);
							Object returnObj = returnClass.newInstance();
							ReflectUtil.setValueofField(field, object, returnObj);
							if (fieldVal != null) {
								getObjectFromJson(fieldVal, returnObj, projectPackage);
							}
						}
					}
				}
			}
		}
		return object;
	}

}
