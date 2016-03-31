package com.example.zahnubeidanci.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.bean.UserInfo;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.SharedPreferenceUtils;
import com.example.zahnubeidanci.utils.ToastUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class MainActivity extends  MainBaseActivity{
	private TextView mTvUserName;
	private ImageView mIvIcUser;
	private Button mBtnLogin;
	private RelativeLayout mRlSummary;
	private TextView mTvUserUid;
	private TextView mTvlearnCount;
	private TextView mTvSignCount;
	private BitmapUtils bitmapUtils;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	private void initView() {
		 bitmapUtils = new BitmapUtils(this);
		mIvHome=(ImageView) findViewById(R.id.iv_btn_home);
		mIvMenu=(ImageView) findViewById(R.id.iv_btn_menu);
		mIvNotify=(ImageView) findViewById(R.id.iv_btn_notify);
		mIvSearch=(ImageView) findViewById(R.id.iv_btn_search);
		mBtnPersonal=(Button) findViewById(R.id.btn_personal);
		mBtnExam=(Button) findViewById(R.id.btn_exam);
		mBtnRecite=(Button) findViewById(R.id.btn_recite);
		super.setClick();//设置click事件
		mIvIcUser = (ImageView) findViewById(R.id.iv_ic_user);
		mTvUserName = (TextView) findViewById(R.id.tv_user_name);
		mTvUserUid = (TextView) findViewById(R.id.tv_user_id);
		mBtnLogin = (Button) findViewById(R.id.btn_login);
		mBtnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(MainActivity.this,LoginActivity.class);
				startActivityForResult(intent, 11);
			}
		});
		
		mRlSummary = (RelativeLayout) findViewById(R.id.rl_summary);
		mTvSignCount = (TextView) findViewById(R.id.tv_sign_count);
		mTvlearnCount = (TextView) findViewById(R.id.tv_learn_count);
		
		mBtnPersonal.setEnabled(false);
	}

	private void initData() {
		Boolean UserLogin = SharedPreferenceUtils.getBoolean(this, "UserLogin", false);
		if(UserLogin==false){
			mBtnLogin.setVisibility(View.VISIBLE);
			mRlSummary.setVisibility(View.INVISIBLE);
			mTvUserName.setText("您还没有登录\n快点击登录吧");
			mTvUserUid.setText("");
			mIvIcUser.setImageResource(R.drawable.ic_contact);
		}else {
			mBtnLogin.setVisibility(View.INVISIBLE);
			mRlSummary.setVisibility(View.VISIBLE);
			String userName = SharedPreferenceUtils.getString(this,  Constants.UserName, "");
			String uid = SharedPreferenceUtils.getString(this, Constants.UserUid, "");
			mTvUserName.setText(userName);
			mTvUserUid.setText(uid);
			HttpUtils httpUtils=new HttpUtils(2000);
			httpUtils.send(HttpMethod.GET, Constants.USERINFO_URL+"?userName="+userName+"&uid"+uid, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ToastUtils.showSafeToast(MainActivity.this,"请链接网络,重新加载页面");
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					Gson gson=new Gson();
					UserInfo userInfo=new UserInfo();
					 userInfo = gson.fromJson(arg0.result, UserInfo.class);
					 userInfo.writeSP(MainActivity.this);
					 bitmapUtils.display(mIvIcUser, userInfo.icon);
					 setViewCount();
				}
			});
			
		}
		
	}
	/** 设置 打卡天数和单词数量 */
	protected void setViewCount() {
		int learnCount=SharedPreferenceUtils.getInt(this, Constants.UserLearnCount, 0);
		int punchCount=SharedPreferenceUtils.getInt(this, Constants.UserPunchCount, 0);
		mTvlearnCount.setText(""+learnCount);
		mTvSignCount.setText(""+punchCount);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		initData();
		super.onActivityResult(requestCode, resultCode, data);
	}

}
