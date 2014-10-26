package Boom.It.Network;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.memorycalendar3.SplashActivity;

public class Websocket  {
	
	private WebSocketClient mWebSocketClient;
	
	public void connectWebSocket(final String message, final String mode) throws JSONException {
			URI uri;
			
			
			final String uemail = SplashActivity.getEmail();
			
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
					
					String send = message+mode;
					
					mWebSocketClient.send(send);			
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
