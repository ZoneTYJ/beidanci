package com.example.zahnubeidanci.activity;

import com.example.zahnubeidanci.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public abstract class MainBaseActivity extends Activity implements OnClickListener {
	private GestureDetector mGestureDetector;
	protected  int currentIndex;//当前下标数值
	protected ImageView mIvHome;
	protected ImageView mIvNotify;
	protected ImageView mIvSearch;
	protected ImageView mIvMenu;
	protected Button mBtnPersonal;
	protected Button mBtnRecite;
	protected Button mBtnExam;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector=new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				if(e1.getRawX()>e2.getRawX()+100){
					doNext();
					return true;
				}
				if(e1.getRawX()<e2.getRawX()-100){
					doPre();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mGestureDetector.onTouchEvent(event);
		return super.onTouchEvent(event);
	}

	protected void doPre() {
//		overridePendingTransition(R.anim.pre_enter_anim, R.anim.pre_exit_anim);
		finish();
	}

	protected void doNext() {
//		overridePendingTransition(R.anim.next_enter_anim, R.anim.next_exit_anim);
		finish();
	}
	
	protected void setClick(){
		mBtnPersonal.setEnabled(true);
		mBtnRecite.setEnabled(true);
		mBtnExam.setEnabled(true);
		mBtnPersonal.setOnClickListener(this);
		mBtnRecite.setOnClickListener(this);
		mBtnExam.setOnClickListener(this);
		setTilleClick();
	}
	
	protected void setTilleClick(){
		mIvHome.setOnClickListener(this);
		mIvNotify.setOnClickListener(this);
		mIvSearch.setOnClickListener(this);
		mIvMenu.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		int nextIndex=0;
		Intent intent=null;
		if(v==mIvHome){
			intent=new Intent(this,MainActivity.class);
			nextIndex=1;
		}
		if(v==mIvNotify){
			intent=new Intent(this,NotifyActivity.class);
			nextIndex=20;
			startActivityForResult(intent, 11);
			return;
		}
		if(v==mIvSearch){
			intent=new Intent(this,SearchActivity.class);
			nextIndex=1;
			startActivityForResult(intent, 11);
			return;
		}
		if(v==mIvMenu){
			intent=new Intent(this,MenuActivity.class);
			nextIndex=40;
			startActivityForResult(intent, 11);
			return;
		}
		if(v==mBtnPersonal){
			intent=new Intent(this,MainActivity.class);
			nextIndex=1;
		}
		if(v==mBtnRecite){
			intent=new Intent(this,ReciteActivity.class);
			nextIndex=2;
		}
		if(v==mBtnExam){
			intent=new Intent(this,ExamActivity.class);
			nextIndex=3;
		}
		if(currentIndex<nextIndex){
			startActivity(intent);
			doNext();
		}else if(currentIndex>nextIndex) {
			startActivity(intent);
			doPre();
		}
	}
	
}
