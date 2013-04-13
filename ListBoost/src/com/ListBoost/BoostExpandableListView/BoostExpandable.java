package com.ListBoost.BoostExpandableListView;


import android.widget.ExpandableListAdapter;


public interface BoostExpandable extends ExpandableListAdapter, MultiLevelCheckable{

	public void notifyDataSetChanged();
}
