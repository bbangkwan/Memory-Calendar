/*
 * Copyright 2013 Piotr Adamus
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.memorycalendar3;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import Boom.It.Network.Websocket;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

import com.chiralcode.colorpicker.ColorPicker;
import com.example.fb_feedlist.R;

public class LED_Setting_Dialog extends Dialog implements OnTouchListener {

	private WebSocketClient mWebSocketClient;
	private String index;
	
	private EditText Title;
	private ColorPicker colorPicker;
    private Button button;
    
    //SharedPreferences관련 변수
    SharedPreferences data;
    SharedPreferences.Editor editor;
    
	public LED_Setting_Dialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		data = context.getSharedPreferences("LED_init", 0); //context가 걸려 있어야 getSharedPreferences가 수행된다.
		editor = data.edit();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ledsetting_dialog); // 커스텀 다이얼로그 레이아웃
        
        colorPicker = (ColorPicker) findViewById(R.id.colorPicker);
        Title = (EditText) findViewById(R.id.title);   
        button = (Button) findViewById(R.id.OK);
        
        button.setOnTouchListener(this);
        
	}
	

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v == button) {
			int color = colorPicker.getColor();
			
			String event_Title = Title.getText().toString();
			String led_Value = "["+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+"]";
	
			editor.putString(event_Title, led_Value);
		    editor.commit();
		    
		    try {
				connectWebSocket();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
            dismiss(); // 이후 MainActivity에서 구현해준 Dismiss 리스너가 작동함
        }
        else
		{
            cancel(); // 이후 MainActivity에서 구현해준 Dismiss와 Cancel 리스너가 작동함
		}
		
		return false;
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

