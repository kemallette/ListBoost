package com.ListBoost.BoostListView;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;

import com.ListBoost.BoostListView.BoostListView.OnActionClickListener;


public class SimpleBoostCursorAdapter extends BoostCursorAdapter{

	private static final String	TAG	= "SimpleBoostCursorAdapter";


	public SimpleBoostCursorAdapter(Context context,
	                                int itemLayout,
	                                Cursor c,
	                                String[] from,
	                                int[] to,
	                                int flags,
	                                OnActionClickListener mActionListener,
	                                int toggleButtonId,
	                                int expandableViewId,
	                                int... expandableViewButtonIds){

		super(new SimpleCursorAdapter(context,
		                              itemLayout,
		                              c,
		                              from,
		                              to,
		                              flags),
		      context,
		      mActionListener,
		      toggleButtonId,
		      expandableViewId,
		      flags,
		      expandableViewButtonIds);

	}

}
