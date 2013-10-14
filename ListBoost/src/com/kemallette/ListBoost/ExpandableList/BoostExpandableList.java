package com.kemallette.ListBoost.ExpandableList;

import java.util.List;

import android.util.SparseArray;
import android.widget.AbsListView;


/**
 * Defines the basic API for a {@link BoostExpandableListView}.
 * 
 * @author kemallette
 * 
 */
public interface BoostExpandableList {

	/**
	 * For group choice mode:
	 * No groups may be checked and the
	 * checkable view will be hidden or disabled. As such, even if
	 * checkChildrenWithGroup is true, it will not function.
	 * 
	 * For child choice mode:
	 * No children may be checked and the checkable view will be hidden or
	 * disabled.
	 * 
	 * Equals the same value as {@link AbsListView#CHOICE_MODE_NONE} so
	 * they can be used interchangeably.
	 */
	public static final int	CHECK_MODE_NONE					= AbsListView.CHOICE_MODE_NONE;

	/**
	 * If applied to group choice mode, zero to all groups can be checked.
	 * 
	 * If applied to child choice mode, zero to all children from any group can
	 * be checked.
	 * 
	 * Equals the same value as {@link AbsListView#CHOICE_MODE_MULTIPLE} so
	 * they can be used interchangeably.
	 */
	public static final int	CHECK_MODE_MULTI				= AbsListView.CHOICE_MODE_MULTIPLE;

	/**
	 * If applied to group choice mode, only one group in the list can be
	 * checked at a time.
	 * 
	 * If applied to child choice mode, only one child (regardless of group) in
	 * the list can be checked at a time.
	 * 
	 * Equals the same value as {@link AbsListView#CHOICE_MODE_SINGLE} so
	 * they can be used interchangeably.
	 */
	public static final int	CHECK_MODE_ONE					= AbsListView.CHOICE_MODE_SINGLE;

	/**
	 * Only one child item per group can be checked at a time. You cannot use
	 * this and set checkChildrenWithGroup to true.
	 */
	public static final int	CHILD_CHECK_MODE_ONE_PER_GROUP	= 14;

	/**
	 * Group and child choice modes are automatically set to this if one item
	 * choice only is enabled. Users should normally never have to set this
	 * directly.
	 */
	public static final int	CHECK_MODE_ONE_ALL				= 15;

	
	
	
	public BoostExpandableListAdapter getBoostAdapter();
	
	
	/*********************************************************************
	 *  
	 *  MultiChoice
	 *  
	 **********************************************************************/
	
	public BoostExpandableList
		enableChoice(int groupChoiceMode, int childChoiceMode);


	public BoostExpandableList disableChoice();
	

	/**
	 * Enables one item choice mode. Only one item at a time throughout the list
	 * (including groups and children) can be checked. isChoiceOn() will return
	 * true.
	 * 
	 * If checkChildrenWithGroup is enabled, it will be disabled until
	 * checkChildrenWithGroup is set to true again.
	 */
	public BoostExpandableList enableOnlyOneItemChoice();


	/**
	 * Disables one item choice mode. isChoiceOn() will return false until
	 * either enableChoice(int groupChoiceMode, int childChoiceMode) or
	 * enableOneItemChoice() are called.
	 */
	public BoostExpandableList disableOnlyOneItemChoice();

	public boolean isOneItemChoiceOn();

	public boolean isChoiceOn();

	/*********************************************************************
	 * Checked ID Getter
	 **********************************************************************/
	/**
	 * 
	 * 
	 * @return an array of all checked group ids
	 */
	public long[] getCheckedGroupIds();


	/**
	 * Use this if you want ids for all checked children in the entire list,
	 * regardless of which group they're in.
	 * 
	 * @return
	 */
	public List<Long> getCheckedChildIds();


	/**
	 * Creates a List<Long> of checked children for the group at groupPosition.
	 * 
	 * @param groupPosition
	 *            - group position where the checked children fall under
	 * @return -
	 */
	public List<Long> getCheckedChildIds(int groupPosition);


	/*********************************************************************
	 * Checked Position Getters
	 **********************************************************************/
	/**
	 * 
	 * if !stableIds, cannot ensure these aren't stale
	 * 
	 * @return
	 */
	public int[] getCheckedGroupPositions();


	/**
	 * 
	 * if !stableIds, cannot ensure these aren't stale
	 * 
	 * @return
	 */
	public SparseArray<int[]> getCheckedChildPositions();


	/**
	 * 
	 * if !stableIds, cannot ensure these aren't stale
	 * 
	 * @return
	 */
	public int[] getCheckedChildPositions(int groupPosition);


	/*********************************************************************
	 * Check State Setters
	 **********************************************************************/
	public BoostExpandableList
		setGroupCheckedState(int groupPosition,
								boolean isChecked);


	public BoostExpandableList
		setChildCheckedState(int groupPosition,
								int childPosition,
								boolean isChecked);


	/*********************************************************************
	 * Check State
	 **********************************************************************/
	public boolean isGroupChecked(int groupPosition);


	public boolean isChildChecked(int groupPosition, int childPosition);


	/*********************************************************************
	 * Choice Mode
	 **********************************************************************/

	public int getGroupChoiceMode();


	public int getChildChoiceMode();


	public BoostExpandableList setGroupChoiceMode(int choiceMode);


	public BoostExpandableList setChildChoiceMode(int choiceMode);


	/**
	 * If true, on a group check change, that group's children will match
	 * the group's check state. In other words, if you check a group, all its
	 * children will also be checked and vice versa. If a group is unchecked,
	 * all its children will be unchecked.
	 * 
	 * @param checkChildrenWithGroup
	 *            - true to enable, false to disable
	 * @return
	 */
	public BoostExpandableList
		checkChildrenWithGroup(boolean checkChildrenWithGroup);


	public boolean checkChildrenWithGroup();


	/***********************************************************
	 * Checked Item Counts
	 ************************************************************/

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


	/*********************************************************************
	 * Clearing
	 **********************************************************************/
	public BoostExpandableList clearAll();


	public BoostExpandableList clearGroups();


	public BoostExpandableList clearChildren();


	public BoostExpandableList
		clearChildren(int groupPosition);


	/*********************************************************************
	 * MultiCheck Listener
	 **********************************************************************/

	public BoostExpandableList
		setExpandableCheckListener(ExpandableListCheckListener mListener);


	public ExpandableListCheckListener getExpandableListCheckListener();


	public BoostExpandableList
		removeExpandableCheckListener();
	
}
