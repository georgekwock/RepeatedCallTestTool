package com.example.calltestdemo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.telephony.TelephonyManager;

public class PhoneUtils {
	/**
	 * 从TelephonyManager中实例化ITelephony，并返回
	 */
	static public ITelephony getITelephony(TelephonyManager telMgr) throws Exception {
		Method getITelephonyMethod = telMgr.getClass().getDeclaredMethod("getITelephony");
		getITelephonyMethod.setAccessible(true);// 私有化函数也能使用
		return (ITelephony) getITelephonyMethod.invoke(telMgr);
	}

	static public void printAllInform(Class clsShow) {
		try {
			// 取得所有方法
			Method[] hideMethod = clsShow.getDeclaredMethods();
			int i = 0;
			for (; i < hideMethod.length; i++) {

			}
			// 取得所有常量
			Field[] allFields = clsShow.getFields();
			for (i = 0; i < allFields.length; i++) {

			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}