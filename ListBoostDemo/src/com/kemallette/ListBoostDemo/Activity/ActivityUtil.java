package com.kemallette.ListBoostDemo.Activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


public class ActivityUtil{

	public interface Callbacks{

		public void enableSwipe();


		public void disableSwipe();


		public void enableSliding();


		public void disableSliding();


		public void enableMultiSelect();


		public void disableMultiSelect();


		public void enableDragSort();


		public void disableDragSort();
	}

	public static final int
								LIST_SLIDING	= 0,
												LIST_SWIPE = 2,
												LIST_MULTI_SELECT = 4,
												LIST_DRAG_SORT = 6,
												EXPANDABLE_SLIDING = 1,
												EXPANDABLE_SWIPE = 3,
												EXPANDABLE_MULTI_SELECT = 5,
												EXPANDABLE_DRAG_SORT = 7;

	public static final String	LIST_FEATURE	= "list_feature";


	public static int getListMode(Intent mActivityIntent){

		int listMode = -1;
		Bundle mExtras = mActivityIntent.getExtras();

		if (mExtras != null
			&& !mExtras.isEmpty())
			listMode = mExtras.getInt(LIST_FEATURE);

		return listMode;
	}


	public static void
		launchBoostDemoActivity(Activity mActivity, int listMode){

		Intent mIntent = new Intent();
		mIntent.setClass(	mActivity,
							BoostDemoActivity.class);
		mIntent.putExtra(	LIST_FEATURE,
							listMode);
		mActivity.startActivity(mIntent);

	}
}
