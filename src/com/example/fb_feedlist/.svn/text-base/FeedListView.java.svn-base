package com.example.fb_feedlist;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class FeedListView extends ListView{
	public static final String TAG = null;
	/**
	 * DataAdapter for this instance
	 */
	private FeedListAdapter adapter;

	/**
	 * Listener for data selection
	 */
	private OnDataSelectionListener selectionListener;

	public FeedListView(Context context) {
		super(context);

		init();
	}

	public FeedListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		init();
	}

	/**
	 * set initial properties
	 */
	private void init() {
		Log.d(TAG, "asd");
        setOnItemClickListener(new OnItemClickAdapter());
	}

	/**
	 * set DataAdapter
	 *
	 * @param adapter
	 */
	public void setAdapter(BaseAdapter adapter) {
		super.setAdapter(adapter);

	}

	/**
	 * get DataAdapter
	 *
	 * @return
	 */
	public BaseAdapter getAdapter() {
		return (BaseAdapter)super.getAdapter();
	}

	/**
	 * set OnDataSelectionListener
	 *
	 * @param listener
	 */
	public void setOnDataSelectionListener(OnDataSelectionListener listener) {
		this.selectionListener = listener;
		Log.d(TAG, "zxc");
	}

	/**
	 * get OnDataSelectionListener
	 *
	 * @return
	 */
	public OnDataSelectionListener getOnDataSelectionListener() {
		Log.d(TAG, "789");
		return selectionListener;
	}

	class OnItemClickAdapter implements OnItemClickListener {

		public OnItemClickAdapter() {
			Log.d(TAG, "456");
		}

		public void onItemClick(AdapterView parent, View v, int position, long id) {
			if (selectionListener == null) {
				return;
			}
			Log.d(TAG, "123");
			// call the OnDataSelectionListener method
			selectionListener.onDataSelected(parent, v, position, id);

		}

	}

}
