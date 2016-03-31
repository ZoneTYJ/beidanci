package com.example.zahnubeidanci.activity;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.R.layout;
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

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class WordActivity extends Activity {
	private String data;
	private TextView tv_word_words;
	private TextView tv_word_phonetic;
	private ImageButton ib_word_horn;
	private TextView tv_word_explanation;
	private Button btn_word_add;
	private String hornUrl;
//	private WordBean wordBean;
	private Button btn_word_next;
	private DictXmlInfo dictXmlInfo;
	private TextView tv_word_example1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word);
		initView();
		initData();
	}

	private void initData() {
		int type = getIntent().getIntExtra("type", -1);
		if (type == 1) {
			btn_word_next.setVisibility(View.VISIBLE);
		} else {
			btn_word_next.setVisibility(View.GONE);
		}
//		wordBeanGson();
		DictXmlInfoGson();
	}

	private void DictXmlInfoGson() {
		data = getIntent().getStringExtra("data");
		Gson gson = new Gson();
		dictXmlInfo = gson.fromJson(data, DictXmlInfo.class);
		if (dictXmlInfo.key == null
				|| TextUtils.isEmpty(dictXmlInfo.key.trim())) {
			tv_word_words.setText("该单词不存在");
			tv_word_phonetic.setVisibility(View.INVISIBLE);
			ib_word_horn.setVisibility(View.INVISIBLE);
			tv_word_explanation.setVisibility(View.INVISIBLE);
			btn_word_add.setVisibility(View.GONE);
		} else {
			tv_word_words.setText(dictXmlInfo.key);
			Symbols symbols = dictXmlInfo.symbols.get(0);
			tv_word_phonetic.setText(symbols.ps);
			hornUrl = symbols.pron;
			String explainText = "";
			for (Means mean : dictXmlInfo.means) {
				explainText += mean.pos+mean.acceptation;
//				explainText += mean.pos+mean.acceptation+ "\n";
			}
			tv_word_explanation.setText(explainText);
			
			Sent sent = dictXmlInfo.sent.get(0);
			if(sent!=null){
				tv_word_example1.setVisibility(View.VISIBLE);
//				tv_word_example1.setText(sent.orig+"\n"+sent.trans);
				tv_word_example1.setText(sent.orig+sent.trans);
			}
		}
	}
	private void initView() {
		Button btn_word_back = (Button) findViewById(R.id.btn_word_back);
		btn_word_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_word_words = (TextView) findViewById(R.id.tv_word_words);
		tv_word_phonetic = (TextView) findViewById(R.id.tv_word_phonetic);
		ib_word_horn = (ImageButton) findViewById(R.id.ib_word_horn);
		ib_word_horn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//TODO播音
				MusicEngine musicEngine=new MusicEngine(hornUrl);
				musicEngine.playMp3();
			}
		});
		tv_word_explanation = (TextView) findViewById(R.id.tv_word_explanation);
		tv_word_example1 = (TextView) findViewById(R.id.tv_word_example1);
		TextView tv_word_example2 = (TextView) findViewById(R.id.tv_word_example2);
		TextView tv_word_example3 = (TextView) findViewById(R.id.tv_word_example3);
		btn_word_add = (Button) findViewById(R.id.btn_word_add);
		btn_word_next = (Button) findViewById(R.id.btn_word_next);
	}

	public void onAddClick(View view) {
		HttpUtils httpUtils = new HttpUtils(1000);
		httpUtils.send(HttpMethod.GET, Constants.ADDWORD_URL + "?word="
				+ dictXmlInfo.key, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.showSafeToast(WordActivity.this, "网络链接失败,请检查网络");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				if (arg0.result.contains("200")) {
					ToastUtils.showSafeToast(WordActivity.this,"添加成功");
				}
//				ToastUtils.showSafeToast(WordActivity.this, arg0.result);
			}
		});
	}

	public void onNxetClick(View view) {
		finish();
	}
}
