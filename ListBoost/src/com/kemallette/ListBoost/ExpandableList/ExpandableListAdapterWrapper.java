package com.kemallette.ListBoost.ExpandableList;


import android.widget.ExpandableListAdapter;


public interface ExpandableListAdapterWrapper	extends
											ExpandableListAdapter{

	public ExpandableListAdapter getWrappedAdapter();


}
