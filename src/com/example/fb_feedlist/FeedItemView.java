/*Copyright 2013 Square, Inc.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.example.fb_feedlist;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class FeedItemView extends LinearLayout {
	public static String TAG = "FeedItemView";
	/**
	 * Icon
	 */
	private ImageView mIcon;

	/**
	 * TextView 01
	 */
	private TextView mText01;

	/**
	 * TextView 02
	 */
	// private TextView mText02;

	/**
	 * TextView 03
	 */
	private TextView mText03;
	/**
	 * TextView 03(수정)
	 */
	private TextView mText04;

	private ImageView mPhoto_FB;

	public FeedItemView(Context context) {
		super(context);
		// Layout Inflation
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View convertView = inflater.inflate(R.layout.listitem, this, true);

		// Set Icon
		mIcon = (ImageView) convertView.findViewById(R.id.iconItem);

		// Set calculated date
		mText01 = (TextView) convertView.findViewById(R.id.dataItem01);

		// Set Text 02
		// mText02 = (TextView) findViewById(R.id.dataItem02);

		// Set date
		mText03 = (TextView) convertView.findViewById(R.id.dataItem03);

		// Set Photo
		mPhoto_FB = (ImageView) convertView.findViewById(R.id.url_image);

	}

	/**
	 * set Text
	 * 
	 * @param index
	 * @param data
	 */
	public void setText(int index, String data) {
		if (index == 0) {
			if (data != "") 
				mText01.setText(data);
			if(data == ""){
				mText01.setText("");
			}
			
		} else if (index == 1) {
			// mText02.setText(data);
		} else if (index == 2) {
			if (data != "") 
				mText03.setText(data);
			if(data == ""){
				mText03.setText("");
			}
		} // 수정
		else if (index == 3) {
			if (data != "") {
				Picasso.with(getContext()).load(data)
						.transform(resizeTransformation)
						.error(R.drawable.icon_face).into(mPhoto_FB);
				Log.d(TAG, data);
			}
			else{
				Log.d(TAG, "진짜 없음." );
				mPhoto_FB.setImageResource(R.drawable.no_img2);
			}
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * set Icon
	 * @param i 
	 * 
	 * @param icon
	 */
	public void setIcon1(int i) {
		if(i == 0){
			mIcon.setImageResource(R.drawable.facebook_icon);
		}
		if(i == 1){
			mIcon.setImageResource(R.drawable.no_img1);
		}
	}

	/*
	 * public void setIcon(Bitmap bitmap) { mIcon.setImageBitmap(bitmap); }
	 */
	
	private Transformation resizeTransformation = new Transformation() {

		@Override
		public Bitmap transform(Bitmap source) {
			float screenWidth = getContext().getResources().getDisplayMetrics().widthPixels; // 화면
																								// 사이즈
			float screenHeight = getContext().getResources()
					.getDisplayMetrics().heightPixels; // 화면 사이즈

			int targetWidth;

			if (screenWidth > 720) {
				targetWidth = (int) screenWidth - 320;
			} else if (screenWidth < 720) {
				targetWidth = (int) screenWidth - 106;
			} else {
				targetWidth = (int) screenWidth - 160;
			}
			double aspectRatio = (double) source.getHeight()
					/ (double) source.getWidth();
			int targetHeight = (int) (targetWidth * aspectRatio);
			Bitmap result = Bitmap.createScaledBitmap(source, targetWidth,
					targetHeight, false);
			if (result != source) {
				// Same bitmap is returned if sizes are the same
				source.recycle();
			}
			return result;
		}

		@Override
		public String key() {
			return "resizeTransformation ";
		}
	};
}
