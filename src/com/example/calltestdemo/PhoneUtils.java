package com.example.calltestdemo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.telephony.TelephonyManager;

public class PhoneUtils {
	/**
	 * ��TelephonyManager��ʵ����ITelephony��������
	 */
	static public ITelephony getITelephony(TelephonyManager telMgr) throws Exception {
		Method getITelephonyMethod = telMgr.getClass().getDeclaredMethod("getITelephony");
		getITelephonyMethod.setAccessible(true);// ˽�л�����Ҳ��ʹ��
		return (ITelephony) getITelephonyMethod.invoke(telMgr);
	}

	static public void printAllInform(Class clsShow) {
		try {
			// ȡ�����з���
			Method[] hideMethod = clsShow.getDeclaredMethods();
			int i = 0;
			for (; i < hideMethod.length; i++) {

			}
			// ȡ�����г���
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