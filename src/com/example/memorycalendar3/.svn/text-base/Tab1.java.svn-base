package com.example.memorycalendar3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.StringTokenizer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import Boom.It.Network.Websocket;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.example.fb_feedlist.R;

import ctartifact.android.common.CTCalendarView;

public class Tab1 extends Activity {
	public static final String TAG = "Tab1";
	
	private BackPressCloseHandler backPressCloseHandler;

	private ArrayList<String> mGroupList = null;
	private ArrayList<ArrayList<String>> mChildList = null;
	private ArrayList<String> mChildListContent = null;

	LinkedList<Integer> list = new LinkedList<Integer>();
	ArrayList<Integer> random_int = null;
	ArrayList<String> search_word = null;// alterdialog와 connectwebsoket에서 쓰이는
											// 배열
	ArrayList<String> simg_urllist = null;
	ArrayList<ArrayList<String>> simg_urlgroup = null;
	/*
	 * Layout
	 */
	private ExpandableListView mListView;
	BaseExpandableListAdapter searchListAdapter;

	Button send_btn;
	EditText edit_search;

	ProgressDialog server_connect_dialog;// 서버와 통신하는 동안에 dialog
	ProgressDialog seach_result_dialog;// 관심사 이미지 불러오는 dialog.
										// 관심사 tab을 누르거나, 서버에 관심사를 추가한 후 정보를 가져온
										// 경우 추가.
	// sharedpreferenced
	SharedPreferences sharedPref;
	SharedPreferences.Editor edior;

	private WebSocketClient mWebSocketClient;

	int key_index = 0;

