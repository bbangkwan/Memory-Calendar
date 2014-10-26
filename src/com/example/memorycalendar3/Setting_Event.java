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
 * ����Ʈ�� ������ Ŭ�� ���� ��Ƽ��Ƽ
 * : �������� Ŭ���ϸ� ItemClickExampleNextActivity�� �̵��Ѵ�.
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
	
	// ��Ƽ��Ƽ�� �������� ����Ʈ��
	private ListView mLvData;
	// ����Ʈ�信 ����� �����
	private ActivityListViewCustomAdapter mCustomAdapter;
	// ����� �� ����Ʈ�信 ����� ������ ����Ʈ
	private ArrayList<Data> mList;
	
	/**���� �߰� */
	//SharedPreferences
	SharedPreferences data;
	SharedPreferences.Editor editor;
	//dialog
	LED_Setting_Dialog led_set_Dialog;
	//sharedPreferences�κ��� �޾ƿ� ���� ����ϱ� ���� arraylist�� ����
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
		/** �����߰�  */
		/** SharedPreference */
		data = getSharedPreferences("LED_init",0);
		System.out.println("Event Setting : "+data.getAll());
		
		/** ++++++++++++++++ */
		// item_click_example_activity.xml�� ������ ����Ʈ�並 ���۷����Ѵ�.
		mLvData = (ListView)findViewById(R.id.item_click_example_activity_lv_list);
		
		// ��� ����Ʈ�� �����Ѵ�.
		mList = new ArrayList<Data>();
		// Ŀ���� ����͸� �����Ѵ�.
		mCustomAdapter = new ActivityListViewCustomAdapter(this, R.layout.item_click_example_row, mList);
		// ����Ʈ�信 ����͸� �����Ѵ�.
		mLvData.setAdapter(mCustomAdapter);
		// ������ Ŭ�� �����ʸ� ����Ѵ�.
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
				//0�̸� MainActivity�� showList()�Լ��� ������ ������� �˷��ش�.
				if(checkedIndex == 0){
					((com.example.fb_feedlist.MainActivity)MainActivity.mContext).showListFB(checkedIndex);
				}
				//1�̸� MainActivity�� showList()�Լ��� �Ѵ� ������� �˷��ش�.
				else if(checkedIndex == 1){
					((com.example.fb_feedlist.MainActivity)MainActivity.mContext).showListFB(checkedIndex);
				}
				//2�̸� MainActivity�� showList()�Լ��� �ϳ� ������� �˷��ش�.
				else{
					((com.example.fb_feedlist.MainActivity)MainActivity.mContext).showListFB(checkedIndex);
				}
				Toast.makeText(Setting_Event.this, "�ֱ� ������ ����Ǿ����ϴ�.",  Toast.LENGTH_SHORT).show();
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
		// ����Ʈ�� Ŭ�����Ѵ�.
		mList.clear();
		// ����Ʈ�� �����͸� �Է��Ѵ�.
		LoadPreferences();
		
		// ����͸� �����Ѵ�. �����ϸ� ����Ʈ���� �����ʹ� ���ΰ�ħ �ȴ�.
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
		* LED_init��� ������ ����� key-value����� �����͸� ��� �ҷ��´�. �׸��� for���� ���Ͽ� �ϳ��� ������ش�.
		*/

		    Map<String, ?> dataSet = data.getAll();
		    for (Map.Entry<String, ?> entry : dataSet.entrySet()) {
		    	
		        LED_Collection.add(entry.getKey());
		        //rgb���� ����

		        String rgb_Value = entry.getValue().toString().replace("[", "");
		        rgb_Value = rgb_Value.replace("]", "");
		        
		        String delimiter = ",";	
		        String[] temp;

	            temp = rgb_Value.split(delimiter);
	            
	            
	            //list�� �߰�.
		        mList.add(new Data(entry.getKey(), "", Color.rgb(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2] ))));
		        
		    } 
		    //�Ǹ������� �׸��߰��� ������ش�.
		    mCustomAdapter.add(new Data("+ �׸��߰�","", Color.BLACK));

		    //����Ʈ�� ��������ġ�� �̵�.
		    mLvData.setSelection(mList.size()-1);
	
	}
	
	public void onItemClick(AdapterView<?> parent, View v, final int position, long id)
	{
		led_set_Dialog = new LED_Setting_Dialog(Setting_Event.this);	
		led_set_Dialog.setTitle("�̺�Ʈ �߰�");
	
	
		if(position == mList.size()-1) //�̺�Ʈ �߰�
		{
	
			// Dialog Dismiss�� Event �ޱ�
			led_set_Dialog.setOnDismissListener(new OnDismissListener() {
		            	@Override	
			            public void onDismiss(DialogInterface dialog) {
		
			                //Toast.makeText(Setting_Event.this, "�߰��Ǿ����ϴ�.",  Toast.LENGTH_SHORT).show();
		
			                mCustomAdapter.clear();
			                LoadPreferences();
			                mCustomAdapter.notifyDataSetChanged();
			            }
			        });
			led_set_Dialog.show();
		}
		else
		{
			// ���� ���̾�α׿� ������ �޽����� �����.
			final String Event_Title = LED_Collection.get(position);
			String message = "["+Event_Title+"]"+" �̺�Ʈ�� �����Ͻðڽ��ϱ�?";
			
			DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick(DialogInterface arg0, int arg1)
				{
					// ���õ� �������� ����Ʈ���� �����Ѵ�.
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
					
					
					// Adapter�� �����Ͱ� �ٲ�� �˸��� ����Ʈ�信 �ٽ� �׸���.
					mCustomAdapter.notifyDataSetChanged();				
				}
			};
			
			// ������ ����� ���̾�α׸� �����Ѵ�.
			new AlertDialog.Builder(this)
				.setMessage(Html.fromHtml(message))
				.setPositiveButton("����", deleteListener)
				.show();
		}
	}
	
	public void connectWebSocket() throws JSONException {
		URI uri;
		final String uemail = SplashActivity.getEmail();
		final JSONObject Event_Obj = new JSONObject();//event ��������
		final JSONObject Sync_Obj = new JSONObject();
		 
		Map<String, ?> MapObject = data.getAll(); //map�� sharedpreference�� �ִ� key-value�� �ִ´�.(sharedpreferenced�� �ִ� event ���� ������ �����Ѵ�.)
		 for (Map.Entry<String, ?> entry : MapObject.entrySet()) {	   
			 Event_Obj.put(entry.getKey(), entry.getValue().toString());
		 } 
		 Sync_Obj.put("Event_Classify", Event_Obj.toString());
		
        //��������.
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
