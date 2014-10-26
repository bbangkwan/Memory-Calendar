package com.example.memorycalendar3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import a2o2.MC.Setting.ActivityListViewCustomAdapter;
import a2o2.MC.Setting.Data;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fb_feedlist.MainActivity;
import com.example.fb_feedlist.R;

/**
 * 리스트뷰 아이템 클릭 예제 액티비티
 * : 아이템을 클릭하면 ItemClickExampleNextActivity로 이동한다.
 * 
 * @author croute
 * @since 2011.07.12
 */
public class Setting_Event extends Activity implements OnItemClickListener
{
	private BackPressCloseHandler backPressCloseHandler;
	
	RadioGroup datechange;
	int checkedIndex;
	final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
	TextView textCheckedID, textCheckedIndex;
	Button setbtn;
	MainActivity mainactivity;
	
	// 액티비티에 보여지는 리스트뷰
	private ListView mLvData;
	// 리스트뷰에 사용할 어댑터
	private ActivityListViewCustomAdapter mCustomAdapter;
	// 어댑터 및 리스트뷰에 사용할 데이터 리스트
	private ArrayList<Data> mList;
	
	/**수진 추가 */
	//SharedPreferences
	SharedPreferences data;
	SharedPreferences.Editor editor;
	//dialog
	LED_Setting_Dialog led_set_Dialog;
	//sharedPreferences로부터 받아온 값을 사용하기 위해 arraylist에 저장
	LinkedList<String> LED_VALUE = new LinkedList<String>();
	LinkedList<String> LED_Collection = null;
	
	private WebSocketClient mWebSocketClient;

	/* (non-Javadoc)
	* @see android.app.Activity#onCreate(android.os.Bundle)
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_tab);
		/** 수진추가  */
		/** SharedPreference */
		data = getSharedPreferences("LED_init",0);
		System.out.println("Event Setting : "+data.getAll());
		
		/** ++++++++++++++++ */
		// item_click_example_activity.xml에 만들어둔 리스트뷰를 레퍼런스한다.
		mLvData = (ListView)findViewById(R.id.item_click_example_activity_lv_list);
		
		// 어레이 리스트를 생성한다.
		mList = new ArrayList<Data>();
		// 커스텀 어댑터를 생성한다.
		mCustomAdapter = new ActivityListViewCustomAdapter(this, R.layout.item_click_example_row, mList);
		// 리스트뷰에 어댑터를 세팅한다.
		mLvData.setAdapter(mCustomAdapter);
		// 아이템 클릭 리스너를 등록한다.
		mLvData.setOnItemClickListener(this);
		
		datechange = (RadioGroup) findViewById(R.id.dategroup);
		datechange.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
		
		//textCheckedID = (TextView)findViewById(R.id.checkedid);
        //textCheckedIndex = (TextView)findViewById(R.id.checkedindex);
		
		LoadPreference();
		
		setbtn = (Button) findViewById(R.id.setbtn);
		setbtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//0이면 MainActivity에 showList()함수에 일주일 단위라고 알려준다.
				if(checkedIndex == 0){
					((com.example.fb_feedlist.MainActivity)MainActivity.mContext).showListFB(checkedIndex);
				}
				//1이면 MainActivity에 showList()함수에 한달 단위라고 알려준다.
				else if(checkedIndex == 1){
					((com.example.fb_feedlist.MainActivity)MainActivity.mContext).showListFB(checkedIndex);
				}
				//2이면 MainActivity에 showList()함수에 일년 단위라고 알려준다.
				else{
					((com.example.fb_feedlist.MainActivity)MainActivity.mContext).showListFB(checkedIndex);
				}
				Toast.makeText(Setting_Event.this, "주기 설정이 변경되었습니다.",  Toast.LENGTH_SHORT).show();
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
	
	OnCheckedChangeListener radioGroupOnCheckedChangeListener = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			// TODO Auto-generated method stub
			RadioButton checkedRadioBtn = (RadioButton) datechange.findViewById(checkedId);
			checkedIndex = datechange.indexOfChild(checkedRadioBtn);
			
			SavePreference(KEY_SAVED_RADIO_BUTTON_INDEX, checkedIndex);
			
