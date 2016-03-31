package com.example.zahnubeidanci.bean;

import android.content.Context;

import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.SharedPreferenceUtils;

public class UserInfo {
	public String name;
	public String uid;
	public String icon;
	public int learn_count;
	public int punch_count;
	
	public void writeSP(Context context){
		SharedPreferenceUtils.saveString(context, Constants.UserName, name);
		SharedPreferenceUtils.saveString(context, Constants.UserUid, uid);
		SharedPreferenceUtils.saveInt(context, Constants.UserPunchCount, punch_count);
		SharedPreferenceUtils.saveInt(context, Constants.UserLearnCount, learn_count);
	}
}
