package com.example.memorycalendar3;

import java.util.ArrayList;

import com.example.fb_feedlist.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class BaseExpandableAdapter extends BaseExpandableListAdapter {
	public static final String TAG = "BaseExpandableAdapter";

   private ArrayList<String> groupList = null;
   private ArrayList<ArrayList<String>> childList = null;
   private LayoutInflater inflater = null;
   private ViewHolder viewHolder = null;
   ExpandableListView list;
   Context bcontext;

   boolean[] mGroupClickState;
   int lastExpandedGroupPosition = -1;

   public BaseExpandableAdapter(Context context, ArrayList<String> groupList,
         ArrayList<ArrayList<String>> childList, ExpandableListView mListView) {
      // TODO Auto-generated constructor stub
      super();
      bcontext = context;
      this.inflater = LayoutInflater.from(context);
      this.groupList = groupList;
      this.childList = childList;
      this.list = mListView;

      mGroupClickState = new boolean[groupList.size()];
      for (int i = 0; i < mGroupClickState.length; i++)
         mGroupClickState[i] = false;
   }

   class ViewHolder {
      public TextView tv_groupName;
      public ImageView tv_childName;
   }

   @Override
   public String getChild(int groupPosition, int childPosition) {
      // TODO Auto-generated method stub
      return childList.get(groupPosition).get(childPosition);
   }

   @Override
   public long getChildId(int groupPosition, int childPosition) {
      // TODO Auto-generated method stub
      return childPosition;
   }

   // 차일드뷰 각각의 ROW
   @Override
   public View getChildView(int groupPosition, int childPosition,
         boolean isLastChild, View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      View v = convertView;

      if (v == null) {
         viewHolder = new ViewHolder();
         v = inflater.inflate(R.layout.crawling_list_row_child, null);
         viewHolder.tv_childName = (ImageView) v.findViewById(R.id.crawling_img);
         v.setTag(viewHolder);
      } else {
         viewHolder = (ViewHolder) v.getTag();
      }

      // viewHolder.tv_childName.setText(getChild(groupPosition,
      // childPosition));

      Picasso.with(bcontext).load(getChild(groupPosition, childPosition))
            .transform(resizeTransformation).error(R.drawable.no_img3)
            .into(viewHolder.tv_childName);
      return v;
   }

   private com.squareup.picasso.Transformation resizeTransformation = new com.squareup.picasso.Transformation(){

      @Override
      public Bitmap transform(Bitmap source) {
    	 Log.d(TAG, "transform");
         float screenWidth = bcontext.getResources().getDisplayMetrics().widthPixels; // 화면사이즈
         float screenHeight = bcontext.getResources().getDisplayMetrics().heightPixels; // 화면사이즈

         int targetWidth;

         if (screenWidth > 720) {
            targetWidth = (int) screenWidth - 320;
         } else if (screenWidth < 720) {
            targetWidth = (int) screenWidth - 106;
         } else {
            targetWidth = (int) screenWidth - 160;
         }
         double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
         int targetHeight = (int) (targetWidth * aspectRatio);
         Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
         if (result != source) {
            // Same bitmap is returned if sizes are the same
            source.recycle();
         }
         return result;
      }

      @Override
      public String key() {
         return "resizeTransformation ";
      }
   };

   @Override
   public int getChildrenCount(int groupPosition) {
      // TODO Auto-generated method stub
      return childList.get(groupPosition).size();
   }

   // 그룹 포지션을 반환한다.
   @Override
   public String getGroup(int groupPosition) {
      // TODO Auto-generated method stub
      return groupList.get(groupPosition);
   }

   // 그룹 사이즈를 반환한다.
   @Override
   public int getGroupCount() {
      // TODO Auto-generated method stub
      return groupList.size();
   }

   // 그룹 ID를 반환한다.
   @Override
   public long getGroupId(int groupPosition) {
      // TODO Auto-generated method stub
      return groupPosition;
   }

   @Override
   public void onGroupExpanded(int groupPosition) {
      // TODO Auto-generated method stub
      super.onGroupExpanded(groupPosition);

      mGroupClickState[groupPosition] = !mGroupClickState[groupPosition];
      // 그룹을 클릭했을때 이전그룹이 열려 있으면 닫음
      if (lastExpandedGroupPosition != -1
            && lastExpandedGroupPosition != groupPosition) {
         list.collapseGroup(lastExpandedGroupPosition);
         mGroupClickState[lastExpandedGroupPosition] = false;
      }

      lastExpandedGroupPosition = groupPosition;
      super.onGroupExpanded(groupPosition);
   }

   // 그룹뷰 각각의 ROW
   @Override
   public View getGroupView(int groupPosition, boolean isExpanded,
         View convertView, ViewGroup parent) {
      // TODO Auto-generated method stub
      View v = convertView;

      if (v == null) {
         viewHolder = new ViewHolder();
         v = inflater.inflate(R.layout.crawling_list_row_group, parent, false);
         viewHolder.tv_groupName = (TextView) v.findViewById(R.id.tv_group);
         // viewHolder.iv_image = (ImageView) v.findViewById(R.id.iv_image);
         v.setTag(viewHolder);
      } else {
         viewHolder = (ViewHolder) v.getTag();
      }

      // 그룹을 펼칠때와 닫을때 아이콘을 변경해 준다.
      /*
       * if(isExpanded){ viewHolder.iv_image.setBackgroundColor(Color.GREEN);
       * }else{ viewHolder.iv_image.setBackgroundColor(Color.WHITE); }
       */

      viewHolder.tv_groupName.setText(getGroup(groupPosition));

      return v;
   }

   @Override
   public boolean hasStableIds() {
      // TODO Auto-generated method stub
      return true;
   }

   @Override
   public boolean isChildSelectable(int groupPosition, int childPosition) {
      // TODO Auto-generated method stub
      return true;
   }
}