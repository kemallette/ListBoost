package com.kemallette.ListBoost.ExpandableList;


import android.widget.ExpandableListAdapter;


public interface BoostExpandable extends ExpandableListAdapter, MultiLevelCheckable{

	public void notifyDataSetChanged();
}
