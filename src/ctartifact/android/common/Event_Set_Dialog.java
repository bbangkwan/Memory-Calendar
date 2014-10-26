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

	//��,��,�� ����
	int year;
	int month;
	String day;
	String LED_value;//led ������ 
	int Event_index; // event�� �з���ȣ�� spinner���� ������� �����ϴ� ����
	
	
	private EditText Title;
	private EditText Memo;
//	private TextView Now_Date;
//	private Spinner Sel_Event;
	private Button Save_Btn;
	private Button Delete_Btn;

//	ArrayList<Data> arraylist;
	ArrayList<String> arraylist;
	
	// ����Ʈ�信 ����� �����
//	private ActivityListViewCustomAdapter mCustomAdapter;
	
	//SharedPreferences���� ����
    SharedPreferences data;
    SharedPreferences LED_SET;
    SharedPreferences Sync;
    
    SharedPreferences.Editor editor;
    SharedPreferences.Editor LED_SET_editor;
    SharedPreferences.Editor Sync_edit;
    
    //1���� led,title,memo���� �����ϱ����� JSONObject
	JSONObject jObject = null;
	
	//context ����
	Context Event_Set_Dialog_context=null;
	
	CTCalendarView Cal;

	@Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_set_dialog); // Ŀ���� ���̾�α� ���̾ƿ�

//        Now_Date = (TextView) findViewById(R.id.date); //���õ� ��¥�� ǥ�����ִ� textview

        Save_Btn = (Button) findViewById(R.id.save);
        Delete_Btn = (Button) findViewById(R.id.delete);
        Title = (EditText) findViewById(R.id.title);
        Memo = (EditText) findViewById(R.id.memo);
        
        Cal = new CTCalendarView();
        
        //���̾�α׿� �ߴ� ��¥����
//        Now_Date.setText(year+"."+month+"."+(Integer.parseInt(day)-1));
        
        /*sharedpreference ���� */
		String LED_SET_pref = year+"-"+month;

    	
		// TODO Auto-generated constructor stub
		data = Event_Set_Dialog_context.getSharedPreferences("LED_init", 0); //context�� �ɷ� �־�� getSharedPreferences�� ����ȴ�.
		LED_SET = Event_Set_Dialog_context.getSharedPreferences(LED_SET_pref, 0); // �Ѵ��� �� �ϸ��� led �������� �����Ѵ�. ����� �̸��� ���̴�.
		Sync = Event_Set_Dialog_context.getSharedPreferences("MY_SHARED_PREF", 0); // �Ѵ� ��ü�� led�������� ���Ŀ� ���� �����Ѵ�.
		
		editor = data.edit();
		LED_SET_editor = LED_SET.edit();
		Sync_edit = Sync.edit();
        /* */
        
     
        /**
         * Title�� �޸� �ҷ��´�.
         */
		int Event_Title = 0;
        
        try {
			jObject = new JSONObject(LED_SET.getString(day, "{}"));
			
//			//spinner���� �̺�Ʈ�� �з��� �°� spinner�� ǥ�����ֱ� ���� title
			Event_Title = jObject.getInt("index");

			Title.setText(jObject.getString("title"));
			Memo.setText(jObject.getString("memo"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block

		}
        
        /**
         * ���ǳ� �Ӽ�
         */
	    Spinner sp = (Spinner) this.findViewById(R.id.spinner);

        //spinner - setting tab���� ������ �з��� �����´�.
        arraylist = new ArrayList<String>();
        Map<String, ?> dataSet = data.getAll();

        for (Map.Entry<String, ?> entry : dataSet.entrySet()) {
//	        Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
	        arraylist.add(entry.getKey());

        	//rgb���� ����
	        String rgb_Value = entry.getValue().toString().replace("[[", "");
	        rgb_Value = rgb_Value.replace("]]", "");
	        
//	        String delimiter = ",";
//	        String[] temp;
//
//            temp = rgb_Value.split(delimiter);
            
            //list�� �߰�.
//            arraylist.add(new Data(entry.getKey(), "", Color.rgb(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2] ))));
	    } 
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, arraylist);
	    
        
        
	    // ���ǳ� ����
	    sp.setPrompt("����");
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

		//�̺�Ʈ �߰��� sharedpreference�� �������� ����������Ѵ�.
				if (v == Save_Btn) {
					
					System.out.println("Month : "+month);
					System.out.println("Day : "+day);			
					
					/**
					 * title, memo, led �������� hashMap�� �����Ѵ�.
					 */
					JSONObject obj = new JSONObject();
					
					try {
						obj.put("led", LED_value);
						if(Title.getText().toString().equals(""))
						{
							obj.put("title","���ο� �̺�Ʈ");
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
					 * ����� hashMap�� String���� sharedpreference�� �����Ͽ� dbȭ �Ѵ�.
					 */
					LED_SET_editor.putString(day,obj.toString());
					LED_SET_editor.commit();
					//event_List ����Ϸ�!!
				
		            dismiss(); // ���� MainActivity���� �������� Dismiss �����ʰ� �۵���
		            
		         // �ٸ� �޿� ���ο� ������ �߰��ϰ� �״޿� �ӹ����� ���ִ� �޼ҵ�
		            Cal.Set_Day(year, month);
		              
		            Toast.makeText(getContext(), "�߰��Ǿ����ϴ�.",  Toast.LENGTH_SHORT).show();
		        }
				else if(v == Delete_Btn ) 
				{
					System.out.println("����");
					LED_SET_editor.remove(day);
					LED_SET_editor.commit();
					dismiss();
					 
					cancel(); 
					Toast.makeText(getContext(), "�����Ǿ����ϴ�.",  Toast.LENGTH_SHORT).show();
				}
		 
	}
	
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
	
	// TODO Auto-generated method stub
		String key = arraylist.get(arg2); //�з� 
		this.Event_index = arg2;
		this.LED_value = data.getString(key, null);  //�ش��ϴ� �з��� rgb��
	}

	@Override	
	public void onNothingSelected(AdapterView<?> arg0) {
	// TODO Auto-generated method stub
	
	}
	
	
}

