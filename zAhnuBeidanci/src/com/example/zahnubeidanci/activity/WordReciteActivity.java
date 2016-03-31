package com.example.zahnubeidanci.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.bean.DictXmlInfo;
import com.example.zahnubeidanci.bean.DictXmlInfo.Means;
import com.example.zahnubeidanci.bean.DictXmlInfo.Sent;
import com.example.zahnubeidanci.bean.DictXmlInfo.Symbols;
import com.example.zahnubeidanci.engine.MusicEngine;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.ToastUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class WordReciteActivity extends Activity implements OnClickListener {
	private Activity maActivity = this;
	private int arryCount = 0;
	private List<DictXmlInfo> dicts;
	private String pronMp3Url;
	private TextView tv_wordrecite_words;
	private TextView tv_wordrecite_phonetic;
	private TextView tv_wordrecite_example1;
	private TextView tv_wordRecite_explanation;
	private Button btn_wordrecite_detail;
	private Button btn_wordrecite_negative;
	private Button btn_wordrecite_positive;
	private int mIndex;
	private int mDicState;
	private Button btn_wordrecite_back;
	private String mUid;
	private int wordCount;
	private ImageButton ib_wordrecite_horn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wordrecite);
		initView();
		initData();
	}

	private void initView() {
		btn_wordrecite_back = (Button) findViewById(R.id.btn_wordrecite_back);
		btn_wordrecite_back.setOnClickListener(this);
		ib_wordrecite_horn = (ImageButton) findViewById(R.id.ib_wordrecite_horn);
		ib_wordrecite_horn.setOnClickListener(this);
		tv_wordrecite_words = (TextView) findViewById(R.id.tv_wordrecite_words);
		tv_wordrecite_phonetic = (TextView) findViewById(R.id.tv_wordrecite_phonetic);
		tv_wordrecite_example1 = (TextView) findViewById(R.id.tv_wordrecite_example1);
		tv_wordRecite_explanation = (TextView) findViewById(R.id.tv_wordRecite_explanation);
		btn_wordrecite_detail = (Button) findViewById(R.id.btn_wordrecite_detail);
		btn_wordrecite_detail.setOnClickListener(this);
		btn_wordrecite_negative = (Button) findViewById(R.id.btn_wordrecite_negative);
		btn_wordrecite_negative.setOnClickListener(this);
		btn_wordrecite_positive = (Button) findViewById(R.id.btn_wordrecite_positive);
		btn_wordrecite_positive.setOnClickListener(this);

		ll_wordrecite_sumweight = (LinearLayout) findViewById(R.id.ll_wordrecite_sumweight);
		tv_wordrecite_pb1 = (TextView) findViewById(R.id.tv_wordrecite_pb1);
		tv_wordrecite_pb2 = (TextView) findViewById(R.id.tv_wordrecite_pb2);
		tv_wordrecite_pb3 = (TextView) findViewById(R.id.tv_wordrecite_pb3);
		tv_wordrecite_pb4 = (TextView) findViewById(R.id.tv_wordrecite_pb4);
		// 用户的id和需要学习的单词数
		mUid = getIntent().getStringExtra("uid");
		wordCount = getIntent().getIntExtra("wordCount", 10);
		// TODO 三种状态单词数
		mIndex = 0;
		// 给进度条设置权重
		ll_wordrecite_sumweight.setWeightSum(wordCount);
		tv_wordrecite_pb3.setText(wordCount+"");
		
	}

	private LinearLayout ll_wordrecite_sumweight;
	private TextView tv_wordrecite_pb1;
	private TextView tv_wordrecite_pb2;
	private TextView tv_wordrecite_pb3;
	private TextView tv_wordrecite_pb4;

	private void initData() {
		if (mIndex == 0) {
			HttpUtils httpUtils = new HttpUtils(2000);
			httpUtils.send(HttpMethod.GET, Constants.RECITEARRYS_URL + "?id="
					+ mUid, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					ToastUtils.showSafeToast(maActivity, "网络请求失败");
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {
					processData(arg0.result);
				}
			});
		} else {
			doFirst(mIndex);
		}
	}

	/** 把获取的json转为Bean 数组 */
	protected void processData(String result) {
		Gson gson = new Gson();
		Dicts datas = gson.fromJson(result, Dicts.class);
		dicts = datas.dicts;
		mIndex = 0;
		doFirst(mIndex);
	}

	private void doFirst(int i) {
		mDicState = 0;
		DictXmlInfo dictXmlInfo = dicts.get(i);
		tv_wordrecite_words.setText(dictXmlInfo.key);
		List<Symbols> symbols = dictXmlInfo.symbols;
		tv_wordrecite_phonetic.setText(symbols.get(0).ps);
		pronMp3Url = symbols.get(0).pron;
		btn_wordrecite_positive.setText("认识");
		btn_wordrecite_negative.setText("不认识");
		setBtnVIsibity(View.GONE, View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE);
	}

	private void doSecond(int i) {
		mDicState = 1;
		DictXmlInfo dictXmlInfo = dicts.get(i);
		Sent sent = dictXmlInfo.sent.get(0);
		tv_wordrecite_example1.setText(sent.orig);
		btn_wordrecite_positive.setText("想起来了");
		btn_wordrecite_negative.setText("没想起来");
		setBtnVIsibity(View.VISIBLE, View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE);
	}

	private void doThird(int i) {
		mDicState = 2;
		DictXmlInfo dictXmlInfo = dicts.get(i);
		List<Means> means = dictXmlInfo.means;
		StringBuffer sb = new StringBuffer();
		for (Means mean : means) {
//			sb.append(mean.pos + mean.acceptation + "\n");
			sb.append(mean.pos + mean.acceptation );
		}
		tv_wordRecite_explanation.setText(sb.toString());
		setBtnVIsibity(View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE,
				View.GONE);
	}

	private void setBtnVIsibity(int gone, int gone2, int gone3, int visible,
			int visible2) {
		tv_wordrecite_example1.setVisibility(gone);
		btn_wordrecite_detail.setVisibility(gone2);
		tv_wordRecite_explanation.setVisibility(gone3);
		btn_wordrecite_negative.setVisibility(visible);
		btn_wordrecite_positive.setVisibility(visible2);
	}

	public class Dicts {
		public List<DictXmlInfo> dicts = new ArrayList<DictXmlInfo>();
	}

	@Override
	public void onClick(View v) {
		if (v == btn_wordrecite_back) {
			finish();
		}
		if (v == btn_wordrecite_positive) {
			DictXmlInfo dictXmlInfo = dicts.get(mIndex);
			int st = 0;
			if (mDicState == 0) {
				if (dictXmlInfo.state == 0) {
					st = 3;
				} else {
					st = dictXmlInfo.state + 1;
				}
				if (st == 3) {
					arryCount++;// 这个单词今天已经学会
				}
			} else {
				if (dictXmlInfo.state == 0) {
					st = 1;
				} else {
					st = dictXmlInfo.state;
				}
			}
			sendNext(st);
		}
		if (v == btn_wordrecite_negative) {
			if (mDicState == 0) {
				doSecond(mIndex);
				return;
			}
			if (mDicState == 1) {
				doThird(mIndex);
				return;
			}
		}
		if (v == btn_wordrecite_detail) {
			sendNext(1);
		}
		if (v == ib_wordrecite_horn) {
			// TODO 播音
			MusicEngine musicEngine=new MusicEngine(pronMp3Url);
			musicEngine.playMp3();
		}
	}

	/**
	 * 跳转到详细信息，并且发送给服务器 执行下一个单词
	 */
	private void sendNext(int st) {
		Intent intent = new Intent(maActivity, WordActivity.class);
		Gson gson = new Gson();
		String json = gson.toJson(dicts.get(mIndex));
		intent.putExtra("data", json);
		intent.putExtra("type", 1);
		startActivityForResult(intent, 11);
		updatePb(st); // 跟新进度条

		//TODO 发送给服务器 更新单词数据
//		HttpUtils httpUtils = new HttpUtils(1000);
//		httpUtils.send(HttpMethod.GET, Constants.UPDATEDIC_URL + "?id=" + mUid
//				+ "&wordname=" + dicts.get(mIndex).key + "&st=" + st, null);
	
	}

	private void creatAlertDiag() {
		AlertDialog.Builder builder=new AlertDialog.Builder(maActivity);
		builder.setMessage("今天的单词已经学完了，赶快打卡吧");
		builder.setNegativeButton("打卡",new android.content.DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				//打卡
				HttpUtils httpUtils = new HttpUtils(1000);
				httpUtils.send(HttpMethod.GET, Constants.SUCESSDIC_URL + "?id=" + mUid, new RequestCallBack<String>() {
					@Override
					public void onFailure(HttpException arg0, String arg1) {
						Toast.makeText(getApplicationContext(), "网络链接失败，请重新打卡", Toast.LENGTH_SHORT)
						.show();
					}
					
					@Override
					public void onSuccess(ResponseInfo<String> arg0) {
						if(arg0.result.contains("200")){
							Toast.makeText(getApplicationContext(), "打卡成功", Toast.LENGTH_SHORT)
							.show();
						}else {
							Toast.makeText(getApplicationContext(), "打卡失败", Toast.LENGTH_SHORT)
							.show();
						}
					}
				});
				finish();
			}
		});
		builder.show();
	}

	private void updatePb(int st) {
		int pre_state = dicts.get(mIndex).state;
		switch (pre_state) {
		case 0:
			updatePbView(tv_wordrecite_pb3, -1);
			break;
		case 1:
			updatePbView(tv_wordrecite_pb4, -1);
			break;
		case 2:
			updatePbView(tv_wordrecite_pb2, -1);
			break;
		default:
			break;
		}
		switch (st) {
		case 1:
			updatePbView(tv_wordrecite_pb4, 1);
			break;
		case 2:
			updatePbView(tv_wordrecite_pb2, 1);
			break;
		case 3:
			updatePbView(tv_wordrecite_pb1, 1);
			break;
		default:
			break;
		}
	}

	private void updatePbView(TextView tv_wordrecite_pb, int i) {
		LinearLayout.LayoutParams layoutParams = (LayoutParams) tv_wordrecite_pb
				.getLayoutParams();
		int weight = (int) layoutParams.weight;
		layoutParams.weight = weight + i;
		tv_wordrecite_pb.setText((weight + i) + "");
		tv_wordrecite_pb.setLayoutParams(layoutParams);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mIndex++;
		mIndex = mIndex % dicts.size();
		if (arryCount == wordCount) {
			creatAlertDiag();//打卡
		}else {
			initData();
		}
	}
}
