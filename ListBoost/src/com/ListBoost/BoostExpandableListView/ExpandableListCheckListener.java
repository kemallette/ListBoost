/**
 * 
 */
package com.ListBoost.BoostExpandableListView;


import android.widget.CompoundButton;


public interface ExpandableListCheckListener{

	public void onGroupCheckChange(CompoundButton checkBox,
	                               int groupPosition,
	                               long groupId,
	                               boolean isChecked);


	public void onChildCheckChange(CompoundButton checkBox,
	                               int groupPosition,
	                               int childPosition,
	                               long childId,
	                               boolean isChecked);

}
