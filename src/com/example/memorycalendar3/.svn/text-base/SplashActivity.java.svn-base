package com.example.memorycalendar3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.fb_feedlist.BasicInfo;
import com.example.fb_feedlist.MainActivity;
import com.example.fb_feedlist.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class SplashActivity extends Activity  {
	Facebook mfacebook;
	SharedPreferences User_Info;
	public static ArrayList<String> albumsid = new ArrayList<String>();
	String dir = "/storage/sdcard0/MemoryCalendar";
	File file = new File(dir);
	String albumid;
	static int a = 0;
	int check = 0;// 최초 어플 설치 및 실행 여부
	public static final String TAG = null;
	
	Handler handler, mHandler;
	
	//주, 월, 년 단위 설정 값 변수.
	final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
	int checkedIndex;
	
	//websocket 
	private WebSocketClient mWebSocketClient;
	
	// SharedPreferences관련 변수
	SharedPreferences sharedPrf;
	SharedPreferences.Editor sharedPrf_editor;
	
	//e-mail 저장
	private static String email="";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.splash);	
		
		mfacebook = new Facebook(BasicInfo.FACEBOOK_APP_ID);
		
		//ID를 가져온다.
		User_Info = getSharedPreferences("FACEBOOK", MODE_PRIVATE);
		String access_token = User_Info.getString("access_token", null);
		long expires = User_Info.getLong("access_expires", 0);
	

		if (access_token != null) {
			mfacebook.setAccessToken(access_token);
		}

		if (expires != 0) {
			mfacebook.setAccessExpires(expires);
		}

		/**
		 * Facebook 접속
		 */
		connect();
		
		handler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch (msg.what) {
				case 1:
					// Strat another Activity Here
					User_Info = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
					checkedIndex = User_Info
							.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
					// 여기에 sharedpreference load 부분 작성.
					User_Info = getSharedPreferences("APP_FIRST_CHECK",
							MODE_PRIVATE);
					check = User_Info.getInt("app_first", 0);
					Log.d(TAG, "sharedpreferences : " + check);

					// 오늘 요일구하기(다음 날 사진을 서버에 전송하기 위해서.)
					Calendar today = Calendar.getInstance();
					// today.add(Calendar.DATE, 1);
					String date = BasicInfo.DateFormat.format(today.getTime());// 오늘
																				// 날짜
																				// 저장.
					Log.d(TAG, "today : " + date);

					if (check == 0) {// 여기는 app 최초 설치 후 실행 시에만 수행.
						today.add(Calendar.DATE, 1);
						String next_date = BasicInfo.DateFormat.format(today
								.getTime());// 오늘 날짜 저장
						NextDay_Photo_Get npg = new NextDay_Photo_Get();

						Log.d(TAG, "first app install");

						check = 1;// 앱을 처음으로 실행을 시켰으니까 변경해서 앞으로는 밑에 if문으로 간다.
						// 여기에 sharedpreference save 부분 작성.
						User_Info = getSharedPreferences("APP_FIRST_CHECK",
								MODE_PRIVATE);
						SharedPreferences.Editor editor = User_Info.edit();
						editor.putInt("app_first", check);
						editor.putString("next_day", next_date);
						editor.commit();
					} else {// 앱을 한번이라도 실행한 경우는 여기로 일단 간다.
						String next_date = User_Info.getString("next_day", null);

						// test를 위한 코드 일듯함. 실 사용시에는 수행이 안될 것으로 생각 되지만 예외가 생길 수
						// 있으니까... null값 처리.
						if (next_date == null) {
							today.add(Calendar.DATE, 1);
							next_date = BasicInfo.DateFormat.format(today
									.getTime());// 오늘 날짜 저장

							NextDay_Photo_Get npg = new NextDay_Photo_Get();

							Log.d(TAG, "next day null");
							User_Info = getSharedPreferences("APP_FIRST_CHECK",
									MODE_PRIVATE);
							SharedPreferences.Editor editor = User_Info.edit();
							editor.putInt("app_first", check);
							editor.putString("next_day", next_date);
							editor.commit();
						}
						Log.d(TAG, "next today : " + next_date);

						if (date.compareTo(next_date) == 0) {
							NextDay_Photo_Get npg = new NextDay_Photo_Get();
							Log.d(TAG, "next today : " + next_date);
							Log.d(TAG, "date change");

							today.add(Calendar.DATE, 1);
							String next_date1 = BasicInfo.DateFormat
									.format(today.getTime());// 오늘 날짜 저장

							User_Info = getSharedPreferences("APP_FIRST_CHECK",
									MODE_PRIVATE);
							SharedPreferences.Editor editor = User_Info.edit();
							editor.putInt("app_first", check);
							editor.putString("next_day", next_date1);
							editor.commit();
							Log.d(TAG, "next today : " + next_date1);
						}

						Log.d(TAG, "date not change");
					}

					finish();

					((com.example.fb_feedlist.MainActivity) MainActivity.mContext)
							.showListFB(checkedIndex);
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "정보를 가져오는데 실패 했습니다.\n 네트워크 상태를 확인 해주시기 바랍니다.", 3000).show();
					finish();
					break;
				default:
					break;
				}
				return false;
			}
		});			
	}
	
	public void connect() {
		try {

			BasicInfo.FacebookInstance = mfacebook;
			if (mfacebook.isSessionValid()) {
				Log.d(TAG, "called.");
				showUserTimeline();				
			} else {
				mfacebook.authorize(this, BasicInfo.FACEBOOK_PERMISSIONS,
						new DialogListener() {

							@Override
							public void onFacebookError(FacebookError e) {
								// TODO Auto-generated method stub
								Log.d(TAG, "facebookerror");
							}

							@Override
							public void onError(DialogError e) {
								// TODO Auto-generated method stub
								Log.d(TAG, "error");
							}

							@Override
							public void onComplete(Bundle values) {
								// TODO Auto-generated method stub
								User_Info = getSharedPreferences("FACEBOOK", MODE_PRIVATE);
								SharedPreferences.Editor editor = User_Info.edit();
								editor.putString("access_token",
										mfacebook.getAccessToken());
								editor.putLong("access_expires",
										mfacebook.getAccessExpires());
								editor.commit();
								
								showUserTimeline();
								Log.d(TAG, "showUserTimeline() calling");
							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub
								Log.d(TAG, "cancel");
							}
						});
				
			}
		} catch (Exception ex) {
			handler.sendEmptyMessageDelayed(2, 2000);
			ex.printStackTrace();
		}

	}
	
	private void showUserTimeline() {
		// TODO Auto-generated method stub
		
		// UserAlbumid를 가져오기 위한 스레드 실행
		GetUserAlbumidThread thread = new GetUserAlbumidThread();
		thread.start();
	}
	
	// album id를 가져오기 위한 스레드
	class GetUserAlbumidThread extends Thread {
		public void run() {
			getUserAlbumid();
		}

		private void getUserAlbumid() {
			// TODO Auto-generated method stub
			try {
				
				albumsid.clear();
				
				final String user_email = BasicInfo.FacebookInstance.request("me");
				try{
					JSONObject albumsObj1 = new JSONObject(user_email);
					setEmail(albumsObj1.getString("email"));
					
					User_Info = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
					
					SharedPreferences.Editor editor = User_Info.edit();
					
					editor.putString("user_email", getEmail());
					editor.commit();
					
				}catch(Exception e){
					Log.d(TAG, "cancel 1");
					handler.sendEmptyMessageDelayed(1, 0000);
				}
				
				final String albumidStr = BasicInfo.FacebookInstance
						.request("me/albums");
				// Log.d(TAG, "albumidStr string : " + albumidStr);
				try{
					JSONObject albumsObj = new JSONObject(albumidStr);
					JSONArray aArray = albumsObj.getJSONArray("data");
					
					int albumcount = aArray.length();
					
					for (int a = 0; a < albumcount; a++) {
						JSONObject aobj = aArray.getJSONObject(a);
						albumsid.add(a, aobj.getString("id"));
						String album_count = aobj.getString("count");
					}
					
					Thread.interrupted();
					// UserTimeline 을 가져오기 위한 스레드 실행
					GetUserTimelineThread thread = new GetUserTimelineThread();
					thread.start();
				}catch(Exception e){
					Log.d(TAG, "cancel 2");
				}	
				
				/*mHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
							
					}
				});*/
			} catch (Exception e) {
				Log.d(TAG, "인터넷 연결 오류.");
				handler.sendEmptyMessageDelayed(2, 2000);
			}
		}
	}	

	class GetUserTimelineThread extends Thread {
		int z;

		public void run() {
			getUserTimeline();
		}

		// 타임라인 가져오기
		private void getUserTimeline() {
			try {
				Bundle params = new Bundle();
				params.putString("pretty", "0");
				params.putString("limit", "500");

				for (z = 0; z < albumsid.size(); z++) {
					albumid = albumsid.get(z);
					//Log.d(TAG, z +" response string : " + albumsid.size());

					final String responseStr = BasicInfo.FacebookInstance
							.request(albumid + "/photos", params);

					// Log.d(TAG, z + " response string : " + responseStr);

					if (!file.exists()) {
						file.mkdirs();
					}

					File savefile = new File(dir + "/" + albumid + ".json");
					try {
						FileOutputStream fo = new FileOutputStream(savefile);
						fo.write(responseStr.getBytes());
						fo.close();
					} catch (IOException e) {

					}
				}
				
				Log.d(TAG, z + "save complete!!");

				
				/**
				 * Facebook 정보를 불러온뒤 이벤트 정보를 서버와 Sync해준다.
				 */
				boolean sync_chk = User_Info.getBoolean("Sync", false);
				if(!sync_chk)
				{
					connectWebSocket();
					System.out.println("싱크함");
				}

				handler.sendEmptyMessageDelayed(1, 0000);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * 다른 액티비티로부터의 응답 처리
	 */
	protected void onActivityResult(int requestCode, int resultCode,
			Intent resultIntent) {
		super.onActivityResult(requestCode, resultCode, resultIntent);

		if (resultCode == RESULT_OK) {
			if (requestCode == 32665) {
				BasicInfo.FacebookInstance.authorizeCallback(requestCode,
						resultCode, resultIntent);
			}
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d(TAG, "splash resume");
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d(TAG, "splash pause");
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.d(TAG, "splash restart");
	}
	
	/**
	 * WebSocket
	 */
	private void connectWebSocket() {
		URI uri;
		User_Info = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		final String uemail = User_Info.getString("user_email", null);
		System.out.println(uemail);
		
		try {
			uri = new URI("ws://175.126.125.198:8081/App");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return;
		}

		mWebSocketClient = new WebSocketClient(uri, uemail) {
			@Override
			public void onOpen(ServerHandshake serverHandshake) {
				Log.i("Websocket", "Opened");
		
				mWebSocketClient.send("SYNC_send#");	
			}
			@Override
			public void onMessage(String s) {
				final String message = s;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
//						System.out.println("From server : "+message);
						JSONObject obj;
						try {
							/* Event_list 동기화 */
							obj = new JSONObject(message);
							
							JSONObject Event_list_obj = new JSONObject(obj.get("Event_list").toString());
							JSONObject Event_Classify_obj = new JSONObject(obj.get("Event_Classify").toString());
							
							Iterator<?> month_keys = Event_list_obj.keys();
							
							while(month_keys.hasNext())
								{
									String month_key = (String)month_keys.next();
									sharedPrf = getSharedPreferences(month_key, 0); 
									sharedPrf_editor = sharedPrf.edit();

									//한달 단위로 object를 분해한다.
									JSONObject event_obj = new JSONObject(Event_list_obj.get(month_key).toString());
									Iterator<?> day_keys = event_obj.keys();
									while (day_keys.hasNext()) {
										String day_key = (String)day_keys.next();
										sharedPrf_editor.putString(day_key, event_obj.get(day_key).toString());
										sharedPrf_editor.commit();
									}
								}
							
							/* Event_Classify 동기화 */
							Iterator<?> itor = Event_Classify_obj.keys();
							while(itor.hasNext())
							{
								String event_classify = (String) itor.next();
								sharedPrf = getSharedPreferences("LED_init", 0); 
								sharedPrf_editor = sharedPrf.edit();
								
								sharedPrf_editor.putString(event_classify, Event_Classify_obj.getString(event_classify).toString());
								sharedPrf_editor.commit();
							}
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
			}

			@Override
			public void onClose(int i, String s, boolean b) {
				Log.i("Websocket", "Closed " + s);
			}

			@Override
			public void onError(Exception e) {
				Log.i("Websocket", "Error " + e.getMessage());
			}
		};
		
		mWebSocketClient.connect();
	}

	public static String getEmail() {
		return email;
	}

	public static void setEmail(String email) {
		SplashActivity.email = email;
	}
}
