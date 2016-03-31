package com.example.zahnubeidanci.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class RegistActivity extends Activity implements OnClickListener {
	private Activity mActivity=this;
	private Button mBtnBack;
	private EditText mEtUserPass;
	private EditText mEtUsername;
	private EditText mEtUserRePass;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_regist);
		mEtUsername = (EditText) findViewById(R.id.et_username);
		mEtUserPass = (EditText) findViewById(R.id.et_userpass);
		mEtUserRePass = (EditText) findViewById(R.id.et_userrepass);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);
	}
	
	public void onRrgistClick(View view){
		String userName = mEtUsername.getText().toString().trim();
		String userPass = mEtUserPass.getText().toString().trim();
		String userRePass = mEtUserRePass.getText().toString().trim();
		if(userPass.equals(userRePass)==false){
			ToastUtils.showSafeToast(mActivity, "两次密码不一致");
			return;
		}
		if(TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPass)){
			ToastUtils.showSafeToast(mActivity, "用户名和密码不能为空");
			return;
		}
		
		HttpUtils httpUtils=new HttpUtils(2000);
		httpUtils.send(HttpMethod.GET, Constants.USERREGISTER_URL+"?name="+userName+"&pass="+userPass, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.showSafeToast(mActivity, "网络请求失败，请检查网络");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (arg0.result.contains("200")) {
					ToastUtils.showSafeToast(mActivity, "注册成功");
					finish();
				} else {
					ToastUtils.showSafeToast(mActivity, "网络请求异常");
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			finish();
		}
	}
}
