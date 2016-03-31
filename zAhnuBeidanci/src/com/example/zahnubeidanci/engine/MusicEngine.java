package com.example.zahnubeidanci.engine;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

public class MusicEngine {
	private MediaPlayer player;
	private String mUrl;
	public MusicEngine(String url) {
		this.mUrl=url;
	}
	public void onCreate() {
		player = new MediaPlayer();
	}
	
	public void onDestroy() {
		player.release();
	}
	
	public void play(String url){
		if(player.getCurrentPosition()!=0){
			player.reset();
		}
		try {
			player.setDataSource(url);
			player.prepareAsync();
			player.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					player.start();
				}
				
			});
			player.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					onDestroy();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void pause() {
		player.pause();
	}
	
	public void continuePlay(){
		player.start();
	}
	
	/** 获取mediaPlayer并且播放 */
	public  void playMp3(){
		if(player==null){
			onCreate();
		}else {
			play(mUrl);
		}
	}
}
