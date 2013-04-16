package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.kemallette.ListBoostDemo.R;
import com.kemallette.ListBoostDemo.Activity.ExamplesListFrag.Callbacks;


public class ExamplesListActivity	extends
									SherlockFragmentActivity implements
															Callbacks{

	private static final int
								LIST_SLIDING	= 1,
												LIST_SWIPE = 2,
												LIST_MULTI_SELECT = 3,
												LIST_DRAG_SORT = 4,
												EXPANDABLE_SLIDING = 5,
												EXPANDABLE_SWIPE = 6,
												EXPANDABLE_MULTI_SELECT = 7,
												EXPANDABLE_DRAG_SORT = 8;


	private SherlockFragment	mListFrag;
	private SherlockFragment	mExpandableListFrag;


	@Override
	protected void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.examples_list_activity);

	}


	@Override
	public void onItemSelected(int position){

		Bundle mBundle = new Bundle();
		mBundle.putInt(	"list_feature",
						position);

		switch(position){
			case LIST_SLIDING:
			case LIST_SWIPE:
			case LIST_MULTI_SELECT:
			case LIST_DRAG_SORT:
				showListFrag(mBundle);
				break;

			case EXPANDABLE_SLIDING:
			case EXPANDABLE_SWIPE:
			case EXPANDABLE_MULTI_SELECT:
			case EXPANDABLE_DRAG_SORT:
				showExpandableListFrag(mBundle);
				break;
		}
	}


	private void showExpandableListFrag(Bundle mBundle){

		FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		if (mExpandableListFrag == null){
			mExpandableListFrag = ExamplesExpandableListFrag.newInstance();
			mTransaction.add(	R.id.container,
								mExpandableListFrag);
		}else
			mTransaction.replace(	R.id.container,
									mExpandableListFrag);

		mTransaction.commit();
	}


	private void showListFrag(Bundle mBundle){


		FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();

		if (mListFrag == null){
			mListFrag = ExamplesListFrag.newInstance();
			mTransaction.add(	R.id.container,
								mListFrag);
		}else
			mTransaction.replace(	R.id.container,
									mListFrag);

		mTransaction.commit();
	}
}
