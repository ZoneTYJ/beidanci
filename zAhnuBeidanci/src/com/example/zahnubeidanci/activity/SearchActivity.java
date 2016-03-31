package com.example.zahnubeidanci.activity;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.JsonUtils;
import com.example.zahnubeidanci.utils.ToastUtils;
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
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class SearchActivity extends Activity {
	private Button btn_back;
	private SearchView sv_search;
	private Activity mActivity=this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		btn_back = (Button) findViewById(R.id.btn_back);
		sv_search = (SearchView) findViewById(R.id.sv_search);
		sv_search.setIconifiedByDefault(false);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		sv_search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				System.out.println(query);
				HttpUtils httpUtils=new HttpUtils(2000);
				httpUtils.send(HttpMethod.GET, Constants.SEARCHWORD_URL+"?w="+query+"&key="+Constants.SEARCH_KEY, new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtils.showSafeToast(mActivity, "网络链接失败");
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						System.out.println(arg0.result);
						String data=arg0.result;
//						String data = JsonUtils.unescapeUnicode(arg0.result);  JSON方法
//						System.out.println("转换后:"+data);
//						if(arg0.result) TODO 如果返回单词不存在则吐司不跳转,修改解析方法
						Intent intent=new Intent(mActivity,WordActivity.class);
						intent.putExtra("data", data);
						startActivity(intent);
						finish();
					}
					
				});
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
	}
}
