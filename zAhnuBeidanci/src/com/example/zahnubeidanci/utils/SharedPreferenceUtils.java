package com.example.zahnubeidanci.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtils {
	public static final String SP_NAME="config";
	private static SharedPreferences sp;
	/**
	 * 获取sp
	 * @param context
	 * @return
	 */
	private static SharedPreferences getSP(Context context){
		if(sp==null){
			sp=context.getSharedPreferences(SP_NAME, 0);
		}
		return sp;
	}
	/**
	 * 保存字符串
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context,String key,String value){
		getSP(context).edit().putString(key, value).commit();
		return ;
	}
	/**
	 * 保存整型
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context,String key,int value){
		getSP(context).edit().putInt(key, value).commit();
		return ;
	}
	/**
	 * 保存布尔型
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveBoolean(Context context,String key,Boolean value){
		getSP(context).edit().putBoolean(key, value).commit();
		return ;
	}
	
	public static int getInt(Context context,String key,int defValue){
		return getSP(context).getInt(key, defValue);
	}
	
	public static Boolean getBoolean(Context context,String key,Boolean defValue){
		return getSP(context).getBoolean(key, defValue);
	}
	
	public static String getString(Context context,String key,String defValue){
		return getSP(context).getString(key, defValue);
	}
	
	
}
