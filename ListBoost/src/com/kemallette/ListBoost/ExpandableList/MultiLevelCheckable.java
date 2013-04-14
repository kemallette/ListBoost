package com.kemallette.ListBoost.ExpandableList;


public interface MultiLevelCheckable extends
									ExpandableListCheckListener{


	/**
	 * Gives a count of all the checked groups in the list
	 * 
	 * @return the total number of checked groups
	 */
	public int getCheckedGroupCount();


	/**
	 * Gives a count of all children in the list regardless of parent group.
	 * 
	 * @return total number of checked children in the list
	 */
	public int getCheckedChildCount();


	/**
	 * Gives a count of children checked for the group at groupPosition
	 * 
	 * @param groupPosition
	 * @return the number of checked children for group at groupPosition
	 */
	public int getCheckedChildCount(int groupPosition);
}
