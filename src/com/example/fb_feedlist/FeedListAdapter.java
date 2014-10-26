package com.example.fb_feedlist;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FeedListAdapter extends BaseAdapter {
	public static String TAG = "FeedListAdapter";

	BufferedInputStream bis = null;
	HttpURLConnection conn = null;

	private Context mContext;

	private ArrayList<FeedItem> mItems = new ArrayList<FeedItem>();

	public FeedListAdapter(Context context) {
		mContext = context;
	}

	public void setListItems(ArrayList<FeedItem> list) {
		mItems = list;
	}

	public void clear() {
		if (mItems != null) {
			mItems.clear();
			Log.d(TAG, "clear");
		}
	}

	public void addItem(FeedItem aItem) {
		mItems.add(aItem);
	}

	public int getCount() {
		return mItems.size();
	}

	public Object getItem(int position) {
		return mItems.get(position);
	}

	public boolean areAllItemsSelectable() {
		return false;
	}

	public boolean isSelectable(int position) {
		return true;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		FeedItemView itemView;

		Comparator<FeedItem> myComparator = new Comparator<FeedItem>() {
			public int compare(FeedItem curItem, FeedItem curItem2) {
					return curItem.getDate().compareTo(curItem2.getDate());
			}
		};

		Collections.sort(mItems, myComparator);
		Collections.reverse(mItems);

		if (convertView == null) {
			itemView = new FeedItemView(mContext);
		} else {
			itemView = (FeedItemView) convertView;
		}

		try {
			FeedItem curItem = mItems.get(position);
			
			String date = curItem.getDate();// facebook에 올린 날짜.
			String message = curItem.getMessage();// facebook 사진 url
			String cal_date = curItem.getCaldate();// 해당 사진이 언제꺼인지 계산한 날짜.
			//Log.d(TAG, mItems.get(position)+ " / " + date + " /\n" + message );
			
			//data가 있을 때 없을 때 처리.
			if(date != ""){
				itemView.setIcon1(0);// facebook 사진인지 서버에서 크롤링한 사진인지 구분하기 위한 icon
				itemView.setText(0, cal_date);
				itemView.setText(2, date);
				itemView.setText(3, message);
			}
			else{
				
				itemView.setIcon1(1);// facebook 사진인지 서버에서 크롤링한 사진인지 구분하기 위한 icon
				itemView.setText(0, "");
				itemView.setText(2, "");
				itemView.setText(3, "");
				Log.d(TAG, "사진 없음.");
			}
			
			
			//Log.d(TAG, mItems.get(position)+ " / " + date + " /\n" + message );
			//Log.d(TAG, cal_date );
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return itemView;
	}

}
