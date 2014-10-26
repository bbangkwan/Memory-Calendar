package com.example.memorycalendar3;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.fb_feedlist.R;

public class Tab extends TabActivity implements OnTabChangeListener {

	TabHost mTabHost;
	
	
	private static final String TAG = null;
	
	private BackPressCloseHandler backPressCloseHandler;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				startActivity(new Intent(Tab.this, SplashActivity.class));
			}
		},10);
		
		
		mTabHost = getTabHost();
		mTabHost.setOnTabChangedListener(this);
		
		mTabHost.addTab(mTabHost
				.newTabSpec("tab1")
				.setIndicator("메모리캘린더",getResources().getDrawable(R.drawable.icon_mc2))
				.setContent(new Intent(this,com.example.fb_feedlist.MainActivity.class)));
		mTabHost.addTab(mTabHost
				.newTabSpec("tab2")
				.setIndicator("일정관리",getResources().getDrawable(R.drawable.icon_cal2))
				.setContent(new Intent(this,hansune.android.exam.ctcalendar.CalendarView.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		mTabHost.addTab(mTabHost
				.newTabSpec("tab3")
				.setIndicator("관심사",getResources().getDrawable(R.drawable.star))
				.setContent(new Intent(this, Tab1.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		mTabHost.addTab(mTabHost
				.newTabSpec("tab4")
				.setIndicator("설정", getResources().getDrawable(R.drawable.icon_set2))
				.setContent(new Intent(this, Setting_Event.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));
		mTabHost.setCurrentTab(0);
		
		// Tab에 색 지정
        for(int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
        	mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg2);
        }
        mTabHost.getTabWidget().setCurrentTab(0);
        mTabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.tab_bg);
        
        backPressCloseHandler = new BackPressCloseHandler(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		backPressCloseHandler.onBackPressed();
	}

	public void onTabChanged(String tabId) {
		// Tab 색 변경
		for(int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.tab_bg2);
        } 
		mTabHost.getTabWidget().getChildAt(mTabHost.getCurrentTab()).setBackgroundResource(R.drawable.tab_bg);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "tab resume");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "tab pause");
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG, "tab restart");
	}

}
