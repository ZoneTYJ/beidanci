package com.example.zahnubeidanci.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageInfoUtils {
	private static final String PACKAGENAME = "com.example.zahnubeidanci";

	/**
	 * 获取本地的版本号 
	 * @param context
	 * @return int类型数值
	 */
	public static int getVersionCode(Context context){
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(PACKAGENAME, 0);
			int versionCode=packageInfo.versionCode;
			return versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 返回当前的版本号
	 * @param context
	 * @return String类型
	 */
	public static String getVersionName(Context context){
		PackageManager packageManager = context.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(PACKAGENAME, 0);
			String versionName = packageInfo.versionName;
			return versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
}
