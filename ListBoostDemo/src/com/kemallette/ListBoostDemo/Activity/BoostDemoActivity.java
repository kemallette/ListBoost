package com.kemallette.ListBoostDemo.Activity;


import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.EXPANDABLE_DRAG_SORT;
import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.EXPANDABLE_MULTI_SELECT;
import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.EXPANDABLE_SLIDING;
import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.EXPANDABLE_SWIPE;
import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.LIST_DRAG_SORT;
import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.LIST_MULTI_SELECT;
import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.LIST_SLIDING;
import static com.kemallette.ListBoostDemo.Activity.ActivityUtil.LIST_SWIPE;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.ActivityUtil.Callbacks;

public class BoostDemoActivity	extends
								SherlockFragmentActivity{

	private int					listMode;

	private SherlockFragment	mListFrag;
	private SherlockFragment	mExpandableListFrag;


	@Override
	protected void onCreate(Bundle arg0){

		super.onCreate(arg0);
		setContentView(R.layout.examples_list_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		listMode = ActivityUtil.getListMode(getIntent());

		initFrag();

		switch(listMode){
			case EXPANDABLE_DRAG_SORT:
				((Callbacks) mExpandableListFrag).enableDragSort();
				break;
			case EXPANDABLE_MULTI_SELECT:
				((Callbacks) mExpandableListFrag).enableMultiSelect();
				break;
			case EXPANDABLE_SLIDING:
				((Callbacks) mExpandableListFrag).enableSliding();
				break;
			case EXPANDABLE_SWIPE:
				((Callbacks) mExpandableListFrag).enableSwipe();
				break;
			case LIST_DRAG_SORT:
				((Callbacks) mListFrag).enableDragSort();
				break;
			case LIST_MULTI_SELECT:
				((Callbacks) mListFrag).enableMultiSelect();
				break;
			case LIST_SLIDING:
				((Callbacks) mListFrag).enableSliding();
				break;
			case LIST_SWIPE:
				((Callbacks) mListFrag).enableSwipe();
				break;
		}
	}


	private void initFrag(){

		FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		switch(listMode){
			case EXPANDABLE_DRAG_SORT:
			case EXPANDABLE_MULTI_SELECT:
			case EXPANDABLE_SLIDING:
			case EXPANDABLE_SWIPE:
				if (mExpandableListFrag == null){
					mExpandableListFrag = BoostExpandableListFragment.newInstance();
					mTransaction.add(	R.id.container,
										mExpandableListFrag);
				}
				break;
			case LIST_DRAG_SORT:
			case LIST_MULTI_SELECT:
			case LIST_SLIDING:
			case LIST_SWIPE:
				if (mListFrag == null){
					mListFrag = ExamplesListFrag.newInstance();
					mTransaction.add(	R.id.container,
										mListFrag);
					mTransaction.commit();
				}
				break;
		}
		mTransaction.commit();

	}

}
