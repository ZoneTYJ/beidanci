package com.example.zahnubeidanci.activity;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.bean.DictionInfo;
import com.example.zahnubeidanci.engine.MusicEngine;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.JsonUtils;
import com.example.zahnubeidanci.utils.ToastUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class WordDictationActivity extends Activity implements OnClickListener  {
	private Activity mActivity=this;
	// TODO quest组件需要随机位置,动态添加到数组
	private Button btn_worddictation_back;
	private String mUid;
	private int wordCount;
	private RelativeLayout rl_worddictation_quest1;
	private RelativeLayout rl_worddictation_quest2;
	private RelativeLayout rl_worddictation_quest3;
	private RelativeLayout rl_worddictation_quest4;
	private TextView tv_worddictation_quest1;
	private TextView tv_worddictation_quest2;
	private TextView tv_worddictation_quest3;
	private TextView tv_worddictation_quest4;
	private TextView tv_worddictation_words;
	private TextView tv_worddictation_phonetic;
	private ImageButton ib_worddictation_horn;
	private String mp3Url;
	private DictionInfo dictionInfo;
	private int currentIndex;
	private TextView tv_worddictation_pb3;
	private TextView tv_worddictation_pb1;
	private TextView tv_worddictation_pb4;
	private LinearLayout ll_worddictation_sumweight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_worddictation);
		initView();
		initData();
	}

	private void initView() {
		btn_worddictation_back = (Button) findViewById(R.id.btn_worddictation_back);
		ll_worddictation_sumweight = (LinearLayout) findViewById(R.id.ll_worddictation_sumweight);
		tv_worddictation_pb1 = (TextView) findViewById(R.id.tv_worddictation_pb1);
		tv_worddictation_pb3 = (TextView) findViewById(R.id.tv_worddictation_pb3);
		tv_worddictation_pb4 = (TextView) findViewById(R.id.tv_worddictation_pb4);
		tv_worddictation_words = (TextView) findViewById(R.id.tv_worddictation_words);
		tv_worddictation_phonetic = (TextView) findViewById(R.id.tv_worddictation_phonetic);
		ib_worddictation_horn = (ImageButton) findViewById(R.id.ib_worddictation_horn);
		rl_worddictation_quest1 = (RelativeLayout) findViewById(R.id.rl_worddictation_quest1);
		tv_worddictation_quest1 = (TextView) findViewById(R.id.tv_worddictation_quest1);
		rl_worddictation_quest2 = (RelativeLayout) findViewById(R.id.rl_worddictation_quest2);
		tv_worddictation_quest2 = (TextView) findViewById(R.id.tv_worddictation_quest2);
		rl_worddictation_quest3 = (RelativeLayout) findViewById(R.id.rl_worddictation_quest3);
		tv_worddictation_quest3 = (TextView) findViewById(R.id.tv_worddictation_quest3);
		rl_worddictation_quest4 = (RelativeLayout) findViewById(R.id.rl_worddictation_quest4);
		tv_worddictation_quest4 = (TextView) findViewById(R.id.tv_worddictation_quest4);
		firstData();
	}

	private void firstData() {
		currentIndex = 0;
		mUid = getIntent().getStringExtra("uid");
		wordCount = getIntent().getIntExtra("wordCount", 10);
		ll_worddictation_sumweight.setWeightSum(wordCount);
		tv_worddictation_pb3.setText(wordCount+"");
		ib_worddictation_horn.setOnClickListener(this);
		btn_worddictation_back.setOnClickListener(this);
		rl_worddictation_quest1.setOnClickListener(this);
		rl_worddictation_quest2.setOnClickListener(this);
		rl_worddictation_quest3.setOnClickListener(this);
		rl_worddictation_quest4.setOnClickListener(this);
	}

	private void initData() {
		rl_worddictation_quest1.setEnabled(true);
		rl_worddictation_quest2.setEnabled(true);
		rl_worddictation_quest3.setEnabled(true);
		rl_worddictation_quest4.setEnabled(true);
		initNetData();
	}

	private void initNetData() {
		HttpUtils httpUtils = new HttpUtils(2000);
		httpUtils.send(HttpMethod.GET, Constants.DICTION_URL + "?index="
				+ currentIndex + "&id=" + mUid, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.showSafeToast(mActivity, "网络链接失败");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				processData(arg0.result);
			}
		});
	}

	/** 把获取的json转为Bean 数组 */
	protected void processData(String result) {
		Gson gson = new Gson();
		dictionInfo = gson.fromJson(result, DictionInfo.class);
		tv_worddictation_words.setText(dictionInfo.key);
		tv_worddictation_phonetic.setText(dictionInfo.symbols.get(0).ps);
		mp3Url = dictionInfo.symbols.get(0).pron;
		tv_worddictation_quest1.setText(dictionInfo.RightMean);
		tv_worddictation_quest2.setText(dictionInfo.wrongdic.get(0).mean);
		tv_worddictation_quest3.setText(dictionInfo.wrongdic.get(1).mean);
		tv_worddictation_quest4.setText(dictionInfo.wrongdic.get(2).mean);
	}

	@Override
	public void onClick(View v) {
		if (v == ib_worddictation_horn) {
			// TODO 播音
			MusicEngine musicEngine=new MusicEngine(mp3Url);
			musicEngine.playMp3();
		}
		if (v == btn_worddictation_back) {
			finish();
		}
		if (v == rl_worddictation_quest1) {
			rl_worddictation_quest1.setEnabled(false);
			doNextNetDic();
			updatePbView(tv_worddictation_pb3, -1);
			updatePbView(tv_worddictation_pb1, 1);
		} else if(v==rl_worddictation_quest2 || v==rl_worddictation_quest3 || v==rl_worddictation_quest4) {
			updatePbView(tv_worddictation_pb3, -1);
			updatePbView(tv_worddictation_pb4, 1);
			if (v == rl_worddictation_quest2) {
				rl_worddictation_quest2.setEnabled(false);
				doSerach(dictionInfo.wrongdic.get(0).key);
			}
			if (v == rl_worddictation_quest3) {
				rl_worddictation_quest3.setEnabled(false);
				doSerach(dictionInfo.wrongdic.get(1).key);
			}
			if (v == rl_worddictation_quest4) {
				rl_worddictation_quest4.setEnabled(false);
				doSerach(dictionInfo.wrongdic.get(2).key);
			}
		}

	}

	private void doSerach(String key) {
		HttpUtils httpUtils = new HttpUtils(1000);

		httpUtils.send(HttpMethod.GET, Constants.SEARCHWORD_URL + "?w=" + key
				+ "&key=" + Constants.SEARCH_KEY,
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						ToastUtils.showSafeToast(mActivity, "网络链接失败");
						doNextNetDic();
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						String data=arg0.result;
//						String data = JsonUtils.unescapeUnicode(arg0.result);
//						System.out.println("转换后:" + data);
						//TODO 修改解析方法
						Intent intent = new Intent(mActivity,
								WordActivity.class);
						intent.putExtra("data", data);
						intent.putExtra("type", 1);
						startActivity(intent);
						doNextNetDic();
					}
				});
	}

	private void doNextNetDic() {
		//网络返回该单词选择后的状态
		HttpUtils httpUtils=new HttpUtils(1000);
		httpUtils.send(HttpMethod.GET, Constants.UPDATE_DICTATION_URL, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				doNextDic();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				doNextDic();
			}
		});
	}

	private void doNextDic() {
		currentIndex++;
		if (currentIndex == wordCount) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setMessage("这轮单词检测结束了,再次检测错的单词吧");
			builder.setPositiveButton("回到主界面", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
//					startActivity(new Intent(mActivity, ExamActivity.class));
					finish();
				}
			});
			builder.show();

		}
		initData();
	}

	private void updatePbView(TextView tv_wordrecite_pb, int i) {
		LinearLayout.LayoutParams layoutParams = (LayoutParams) tv_wordrecite_pb
				.getLayoutParams();
		int weight = (int) layoutParams.weight;
		layoutParams.weight = weight + i;
		tv_wordrecite_pb.setText((weight + i) + "");
		tv_wordrecite_pb.setLayoutParams(layoutParams);
	}
}
