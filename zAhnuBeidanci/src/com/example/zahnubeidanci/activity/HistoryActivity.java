package com.example.zahnubeidanci.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.view.BaseViewPager;
import com.viewpagerindicator.TabPageIndicator;

public class HistoryActivity extends Activity {
	private String[] titles;
	private Activity mActivity = this;
	// private List<DictXmlInfo> mDics;
	private TabPageIndicator tp_indicator;
	private ViewPager vp_pager;
	protected int currentPage = 0;

	private List<BaseViewPager> views=new ArrayList<BaseViewPager>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		initView();
		initData();
	}

	private void initData() {
		tp_indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				views.get(arg0).initData();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private void initView() {
		titles = new String[] { "今日单词", "新的单词", "正在学习", "掌握单词" };
		for(int i=0;i<titles.length;i++){
			views.add(new BaseViewPager(mActivity, i));
		}

		Button btn_worddictation_back = (Button) findViewById(R.id.btn_worddictation_back);
		btn_worddictation_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tp_indicator = (TabPageIndicator) findViewById(R.id.tp_indicator);
		vp_pager = (ViewPager) findViewById(R.id.vp_pager);

		vp_pager.setAdapter(new MyvpAdapater());
		tp_indicator.setViewPager(vp_pager);
		views.get(0).initData();
	}

	class MyvpAdapater extends PagerAdapter {
		@Override
		public CharSequence getPageTitle(int position) {
			return titles[position];
		}

		@Override
		public int getCount() {
			return titles.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
//			container.addView(views.get(position));
//			return views.get(position);
			container.addView(views.get(position).rootView);
			return views.get(position).rootView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}

}
