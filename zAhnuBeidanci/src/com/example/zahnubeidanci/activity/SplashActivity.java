package com.example.zahnubeidanci.activity;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.PackageInfoUtils;
import com.example.zahnubeidanci.utils.SharedPreferenceUtils;
import com.example.zahnubeidanci.utils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.Window;
import android.widget.TextView;

public class SplashActivity extends Activity {

	private static final String DOWN_TARGET = "/mnt/sdcard/beidanciAHNU.apk";
	private TextView mTvVersion;
	private int mVersionCode;
	private AlertDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		initView();
		initData();
	}

	private void initView() {
		mTvVersion = (TextView) findViewById(R.id.tv_version);
	}

	private void initData() {
		String versionName = PackageInfoUtils.getVersionName(this);
		mTvVersion.setText("版本号:"+versionName);
		mVersionCode = PackageInfoUtils.getVersionCode(this);
		
		boolean autoUpdate=SharedPreferenceUtils.getBoolean(this, Constants.VERSION_UPDATE, true);
		if(autoUpdate){
			getNetVersionName();
		}else {
			loadMainAct();
		}
	}
	/** 网络检查是否是最新版本 */
	private void getNetVersionName() {
		HttpUtils httpUtils=new HttpUtils(2000);
		httpUtils.send(HttpMethod.GET, Constants.UPDATE_URL, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				loadMainAct();
			}
			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				checkVersionData(arg0.result);
			}

		});
	}
	/** 解析json数据格式检查版本号 */
	private void checkVersionData(String result) {
		JSONObject jsonObject;
		try {
			jsonObject=new JSONObject(result);
			String downloadurl = jsonObject.getString("downloadurl");
			int version=jsonObject.getInt("version");
			String desc=jsonObject.getString("desc");
//			System.out.println("-----------\n下载版本"+downloadurl+" "+version+" "+desc);
			if(version!=mVersionCode){
				showDiaUpdate(downloadurl,desc);
			}else {
				loadMainAct();
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/** 弹出是否更新的对话框 */
	private void showDiaUpdate(final String downloadurl,String desc) {
		AlertDialog.Builder builder=new Builder(SplashActivity.this);
//		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("更新提示");
		builder.setMessage(desc);
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				loadMainAct();
			}
		});
		
		builder.setPositiveButton("升级", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				donwloadApk(downloadurl);
			}
		});
		builder.setNegativeButton("取消", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mDialog.dismiss();
				loadMainAct();
			}
		});
		mDialog = builder.show();//对话框展示
	}
	
	/**下载更新包并且提示安装
	 * @param downloadurl
	 */
	protected void donwloadApk(String downloadurl) {
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			HttpUtils httpUtils=new HttpUtils(2000);
			httpUtils.download(downloadurl, DOWN_TARGET, new RequestCallBack<File>() {
				
				@Override
				public void onSuccess(ResponseInfo<File> arg0) {
					ToastUtils.showSafeToast(SplashActivity.this, "下载失败");
					loadMainAct();
				}
				
				@Override
				public void onFailure(HttpException arg0, String arg1) {
					Intent intent=new Intent();
					intent.setAction("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setDataAndType(Uri.fromFile(new File(DOWN_TARGET)), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}
			});
		}else {
			ToastUtils.showSafeToast(SplashActivity.this, "请挂在sd卡");
		}
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		loadMainAct();
	}
	
	private void loadMainAct() {
//		Intent intent=new Intent(this,MainActivity.class);
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}

	
}
