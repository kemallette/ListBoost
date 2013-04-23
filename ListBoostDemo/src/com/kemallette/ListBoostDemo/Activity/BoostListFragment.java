package com.kemallette.ListBoostDemo.Activity;


import java.util.ArrayList;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoost.List.BoostAdapter;
import com.kemallette.ListBoost.List.BoostListView;
import com.kemallette.ListBoost.List.OnSlidingMenuItemClickListener;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.ActivityUtil.Callbacks;
import com.kemallette.ListBoostDemo.Task.Tasks;
import com.kemallette.ListBoostDemo.Task.Tasks.Task;


public class BoostListFragment	extends
								SherlockFragment implements
												Callbacks,
												OnSlidingMenuItemClickListener{

	private final int[]					mSlidingViewButtonIds	= new int[] {	R.id.b1,
						R.id.b2,
						R.id.b3,
						R.id.b4								};

	private final ArrayList<Integer>	enabledFeatures			= new ArrayList<Integer>(4);

	private BoostListView				mList;
	private BoostAdapter				mAdapter;

	private ArrayAdapter<Task>			mAdapterToBeWrapped;


	public BoostListFragment(){

		super();
	}


	public static BoostListFragment newInstance(int listFeature){

		BoostListFragment mFrag = new BoostListFragment();

		Bundle mBundle = new Bundle();
		mBundle.putInt(	ActivityUtil.LIST_FEATURE,
						listFeature);
		mFrag.setArguments(mBundle);

		return mFrag;
	}


	@Override
	public View onCreateView(LayoutInflater inflater,
								ViewGroup container,
								Bundle savedInstanceState){

		return inflater.inflate(R.layout.boost_list_frag,
								container,
								false);
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){

		super.onViewCreated(view,
							savedInstanceState);

		init();

	}


	@Override
	public void onSlideItemClick(View itemView, View clickedView, int position){

		Toast.makeText(	getActivity(),
						((Button) clickedView).getText(),
						Toast.LENGTH_SHORT)
				.show();
	}


	private void init(){

		mList = (BoostListView) getView().findViewById(R.id.list);


		mAdapterToBeWrapped = new ArrayAdapter<Task>(
														getActivity(),
														R.layout.combined_list_item,
														android.R.id.text1,
														Tasks.TASKS);

		mAdapter = new BoostDemoAdapter(mAdapterToBeWrapped);

		mList.setAdapter(mAdapter);

		if (getArguments() != null
			&& !getArguments().isEmpty()){

			int listFeature = getArguments().getInt(ActivityUtil.LIST_FEATURE);

			switch(listFeature){
				case ActivityUtil.LIST_DRAG_SORT:
					enableDragSort();
					break;
				case ActivityUtil.LIST_MULTI_SELECT:
					enableMultiSelect();
					break;
				case ActivityUtil.LIST_SLIDING:
					enableSliding();
					break;
				case ActivityUtil.LIST_SWIPE:
					enableSwipe();
					break;

			}
		}

	}


	@Override
	public void enableSliding(){

		if (mAdapter != null
			&& !enabledFeatures.contains(ActivityUtil.LIST_SLIDING)){
			mAdapter.enableSlidingMenu(	this,
										mSlidingViewButtonIds);
			enabledFeatures.add(ActivityUtil.LIST_SLIDING);
		}
	}


	@Override
	public void disableSliding(){

		if (mAdapter != null){
			mAdapter.disableSlidingMenu();
			enabledFeatures.remove(ActivityUtil.LIST_SLIDING);
		}
	}


	@Override
	public void enableSwipe(){

		if (mAdapter != null
			&& !enabledFeatures.contains(ActivityUtil.LIST_SWIPE)){
			mAdapter.enableSwipeToReveal();
			enabledFeatures.add(ActivityUtil.LIST_SWIPE);
		}

	}


	@Override
	public void disableSwipe(){

		if (mAdapter != null){
			mAdapter.disableSwipeToReveal();
			enabledFeatures.remove(ActivityUtil.LIST_SWIPE);
		}
	}


	@Override
	public void enableMultiSelect(){


		// TODO
		if (mAdapter != null)
			enabledFeatures.add(ActivityUtil.LIST_MULTI_SELECT);
	}


	@Override
	public void disableMultiSelect(){


		// TODO
		if (mAdapter != null)
			enabledFeatures.remove(ActivityUtil.LIST_MULTI_SELECT);

	}


	@Override
	public void enableDragSort(){

		// TODO

	}


	@Override
	public void disableDragSort(){

		// TODO

	}

	/**
	 * This class overrides BoostAdapter getView in order to set visibility for
	 * certain list items depending on enabled list features. For example, if
	 * sliding context menu is not enabled, we make the slide toggle button
	 * {@link View}.INVISIBLE
	 * 
	 * @author p00n
	 */
	private class BoostDemoAdapter	extends
									BoostAdapter{

		private class Holder{

			CheckBox	mBox;
			Button		mSlideToggle;
			TextView	mText;
		}

		private Holder	mHolder;


		public BoostDemoAdapter(BaseAdapter wrapped){

			super(wrapped);
		}


		@Override
		public View
			getView(int position, View convertView, ViewGroup viewGroup){

			convertView = super.getView(position,
										convertView,
										viewGroup);

			if (convertView != null){

				mHolder = (Holder) convertView.getTag();
				if (mHolder == null){

					mHolder = new Holder();

					mHolder.mBox = (CheckBox) convertView.findViewById(android.R.id.checkbox);
					mHolder.mSlideToggle = (Button) convertView.findViewById(R.id.slide_toggle_button);
					mHolder.mText = (TextView) convertView.findViewById(android.R.id.text1);
				}

				mHolder.mText.setText(Html.fromHtml(getItem(position).toString()));

				if (enabledFeatures.contains(ActivityUtil.LIST_MULTI_SELECT))
					mHolder.mBox.setVisibility(View.VISIBLE);
				else
					mHolder.mBox.setVisibility(View.INVISIBLE);


				if (enabledFeatures.contains(ActivityUtil.LIST_SLIDING))
					mHolder.mSlideToggle.setVisibility(View.VISIBLE);
				else
					mHolder.mSlideToggle.setVisibility(View.INVISIBLE);
			}
			return convertView;
		}
	}

}
