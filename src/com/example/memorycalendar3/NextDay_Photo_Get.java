package com.example.memorycalendar3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.example.fb_feedlist.BasicInfo;

public class NextDay_Photo_Get {
	public static final String TAG = "NextDay_Photo_Get";

	ArrayList<String> albumsid = new ArrayList<String>();

	Handler nHandler = new Handler();

	String albumid;

	int photocount_all;

	String dir = "/storage/sdcard0/MemoryCalendar";
	File file = new File(dir);

	Calendar cal_today = Calendar.getInstance();

	Date curDateObj;// 9.16 수정.

	public NextDay_Photo_Get() {

		Log.d(TAG, "NextDay_Photo_Get");

		albumsid = SplashActivity.albumsid;

		new Handler().postDelayed(new Runnable() {// 5 초 후에 실행
					@Override
					public void run() {
						// 실행할 동작 코딩
						GetNextPhotoThread thread = new GetNextPhotoThread();
						thread.start();
					}
				}, 10000);

	}

	class GetNextPhotoThread extends Thread {
		int z;// 저장된 파일을 열기위한 for문 변수.
		int i;// listview에 뿌리기 위한 for문 변수.

		int photocount;

		String curMessage = null;// facebook 사진 url.
		String curmessage = null;
		String dateStr = null;// 각 사진을 facebook에 update한 시간.
		String datestr = null;

		ArrayList<String> fbphoto_info = new ArrayList<String>();

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			getnextphoto();
		}

		private void getnextphoto() {
			// TODO Auto-generated method stub
			try {
				Log.d(TAG, "read next photo");
				for (z = 0; z < albumsid.size(); z++) {
					albumid = albumsid.get(z);

					File savefile = new File(dir + "/" + albumid + ".json");
					FileInputStream fi = new FileInputStream(savefile);
					BufferedReader br = new BufferedReader(
							new InputStreamReader(fi));

					String str, responseStr = "";

					// 저장된 json data를 읽어 드림.
					while ((str = br.readLine()) != null) {
						responseStr += str;
					}

					try {
						JSONObject resultObj = new JSONObject(responseStr);
						JSONArray jArray = resultObj.getJSONArray("data");

						// Log.d(TAG, "id response string : " + jArray);

						photocount = jArray.length();// 각 json data의 길이.

						// 각각 사진 정보를 읽어드림.

						for (i = 0; i < photocount; i++) {

							JSONObject obj = jArray.getJSONObject(i);

							try {
								curmessage = obj.getString("source");// photo 원본
																		// url.

							} catch (Exception ex) {
								ex.printStackTrace();
							}

							try {
								// facebook에 올린 시간. BasicInfo에 설정을 해놓은 시간 설정에
								// 맞춰서 변환.
								String curDate = obj.getString("created_time");

								curDateObj = BasicInfo.OrigDateFormat
										.parse(curDate);
								datestr = BasicInfo.DateFormat
										.format(curDateObj);
							} catch (Exception ex) {
								ex.printStackTrace();
							}

							cal_today.add(Calendar.DATE, 1);

							int c = 0;

							while (c > -1820) {
								// 이전 날짜 값을 얻어서 날짜 변환.
								cal_today.add(Calendar.DATE, c);
								String date = BasicInfo.DateFormat
										.format(cal_today.getTime());

								// facebook update date와 일치하는 사진 검색.('0'이면 일치.)
								if (date.compareTo(datestr) == 0) {

									// fbphoto_info = new ArrayList<String>();
									/*
									 * 각 사진의 정보를 담는다.
									 */
									curMessage = curmessage;
									dateStr = datestr;
									fbphoto_info.add(curMessage);// 다음 날 사진
																	// url저장.
									Log.d(TAG, "next day : " + date);
								}
								cal_today = Calendar.getInstance();// 다시 오늘 날짜로
																	// 초기화.
								cal_today.add(Calendar.DATE, 1);

								c -= 7;
							}
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				connect(fbphoto_info);
				
			} catch (IOException e) {

			}
		}
	}

	public void connect(ArrayList<String> fbphoto_info) {
		// TODO Auto-generated method stub
		Log.d(TAG, "next photo info : " + fbphoto_info.size());
		Log.d(TAG, "next photo info : " + fbphoto_info);
	}
}