			//textCheckedIndex.setText("checkedIndex = " + checkedIndex);
			//textCheckedID.setText("checkedID = " + checkedRadioBtn.getText());						
		}		
	};
	
	private void LoadPreference() {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		int savedRadioIndex = sharedPreferences.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
		RadioButton savedCheckedRadioButton = (RadioButton) datechange.getChildAt(savedRadioIndex);
		savedCheckedRadioButton.setChecked(true);
	}

	private void SavePreference(String key, int value) {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit(); 
	}
			
	
	
	/* (non-Javadoc)
	* @see android.app.Activity#onResume()
	*/
	@Override
	protected void onResume()
	{
		super.onResume();
		// 리스트를 클리어한다.
		mList.clear();
		// 리스트에 데이터를 입력한다.
		LoadPreferences();
		
		// 어댑터를 갱신한다. 갱신하면 리스트뷰의 데이터는 새로고침 된다.
		mCustomAdapter.notifyDataSetChanged();
	}
	
	/* (non-Javadoc)
	* @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	*/
	
	protected void LoadPreferences(){
		
		SharedPreferences data = getSharedPreferences("LED_init",0);
		
		editor = data.edit();
		
		LED_Collection = new LinkedList<String>();
		LED_VALUE.clear();

		/*
		* LED_init라는 공간에 저장된 key-value방식의 데이터를 모두 불러온다. 그리고 for문을 통하여 하나씩 출력해준다.
		*/

		    Map<String, ?> dataSet = data.getAll();
		    for (Map.Entry<String, ?> entry : dataSet.entrySet()) {
		    	
		        LED_Collection.add(entry.getKey());
		        //rgb값만 추출

		        String rgb_Value = entry.getValue().toString().replace("[", "");
		        rgb_Value = rgb_Value.replace("]", "");
		        
		        String delimiter = ",";	
		        String[] temp;

	            temp = rgb_Value.split(delimiter);
	            
	            
	            //list에 추가.
		        mList.add(new Data(entry.getKey(), "", Color.rgb(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2] ))));
		        
		    } 
		    //맨마지막에 항목추가를 만들어준다.
		    mCustomAdapter.add(new Data("+ 항목추가","", Color.BLACK));

		    //리스트의 마지막위치로 이동.
		    mLvData.setSelection(mList.size()-1);
	
	}
	
	public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
	{
		led_set_Dialog = new LED_Setting_Dialog(Setting_Event.this);	
		led_set_Dialog.setTitle("이벤트 추가");
	
	
		if(position == mList.size()-1) //이벤트 추가
		{
	
			// Dialog Dismiss시 Event 받기
			led_set_Dialog.setOnDismissListener(new OnDismissListener() {
		            	@Override	
			            public void onDismiss(DialogInterface dialog) {
		
			                //Toast.makeText(Setting_Event.this, "추가되었습니다.",  Toast.LENGTH_SHORT).show();
		
			                mCustomAdapter.clear();
			                LoadPreferences();
			                mCustomAdapter.notifyDataSetChanged();
			            }
			        });
			led_set_Dialog.show();
		}
		else
		{
			// 삭제 다이얼로그에 보여줄 메시지를 만든다.
			final String Event_Title = LED_Collection.get(position);
			String message = "["+Event_Title+"]"+" 이벤트를 삭제하시겠습니까?";
			
			DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					// 선택된 아이템을 리스트에서 삭제한다.
					mList.remove(position);
					LED_Collection.remove(position);
					System.out.println(data.getAll());
					SharedPreferences.Editor editor = data.edit();
					editor.remove(Event_Title); 
					editor.commit();
					
					try {
						connectWebSocket();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					// Adapter에 데이터가 바뀐걸 알리고 리스트뷰에 다시 그린다.
					mCustomAdapter.notifyDataSetChanged();				
				}
			};
			
			// 삭제를 물어보는 다이얼로그를 생성한다.
			new AlertDialog.Builder(this)
				.setMessage(Html.fromHtml(message))
				.setPositiveButton("삭제", deleteListener)
				.show();
		}
	}
	
	public void connectWebSocket() throws JSONException {
		URI uri;
		final String uemail = SplashActivity.getEmail();
		final JSONObject Event_Obj = new JSONObject();//event 설정내용
		final JSONObject Sync_Obj = new JSONObject();
		 
		Map<String, ?> MapObject = data.getAll(); //map에 sharedpreference에 있는 key-value를 넣는다.(sharedpreferenced에 있는 event 설정 정보를 저장한다.)
		 for (Map.Entry<String, ?> entry : MapObject.entrySet()) {	   
			 Event_Obj.put(entry.getKey(), entry.getValue().toString());
		 } 
		 Sync_Obj.put("Event_Classify", Event_Obj.toString());
		
        //서버접속.
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
				
				
				mWebSocketClient.send("Sync_Event_Classify#"+Sync_Obj.toString());			
			}

			@Override
			public void onMessage(String s) {
				final String message = s;
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						
					}
			
					
					
				});
			}

			private void runOnUiThread(Runnable runnable) {
				// TODO Auto-generated method stub
				
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
}
