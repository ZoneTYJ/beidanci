package com.example.zahnubeidanci.activity;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.SharedPreferenceUtils;
import com.example.zahnubeidanci.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
	private Button mBtnBack;
	private CheckBox mCb;
	private EditText mEtUserPass;
	private EditText mEtUsername;
	private Activity mActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView() {
		setContentView(R.layout.activity_login);
		mEtUsername = (EditText) findViewById(R.id.et_username);
		mEtUserPass = (EditText) findViewById(R.id.et_userpass);
		mCb = (CheckBox) findViewById(R.id.cb_remeber);
		mBtnBack = (Button) findViewById(R.id.btn_back);
		mBtnBack.setOnClickListener(this);

		String name = SharedPreferenceUtils.getString(mActivity,
				Constants.UserName, "");
		if (!TextUtils.isEmpty(name)) {
			mEtUsername.setText(name);
			String pass = SharedPreferenceUtils.getString(mActivity,
					Constants.UserPass, "");
			if (!TextUtils.isEmpty(pass)) {
				mEtUserPass.setText(pass);

				mCb.setChecked(true);
			}
		}
	}

	public void onLoginClick(View view) {
		String userName = mEtUsername.getText().toString().trim();
		String userPass = mEtUserPass.getText().toString().trim();
		if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(userPass)) {
			ToastUtils.showSafeToast(this, "用户名和密码不能为空");
		} else {
			SharedPreferenceUtils
					.saveString(this, Constants.UserName, userName);
			if (mCb.isChecked() == true) {
				SharedPreferenceUtils.saveString(this, Constants.UserPass,
						userPass);
			} else {
				SharedPreferenceUtils.saveString(this, Constants.UserPass, "");
			}
		}
		NetSendLogin(userName, userPass);
	}

	/** 网络发送登录数据 */
	private void NetSendLogin(String userName, String userPass) {
		HttpUtils httpUtils = new HttpUtils(2000);
		httpUtils.send(HttpMethod.GET, Constants.USERLOGIN_URL + "?user="
				+ userName + "&pass=" + userPass,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtils.showSafeToast(mActivity, "网络请求失败，请检查网络");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if (arg0.result.contains("200")) {
							SharedPreferenceUtils.saveBoolean(mActivity, "UserLogin", true);
							loadMainAct();
						} else {
							ToastUtils.showSafeToast(mActivity, "网络请求异常");
							SharedPreferenceUtils.saveString(mActivity,
									Constants.UserName, "");
							SharedPreferenceUtils.saveString(mActivity,
									Constants.UserPass, "");
						}
					}
				});
	}

	protected void loadMainAct() {
		finish();
	}

	@Override
	public void onClick(View v) {
		if (v == mBtnBack) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		if (v == mCb) {
			if (mCb.isChecked()) {
				mCb.setChecked(false);
			} else {
				mCb.setChecked(true);
			}
		}

	}

	public void anonymousLogin(View view) {
		SharedPreferenceUtils.saveBoolean(mActivity, "UserLogin", true);
		SharedPreferenceUtils.saveString(this, Constants.UserName, "Xroot");
		finish();
	}

	public void registClick(View v) {
		startActivity(new Intent(this, RegistActivity.class));
	}

}
