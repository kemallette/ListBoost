package com.kemallette.ListBoost.ExpandableList;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import android.content.Context;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;


public class BoostExpandableListView extends
									ExpandableListView	implements
														ExpandableListCheckListener,
														MultiLevelCheckable{


	private static final String						TAG								=
																						"BoostExpandableListView";

	public static final int							CHECK_MODE_NONE					=
																						10;
	public static final int							CHECK_MODE_MULTI				=
																						12;

	public static final int							GROUP_CHECK_MODE_ONE			=
																						11;
	/**
	 * Only one child out of the entire list can be checked at one time. You
	 * cannot use this and set checkChildrenOnGroupCheck true
	 */
	public static final int							CHILD_CHECK_MODE_ONE			=
																						13;
	/**
	 * Only one child item per group can be checked at a time. You cannot use
	 * this and set checkChidrenOnGroupCheck true.
	 */
	public static final int							CHILD_CHECK_MODE_ONE_PER_GROUP	=
																						14;

	public static final int							GROUP_INDICATOR_LEFT			= 0,
		GROUP_INDICATOR_RIGHT = 1, GROUP_INDICATOR_NONE = 2;

	private boolean									checkChildrenOnGroupCheck		=
																						false;


	private int										groupCheckMode					=
																						CHECK_MODE_NONE;
	private int										childCheckMode					=
																						CHECK_MODE_NONE;

	private int										groupCheckTotal,
													childCheckTotal;

	private int										groupIndicatorPosition			=
																						GROUP_INDICATOR_LEFT;

	private ArrayList<ExpandableListCheckListener>	mCheckListeners;


	/**
	 * 
	 */
	private BitSet									mCheckedGroups;
	private HashMap<Integer, BitSet>				mCheckedChildren;

	private BoostExpandableAdapter					mAdapter;


	// TODO: test for group/child count changes and make sure bitsets are
	// long enough to deal with them

	public BoostExpandableListView(	Context context,
									AttributeSet attrs,
									int defStyle){

		super(	context,
				attrs,
				defStyle);

		// TODO: set check modes and checkChildrenOnGroupCheck from xml attrs

	}


	public BoostExpandableListView(	Context context,
									AttributeSet attrs){

		this(	context,
				attrs,
				-1);
	}


	public BoostExpandableListView(Context context){

		this(	context,
				null);
	}


	@Override
	public void onRestoreInstanceState(Parcelable state){

		super.onRestoreInstanceState(state);

		// TODO: restore necessary saved fields
	}


	@Override
	public Parcelable onSaveInstanceState(){

		return super.onSaveInstanceState();

		// TODO: save all necessary fields
	}


	@Override
	public void onGlobalLayout(){

		super.onGlobalLayout();

		// TODO: Implement group indicator on right/left side or GONE
	}


	@Override
	public void setAdapter(ExpandableListAdapter adapter){

		super.setAdapter(adapter);

		Log.e(	TAG,
				"Need to use an adapter that implements BoostExpadable");
	}


	public void setAdapter(SimpleBoostExpandableAdapter adapter){

		super.setAdapter(adapter);

		mAdapter = adapter;

		((BaseBoostExpandableAdapter) mAdapter).setExpandableCheckListener(this);
	}


	public void setAdapter(SimpleBoostCursorTreeAdapter adapter){

		super.setAdapter(adapter);

		mAdapter = adapter;

		((BaseBoostCursorTreeAdapter) mAdapter).setExpandableCheckListener(this);
	}


	private void validateCheckedGroups(){

		if (mCheckedGroups == null){

			mCheckedGroups = new BitSet(mAdapter.getGroupCount());
			return;

		}else if (mCheckedGroups.length() < mAdapter.getGroupCount()){

			BitSet newBitSet = new BitSet(mAdapter.getGroupCount());

			copyBitSet(	mCheckedGroups,
						newBitSet);
			mCheckedGroups = newBitSet;
		}


	}


	private void validateCheckState(){

		validateCheckedChildren();

		int i = 0;
		for (BitSet mChildren : mCheckedChildren.values()){
			if (mChildren.cardinality() == mChildren.length())
				setGroupChoice(	i,
								true,
								true);
			else if (mChildren.isEmpty())
				setGroupChoice(	i,
								false,
								true);
			i++;
		}
	}


	private void validateCheckedChildren(int groupPosition){

		if (mCheckedChildren == null){
			mCheckedChildren =
								new HashMap<Integer, BitSet>(mAdapter.getGroupCount());
			return;
		}

		BitSet mChildren = mCheckedChildren.get(groupPosition);

		if (mChildren == null){
			mChildren = new BitSet(mAdapter.getChildrenCount(groupPosition));
			return;
		}

		if (mChildren.length() < mAdapter.getChildrenCount(groupPosition)){
			BitSet copyTo =
							new BitSet(mAdapter.getChildrenCount(groupPosition));
			copyBitSet(	mChildren,
						copyTo);
			mCheckedChildren.put(	groupPosition,
									copyTo);
		}

	}


	private void validateCheckedChildren(){

		if (mCheckedChildren == null){
			mCheckedChildren =
								new HashMap<Integer, BitSet>(mAdapter.getGroupCount());
			return;
		}

		for (int i : mCheckedChildren.keySet())
			validateCheckedChildren(i);
	}


	private BitSet copyBitSet(BitSet copyFrom, BitSet copyTo){

		int i = copyFrom.nextSetBit(0);
		while (i > -1){

			copyTo.set(	i,
						copyFrom.get(i));

			i = copyFrom.nextSetBit(i + 1);
		}

		return copyTo;
	}


	private void refresh(){

		if (mAdapter != null){
			mAdapter.notifyDataSetChanged();
			postInvalidate();
		}
	}


	public int getGroupIndicatorPosition(){

		return groupIndicatorPosition;
	}


	public BoostExpandableListView
		setGroupIndicatorPosition(int groupIndicatorPosition){

		if (groupIndicatorPosition > GROUP_INDICATOR_NONE
			|| groupIndicatorPosition < GROUP_INDICATOR_LEFT)
			Log.e(	TAG,
					"setGroupIndicatorPosition(groupIndicatorPosition) failed. groupIndicatorPosition: "
						+ groupIndicatorPosition
						+ " is not valid.");

		this.groupIndicatorPosition = groupIndicatorPosition;

		return this;
	}


	public int getGroupCheckMode(){

		return groupCheckMode;
	}


	public BoostExpandableListView setGroupCheckMode(int groupCheckMode){

		this.groupCheckMode = groupCheckMode;

		if (groupCheckMode != CHECK_MODE_NONE
			&& mCheckedGroups == null)
			mCheckedGroups = new BitSet(mAdapter.getGroupCount());

		return this;
	}


	public int getChildCheckMode(){

		return childCheckMode;
	}


	public BoostExpandableListView setChildCheckMode(int childCheckMode){

		this.childCheckMode = childCheckMode;

		if (childCheckMode != CHECK_MODE_NONE
			&& mCheckedChildren == null)
			mCheckedChildren = new HashMap<Integer, BitSet>();

		return this;
	}


	/**
	 * Gives a count of ALL checked items in the list (groups and children
	 * both).
	 * 
	 * @return total checked items
	 */
	@Override
	public int getCheckedItemCount(){


		return getCheckedGroupCount()
				+ getCheckedChildCount();
	}


	/**
	 * Gives a count of checked groups in the list
	 * 
	 * @return the number of checked groups
	 */
	@Override
	public int getCheckedGroupCount(){

		if (mCheckedGroups != null){
			validateCheckedGroups();
			groupCheckTotal = mCheckedGroups.cardinality();
		}else
			groupCheckTotal = 0;

		return groupCheckTotal;
	}


	/**
	 * Gives a count of ALL checked children in the list regardless of group
	 * parent.
	 * 
	 * @return total checked children
	 */
	@Override
	public int getCheckedChildCount(){

		childCheckTotal = 0;

		validateCheckedChildren();

		if (mCheckedChildren != null)
			for (BitSet mChildren : mCheckedChildren.values())
				childCheckTotal += mChildren.cardinality();

		return childCheckTotal;
	}


	/**
	 * Gives a count of all the checked children for the parent group at a
	 * specified group at groupPosition.
	 * 
	 * @param groupPosition
	 *            - position in the list of the group you want to gather the
	 *            checked child count from
	 * @return total checked child count for the specified group at
	 *         groupPosition
	 */
	@Override
	public int getCheckedChildCount(int groupPosition){

		if (mCheckedChildren != null){

			validateCheckedChildren(groupPosition);

			BitSet mChildren = mCheckedChildren.get(groupPosition);
			if (mChildren != null)
				return mChildren.cardinality();
			else
				return -1;

		}else
			return -1;

	}


	/**
	 * Clears all checked items in the list and resets the all checked counts.
	 */
	@Override
	public void clearChoices(){

		if (mCheckedChildren != null)
			mCheckedChildren.clear();

		if (mCheckedGroups != null)
			mCheckedGroups.clear();

		groupCheckTotal = 0;
		childCheckTotal = 0;

		refresh();
	}


	/**
	 * Clears all checked groups in the list.
	 * 
	 * Caution: if you set checkChildrenOnGroupCheck to true, all checked
	 * children under the checked parent groups will be cleared as well.
	 */
	public void clearGroupChoices(){

		validateCheckedGroups();

		int gPos = mCheckedGroups.nextSetBit(0);

		while (mCheckedGroups.nextSetBit(gPos) > -1){

			if (checkChildrenOnGroupCheck)
				clearGroupChildChoices(gPos);

			gPos = mCheckedGroups.nextSetBit(gPos + 1);
		}

		mCheckedGroups.clear();

		groupCheckTotal = 0;

		refresh();
	}


	/**
	 * Clears all checked children in the list.
	 * 
	 * Caution: if you set checkChildrenOnGroupCheck to true, groups with all
	 * children checked will also be cleared.
	 */
	public void clearChildChoices(){

		validateCheckedChildren();

		for (BitSet mChildrenSet : mCheckedChildren.values())
			mChildrenSet.clear();

		childCheckTotal = 0;

		validateCheckState();
	}


	/**
	 * Clears all checked children for the specified parent group at
	 * groupPosition.
	 * 
	 * @param groupPosition
	 *            - the position in the list of the parent group from which you
	 *            want to clear checked children.
	 */
	public void clearGroupChildChoices(int groupPosition){

		validateCheckedChildren(groupPosition);

		BitSet mChildren = mCheckedChildren.get(groupPosition);

		if (mChildren != null)
			mChildren.clear();

		if (checkChildrenOnGroupCheck)
			setGroupChoice(	groupPosition,
							false,
							true);
	}


	public Long[] getCheckedGroupIds(){


		if (groupCheckMode != CHECK_MODE_NONE
			&& mCheckedGroups != null){

			ArrayList<Long> mGroupIds = new ArrayList<Long>();
			int groupPos = mCheckedGroups.nextSetBit(0);

			while (groupPos > -1){

				mGroupIds.add(mAdapter.getGroupId(groupPos));
				groupPos = mCheckedGroups.nextSetBit(groupPos + 1);
			}

			return (Long[]) mGroupIds.toArray();

		}else
			Log.e(	TAG,
					"Can't get checked group item positions because group check mode is CHECK_MODE_NONE or mCheckedGroups is null");

		return null;
	}


	public Long[] getCheckedChildIds(int groupPosition){

		if (childCheckMode != CHECK_MODE_NONE
			&& mCheckedChildren != null){

			BitSet mChildren = mCheckedChildren.get(groupPosition);

			if (!mChildren.isEmpty()){

				ArrayList<Long> mChildIds = new ArrayList<Long>();

				int childPos = mChildren.nextSetBit(0);

				while (childPos > -1){

					mChildIds.add(mAdapter.getChildId(	groupPosition,
														childPos));
					childPos = mChildren.nextSetBit(childPos + 1);
				}

				return (Long[]) mChildIds.toArray();
			}

		}else
			Log.e(	TAG,
					"Can't get checked child item positions because child check mode is CHECK_MODE_NONE or mCheckedChildren is null");

		return null;
	}


	public int getCheckedGroupPosition(){

		if (groupCheckMode == GROUP_CHECK_MODE_ONE){

			if (mCheckedGroups != null)
				return mCheckedGroups.nextSetBit(0);
		}else
			Log.e(	TAG,
					"Can't get checked group item position because group check mode is not GROUP_CHECK_MODE_ONE");

		return -1;
	}


	/**
	 * This checks for the single checked child. This method is ONLY for use if
	 * childCheckMode is CHILD_CHECK_MODE_ONE
	 * 
	 * @return int[] containing {groupPosition, childPosition}
	 */
	public int[] getCheckedChildPosition(){

		if (childCheckMode == CHILD_CHECK_MODE_ONE){
			if (mCheckedChildren != null){

				BitSet mChildren;
				int childPos = 0;
				for (Integer checkedGroup : mCheckedChildren.keySet()){

					mChildren = mCheckedChildren.get(checkedGroup);

					if (mChildren != null){
						childPos = mChildren.nextSetBit(0);
						if (childPos > -1)
							return new int[] {	checkedGroup,
												childPos };
					}
				}
			}

		}else
			Log.e(	TAG,
					"Can't get checked child item position because group child mode is not CHILD_CHECK_MODE_ONE");
		return null;
	}


	/**
	 * @param groupPos
	 * @return checked child position in group at groupPosition
	 */
	public int getCheckedChildPosition(int groupPosition){

		if (childCheckMode != CHILD_CHECK_MODE_ONE_PER_GROUP
			&& mCheckedChildren != null){

			BitSet mChildren = mCheckedChildren.get(groupPosition);
			if (mChildren != null)
				return mChildren.nextSetBit(0);
		}else
			Log.e(	TAG,
					"Can't get checked child item position because group child mode is not CHILD_CHECK_MODE_ONE_PER_GROUP");
		return -1;
	}


	public boolean isGroupChecked(int groupPosition){

		if (mCheckedGroups != null)
			return mCheckedGroups.get(groupPosition);

		return false;
	}


	public boolean isChildChecked(int groupPosition, int childPosition){

		if (mCheckedChildren != null){

			BitSet mBitSet = mCheckedChildren.get(groupPosition);

			if (mBitSet != null)
				return mBitSet.get(childPosition);
		}

		return false;
	}


	public BoostExpandableListView setGroupChoice(int groupPosition,
													boolean isChecked,
													boolean refreshList){

		if (groupCheckMode == CHECK_MODE_NONE){
			Log.w(	TAG,
					"Can't set group checked without enabling a group check mode.");
			return this;
		}

		validateCheckedGroups();

		if (mCheckedGroups == null)
			mCheckedGroups = new BitSet(mAdapter.getGroupCount());

		if (isChecked != mCheckedGroups.get(groupPosition))
			// statechange
			// hasn't been
			// recorded
			if (isChecked){

				if (groupCheckMode == GROUP_CHECK_MODE_ONE){// Only one
															// group at a
															// time.
					if (checkChildrenOnGroupCheck)
						clearGroupChoices();

					mCheckedGroups.clear();
				}

				mCheckedGroups.set(groupPosition);

			}else
				// Not checked and hasn't been recorded
				mCheckedGroups.clear(groupPosition);

		if (checkChildrenOnGroupCheck
			&& childCheckMode == CHECK_MODE_MULTI)
			setGroupChildrenChoices(groupPosition,
									isChecked,
									refreshList);
		else
			Log.w(	TAG,
					"Can't have both child_check_mode_one or one per group "
						+ "and checkChildrenOnGroupCheck true simultaneously.");

		if (refreshList)
			refresh();

		return this;
	}


	public BoostExpandableListView setChildChoice(int groupPosition,
													int childPosition,
													boolean isChecked,
													boolean refreshList){


		if (childCheckMode == CHECK_MODE_NONE)
			Log.w(	TAG,
					"Can't set group checked without enabling a child check mode.");

		validateCheckedChildren(groupPosition);

		if (childCheckMode == CHILD_CHECK_MODE_ONE_PER_GROUP)
			clearGroupChildChoices(groupPosition);
		else if (childCheckMode == CHILD_CHECK_MODE_ONE)
			clearChildChoices();

		BitSet mBs = null;

		if (mCheckedChildren != null)
			mBs = mCheckedChildren.get(groupPosition);
		else
			mCheckedChildren = new HashMap<Integer, BitSet>();

		if (mBs == null)
			mBs = new BitSet(mAdapter.getChildrenCount(groupPosition));

		if (mBs.get(childPosition) != isChecked)
			mBs.set(childPosition,
					isChecked);

		if (refreshList)
			refresh();

		return this;
	}


	public BoostExpandableListView
		setGroupChildrenChoices(int groupPosition,
								boolean isChecked,
								boolean refreshList){

		if (childCheckMode == CHECK_MODE_NONE
			|| childCheckMode == CHILD_CHECK_MODE_ONE
			|| childCheckMode == CHILD_CHECK_MODE_ONE_PER_GROUP){
			if (isChecked)
				Log.e(	TAG,
						"You must set childCheckMode to CHECK_MODE_MULTI to set more than one child per group");
			return this;
		}

		validateCheckedGroups();
		validateCheckedChildren(groupPosition);

		if (mCheckedChildren == null){

			if (!isChecked)
				return this;

			mCheckedChildren =
								new HashMap<Integer, BitSet>(mAdapter.getGroupCount());
		}

		BitSet checkedChildren = mCheckedChildren.get(groupPosition);
		if (checkedChildren == null){

			if (!isChecked)
				return this;

			checkedChildren =
								new BitSet(mAdapter.getChildrenCount(groupPosition));
			mCheckedChildren.put(	groupPosition,
									checkedChildren);
		}

		if (isChecked)
			checkedChildren.set(0,
								checkedChildren.length(),
								true);
		else
			checkedChildren.set(0,
								checkedChildren.length(),
								false);

		if (checkChildrenOnGroupCheck){
			if (mCheckedGroups == null)
				mCheckedGroups = new BitSet(mAdapter.getGroupCount());
			mCheckedGroups.set(	groupPosition,
								isChecked);
		}

		if (refreshList)
			refresh();

		return this;
	}


	public BoostExpandableListView
		setCheckChildrenOnGroupCheck(boolean checkChildrenOnGroupCheck){

		this.checkChildrenOnGroupCheck = checkChildrenOnGroupCheck;

		return this;
	}


	/***********************************************************
	 * 
	 * Register/Undregister client ExpandableListChecklisteners
	 * 
	 ************************************************************/
	public boolean
		registerExpandableCheckListener(ExpandableListCheckListener listener){

		if (mCheckListeners == null)
			mCheckListeners = new ArrayList<ExpandableListCheckListener>();

		return mCheckListeners.add(listener);
	}


	public boolean
		unregisterExpandableCheckListener(ExpandableListCheckListener listener){

		if (mCheckListeners == null)
			return false;
		else
			return mCheckListeners.remove(listener);
	}


	/***********************************************************
	 * 
	 * MultiLevelCheckable Callbacks
	 * 
	 ************************************************************/
	@Override
	public void onGroupCheckChange(CompoundButton checkBox,
									int groupPosition,
									long groupId,
									boolean isChecked){

		setGroupChoice(	groupPosition,
						isChecked,
						false);

		for (ExpandableListCheckListener mListener : mCheckListeners)
			mListener.onGroupCheckChange(	checkBox,
											groupPosition,
											groupId,
											isChecked);

	}


	@Override
	public void onChildCheckChange(CompoundButton checkBox,
									int groupPosition,
									int childPosition,
									long childId,
									boolean isChecked){

		setChildChoice(	groupPosition,
						childPosition,
						isChecked,
						false);

		for (ExpandableListCheckListener mListener : mCheckListeners)
			mListener.onChildCheckChange(	checkBox,
											groupPosition,
											childPosition,
											childId,
											isChecked);
	}

}
