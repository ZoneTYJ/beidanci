package com.example.zahnubeidanci.activity;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.bean.DictionInfo;
import com.example.zahnubeidanci.bean.ReciteUserInfo;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.SharedPreferenceUtils;
import com.example.zahnubeidanci.utils.ToastUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReciteActivity extends MainBaseActivity {
	private Activity mActivity=this;
	private ReciteUserInfo reciteUserInfo;
	private TextView tv_recite_todaydic;
	private TextView tv_recite_alreadydic;
	private TextView tv_recite_newdic;
	private Button bt_recite_start;
	private Button bt_recite_todaydic;
	private Button bt_recite_newdic;
	private Button bt_recite_grasp;
	private Button bt_recite_fresh;
	private String uid;
	//private static DictionInfo dictionInfo=new DictionInfo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recite);
		initView();
		initData();
	}

	private void initView() {
		mIvHome=(ImageView) findViewById(R.id.iv_btn_home);
		mIvMenu=(ImageView) findViewById(R.id.iv_btn_menu);
		mIvNotify=(ImageView) findViewById(R.id.iv_btn_notify);
		mIvSearch=(ImageView) findViewById(R.id.iv_btn_search);
		mBtnPersonal=(Button) findViewById(R.id.btn_personal);
		mBtnExam=(Button) findViewById(R.id.btn_exam);
		mBtnRecite=(Button) findViewById(R.id.btn_recite);
		super.setClick();//设置click事件
		mBtnRecite.setEnabled(false);
		
		bt_recite_start = (Button) findViewById(R.id.bt_recite_start);
		bt_recite_start.setOnClickListener(this);
		tv_recite_todaydic = (TextView) findViewById(R.id.tv_recite_todaydic);
		tv_recite_alreadydic = (TextView) findViewById(R.id.tv_recite_alreadydic);
		tv_recite_newdic = (TextView) findViewById(R.id.tv_recite_newdic);
		bt_recite_todaydic = (Button) findViewById(R.id.bt_recite_todaydic);
		bt_recite_newdic = (Button) findViewById(R.id.bt_recite_newdic);
		bt_recite_grasp = (Button) findViewById(R.id.bt_recite_grasp);
		bt_recite_fresh = (Button) findViewById(R.id.bt_recite_fresh);
		bt_recite_todaydic.setOnClickListener(this);
		bt_recite_newdic.setOnClickListener(this);
		bt_recite_grasp.setOnClickListener(this);
		bt_recite_fresh.setOnClickListener(this);
		
	}
	
	private void initData() {
		initNetdata();
	}

	private void initNetdata() {
		uid = SharedPreferenceUtils.getString(this, Constants.UserUid, "");
		HttpUtils httpUtils=new HttpUtils(1000);
		httpUtils.send(HttpMethod.GET, Constants.RECITE_USERINFO_URL+"?uid="+uid, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				processData(responseInfo.result);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ToastUtils.showSafeToast(mActivity, "网络请求失败");
			}
		});
	}

	protected void processData(String result) {
		Gson gson=new Gson();
		reciteUserInfo = gson.fromJson(result, ReciteUserInfo.class);
		tv_recite_todaydic.setText(reciteUserInfo.todaydic+"");
		tv_recite_alreadydic.setText(reciteUserInfo.alreadydic+"");
		tv_recite_newdic.setText(reciteUserInfo.newdic+"");
		if(reciteUserInfo.todaydic==reciteUserInfo.alreadydic){
			bt_recite_start.setText("打卡");
		}else {
			bt_recite_start.setText("开始");
		}
	}

	@Override
	public void onClick(View v) {
		if(v==bt_recite_start){
			Button btn=(Button) v;
			if("打卡".equals(btn.getText().toString().trim())){
				btn.setText("成功");
				//TODO 网络请求打卡成功
				ToastUtils.showSafeToast(mActivity, "今天打卡成功");
			}else if ("开始".equals(btn.getText().toString().trim())) {
				Intent intent;
				intent=new Intent(mActivity,WordReciteActivity.class);
				intent.putExtra("uid", uid);
				intent.putExtra("wordCount", reciteUserInfo.todaydic-reciteUserInfo.alreadydic);
				startActivityForResult(intent,11);
			}
		}
		if(v==bt_recite_todaydic){
			startHistory(0);
		}
		if(v==bt_recite_newdic){
			startHistory(1);
		}
		if(v==bt_recite_grasp){
			startHistory(2);
		}
		if(v==bt_recite_fresh){
			startHistory(3);
		}
		super.onClick(v);
	}

	private void startHistory(int index) {
		Intent intent;
		intent=new Intent(mActivity,HistoryActivity.class);
		intent.putExtra("index", index);
		startActivityForResult(intent,11);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		initData();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
