package ctartifact.android.common;



import java.util.ArrayList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fb_feedlist.R;



public class Event_Set_Dialog extends Dialog implements OnClickListener,OnItemSelectedListener {

	//년,월,일 저장
	int year;
	int month;
	String day;
	String LED_value;//led 설정값 
	int Event_index; // event의 분류번호가 spinner에서 몇번인지 저장하는 변수
	
	
	private EditText Title;
	private EditText Memo;
//	private TextView Now_Date;
//	private Spinner Sel_Event;
	private Button Save_Btn;
	private Button Delete_Btn;

//	ArrayList<Data> arraylist;
	ArrayList<String> arraylist;
	
	// 리스트뷰에 사용할 어댑터
//	private ActivityListViewCustomAdapter mCustomAdapter;
	
	//SharedPreferences관련 변수
    SharedPreferences data;
    SharedPreferences LED_SET;
    SharedPreferences Sync;
    
    SharedPreferences.Editor editor;
    SharedPreferences.Editor LED_SET_editor;
    SharedPreferences.Editor Sync_edit;
    
    //1일의 led,title,memo값을 저장하기위한 JSONObject
	JSONObject jObject = null;
	
	//context 선언
	Context Event_Set_Dialog_context=null;
	
	CTCalendarView Cal;

	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_set_dialog); // 커스텀 다이얼로그 레이아웃

//        Now_Date = (TextView) findViewById(R.id.date); //선택된 날짜를 표시해주는 textview

        Save_Btn = (Button) findViewById(R.id.save);
        Delete_Btn = (Button) findViewById(R.id.delete);
        Title = (EditText) findViewById(R.id.title);
        Memo = (EditText) findViewById(R.id.memo);
        
        Cal = new CTCalendarView();
        
        //다이얼로그에 뜨는 날짜셋팅
//        Now_Date.setText(year+"."+month+"."+(Integer.parseInt(day)-1));
        
        /*sharedpreference 셋팅 */
		String LED_SET_pref = year+"-"+month;

    	
		// TODO Auto-generated constructor stub
		data = Event_Set_Dialog_context.getSharedPreferences("LED_init", 0); //context가 걸려 있어야 getSharedPreferences가 수행된다.
		LED_SET = Event_Set_Dialog_context.getSharedPreferences(LED_SET_pref, 0); // 한달의 각 일마다 led 설정값을 저장한다. 저장소 이름은 달이다.
		Sync = Event_Set_Dialog_context.getSharedPreferences("MY_SHARED_PREF", 0); // 한달 전체의 led설정값을 형식에 맞쳐 저장한다.
		
		editor = data.edit();
		LED_SET_editor = LED_SET.edit();
		Sync_edit = Sync.edit();
        /* */
        
     
        /**
         * Title과 메모를 불러온다.
         */
		int Event_Title = 0;
        
        try {
			jObject = new JSONObject(LED_SET.getString(day, "{}"));
			
//			//spinner에서 이벤트의 분류에 맞게 spinner를 표시해주기 위해 title
			Event_Title = jObject.getInt("index");

			Title.setText(jObject.getString("title"));
			Memo.setText(jObject.getString("memo"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block

		}
        
        /**
         * 스피너 속성
         */
	    Spinner sp = (Spinner) this.findViewById(R.id.spinner);

        //spinner - setting tab에서 설정한 분류를 가져온다.
        arraylist = new ArrayList<String>();
        Map<String, ?> dataSet = data.getAll();

        for (Map.Entry<String, ?> entry : dataSet.entrySet()) {
//	        Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
	        arraylist.add(entry.getKey());

        	//rgb값만 추출
	        String rgb_Value = entry.getValue().toString().replace("[[", "");
	        rgb_Value = rgb_Value.replace("]]", "");
	        
//	        String delimiter = ",";
//	        String[] temp;
//
//            temp = rgb_Value.split(delimiter);
            
            //list에 추가.
//            arraylist.add(new Data(entry.getKey(), "", Color.rgb(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2] ))));
	    } 
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arraylist);
	    
        
        
	    // 스피너 제목
	    sp.setPrompt("선택");
	    sp.setAdapter(adapter);
	    sp.setOnItemSelectedListener(this);
	    sp.setSelection(Event_Title);
	    
   
	    Save_Btn.setOnClickListener(this);
	    Delete_Btn.setOnClickListener(this);
	}
	

    public Event_Set_Dialog(Context context) {
    	super(context);

		this.Event_Set_Dialog_context = context; 
    }
	
	
	public void Save_Year(int value){
		this.year = value;
	}
	
	public void Save_Month(int value){
		this.month = value;
	}
	
	public void Save_Day(String value){
		this.day = value;
	}
	
	@Override
	public void onClick(View v) {

		//이벤트 추가후 sharedpreference에 설정값을 저장해줘야한다.
				if (v == Save_Btn) {
					
					System.out.println("Month : "+month);
					System.out.println("Day : "+day);			
					
					/**
					 * title, memo, led 설정값을 hashMap에 저장한다.
					 */
					JSONObject obj = new JSONObject();
					
					try {
						obj.put("led", LED_value);
						if(Title.getText().toString().equals(""))
						{
							obj.put("title","새로운 이벤트");
//							obj.put("categorize",)
						}
						else
						{
							obj.put("title", Title.getText().toString());
						}
						
						obj.put("memo", Memo.getText().toString());
						obj.put("index", Event_index);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
					/**
					 * 저장된 hashMap을 String으로 sharedpreference에 저장하여 db화 한다.
					 */
					LED_SET_editor.putString(day,obj.toString());
					LED_SET_editor.commit();
					//event_List 저장완료!!
				
		            dismiss(); // 이후 MainActivity에서 구현해준 Dismiss 리스너가 작동함
		            
		         // 다른 달에 새로운 일정을 추가하고 그달에 머물도록 해주는 메소드
		            Cal.Set_Day(year, month);
		              
		            Toast.makeText(getContext(), "추가되었습니다.",  Toast.LENGTH_SHORT).show();
		        }
				else if(v == Delete_Btn ) 
				{
					System.out.println("삭제");
					LED_SET_editor.remove(day);
					LED_SET_editor.commit();
					dismiss();
					 
					cancel(); 
					Toast.makeText(getContext(), "삭제되었습니다.",  Toast.LENGTH_SHORT).show();
				}
		 
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	
	// TODO Auto-generated method stub
		String key = arraylist.get(arg2); //분류 
		this.Event_index = arg2;
		this.LED_value = data.getString(key, null);  //해당하는 분류의 rgb값
	}

	@Override	
	public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
	}
	
	
}

