package ctartifact.android.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import Boom.It.Network.Websocket;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fb_feedlist.R;
import com.example.memorycalendar3.Tab1;

// 원본 : http://funpython.com/blog/59?category=2
// 수정 : 한수댁
public class CTCalendarView extends Activity implements OnClickListener,
		LoaderManager.LoaderCallbacks<Cursor>, ViewBinder {
	/**
	 * 테스트
	 */
	Event_Set_Dialog event_set;

	// +++++++++

	private Calendar rightNow;
	private GregorianCalendar gCal;
	private int iYear = 0;
	private int iMonth = 0;

	private int startDayOfweek = 0;
	private int maxDay = 0;
	private int oneday_width = 0;
	private int oneday_height = 0;

	ArrayList<String> daylist; // 일자 목록을 가지고 있는다. 1,2,3,4,.... 28?30?31?
	ArrayList<String> actlist; // 일자에 해당하는 활동내용을 가지고 있는다.

	TextView aDateYear, aDateTxt;

	private int dayCnt;
	private int mSelect = -1;

	// SharedPreferences관련 변수
	SharedPreferences LED_FINAL;
	SharedPreferences LED_SET;
	
	SharedPreferences.Editor LED_FINAL_editor;

	// WebSocket 관련 변수
	private WebSocketClient mWebSocketClient;
//	private String UserID = "arduino";

	// 달력 일정 보기
	private static final String[] PROJECTION = new String[] { CalendarContract.Events._ID, CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND };
	private static final String[] ROW_COLUMNS = new String[] { CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND };
	private static final int[] ROW_IDS = new int[] { R.id.titleTv, R.id.startDateTv, R.id.endDateTv };
	private SimpleCursorAdapter adapter = null;
	private ListView listView = null;

	// 1일의 rgb값을 저장하기위한 JSONObject
	JSONObject jObject = null;

	protected void initialize() {
		setContentView(R.layout.calendarview);
		
		rightNow = Calendar.getInstance();
		gCal = new GregorianCalendar();
		iYear = rightNow.get(Calendar.YEAR);
		iMonth = rightNow.get(Calendar.MONTH);

		ImageButton btnMPrev = (ImageButton) findViewById(R.id.btn_calendar_prevmonth);
		btnMPrev.setOnClickListener(this);
		ImageButton btnMNext = (ImageButton) findViewById(R.id.btn_calendar_nextmonth);
		btnMNext.setOnClickListener(this);
		ImageButton btnToday = (ImageButton) findViewById(R.id.btn_today);
		btnToday.setOnClickListener(this);
		ImageButton btn_effect = (ImageButton) findViewById(R.id.btn_effect);
		btn_effect.setOnClickListener(this);
		
		//ImageButton AddSchedule = (ImageButton) findViewById(R.id.AddSchedule);
		//AddSchedule.setOnClickListener(this);

		//btnToday.setText("오늘");

		aDateYear = (TextView) findViewById(R.id.CalendarYearTxt);
		aDateTxt = (TextView) findViewById(R.id.CalendarMonthTxt);
		
		makeCalendardata(iYear, iMonth);

		// sharedpreference 초기화
		LED_FINAL = getSharedPreferences("LED_ALL", 0);
		LED_FINAL_editor = LED_FINAL.edit();
		
		/*
		// 달력 일정 보기
		adapter = new SimpleCursorAdapter(this, R.layout.cal_list_row, null, ROW_COLUMNS, ROW_IDS);
		adapter.setViewBinder(this);

		listView = (ListView) findViewById(R.id.scheduleListView);
		listView.setAdapter(adapter);

		getLoaderManager().initLoader(0, null, this);
		*/

	}

	// 달력의 일자를 표시한다.
	private void printDate(String thisYear, String thisMonth) {

		if (thisMonth.length() == 1) {
			aDateYear.setText(String.valueOf(thisYear));
			aDateTxt.setText("0" + thisMonth);
		} else {
			aDateYear.setText(String.valueOf(thisYear));
			aDateTxt.setText(thisMonth);
		}
	}

	// 달력에 표시할 일자를 배열에 넣어 구성한다.
	private void makeCalendardata(int thisYear, int thisMonth) {
		// sharedpreference로 부터 설정되었으는 이벤트 목록을 불러온다.
		String LED_SET_pref = iYear + "-" + iMonth;
		System.out.println("pref : " + LED_SET_pref);
		LED_SET = getSharedPreferences(LED_SET_pref, 0); // 한달의 각 일마다 led 설정값을 저장한다. 저장소 이름은 달이다.

		printDate(String.valueOf(thisYear), String.valueOf(thisMonth + 1));

		rightNow.set(thisYear, thisMonth, 1);
		gCal.set(thisYear, thisMonth, 1);
		startDayOfweek = rightNow.get(Calendar.DAY_OF_WEEK);

		maxDay = gCal.getActualMaximum((Calendar.DAY_OF_MONTH));
		if (daylist == null)
			daylist = new ArrayList<String>();
		daylist.clear();

		if (actlist == null)
			actlist = new ArrayList<String>();
		actlist.clear();

		daylist.add("일");
		actlist.add("");
		daylist.add("월");
		actlist.add("");
		daylist.add("화");
		actlist.add("");
		daylist.add("수");
		actlist.add("");
		daylist.add("목");
		actlist.add("");
		daylist.add("금");
		actlist.add("");
		daylist.add("토");
		actlist.add("");

		if (startDayOfweek != 1) {
			gCal.set(thisYear, thisMonth - 1, 1);
			int prevMonthMaximumDay = (gCal.getActualMaximum((Calendar.DAY_OF_MONTH)) + 2);
			for (int i = startDayOfweek; i > 1; i--) {
				daylist.add(Integer.toString(prevMonthMaximumDay - i));
				actlist.add("p");
			}
		}

		for (int i = 1; i <= maxDay; i++) // 일자를 넣는다.
		{
			daylist.add(Integer.toString(i));
		}

	
		for (int i = startDayOfweek; i <= maxDay + startDayOfweek; i++) {
			/**
			 * 달력은 1일~31일이지만 event가 저장되어 있는 SharedPreferences index는 1~35로 고정되어
			 * 있는 값이다. 그래서 SharedPreferences를 1~31일로 되어있는 한달단위의 index로 변환 시켜주어
			 * 날짜와 이벤트를 일치시켜야 한다.
			 */
			// sharedpreference를 확인하여 일정이 추가 되었있는 날만 표시 해준다.
			String act_list = LED_SET.getString((String.valueOf(i)), "");

			if (act_list.equals("")) {
				actlist.add(" ");
			} else {
				actlist.add("●");
			}
		}

		int dayDummy = (startDayOfweek - 1) + maxDay;
		if (dayDummy > 35) {
			dayDummy = 42 - dayDummy;
		} else {
			dayDummy = 35 - dayDummy;
		}

		// 자투리..그러니까 빈칸을 넣어 달력 모양을 이쁘게 만들어 준다.
//		if (dayDummy != 0) {
//			for (int i = 1; i <= dayDummy; i++) {
//				daylist.add(Integer.toString(i));
//				actlist.add("n");
//			}
//		}

		makeCalendar();
	}

	private void makeCalendar() {
		float screenWidth = getBaseContext().getResources().getDisplayMetrics().widthPixels; // 화면 사이즈

		final Oneday[] oneday = new Oneday[daylist.size()];
		final Calendar today = Calendar.getInstance();
		TableLayout tl = (TableLayout) findViewById(R.id.tl_calendar_monthly);
		tl.removeAllViews();

		dayCnt = 0;
		int maxRow = ((daylist.size() > 42) ? 7 : 6);
		int maxColumn = 7;

		oneday_width = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		oneday_height = getWindow().getWindowManager().getDefaultDisplay().getHeight();

		if (screenWidth > 720) {
			oneday_height = ((((oneday_height >= oneday_width) ? oneday_height : oneday_width) - tl.getTop()) / (maxRow + 1)) - 40;
			oneday_width = oneday_width;
		} else {
			oneday_height = ((((oneday_height >= oneday_width) ? oneday_height : oneday_width) - tl.getTop()) / (maxRow + 1)) - 20;
			oneday_width = oneday_width;
		}

		oneday_width = (oneday_width / maxColumn) + 1;

		int daylistsize = daylist.size() - 1;
		for (int i = 1; i <= maxRow; i++) {
			TableRow tr = new TableRow(this);
			tr.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			for (int j = 1; j <= maxColumn; j++) {
				// calender_oneday를 생성해 내용을 넣는다.
				oneday[dayCnt] = new Oneday(getApplicationContext());

				// 요일별 색상 정하기
				if ((dayCnt % 7) == 0) {
					oneday[dayCnt].setTextDayColor(Color.RED);
				} else if ((dayCnt % 7) == 6) {
					oneday[dayCnt].setTextDayColor(Color.BLUE);
				} else {
					oneday[dayCnt].setTextDayColor(Color.BLACK);
				}

				// 요일 표시줄 설정
				if (dayCnt >= 0 && dayCnt < 7) {
					oneday[dayCnt].setBgDayPaint(Color.DKGRAY); // 배경색상
					oneday[dayCnt].setTextDayTopPadding(10); // 일자표시 할때 top padding
					oneday[dayCnt].setTextDayColor(Color.WHITE); // 일자의 글씨 색상
					if (screenWidth > 720) {
						oneday[dayCnt].setTextDaySize(70);
						oneday[dayCnt].setLayoutParams(new LayoutParams(oneday_width, 160)); // 일자 컨트롤 크기
					} else if (screenWidth < 720) {
						oneday[dayCnt].setTextDaySize(22);
						oneday[dayCnt].setLayoutParams(new LayoutParams(oneday_width, 60)); // 일자 컨트롤 크기
					} else {
						oneday[dayCnt].setTextDaySize(35); // 일자의 글씨크기
						oneday[dayCnt].setLayoutParams(new LayoutParams(oneday_width, 90)); // 일자 컨트롤 크기
					}

					oneday[dayCnt].isToday = false;

				} else {

					oneday[dayCnt].isToday = false;
					oneday[dayCnt].setDayOfWeek(dayCnt % 7 + 1);
					oneday[dayCnt].setDay(Integer.valueOf(daylist.get(dayCnt)).intValue());

					/* 일정이 있는날을 표시해준다. */
					oneday[dayCnt].setTextActcntSize(20);

					// String act_list =
					// LED_SET.getString((String.valueOf(dayCnt-6)),"");

					try {
						jObject = new JSONObject(LED_SET.getString((String.valueOf(dayCnt - 6)), "{}"));

						// rgb값만 추출
						String rgb_Value = jObject.getString("led").replace("[", "");
						rgb_Value = rgb_Value.replace("]", "");

						String delimiter = ",";
						String[] temp;

						temp = rgb_Value.split(delimiter);

						oneday[dayCnt].setTextActcntColor(Color.rgb(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2])));

						if (screenWidth > 720) {
							oneday[dayCnt].setTextActcntSize(80);
						} else if (screenWidth < 720) {
							oneday[dayCnt].setTextActcntSize(18);
						} else {
							oneday[dayCnt].setTextActcntSize(24);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block

					}

					/* */

					if (screenWidth > 720) {
						oneday[dayCnt].setTextActcntTopPadding(80);
					} else if (screenWidth < 720) {
						oneday[dayCnt].setTextActcntTopPadding(20);
					} else {
						oneday[dayCnt].setTextActcntTopPadding(40);
					}
					oneday[dayCnt].setBgSelectedDayPaint(Color.rgb(0, 162, 232));
					oneday[dayCnt].setBgTodayPaint(Color.rgb(144, 194, 255));
					oneday[dayCnt].setBgActcntPaint(Color.rgb(251, 247, 176));
					oneday[dayCnt].setLayoutParams(new LayoutParams(oneday_width, oneday_height));

					if (actlist.get(dayCnt).equals("p")) {
						if (screenWidth > 720) {
							oneday[dayCnt].setTextDaySize(70);
						} else if (screenWidth < 720) {
							oneday[dayCnt].setTextDaySize(20);
						} else {
							oneday[dayCnt].setTextDaySize(35);
						}
						actlist.set(dayCnt, "");
						oneday[dayCnt].setTextDayTopPadding(-4);
						oneday[dayCnt].setBgDayPaint(Color.rgb(240, 240, 240)); // 이전 달 배경색상
						
						if (iMonth - 1 < Calendar.JANUARY) {
							oneday[dayCnt].setMonth(Calendar.DECEMBER);
							oneday[dayCnt].setYear(iYear - 1);
						} else {
							oneday[dayCnt].setMonth(iMonth - 1);
							oneday[dayCnt].setYear(iYear);
						}

						// 다음 달 블럭 표시
					} else if (actlist.get(dayCnt).equals("n")) {
						if (screenWidth > 720) {
							oneday[dayCnt].setTextDaySize(70);
						} else if (screenWidth < 720) {
							oneday[dayCnt].setTextDaySize(20);
						} else {
							oneday[dayCnt].setTextDaySize(35);
						}
						actlist.set(dayCnt, "");
						oneday[dayCnt].setTextDayTopPadding(-4);
						oneday[dayCnt].setBgDayPaint(Color.rgb(240, 240, 240)); // 다음 달 배경색상
						
						if (iMonth + 1 > Calendar.DECEMBER) {
							oneday[dayCnt].setMonth(Calendar.JANUARY);
							oneday[dayCnt].setYear(iYear + 1);
						} else {
							oneday[dayCnt].setMonth(iMonth + 1);
							oneday[dayCnt].setYear(iYear);
						}

						// 현재 달 블력 표시
					} else {

						if (screenWidth > 720) {
							oneday[dayCnt].setTextDaySize(70);
						} else if (screenWidth < 720) {
							oneday[dayCnt].setTextDaySize(20);
						} else {
							oneday[dayCnt].setTextDaySize(35);
						}
						oneday[dayCnt].setYear(iYear);
						oneday[dayCnt].setMonth(iMonth);
						oneday[dayCnt].setBgDayPaint(Color.rgb(253, 253, 253)); // 이번 달 배경색상

						// 오늘 표시
						if (oneday[dayCnt].getDay() == today.get(Calendar.DAY_OF_MONTH)
								&& oneday[dayCnt].getMonth() == today.get(Calendar.MONTH)
								&& oneday[dayCnt].getYear() == today.get(Calendar.YEAR)) {

							oneday[dayCnt].isToday = true;
							actlist.set(dayCnt, "오늘");
							if (screenWidth > 720) {
								oneday[dayCnt].setTextActcntSize(50);
							} else if (screenWidth < 720){
								oneday[dayCnt].setTextActcntSize(18);
							} else { 
								oneday[dayCnt].setTextActcntSize(26);
							}
							
								oneday[dayCnt].invalidate();
							mSelect = dayCnt;
						}
					}

					oneday[dayCnt]
							.setOnLongClickListener(new OnLongClickListener() {
								@Override
								public boolean onLongClick(View v) {

									return false;
								}
							});

					oneday[dayCnt].setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {

							if (oneday[v.getId()].getTextDay() != "" && event.getAction() == MotionEvent.ACTION_UP) {
								if (mSelect != -1) {
									oneday[mSelect].setSelected(false);
									oneday[mSelect].invalidate();
								}
								oneday[v.getId()].setSelected(true);
								oneday[v.getId()].invalidate();
								mSelect = v.getId();

								event_set = new Event_Set_Dialog(CTCalendarView.this);
								event_set.Save_Year(iYear);
								event_set.Save_Day(String.valueOf((mSelect - 6)));// 몇번째 LED인지
																	// 저장한다.
								event_set.Save_Month(iMonth);

								event_set.setTitle("이벤트 추가");
								event_set.setOnDismissListener(new OnDismissListener() {
											@Override
											public void onDismiss(
													DialogInterface dialog) {

													makeCalendardata(iYear, iMonth);
											}
										});

								event_set.show();

								onTouched(oneday[mSelect]);
							}
							return false;
						}
					});
				}

				oneday[dayCnt].setTextDay(daylist.get(dayCnt).toString()); // 요일,일자 넣기
				oneday[dayCnt].setTextActCnt(actlist.get(dayCnt).toString());// 활동내용 넣기
				oneday[dayCnt].setId(dayCnt); // 생성한 객체를 구별할수 있는 id넣기
				oneday[dayCnt].invalidate();
				tr.addView(oneday[dayCnt]);

				if (daylistsize != dayCnt) {
					dayCnt++;
				} else {
					break;
				}
			}
			tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		}
	}

	/**
	 * 숫자를 2자리 문자로 변환, 2 -> 02
	 * 
	 * @param value
	 * @return
	 */
	protected String doubleString(int value) {
		String temp;

		if (value < 10) {
			temp = "0" + String.valueOf(value);

		} else {
			temp = String.valueOf(value);
		}
		return temp;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_calendar_nextmonth:
			if (iMonth == 11) {
				iYear = iYear + 1;
				iMonth = 0;
			} else {
				iMonth = iMonth + 1;
			}
			makeCalendardata(iYear, iMonth);
			break;
		case R.id.btn_calendar_prevmonth:
			if (iMonth == 0) {
				iYear = iYear - 1;
				iMonth = 11;
			} else {
				iMonth = iMonth - 1;
			}
			makeCalendardata(iYear, iMonth);
			break;

		case R.id.btn_today:
			gotoToday();
			break;

		//효과 모드 버튼
		case R.id.btn_effect:
			//addSchedule();
			new AlertDialog.Builder(this, ProgressDialog.THEME_HOLO_DARK)
			.setTitle("Mode를 선택해주세요.")
			.setItems(R.array.foods, 
				new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					
					Websocket socket = new Websocket();
					try {
						switch (which) {
						
						case 0: //raindow mode							
							socket.connectWebSocket("EventMode#","rainbow");
						break;
							
						case 1: //heart mode
							socket.connectWebSocket("EventMode#","heart");
						break;
						
						case 2: //smile mode
							socket.connectWebSocket("EventMode#","smile");
						break;
						
	
						default:
							break;
						}
					}
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}	     
					
					Toast.makeText(CTCalendarView.this, "이벤트 실행", Toast.LENGTH_SHORT).show();
				}
			})
			.setNegativeButton("취소", null)
			.show();
			break;
		}

	}

	/**
	 * 서브 클래스에서 오버라이드 해서 터치한 날짜 입력 받기
	 * 
	 * @param oneday
	 */
	protected void onTouched(Oneday oneday) {

	}

	/**
	 * 해당 일이 기준일 범위 안에 있는지 검사
	 * 
	 * @param test
	 *            검사할 날짜
	 * @param basis
	 *            기준 날짜
	 * @param during
	 *            기간(일)
	 * @return
	 */
	protected boolean isInside(Oneday test, Oneday basis, int during) {
		Calendar calbasis = Calendar.getInstance();
		calbasis.set(basis.getYear(), basis.getMonth(), basis.getDay());
		calbasis.add(Calendar.DAY_OF_MONTH, during);

		Calendar caltest = Calendar.getInstance();
		caltest.set(test.getYear(), test.getMonth(), test.getDay());

		if (caltest.getTimeInMillis() < calbasis.getTimeInMillis()) {
			return true;
		}
		return false;
	}

	/**
	 * 오늘 달력으로 이동
	 */
	public void gotoToday() {
		final Calendar today = Calendar.getInstance();
		iYear = today.get(Calendar.YEAR);
		iMonth = today.get(Calendar.MONTH);

		makeCalendardata(today.get(Calendar.YEAR), today.get(Calendar.MONTH));
	}

	// 다른 달에 새로운 일정을 추가하고 그달에 머물도록 해주는 메소드
	public void Set_Day(int year, int month) {
		iYear = year;
		iMonth = month;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, Menu.NONE, "동기화").setIcon(android.R.drawable.ic_menu_rotate);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case 0:
			

			try {
				connectWebSocket();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	        
			break;
			
		

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * WebSocket
	 * @throws JSONException 
	 */
	private void connectWebSocket() throws JSONException {

		
		//동기화 변수
		String LED_SET_pref; //2014-9 식으로 sharedpreferened의 저장소를 지정해준다.
		Map<String, ?> MapObject; // sharedpreferenced에 있는 내용을 저장한다.
		
		URI uri;
		SharedPreferences sharedPref;
		sharedPref = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);

		final String uemail = sharedPref.getString("user_email", null);

		//JSON & hashMap
		 final JSONObject Event_list_obj = new JSONObject(); //event list
		//
		 
	
		/**
		 * SharedPreferenced에 있는 내용을 JSON object로 변환하는 작업.
		 */
        for(int i = 0; i<12; i++){
        	JSONObject obj_single = new JSONObject();
        	LED_SET_pref = String.valueOf(iYear)+"-"+String.valueOf(i);
        	
        	sharedPref = getSharedPreferences(LED_SET_pref, 0);  //월별로 추가된 이벤트 내역을 추가한다.
        	MapObject = sharedPref.getAll(); //
   
            for (Map.Entry<String, ?> entry : MapObject.entrySet()) {	   
            	obj_single.put(entry.getKey(), entry.getValue().toString());
    	    } 
            Event_list_obj.put(iYear+"-"+i, obj_single.toString());
        }
        
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
				
				/**
				 * 현재 표시된 달의 LED setting 값을 저장한다.
				 */
				String LED_ALL = "[" ; 
				for(int i=1 ; i<36;i++)
				{
					try {
						jObject = new JSONObject(LED_SET.getString(String.valueOf(i), "{}"));
						LED_ALL += jObject.get("led");
						
						if(i%7 == 0 && i != 35)
						{
							LED_ALL += "]^[";
						}
						else if(i%8 < 8 && i != 35 )
						{
							LED_ALL +=",";
						}
					} catch (JSONException e) {
						LED_ALL += "[]";
					
						if(i%7 == 0 && i != 35)
						{
							LED_ALL += "]^[";
						}
						else if(i%7 != 0)
						{
							LED_ALL +=",";
						}
					}
				}
				
				LED_ALL += "]";
//				System.out.println("get ALL : "+LED_ALL);
				LED_FINAL_editor.putString(String.valueOf(iMonth), LED_ALL);
				LED_FINAL_editor.commit();
				/* LED 설정값 저장 완료.*/
				
				JSONObject Sync_Obj = new JSONObject();
				try {
					Sync_Obj.put("Event_list", Event_list_obj.toString());
					Sync_Obj.put("LED", LED_FINAL.getString(String.valueOf(iMonth), "[]"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
//				mWebSocketClient.send(LED_FINAL.getString(String.valueOf(iMonth), "led#[]"));

				mWebSocketClient.send("SYNC_save#"+Sync_Obj.toString());			
			}

			
			@Override
			public void onMessage(String s) {
				final String message = s;
				runOnUiThread(new Runnable() {
			
					@Override
					public void run() {
						
						SharedPreferences sharedPref;
						sharedPref = getSharedPreferences("MY_SHARED_PREF", 0);
						
						if(message.equals("ok"))
						{
							Toast.makeText(CTCalendarView.this, "동기화 완료", Toast.LENGTH_SHORT).show();
		
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putBoolean("Sync", true);
							editor.commit();
						}
						else
						{	
							Toast.makeText(CTCalendarView.this, "동기화 실패", Toast.LENGTH_SHORT).show();
							
							SharedPreferences.Editor editor = sharedPref.edit();
							editor.putBoolean("Sync", false);
							editor.commit();
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

	public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
		return (new CursorLoader(this, CalendarContract.Events.CONTENT_URI,
				PROJECTION, null, null, CalendarContract.Events.DTSTART));
	}

	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		adapter.swapCursor(cursor);
	}

	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		long time = 0;
		String formattedTime = null;

		switch (columnIndex) {
		case 2:
		case 3:
			time = cursor.getLong(columnIndex);
			Log.d("ghlab", "time>>" + time);
			formattedTime = DateUtils.formatDateTime(this, time, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);

			((TextView) view).setText(formattedTime);
			break;

		default:
			return (false);
		}

		return (true);
	}

	public void addSchedule() {

		Intent intent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI);

		// intent.putExtra(Events.TITLE, "모바일앱 톡데이!");

		// intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, 0);
		// intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, 0);

		Calendar cal = Calendar.getInstance();

		// cal.set(2011, 12 - 1, 21, 15, 0);
		// long startTime = cal.getTimeInMillis();

		// cal.set(2011, 12 - 1, 21, 16, 30);
		// long endTime = cal.getTimeInMillis();

		// intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
		// intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
		// intent.putExtra(Events.EVENT_LOCATION, "27층 8번방");
		// intent.putExtra(Events.DESCRIPTION, "모바일앱 톡데이 진행");
		// intent.putExtra(Intent.EXTRA_EMAIL, "김건호<gunho.kim@nhn.com>");
		// intent.putExtra(Events.ACCESS_LEVEL, Events.ACCESS_DEFAULT);
		// intent.putExtra(Events.AVAILABILITY, Events.AVAILABILITY_FREE);

		startActivity(intent);
	}
}