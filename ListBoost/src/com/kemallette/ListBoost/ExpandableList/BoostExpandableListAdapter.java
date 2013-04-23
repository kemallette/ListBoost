package com.kemallette.ListBoost.ExpandableList;


import android.content.Context;
import android.widget.ExpandableListAdapter;


public class BoostExpandableListAdapter	extends
										BaseBoostExpandableAdapter{

	public BoostExpandableListAdapter(	Context context,
										BoostExpandableListView expandableListView,
										ExpandableListAdapter adapterToWrap){

		super(	context,
				expandableListView,
				adapterToWrap);
	}

}
