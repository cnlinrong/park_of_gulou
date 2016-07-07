package com.funo.park.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReflectUtil {

	/*
	 * 获取成员变量的值
	 */
	public static Object getValueofField(Field field, Object bean) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Object fieldVal = null;
		String fieldName = field.getName();
		StringBuffer fsb = new StringBuffer();
		fsb.append("get").append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1, fieldName.length()));
		Method method = bean.getClass().getMethod(fsb.toString(), null);
		fieldVal = method.invoke(bean, null);
		return fieldVal;
	}

	/*
	 * 给成员变量赋值
	 */
	public static void setValueofField(Field field, Object bean, Object fieldVal) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		String fieldName = field.getName();
		StringBuffer fsb = new StringBuffer();
		fsb.append("set").append(fieldName.substring(0, 1).toUpperCase())
				.append(fieldName.substring(1, fieldName.length()));
		Method method = bean.getClass().getMethod(fsb.toString(), field.getType());
		method.invoke(bean, fieldVal);

	}

	/*
	 * 判断一个类是不是基本数据的封装类或字符串或时间类型
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isBasicTypeOfWrapper(Class fieldClass) {
		boolean flag = false;
		if (fieldClass.equals(String.class) || fieldClass.equals(Date.class) || fieldClass.equals(Boolean.class)
				|| fieldClass.equals(Byte.class) || fieldClass.equals(Character.class)
				|| fieldClass.equals(Double.class) || fieldClass.equals(Float.class) || fieldClass.equals(Integer.class)
				|| fieldClass.equals(Long.class) || fieldClass.equals(Short.class) || fieldClass.isPrimitive()) {
			flag = true;
		}

		return flag;

	}

	/*
	 * 判断一个类是不是基本数据的封装类或字符串或时间类型
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isList(Class fieldClass) {
		boolean flag = false;
		if (fieldClass.equals(List.class) || fieldClass.equals(ArrayList.class)) {
			flag = true;
		}

		return flag;

	}

	/*
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static Class getListElementClass(Field field) {
		Type gtype = field.getGenericType();
		if (gtype instanceof ParameterizedType) {

			// list the raw type information
			ParameterizedType ptype = (ParameterizedType) gtype;
			Type rtype = ptype.getRawType();
			System.out.println("rawType is instance of " + rtype.getClass().getName());
			System.out.println(" (" + rtype + ")");

			// list the actual type arguments
			Type[] targs = ptype.getActualTypeArguments();
			System.out.println("actual type arguments are:");
			for (int j = 0; j < targs.length; j++) {
				System.out.println(" instance of " + targs[j].getClass().getName() + ":");
				System.out.println("  (" + targs[j] + ")");
			}
			return (Class) targs[0];

		} else {
			System.out.println("getGenericType is not a ParameterizedType!");
			return null;
		}

	}

	/*
	 * 获取该类的所有属性（包括自定义的父类的所有属性）
	 */
	@SuppressWarnings("rawtypes")
	public static List<Field> getFieldList(List<Field> fieldList, Class clazz, String projectPackage) {
		if (fieldList == null) {
			fieldList = new ArrayList<Field>();
		}

		if (clazz.getPackage().getName().startsWith(projectPackage)) {
			Field[] fieldArr = clazz.getDeclaredFields();
			for (int i = 0; i < fieldArr.length; i++) {
				fieldList.add(fieldArr[i]);
			}
			getFieldList(fieldList, clazz.getSuperclass(), projectPackage);
		}

		return fieldList;

	}

}
