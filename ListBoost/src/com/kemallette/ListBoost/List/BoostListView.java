package com.kemallette.ListBoost.List;


import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.kemallette.ListBoost.R;
import com.kemallette.ListBoost.List.BaseBoostAdapter.ViewHolder;

public class BoostListView extends ListView{

	private static final String	TAG	= "BoostListView";

	class Holder{

		View	mSlideMenuToggle;


		Holder(){

		}
	}

	private boolean	     isSlidingEnabled	= false;

	private int slideToggleId;
	
	private BoostAdapter	mAdapter;


	public BoostListView(Context context){

		super(context);
	}


	public BoostListView(Context context, AttributeSet attrs){

		super(context,
		      attrs);
	}


	public BoostListView(Context context, AttributeSet attrs, int defStyle){

		super(context,
		      attrs,
		      defStyle);
	}


	public void setAdapter(BoostAdapter adapter){

		this.mAdapter = adapter;

		super.setAdapter(adapter);
	}


	public void setAdapter(BaseAdapter adapter){

		setAdapter(new BoostAdapter(adapter));
	}


	public void
	    enableSlidingMenus(OnSlidingMenuItemClickListener mSlidingMenuClickListener,
	                       int slideToggleId,
	                       int slidingLayoutId,
	                       int... slidingViewButtonIds){

		isSlidingEnabled = true;
		mAdapter.enableSlidingMenu(mSlidingMenuClickListener,
		                           slideToggleId,
		                           slidingLayoutId,
		                           slidingViewButtonIds);
		this.slideToggleId = slideToggleId;
		refreshVisibleItems();
	}


	public void
	    enableSlidingMenu(OnSlidingMenuItemClickListener mSlidingMenuClickListener,
	                      int... slidingViewButtonIds){

		enableSlidingMenus(mSlidingMenuClickListener,
		                   R.id.slide_toggle_button,
		                   R.id.sliding_view_id,
		                   slidingViewButtonIds);
	}


	public void disableSlidingMenus(){

		isSlidingEnabled = false;
		mAdapter.disableSlidingMenu();
		refreshVisibleItems();
	}


	/*
	 * Applies the user supplied color values to the visible list items. This
	 * does not touch the recycled off screen views.
	 */
	private void refreshVisibleItems(){

		View listItem;
		ViewHolder mHolder;

		// These are both implemented in AdapterView which means
		// they're flat list positions
		int firstVis = getFirstVisiblePosition();
		int lastVis = getLastVisiblePosition();

		/* This is the "conversion" from flat list positions to ViewGroup child positions */
		int count = lastVis - firstVis;

		while (count >= 0){ // looping through visible list items

			/*
			 * getChildAt(pos) is implemented in ViewGroup and has a different meaning for
			 * its position values. ViewGroup tracks visible items as children and is 0 indexed.
			 * This means you'll have 0 - X positions where X is however many items it takes
			 * to fill the visible area of your screen; usually less than 10.
			 */
			listItem = getChildAt(count);

			if (listItem != null){

				mHolder = (ViewHolder) listItem.getTag();
				if (mHolder == null){ // This shouldn't happen, but we'll make
					                  // sure in case some strange concurrency
					                  // bugs appear
					mHolder = ViewHolder.getInstance();
					mHolder.mSlideOpenToggle =
					                           (View) listItem.findViewById(slideToggleId);
					listItem.setTag(mHolder);
				}

				if (isSlidingEnabled){
					mHolder.mSlideOpenToggle.setVisibility(View.VISIBLE);
				}else
					mHolder.mSlideOpenToggle.setVisibility(View.INVISIBLE);
				mAdapter.setupSlidingMenu(listItem, firstVis + count);
			}
			count--;
		}
	}


	/**
	 * Collapses the currently open view.
	 * 
	 * @return true if a view was collapsed, false if there was no open view.
	 */
	public boolean collapse(){

		if (mAdapter != null)
			return mAdapter.collapseLastOpen();
		return false;
	}


	/**
	 * Registers a OnItemClickListener for this listview which overrides any already registered.
	 * When an item is clicked, it slides the menu open.
	 * 
	 * You can disable this at any time by setting your own onItemClickListener to your list.
	 * 
	 */
	public void enableOpenOnItemClick(){

		this.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> adapterView,
			                        View view,
			                        int i,
			                        long l){

				BoostAdapter adapter = (BoostAdapter) getAdapter();
				adapter.getExpandToggleButton(view)
				       .performClick();
			}
		});
	}


	@Override
	public Parcelable onSaveInstanceState(){

		if (mAdapter != null)
			return mAdapter.onSaveInstanceState(super.onSaveInstanceState());
		else
			return super.onSaveInstanceState();
	}


	@Override
	public void onRestoreInstanceState(Parcelable state){

		if (!(state instanceof BaseBoostAdapter.SavedState)){
			super.onRestoreInstanceState(state);
			return;
		}

		BaseBoostAdapter.SavedState ss = (BaseBoostAdapter.SavedState) state;
		super.onRestoreInstanceState(ss.getSuperState());

		if (mAdapter != null)
			mAdapter.onRestoreInstanceState(ss);
	}
}
