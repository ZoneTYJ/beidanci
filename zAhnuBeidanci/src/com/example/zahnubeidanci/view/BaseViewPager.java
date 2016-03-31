package com.example.zahnubeidanci.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.bean.DictXmlInfo;
import com.example.zahnubeidanci.bean.HistoryInfo;
import com.example.zahnubeidanci.engine.MusicEngine;
import com.example.zahnubeidanci.utils.Constants;
import com.example.zahnubeidanci.utils.SharedPreferenceUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class BaseViewPager extends RefreshListView {
	private Context mActivity;
	private int index;
	private RefreshListView mListView;
	private MyPronClick myPronClick;
	private MylvAdapter mylvAdapter;
	private List<DictXmlInfo> mDics;
	protected boolean isRefreshing=false;
	protected boolean isLoadMore=false;
	protected String moreUrl;
	public View rootView;

	public BaseViewPager(Context context, int index) {
		super(context);
		this.index = index;
		mActivity = context;
		rootView=View.inflate(mActivity, R.layout.listview, null);
		initView();
	}

	private void initView() {
		mListView=(RefreshListView) rootView.findViewById(R.id.lv);
//		mListView = new RefreshListView(mActivity);
		myPronClick = new MyPronClick();

		mListView.setMyOnRefreshListener(new MyOnRefreshListener() {

			@Override
			public void onRefreshing() {
				isRefreshing = true;
				// 处理刷新业务
				getDataFromServer();
			}

			@Override
			public void onLoadingMore() {
				if (!TextUtils.isEmpty(moreUrl)) {
					// 加载更多
					isLoadMore = true;
					getMoreDataFromServer();
				} else {
					// 没有更多数据时，需要恢复加载更多的状态
					mListView.loadMoreFinished();
					Toast.makeText(mActivity, "亲，没有更多数据了", 0).show();
				}
			}
		});
		mDics=new ArrayList<DictXmlInfo>();
		mylvAdapter = new MylvAdapter();
		mListView.setAdapter(mylvAdapter);
	}
	
	public void initData() {
		getDataFromServer();
	}
	
	protected void processListData(String result) {
		Gson gson=new Gson();
		HistoryInfo listDic = gson.fromJson(result, HistoryInfo.class);
		moreUrl=listDic.nextUrl;
		if(!isLoadMore) {// 不是加载更多
			mDics=listDic.dics;
		}else {
			mDics.addAll(listDic.dics);
		}
		mylvAdapter.notifyDataSetChanged();
	}
	
	private void getDataFromServer() {
		String mUid = SharedPreferenceUtils.getString(mActivity, Constants.UserUid, "");
		HttpUtils httpUtils = new HttpUtils(1000);
		httpUtils.configDefaultHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, Constants.HISTORY_URL+index+"?id=" + mUid,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						processListData(responseInfo.result);
						if (isRefreshing) {
							isRefreshing = false;
							// 恢复下拉刷新状态
							mListView.refreshFinished(true);
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if (isRefreshing) {
							isRefreshing = false;
							// 恢复下拉刷新状态
							mListView.refreshFinished(false);
						}
					}
				});
	}
	
	private void getMoreDataFromServer() {
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.configDefaultHttpCacheExpiry(0);
		httpUtils.send(HttpMethod.GET, moreUrl,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						processListData(responseInfo.result);
						// 恢复加载更多的状态
						mListView.loadMoreFinished();
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// 恢复加载更多的状态
						mListView.loadMoreFinished();
					}
				});
	}

	class MylvAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mDics.size();
		}

		@Override
		public Object getItem(int position) {
			return mDics.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			lvHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.item_history_vp,
						null);
				holder = new lvHolder();
				holder.tv_wordrecite_words = (TextView) convertView
						.findViewById(R.id.tv_wordrecite_words);
				holder.tv_wordrecite_phonetic = (TextView) convertView
						.findViewById(R.id.tv_wordrecite_phonetic);
				holder.ib_wordrecite_horn = (ImageButton) convertView
						.findViewById(R.id.ib_wordrecite_horn);
				convertView.setTag(holder);
			} else {
				holder = (lvHolder) convertView.getTag();
			}
			DictXmlInfo dictXmlInfo = mDics.get(position);
			holder.ib_wordrecite_horn.setTag(dictXmlInfo.symbols.get(0).pron);
			holder.ib_wordrecite_horn.setOnClickListener(myPronClick);
			holder.tv_wordrecite_words.setText(dictXmlInfo.key);
			holder.tv_wordrecite_phonetic
					.setText(dictXmlInfo.symbols.get(0).ps);
			return convertView;
		}
	}

	class lvHolder {
		public TextView tv_wordrecite_words;
		public TextView tv_wordrecite_phonetic;
		public ImageButton ib_wordrecite_horn;
	}

	class MyPronClick implements OnClickListener {

		@Override
		public void onClick(View v) {
			//TODO播音
			String url = (String) v.getTag();
			MusicEngine musicEngine=new MusicEngine(url);
			musicEngine.playMp3();
		}

	}
}