	int add_check;
	String word;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab2);

		mListView = (ExpandableListView) findViewById(R.id.crawlinglist);

		searchResult();
		
		// 관심사 설정 dialog 띄우기.
		send_btn = (Button) findViewById(R.id.search_list);
		send_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				searchSend();
			}
		});
		
		backPressCloseHandler = new BackPressCloseHandler(this);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		backPressCloseHandler.onBackPressed();
	}

	// 리스트뷰가 제대로 갱신이 되지 않음. 이 부분처리하고, ansynctask처리하면 끝.
	public void searchResult() {
		seach_result_dialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_DARK);
		seach_result_dialog.setTitle("Memory Calendar");
		seach_result_dialog.setIcon(R.drawable.memory_calander_ic);
		seach_result_dialog.setMessage("관심사 사진을 불러오는 중입니다.");
		seach_result_dialog.setIndeterminate(false);
		seach_result_dialog.setCancelable(false);
		seach_result_dialog.setCanceledOnTouchOutside(false);
		seach_result_dialog.show();
		
		sharedPref = getSharedPreferences("Favorite", MODE_PRIVATE);
		edior = sharedPref.edit();

		// sharedpref에 저장된 관심사 키워드를 불러온다.
		search_word = new ArrayList<String>();

		Log.d(TAG, "searchSend all : " + sharedPref.getAll());

		Map<String, ?> MapObject;
		// map에 sharedpreference에 있는 key-value를 넣는다.(sharedpreferenced에 있는 event
		// 설정 정보를 저장한다.)
		MapObject = sharedPref.getAll();
		//int q = 0;

		simg_urlgroup = new ArrayList<ArrayList<String>>();

		mGroupList = new ArrayList<String>();
		mChildList = new ArrayList<ArrayList<String>>();
		mChildListContent = new ArrayList<String>();

		Random random = new Random();
		int random_number;
		
		random_int = new ArrayList<Integer>();
		
		for (int i = 0; i < 20; ++i)
			list.addLast(i); // 1~20까지의 값을 집어 넣음
		
		for (int i = list.size(); i > 5; i--) {

			int index = random.nextInt(i); // 현재 list 길이 만큼의 숫자에서 랜덤 숫자 뽑아냄

			random_int.add((Integer) list.get(index));

			random_number = (Integer) list.get(index); // 랜덤 값이 나온 값을 리스트에서 뽑아냄
			// (결국 랜덤으로 나온 값을 뽑아내는것과
			// 같음

			list.remove(index); // 뽑아낸 리스트 제거

		}
		Log.d(TAG, "random list: " + random_int);

		// 불러오면서 몬가가 잘 못 불러와진다. shared에서 불러오는게 이상함....

		for (Map.Entry<String, ?> entry : MapObject.entrySet()) {
			// 관심사 설정하는 dialog에 있는 리스트에 보여주기 위해 searchSend array에 key값만 저장한다.
			search_word.add(entry.getKey());
		}
		// Log.d(TAG, "searchSend() : " + sharedPref.getAll());
		//Log.d(TAG, "searchSend() : " + search_word);
		
		// 각 search_word별로 검색어를 저장.
		for (int word_count = 0; word_count < search_word.size(); word_count++) {
			Log.d(TAG, "검색어... : " + search_word.get(word_count) + " / "
					+ word_count);
			
			// 먼저 각 search_word에 해당하는 문자열을 전부 다 가져온다.
			String str = sharedPref
					.getString(search_word.get(word_count), null);
			
			if(str.length() == 1){
				edior.remove(search_word.get(word_count));
				edior.commit();
				Log.d(TAG,
						"삭제 (주소 없음.) : "+search_word.get(word_count));
				search_word.remove(search_word.get(word_count));
				Log.d(TAG, "searchSend() : " + sharedPref.getAll());
			}
			else{
				// 가져온 문자열을 ","이라는 토큰을 찾아내서 쪼갠다.
				StringTokenizer st = new StringTokenizer(str, ",");

				simg_urllist = new ArrayList<String>();

				while (st.hasMoreElements()) {
					// 쭉 읽으면서 ","가 나오면 그 전까지는 해당 변수에 넣고, 그 다음부터 읽어가기 시작한다.
					simg_urllist.add(st.nextToken());
				}

				simg_urlgroup.add(word_count, simg_urllist);
			}		
		}

		// 먼저 각 그룹에 들어 갈 것들을 세팅
		for (int group_set = 0; group_set < search_word.size(); group_set++) {
			mGroupList.add(search_word.get(group_set));
		}

		// 각 그룹에 child로 들어갈 애들을 세팅.
		for (int i = 0; i < simg_urlgroup.size(); i++) {
			mChildListContent = new ArrayList<String>();
			for (int z = 0; z < 5; z++) {
				int in = random_int.get(z);
				mChildListContent.add(simg_urlgroup.get(i).get(in));
			}
			mChildList.add(mChildListContent);
		}
		
		searchListAdapter = new BaseExpandableAdapter(this, mGroupList,
				mChildList, mListView);
		
		mListView.setAdapter(searchListAdapter);
		
		
		new Handler().postDelayed(new Runnable() {// 5 초 후에 실행
			@Override
			public void run() {
				// 실행할 동작 코딩
				seach_result_dialog.dismiss();
			}
		}, 3000);
		
	}

	protected void searchSend() {
		// TODO Auto-generated method stub
		DialogInterface mPopupDlg = null;
		
		Context searchContext = getApplicationContext();
		
		final ArrayAdapter<String> Adapter;
		
		sharedPref = getSharedPreferences("Favorite", MODE_PRIVATE);
		edior = sharedPref.edit();

		// sharedpref에 저장된 관심사 키워드를 불러온다.
		search_word = new ArrayList<String>();

		Log.d(TAG, "searchSend all : " + sharedPref.getAll());

		Map<String, ?> MapObject;
		// map에 sharedpreference에 있는 key-value를 넣는다.(sharedpreferenced에 있는 event
		// 설정 정보를 저장한다.)
		MapObject = sharedPref.getAll();
		
		for (Map.Entry<String, ?> entry : MapObject.entrySet()) {
			// 관심사 설정하는 dialog에 있는 리스트에 보여주기 위해 searchSend array에 key값만 저장한다.
			search_word.add(entry.getKey());
		}
		
		final AlertDialog.Builder search_dialog = new AlertDialog.Builder(
				Tab1.this, ProgressDialog.THEME_HOLO_DARK);

		LayoutInflater factory = (LayoutInflater) searchContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		final View searchview = factory.inflate(R.layout.search_dialog, null);

		final EditText input_word = (EditText) searchview
				.findViewById(R.id.search_word);
		
		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		input_word.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				imm.showSoftInput(input_word, InputMethodManager.SHOW_FORCED);
			}
		});		
		
		final ListView search_word_list = (ListView) searchview
				.findViewById(R.id.search_list);

		Adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice, search_word);

		search_word_list.setAdapter(Adapter);
		
		search_word_list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		input_word.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				switch (actionId) {
	            case EditorInfo.IME_ACTION_SEARCH:
	            	if (input_word.getText().toString().length() == 0) {
						// 공백일 때 처리할 내용
						Toast.makeText(getApplicationContext(),
								"공백입니다. 다시 입력하세요.", 1000).show();
					} else {
						// 공백이 아닐 때 처리할 내용
						add_check = 0;

						word = input_word.getText().toString();
						search_word.add(word);
						input_word.setText("");
						Adapter.notifyDataSetChanged();
						Log.d(TAG, "add :  " + search_word);
						imm.hideSoftInputFromWindow(input_word.getWindowToken(), 0);
						//save_search_word(word, add_check);
					}
	                return true;
	 
	            default:
	                Toast.makeText(getApplicationContext(), "기본", Toast.LENGTH_LONG).show();
	                return false;
	        }
			}
		});

		search_dialog.setView(searchview);
		search_dialog.setTitle("관심사 설정");
		// search word 추가.
		search_dialog.setPositiveButton("추가",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		// dialog 닫기.
		search_dialog.setNegativeButton("확인",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						imm.hideSoftInputFromWindow(input_word.getWindowToken(), 0);
						
						save_search_word(search_word);
						
						connectWebSocket();//
						
						System.out.println(search_word);
						
						System.out.println("관심사 정보 가져오기");
					}
				});
		// 선택한 검색어 삭제.
		search_dialog.setNeutralButton("삭제",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});

		final AlertDialog dialog = search_dialog.create();
		dialog.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		dialog.show();

		dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						if (input_word.getText().toString().length() == 0) {
							// 공백일 때 처리할 내용
							Toast.makeText(getApplicationContext(),
									"공백입니다. 다시 입력하세요.", 1000).show();
						} else {
							// 공백이 아닐 때 처리할 내용
							add_check = 0;

							word = input_word.getText().toString();
							search_word.add(word);
							input_word.setText("");
							Adapter.notifyDataSetChanged();
							Log.d(TAG, "add :  " + search_word);
							imm.hideSoftInputFromWindow(input_word.getWindowToken(), 0);
							//save_search_word(word, add_check);
						}
					}
				});
		// 삭제 클릭 후 dialog 안 닫히게.
		dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						ArrayList<String> delete_list = new ArrayList<String>();
						add_check = 1;

						SparseBooleanArray sparse = search_word_list
								.getCheckedItemPositions();
						if (sparse.size() != 0) {
							for (int i = search_word_list.getCount() - 1; i >= 0; i--) {
								if (sparse.get(i)) {
									delete_list.add(search_word.get(i));
									Log.d(TAG, "delete_list : " + delete_list);
									search_word.remove(i);

								}
							}
							Toast.makeText(getApplicationContext(),
									"삭제되었습니다." + delete_list, 1000).show();

							search_word_list.clearChoices();
							Adapter.notifyDataSetChanged();
							imm.hideSoftInputFromWindow(input_word.getWindowToken(), 0);
							delete_search_word(delete_list, add_check);
						}

						else
							Toast.makeText(getApplicationContext(), "삭제안됨",
									1000).show();
					}
				});
	}

	/**
	 * WebSocket
	 */
	private void connectWebSocket() {
		key_index = 0;

		sharedPref = getSharedPreferences("Favorite", MODE_PRIVATE);
		edior = sharedPref.edit();

		SharedPreferences Pref = getSharedPreferences("MY_SHARED_PREF",
				MODE_PRIVATE);
		final String uemail = Pref.getString("user_email", null);

		final ArrayList<String> favorite_key = new ArrayList<String>();

		server_connect_dialog = new ProgressDialog(this,
				ProgressDialog.THEME_HOLO_DARK);
		server_connect_dialog.setTitle("Memory Calendar");
		server_connect_dialog.setIcon(R.drawable.memory_calander_ic);
		server_connect_dialog.setMessage("connecting...");
		server_connect_dialog.setIndeterminate(false);
		server_connect_dialog.setCancelable(false);
		server_connect_dialog.setCanceledOnTouchOutside(false);
		server_connect_dialog.show();

		URI uri;

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

				String keyword = "url#";
				//이부분에서 꼬이는 것 같음... 왜 그럴까....
				Map<String, ?> MapObject; // sharedpreferenced에 있는 내용을
											// 저장한다.
				MapObject = sharedPref.getAll(); // map에
													// sharedpreference에
													// 있는
													// key-value를
													// 넣는다.(sharedpreferenced에
													// 있는 event 설정 정보를
													// 저장한다.)
				for (Map.Entry<String, ?> entry : MapObject.entrySet()) {
					keyword += entry.getKey() + ",";
					favorite_key.add(entry.getKey());
				}
				favorite_key.add("");// 서버에서 마지막에 공백 url을 보내서 리스트의 갯수를
										// 맞춰주기 위해
										// arraylist에 공백을 추가해 준다.

				System.out.println("favorite_key : " + favorite_key);

				mWebSocketClient.send(keyword);
			}

			@Override
			public void onMessage(String s) {
				final String message = s;

				//Log.d(TAG, "onMessage_ : " + search_word.size());
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Log.d(TAG, "onMessage key_index : " + key_index);
						if (key_index < search_word.size()) {
							
							edior.putString(favorite_key.get(key_index),
									message);
							edior.commit();
							System.out.println("onMessgae_ : "+sharedPref
									.getString(search_word.get(key_index), null));
							key_index++;
						} else {
							System.out.println("전송완료");
							Log.d(TAG, "전송완료");

							searchResult();
							searchListAdapter.notifyDataSetChanged();
							server_connect_dialog.dismiss();
						}
					}
				});
			}

			@Override
			public void onClose(int i, String s, boolean b) {				
				Log.i("Websocket", "Closed " + s);
				runOnUiThread(new Runnable() {
	                 public void run() {

	                	 Toast.makeText(getApplicationContext(),
	     						"서버 에러 입니다. 조금있다 다시 시도해주시바랍니다.", 1000).show();
	                 }
	            });
				server_connect_dialog.dismiss();
				
			}

			@Override
			public void onError(Exception e) {				
				Log.i("Websocket", "Error " + e.getMessage());
				runOnUiThread(new Runnable() {
	                 public void run() {

	                	 Toast.makeText(getApplicationContext(),
	     						"관리자에게 문의 부탁드립니다.", 1000).show();
	                 }
	            });
				server_connect_dialog.dismiss();
			}
		};
		Log.d(TAG, "websoceket end");
		mWebSocketClient.connect();
	}

	// 서버에 관심사 검색어 전송하는 함수. add_check가 0이면 서버에 저장. 1이면 서버에서 삭제.
	/*protected void save_search_word(String keyword, int add_check) {
		// TODO Auto-generated method stub

		if (add_check == 0) {
			Log.d(TAG, "save_search_word : " + keyword);
			// 입력한 키워드를 sharedpref에 저장한다.
			edior.putString(keyword, " ");
			edior.commit();
		}
	}*/
	protected void save_search_word(ArrayList<String> search_word) {
		// TODO Auto-generated method stub
		int add_count = search_word.size();
		for(int a = 0 ; a < add_count ; a++){
			edior.putString(search_word.get(a), " ");
			edior.commit();
		}
	}

	protected void delete_search_word(ArrayList<String> delete_list,
			int add_check2) {
		int count = delete_list.size();
		for (int d = 0; d < count; d++) {
			edior.remove(delete_list.get(d));
			edior.commit();
			Log.d(TAG, "delete_search_word : " + delete_list.get(d));
		}
	}
}