package com.example.fb_feedlist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.memorycalendar3.BackPressCloseHandler;
import com.example.memorycalendar3.SplashActivity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class MainActivity extends Activity {
	public static final String TAG = null;
	TextView nameText, today, toYear, toMonth, today1, today2;
	Button connectBtn, getList;
	LinearLayout list;
	FeedListView feedList;
	FeedListAdapter feedAdapter;
	FeedItem curItem;

	Handler mHandler = new Handler();

	String albumid;

	int photocount_all;

	String dir = "/storage/sdcard0/MemoryCalendar";
	File file = new File(dir);

	Calendar cal_today = Calendar.getInstance();

	String today_datestr;// 9.16 수정.

	Date curDateObj;// 9.16 수정.

	ArrayList<String> albumsid = new ArrayList<String>();

	public static Context mContext;

	ProgressDialog getFbPhoto_dialog;

	ShowPhoto showphoto;

	int date_set;
	
	private BackPressCloseHandler backPressCloseHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mContext = this;

		// getList = (Button) findViewById(R.id.getlist);
		nameText = (TextView) findViewById(R.id.nameText);
		toYear = (TextView) findViewById(R.id.toYear);
		toMonth = (TextView) findViewById(R.id.toMonth);
		today1 = (TextView) findViewById(R.id.today1);
		today2 = (TextView) findViewById(R.id.today2);
		list = (LinearLayout) findViewById(R.id.listset_Layout);

		// 리스트뷰 처리
		feedList = (FeedListView) findViewById(R.id.feedList);
		feedAdapter = new FeedListAdapter(this);
		feedList.setAdapter(feedAdapter);

		// data가 있을 때만 눌리게 설정.

		feedList.setOnDataSelectionListener(new OnDataSelectionListener() {

			@Override
			public void onDataSelected(AdapterView parent, View v,
					int position, long id) {
				// TODO Auto-generated method stub

				curItem = (FeedItem) feedAdapter.getItem(position);
				String curDate = curItem.getDate();
				String curPiUrl = curItem.getMessage();

				Context sContext = getApplicationContext();
				if (curPiUrl != "") {
					LayoutInflater factory = (LayoutInflater) sContext
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View view = factory.inflate(
							R.layout.photosend_dialog, null);

					ImageView iv = (ImageView) view.findViewById(R.id.photo);

					Picasso.with(MainActivity.this).load(curPiUrl)
							.transform(resizeTransformation)
							.error(R.drawable.icon_face).into(iv);

					AlertDialog.Builder fb_builder = new AlertDialog.Builder(
							MainActivity.this, ProgressDialog.THEME_HOLO_DARK);
					fb_builder
							.setIcon(R.drawable.memory_calander_ic)
							.setTitle("Memory Calendar")
							.setMessage("사진을 액자로 전송하시겠습니까?")
							.setView(view)
							.setPositiveButton("전송",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											Toast.makeText(
													getApplicationContext(),
													"전송 완료", 1000).show();
										}
									})
							.setNegativeButton("취소",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											Toast.makeText(
													getApplicationContext(),
													"취소", 1000).show();
										}
									}).show();

				}
			}
		});

		long now = System.currentTimeMillis();

		// 현재 시간을 저장 한다.
		Date date = new Date(now);

		// 시간 포맷 지정
		SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
		SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
		SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");

		// 지정된 포맷으로 String 타입 리턴
		String strCurYear = CurYearFormat.format(date);
		String strCurMonth = CurMonthFormat.format(date);
		String strCurDay = CurDayFormat.format(date);

		toYear.setText(strCurYear);
		toMonth.setText(strCurMonth);
		today1.setText(strCurDay);

		// 오늘 요일구하기
		Calendar cal = Calendar.getInstance();

		int day_of_week = cal.get(Calendar.DAY_OF_WEEK);

		if (day_of_week == 1)
			today2.setText("일요일");
		else if (day_of_week == 2)
			today2.setText("월요일");
		else if (day_of_week == 3)
			today2.setText("화요일");
		else if (day_of_week == 4)
			today2.setText("수요일");
		else if (day_of_week == 5)
			today2.setText("목요일");
		else if (day_of_week == 6)
			today2.setText("금요일");
		else if (day_of_week == 7)
			today2.setText("토요일");

		// today_datestr = BasicInfo.DateFormat.format(today_date);
		today_datestr = BasicInfo.DateFormat.format(cal_today.getTime());
		
		backPressCloseHandler = new BackPressCloseHandler(this);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		backPressCloseHandler.onBackPressed();
	}

	private Transformation resizeTransformation = new Transformation() {

		@Override
		public Bitmap transform(Bitmap source) {
			float screenWidth = MainActivity.this.getResources()
					.getDisplayMetrics().widthPixels; // 화면 사이즈
			float screenHeight = MainActivity.this.getResources()
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

	/*
	 * 주, 월, 년 단위 설정을 하게 되면, 저장된 json 파일에 있는 json data를 읽으면서 해당되는 조건에 맞는 사진을
	 * 검색하여 listview에 뿌려준다.
	 */
	public void showListFB(int checkedIndex) {
		albumsid = SplashActivity.albumsid;

		date_set = checkedIndex;

		showphoto = new ShowPhoto();
		showphoto.execute();// ShowPhoto AsyncTask를 실행하게 된다.
	}

	class ShowPhoto extends AsyncTask<Void, Void, Void> {

		int z;// 저장된 파일을 열기위한 for문 변수.
		int i, j;// listview에 뿌리기 위한 for문 변수.

		int photocount;

		String responStr;

		String curname;
		String curName = null;// facebook user name.
		String curMessage = null;// facebook 사진 url.
		String curmessage = null;
		String dateStr = null;// 각 사진을 facebook에 update한 시간.
		String datestr = null;
		String curDate = null;// 각 사진이 언제 올린거였는지를 계산하기 위한 변수.
		String curalbumid = null;

		ArrayList<ArrayList<String>> FBphoto_info = new ArrayList<ArrayList<String>>();
		ArrayList<String> fbphoto_info = new ArrayList<String>();

		ArrayList<String> album_info = new ArrayList<String>();

		FileInputStream fi;
		BufferedReader br;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
			getFbPhoto_dialog = new ProgressDialog(MainActivity.this,
					ProgressDialog.THEME_HOLO_DARK);
			getFbPhoto_dialog.setTitle("Memory Calendar");
			getFbPhoto_dialog.setIcon(R.drawable.memory_calander_ic);
			getFbPhoto_dialog.setMessage("잠시만 기다려주세요...");
			getFbPhoto_dialog.setIndeterminate(false);
			getFbPhoto_dialog.setCancelable(false);
			getFbPhoto_dialog.show();
		}

		@Override
		protected Void doInBackground(Void... voids) {
			// TODO Auto-generated method stub
			int y = 0;
			try {
				feedAdapter.clear();
				for (z = 0; z < albumsid.size(); z++) {
					albumid = albumsid.get(z);

					File savefile = new File(dir + "/" + albumid + ".json");
					fi = new FileInputStream(savefile);
					br = new BufferedReader(new InputStreamReader(fi));

					String str, responseStr1 = "";

					// 저장된 json data를 읽어 드림.
					while ((str = br.readLine()) != null) {
						responseStr1 += str;
					}
					album_info.add(z, responseStr1);
				}
				fi.close();
				br.close();
				Log.d(TAG, "MainActivity 앨범 정보 load 끝");
				Log.d(TAG, " " + album_info.size());
				for (int p = 0; p < album_info.size(); p++) {
					String responseStr = album_info.get(p);
					try {
						JSONObject resultObj = new JSONObject(responseStr);
						JSONArray jArray = resultObj.getJSONArray("data");

						// Log.d(TAG, "id response string : " + jArray);

						photocount = jArray.length();// 각 json data의 길이.

						// 각각 사진 정보를 읽어드림.

						for (i = 0; i < photocount; i++) {

							JSONObject obj = jArray.getJSONObject(i);

							String curId = obj.getString("id");// id인데.. 솔직히
																// 필요없는 변수.

							JSONObject fromObj = obj.getJSONObject("from");
							curname = fromObj.getString("name");// 사용자 이름.

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

							int c = 0;
							if (date_set == 0) {
								while (c > -1820) {
									// 이전 날짜 값을 얻어서 날짜 변환.
									cal_today.add(Calendar.DATE, c);
									String date = BasicInfo.DateFormat
											.format(cal_today.getTime());

									// facebook update date와 일치하는 사진 검색.('0'이면
									// 일치.)
									if (date.compareTo(datestr) == 0) {

										fbphoto_info = new ArrayList<String>();

										/*
										 * 각 사진의 정보를 담는다.
										 */
										curName = curname;
										fbphoto_info.add(curName);

										curMessage = curmessage;
										fbphoto_info.add(curMessage);

										dateStr = datestr;
										fbphoto_info.add(dateStr);

										if (c == 0)
											curDate = "오늘";
										else
											curDate = Integer.toString(c / 7
													* (-1))
													+ " 주 전";

										fbphoto_info.add(curDate);

										/*
										 * 각 사진의 정보를 담은 fbphoto_info 배열을 다시
										 * FBphoto_info 배열에 넣는다.
										 */
										FBphoto_info.add(y, fbphoto_info);

										y++;
									}
									cal_today = Calendar.getInstance();// 다시 오늘
																		// 날짜로
																		// 초기화.
									c -= 7;
								}
							}
							if (date_set == 1) {

								while (c > -60) {
									// 이전 날짜 값을 얻어서 날짜 변환.
									cal_today.add(Calendar.MONTH, c);
									String date = BasicInfo.DateFormat
											.format(cal_today.getTime());

									// facebook update date와 일치하는 사진 검색.('0'이면
									// 일치.)
									if (date.compareTo(datestr) == 0) {

										fbphoto_info = new ArrayList<String>();

										/*
										 * 각 사진의 정보를 담는다.
										 */
										curName = curname;
										fbphoto_info.add(curName);

										curMessage = curmessage;
										fbphoto_info.add(curMessage);

										dateStr = datestr;
										fbphoto_info.add(dateStr);
										if (c == 0)
											curDate = "오늘";
										else
											curDate = Integer
													.toString(c * (-1))
													+ " 달 전";

										fbphoto_info.add(curDate);

										/*
										 * 각 사진의 정보를 담은 fbphoto_info 배열을 다시
										 * FBphoto_info 배열에 넣는다.
										 */
										FBphoto_info.add(y, fbphoto_info);

										y++;
									}

									cal_today = Calendar.getInstance();// 다시 오늘
																		// 날짜로
																		// 초기화.
									c--;
								}
							}
							if (date_set == 2) {

								while (c > -5) {
									// 이전 날짜 값을 얻어서 날짜 변환.
									cal_today.add(Calendar.YEAR, c);
									String date = BasicInfo.DateFormat
											.format(cal_today.getTime());

									// facebook update date와 일치하는 사진 검색.('0'이면
									// 일치.)
									if (date.compareTo(datestr) == 0) {

										fbphoto_info = new ArrayList<String>();

										/*
										 * 각 사진의 정보를 담는다.
										 */
										curName = curname;
										fbphoto_info.add(curName);

										curMessage = curmessage;
										fbphoto_info.add(curMessage);

										dateStr = datestr;
										fbphoto_info.add(dateStr);
										if (c == 0)
											curDate = "오늘";
										else
											curDate = Integer
													.toString(c * (-1))
													+ " 년 전";

										fbphoto_info.add(curDate);

										/*
										 * 각 사진의 정보를 담은 fbphoto_info 배열을 다시
										 * FBphoto_info 배열에 넣는다.
										 */
										FBphoto_info.add(y, fbphoto_info);

										y++;
									}
									cal_today = Calendar.getInstance();// 다시 오늘
																		// 날짜로
																		// 초기화.
									c--;
								}
							}

						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			} catch (IOException e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			FeedItem curItem = null;
			String Name = null;
			String Message = null;
			String Date = null;
			String Date_cal = null;
			if (FBphoto_info.size() == 0) {
				Name = "";
				Message = "";
				Date = "";
				Date_cal = "";
				curItem = new FeedItem(Name, Date, Message, Date_cal);
				feedAdapter.addItem(curItem);
			}
			for (int a = 0; a < FBphoto_info.size(); a++) {

				for (int b = 0; b < fbphoto_info.size(); b++) {
					Name = FBphoto_info.get(a).get(0);
					Message = FBphoto_info.get(a).get(1);
					Date = FBphoto_info.get(a).get(2);
					Date_cal = FBphoto_info.get(a).get(3);

					if (b == 3) {
						curItem = new FeedItem(Name, Date, Message, Date_cal);
						feedAdapter.addItem(curItem);

					}
				}
			}
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					feedAdapter.notifyDataSetChanged();
				}
			});

			Log.d(TAG, "불러온 사진 개수 : " + FBphoto_info.size());

			FBphoto_info.clear();
			fbphoto_info.clear();
			new Handler().postDelayed(new Runnable() {// 5 초 후에 실행
						@Override
						public void run() {
							// 실행할 동작 코딩
							getFbPhoto_dialog.dismiss();
						}
					}, 1000);
		}

	}

	protected void onPause() {
		super.onPause();

		// saveProperties();
	}

	protected void onResume() {
		super.onResume();

		// loadProperties();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

}