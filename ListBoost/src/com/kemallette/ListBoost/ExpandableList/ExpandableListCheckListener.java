/**
 * Interface definition for a callback to be invoked when the checked state of
 * an item has changed.
 */
package com.kemallette.ListBoost.ExpandableList;


import android.widget.Checkable;

public interface ExpandableListCheckListener{

	/**
	 * Called when the checked state of a group item has changed.
	 * 
	 * @param checkedView
	 *            - The checkable view that has changed checked state
	 * @param groupPosition
	 *            - The group item position in the list
	 * @param groupId
	 *            - The id of the group item
	 * @param isChecked
	 *            - Indicates the state of the item - true if checked, false if
	 *            not.
	 */
	public void onGroupCheckChange(Checkable checkedView, int groupPosition,
									long groupId, boolean isChecked);


	/**
	 * Called when the checked state of a child item has changed.
	 * 
	 * @param checkedView
	 *            - The checkable view that has changed checked state
	 * @param groupId
	 *            - The id of the group item
	 * @param groupPosition
	 *            - The child's parent group position in the list
	 * @param childPosition
	 *            - The child's position within its parent group
	 * @param childId
	 *            - The id of the child item
	 * @param isChecked
	 *            - Indicates the state of the item - true if checked, false if
	 *            not.
	 */
	public void onChildCheckChange(Checkable checkedView,
									int groupPosition,
									long groupId,
									int childPosition,
									long childId,
									boolean isChecked);

}
