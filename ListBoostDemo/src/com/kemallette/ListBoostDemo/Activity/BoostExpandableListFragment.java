package com.kemallette.ListBoostDemo.Activity;


import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragment;
import com.kemallette.ListBoostDemo.Activity.ActivityUtil.Callbacks;


public class BoostExpandableListFragment extends
										SherlockFragment implements
														Callbacks{


	public static BoostExpandableListFragment newInstance(int listFeature){

		BoostExpandableListFragment mFrag = new BoostExpandableListFragment();

		Bundle mBundle = new Bundle();
		mBundle.putInt(	ActivityUtil.LIST_FEATURE,
						listFeature);
		mFrag.setArguments(mBundle);

		return mFrag;
	}


	@Override
	public void enableSwipe(){

		// TODO Auto-generated method stub

	}


	@Override
	public void disableSwipe(){

		// TODO Auto-generated method stub

	}


	@Override
	public void enableSliding(){

		// TODO Auto-generated method stub

	}


	@Override
	public void disableSliding(){

		// TODO Auto-generated method stub

	}


	@Override
	public void enableMultiSelect(){

		// TODO Auto-generated method stub

	}


	@Override
	public void disableMultiSelect(){

		// TODO Auto-generated method stub

	}


	@Override
	public void enableDragSort(){

		// TODO Auto-generated method stub

	}


	@Override
	public void disableDragSort(){

		// TODO Auto-generated method stub

	}


}
