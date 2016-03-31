package com.example.zahnubeidanci.activity;

import java.util.Arrays;
import java.util.List;

import com.example.zahnubeidanci.R;
import com.example.zahnubeidanci.utils.SharedPreferenceUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MenuActivity extends Activity{
	private Activity mActivity=this;
	private ListView lv_menu_list;
	private List<String> mListData;
	private int[] mIndexs={3,1,1,1};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		initView();
		initData();
	}

	private void initView() {
		lv_menu_list = (ListView) findViewById(R.id.lv_menu_list);
	}
	
	private void getNetInit() {
		//TODO 网络获取初始化结果
	}

	private void initData() {
		getNetInit();
		String[] strs=new String[]{"学习量","发音","其他","退出账号"};
		mListData = Arrays.asList(strs);
		lv_menu_list.setAdapter(new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, mListData));
		lv_menu_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					CreatDiag(0,"设置每天学习单词量",new String[]{"10","20","50","100"});
					break;
				case 1:
					CreatDiag(1,"设置发音",new String[]{"美式","英氏"});break;
				case 2:
					break;
				case 3:
					AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
					builder.setTitle("确定要退出账号吗?");
					builder.setNegativeButton("取消", null);
					builder.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							SharedPreferenceUtils.saveBoolean(mActivity, "UserLogin", false);
							finish();
						}
					});
					builder.show();
					break;
				default:
					break;
				}
			}

			private void CreatDiag(final int position,String title,String[] items) {
				AlertDialog.Builder builder=new AlertDialog.Builder(mActivity);
				builder.setTitle(title);
				builder.setSingleChoiceItems(items, mIndexs[position],new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mIndexs[position]=which;
						//TODO网络发送储存 要传递URL和接口
						dialog.dismiss();
					}
				});
				builder.show();
			}
		});
	}
	
	public void onBackClick(View view){
		finish();
	}

}
