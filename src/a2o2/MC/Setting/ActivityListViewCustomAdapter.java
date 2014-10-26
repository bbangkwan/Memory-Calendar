package a2o2.MC.Setting;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fb_feedlist.R;

/**
 * 아이템 클릭 예제에 사용할 리스트뷰 커스텀 어댑터
 * ArrayAdapter를 확장했다.
 * Generic 으로 Data 클래스를 사용했다.
 */
public class ActivityListViewCustomAdapter extends ArrayAdapter<Data>
{
	private Context mContext;
	private int mLayoutResource;
	private ArrayList<Data> mList;
	
	private LayoutInflater mInflater;
	public Object mLvData;
	

	
	/**
	 * ListViewCustomAdapter 생성자
	 * 
	 * @param context 컨텍스트
	 * @param rowLayoutResource 레이아웃리소스(일반적으로는 텍스트뷰 리소스:리스트는 기본적으로 텍스트를 뿌리도록 되어있다.)
	 * @param objects 입력된 리스트
	 */
	public ActivityListViewCustomAdapter(Context context, int rowLayoutResource, ArrayList<Data> objects)
	{
		super(context, rowLayoutResource, objects);
		this.mContext = context;
		this.mLayoutResource = rowLayoutResource;
		this.mList = objects;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * ListView에 사용되는 아이템의 숫자를 반환한다.
	 */
	@Override
	public int getCount()
	{
		return mList.size();
	}

	/**
	 * position(index)를 통해 리스트에서 해당 position의 item을 반환한다.
	 */
	@Override
	public Data getItem(int position)
	{
		return mList.get(position);
	}

	/**
	 * item(여기서는 Data)를 통해 리스트에서 해당 item의 position을 반환한다.
	 */
	@Override
	public int getPosition(Data item)
	{
		return mList.indexOf(item);
	}

	/**
	 * 해당 position의 뷰를 만들어 반환한다.
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		// convertView(View)가 없는 경우 inflater로 뷰 객체를 생성한다.
		if(convertView == null)
		{
			convertView = mInflater.inflate(mLayoutResource, null);
		}
		
		// 리스트에서 포지션에 맞는 데이터를 받아온다.
		Data data = getItem(position);
		
		// 데이터가 있는 경우에만 처리한다.
		if(data != null)
		{
			// 어댑터에서의 findViewById() 메소드는 convertView 객체를 통해서 사용한다.
			
			// convertView를 통해 위젯을 레퍼런스 한다.
			ImageView ivColor = (ImageView)convertView.findViewById(R.id.item_click_example_row_iv_color);
			TextView tvTitle = (TextView)convertView.findViewById(R.id.item_click_example_row_tv_title);
			
			
			/**
			 * listView의 각 row에 원을 그려준다.
			 */
			 // Paint 생성 & 속성 지정
			Bitmap pallet = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Paint pnt = new Paint();
			Canvas canvas = new Canvas(pallet);
			// Paint 속성 변경
            pnt.setStyle(Style.FILL);
            pnt.setAntiAlias(true);
            pnt.setFilterBitmap(true);
            pnt.setDither(true);
            pnt.setColor(data.getColor());
         
            canvas.drawCircle(30, 35, 25, pnt);
			
			
			
			if(position == (getCount()-1))// 마지막 항목은 도형이 없어야 되므로 null을 입력해준다.
			{
				
				//추가 이미지를 넣어줘야 한다.
//				LayoutParams params = (LayoutParams) ivColor.getLayoutParams();
//				params.height = 50;
//				params.width = 50;
//				ivColor.setLayoutParams(params);
				ivColor.setImageResource(R.drawable.add);
				
				System.out.println("추가항목");
		
//				//텍스트를 수정해준다.
//				tvTitle.setTextColor(Color.RED);
//				tvTitle.setTextSize(20);
//				tvDescription.setVisibility(View.GONE);	
//				tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
			}
			else
			{
	            ivColor.setImageBitmap(pallet);
			}
			
			// 타이틀을 세팅한다.
			tvTitle.setText(data.getTitle());
			
		}
		
		// 만들어진 뷰(어댑터를 통해 아이템을 사용자에게 보여지도록 바꾼)를 반환한다.
		return convertView;
	}

}
