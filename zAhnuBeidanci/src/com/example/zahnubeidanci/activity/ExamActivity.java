package com.example.zahnubeidanci.activity;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.bean.ExamUserInfo;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class ExamActivity  extends MainBaseActivity{
	private Activity mActivity=this;
	private TextView tv_exam_todaydic1;
	private TextView tv_exam_alreadydic1;
	private Button btn_exam_star1;
	private TextView tv_exam_todaydic2;
	private TextView tv_exam_alreadydic2;
	private Button btn_exam_star2;
	private ReciteUserInfo dictation;
	private ReciteUserInfo spell;
	private String uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exam);
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
		mBtnExam.setEnabled(false);
		
		tv_exam_todaydic1 = (TextView) findViewById(R.id.tv_exam_todaydic1);
		tv_exam_alreadydic1 = (TextView) findViewById(R.id.tv_exam_alreadydic1);
		btn_exam_star1 = (Button) findViewById(R.id.btn_exam_star1);
		tv_exam_todaydic2 = (TextView) findViewById(R.id.tv_exam_todaydic2);
		tv_exam_alreadydic2 = (TextView) findViewById(R.id.tv_exam_alreadydic2);
		btn_exam_star2 = (Button) findViewById(R.id.btn_exam_star2);
		btn_exam_star1.setOnClickListener(this);
		btn_exam_star2.setOnClickListener(this);
	}
	
	
	private void initData() {
		netInitData();
	}
	
	private void netInitData() {
		uid = SharedPreferenceUtils.getString(this, Constants.UserUid, "");
		HttpUtils httpUtils=new HttpUtils(1000);
		httpUtils.send(HttpMethod.GET, Constants.EXAM_USERINFO_URL+"?uid="+uid, new RequestCallBack<String>() {

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
		ExamUserInfo examUserInfo = gson.fromJson(result, ExamUserInfo.class);
		dictation = examUserInfo.dictation;
		tv_exam_todaydic1.setText(dictation.todaydic+"");
		tv_exam_alreadydic1.setText(dictation.alreadydic+"");
		if(dictation.todaydic==dictation.alreadydic){
			btn_exam_star1.setText("完成");
		}else {
			btn_exam_star1.setText("开始");
		}
		spell = examUserInfo.spell;
		tv_exam_todaydic2.setText(spell.todaydic+"");
		tv_exam_alreadydic2.setText(spell.alreadydic+"");
		if(spell.todaydic==spell.alreadydic){
			btn_exam_star2.setText("完成");
		}else {
			btn_exam_star2.setText("开始");
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v==btn_exam_star1){
			Button btn=(Button) v;
			if("开始".equals(btn.getText().toString().trim())){
				Intent intent;
				intent=new Intent(mActivity,WordDictationActivity.class);
				intent.putExtra("uid", uid);
				intent.putExtra("wordCount", dictation.todaydic-dictation.alreadydic);
				startActivityForResult(intent,11);
			}
		}
		if(v==btn_exam_star2){
			Button btn=(Button) v;
			if("开始".equals(btn.getText().toString().trim())){
				Intent intent;
				//TODO修改
				intent=new Intent(mActivity,WordDictationActivity.class);
				intent.putExtra("uid", uid);
				intent.putExtra("wordCount", spell.todaydic-spell.alreadydic);
				startActivityForResult(intent,11);
			}
		}
		super.onClick(v);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		initData();
		super.onActivityResult(requestCode, resultCode, data);
	}
}
