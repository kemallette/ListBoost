package com.kemallette.ListBoost.ExpandableList;


import android.widget.ExpandableListAdapter;


public interface BoostExpandableAdapter	extends
										ExpandableListAdapter{

	public static final String	ID	= "id",
									IS_CHECKED = "isChecked",
									IS_GROUP = "isGroup",
									GROUP_POSITION = "groupPosition",
									CHILD_POSITION = "childPosition";


	public void notifyDataSetChanged();

}
